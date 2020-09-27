/*
 * Copyright (c) 2020. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package rocks.process.acrm.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rocks.process.acrm.data.domain.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
	Customer findByMobile(String mobile);
	Customer findByMobileAndIdNot(String mobile, Long agentId);
}