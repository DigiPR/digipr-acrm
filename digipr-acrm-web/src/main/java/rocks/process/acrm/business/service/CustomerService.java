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
import java.util.List;

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
				customer.setAgent(agentService.getCurrentAgent());
				return customerRepository.save(customer);
			}
			throw new Exception("Mobile number " + customer.getMobile() + " already assigned to a customer.");
		}
		if (customerRepository.findByMobileAndIdNot(customer.getMobile(), customer.getId()) == null) {
			if (customer.getAgent() == null) {
				customer.setAgent(agentService.getCurrentAgent());
			}
			return customerRepository.save(customer);
		}
		throw new Exception("Mobile number " + customer.getMobile() + " already assigned to a customer.");
	}

	public void deleteCustomer(Long customerId)
	{
		customerRepository.deleteById(customerId);
	}
	
	public Customer findCustomerById(Long customerId) throws Exception {
		List<Customer> customerList = customerRepository.findByIdAndAgentId(customerId, agentService.getCurrentAgent().getId());
		if(customerList.isEmpty()){
			throw new Exception("No customer with ID "+customerId+" found.");
		}
		return customerList.get(0);
	}

	public List<Customer> findAllCustomers() {
		return customerRepository.findByAgentId(agentService.getCurrentAgent().getId());
	}
	
}
