package com.cis.new_connection_service.service.impl;

import com.cis.new_connection_service.common.ApplicationStatus;
import com.cis.new_connection_service.dto.ConnectionRequestDto;
import com.cis.new_connection_service.dto.ConnectionResponseDto;
import com.cis.new_connection_service.entity.ConsumerMasterVO;
import com.cis.new_connection_service.entity.NewConnectionVO;
import com.cis.new_connection_service.repository.ConsumerRepository;
import com.cis.new_connection_service.repository.NewConnectionRepository;
import com.cis.new_connection_service.service.ConnectionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class ConnectionServiceImpl implements ConnectionService {

    @Autowired
    private NewConnectionRepository newConnectionRepository;

    @Autowired
    private ConsumerRepository consumerRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ConnectionRequestDto createConnection(ConnectionRequestDto request) {
        NewConnectionVO connection = modelMapper.map(request, NewConnectionVO.class);
        connection.setApplicationNumber(generateApplicationNumber());
        connection.setApplicationStatus(ApplicationStatus.SUBMITTED.toString());
        connection.setCustomerId(generateCustomerId());
        if(Integer.parseInt(request.getPhase())==1){
            connection.setConnectionType("LT");
        }else if(Integer.parseInt(request.getPhase())==1){
            connection.setConnectionType("HT");
        }
        NewConnectionVO savedConnection = newConnectionRepository.save(connection);
        return modelMapper.map(savedConnection, ConnectionRequestDto.class);
    }

    @Override
    public List<ConnectionResponseDto> getAllPendingConnectionDetails() {
        List<NewConnectionVO> allPendingConnection = newConnectionRepository.findAllByApplicationStatus(ApplicationStatus.SUBMITTED.toString());
        List<ConnectionResponseDto> connectionResponseDtoList = allPendingConnection.stream().map(connection -> modelMapper.map(connection, ConnectionResponseDto.class))
                .toList();
        return connectionResponseDtoList;
    }

    @Override
    @Transactional
    public ConsumerMasterVO approvedByCustomerId(Long customerId) {
        ConsumerMasterVO savedConsumer=null;
        NewConnectionVO pendingCustomer = newConnectionRepository
                .findByCustomerIdAndApplicationStatus(customerId, ApplicationStatus.SUBMITTED.toString()).orElseThrow(()->new RuntimeException("consumer not found"));
        if(pendingCustomer!=null){
            pendingCustomer.setApplicationStatus(ApplicationStatus.UNDER_REVIEW.toString());
            NewConnectionVO saved = newConnectionRepository.save(pendingCustomer);
            if(saved!=null){
                ConsumerMasterVO consumer = new ConsumerMasterVO();
                consumer.setAccountNo(generateAccountNo());
                consumer.setBillingStatus("N");
                consumer.setConsName(pendingCustomer.getApplicantName());
                consumer.setConsAddress(pendingCustomer.getStreet()+" "+pendingCustomer.getCity()+" "+pendingCustomer.getDistrict() );
                consumer.setCustomerId(pendingCustomer.getCustomerId());
                consumer.setMobileNumber(pendingCustomer.getMobileNumber());
                if(pendingCustomer.getPhase().equals("1")){
                    consumer.setLoadType("L");
                    consumer.setTypeOfSupply("LT");
                }else{
                    consumer.setLoadType("H");
                    consumer.setTypeOfSupply("HT");
                }
                 savedConsumer = consumerRepository.save(consumer);
            }else{
                throw new RuntimeException("consumer not found");
            }
        }

        return savedConsumer;
    }

    @Override
    public String rejectedByCustomerId(Long customerId) {
        NewConnectionVO pendingCustomer = newConnectionRepository
                .findByCustomerIdAndApplicationStatus(customerId, ApplicationStatus.SUBMITTED.toString())
                .orElseThrow(()->new RuntimeException("consumer not found"));
        pendingCustomer.setApplicationStatus(ApplicationStatus.REJECTED.toString());
        newConnectionRepository.save(pendingCustomer);
        return "Rejected by customer id: "+pendingCustomer.getCustomerId();
    }

    @Override
    public ConnectionRequestDto searchWithApplicationNumber(String applicationNumber) {
        List<NewConnectionVO> newConnectionVOS = newConnectionRepository.findByApplicationNumber(applicationNumber);
        List<ConnectionRequestDto> list = newConnectionVOS.stream()
                .map(consumer -> modelMapper.map(consumer, ConnectionRequestDto.class)).toList();
        if(list.size()>0){
            return list.get(0);
        }else {
            return null;
        }
    }

    private String generateApplicationNumber() {
        return "CONN-" + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
    }

    private Long generateCustomerId() {
        Long customerId;
        do {
            customerId = ThreadLocalRandom.current()
                    .nextLong(1000000000L, 9999999999L);
        } while (newConnectionRepository.existsByCustomerId(customerId));
        return customerId;
    }

    private Long generateAccountNo(){
        Long accountNo;
        do{
            accountNo = ThreadLocalRandom.current()
                    .nextLong(1000000000L, 9999999999L);
        }while(consumerRepository.existsByAccountNo(accountNo));
        return accountNo;
    }
}
