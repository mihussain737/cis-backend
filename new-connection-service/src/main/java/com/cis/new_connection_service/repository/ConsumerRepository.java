package com.cis.new_connection_service.repository;

import com.cis.new_connection_service.entity.ConsumerMasterVO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConsumerRepository extends JpaRepository<ConsumerMasterVO,String> {
    boolean existsByAccountNo(Long accountNo);
    Optional<ConsumerMasterVO> findByAccountNo(Long accountNo);
    Optional<ConsumerMasterVO> findByConsumerId(String consumerId);
}
