/*
 * Copyright (c) 2020. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package rocks.process.acrm.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import rocks.process.acrm.data.domain.Customer;
import rocks.process.acrm.data.repository.CustomerRepository;

import javax.validation.Valid;

@Service
@Validated
public class CustomerService {

	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private AgentService agentService;

	public Customer editCustomer(@Valid Customer customer) throws Exception {
		if (customer.getId() == null) {
			if (customerRepository.findByMobile(customer.getMobile()) == null) {
				return customerRepository.save(customer);
			}
			throw new Exception("Mobile number " + customer.getMobile() + " already assigned to a customer.");
		}
		if (customerRepository.findByMobileAndIdNot(customer.getMobile(), customer.getId()) == null) {
			return customerRepository.save(customer);
		}
		throw new Exception("Mobile number " + customer.getMobile() + " already assigned to a customer.");
	}
	
}
