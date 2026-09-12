package com.cis.new_connection_service.repository;

import com.cis.new_connection_service.entity.NewConnectionVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewConnectionRepository extends JpaRepository<NewConnectionVO,Long> {
    List<NewConnectionVO> findAllByApplicationStatus(String applicationStatus);

    boolean existsByCustomerId(Long customerId);
}
