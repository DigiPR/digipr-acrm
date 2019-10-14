package rocks.process.acrm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import rocks.process.acrm.business.service.AgentService;
import rocks.process.acrm.business.service.CustomerService;
import rocks.process.acrm.data.domain.Agent;
import rocks.process.acrm.data.domain.Customer;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class DigiprAcrmDataApplication {
	
	@Autowired
	private AgentService agentService;
	
	@Autowired
	private CustomerService customerService;

	public static void main(String[] args) {
		SpringApplication.run(DigiprAcrmDataApplication.class, args);
	}
	
	@PostConstruct
	private void initDemoData() throws Exception {
		Agent agent = new Agent();
		agent.setName("Hans Müller");
		agent.setEmail("hans@mueller.ch");
		agent = agentService.saveAgent(agent);
		Customer customer = new Customer();
		customer.setName("Lisa Meyer");
		customer.setMobile("+41794567896");
		customer.setEmail("lisa@meyer.ch");
		customer.setAgent(agent);
		customerService.editCustomer(customer);
	}
}
