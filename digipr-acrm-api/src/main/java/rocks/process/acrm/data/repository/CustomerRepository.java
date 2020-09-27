/*
 * Copyright (c) 2020. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package rocks.process.acrm.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rocks.process.acrm.data.domain.Customer;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
	Customer findByMobile(String mobile);
	Customer findByMobileAndIdNot(String mobile, Long agentId);
	List<Customer> findByAgentId(Long agentId);
	List<Customer> findByIdAndAgentId(Long customerId, Long agentId);
}