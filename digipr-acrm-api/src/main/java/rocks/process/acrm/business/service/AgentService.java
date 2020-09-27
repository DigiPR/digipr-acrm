/*
 * Copyright (c) 2020. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package rocks.process.acrm.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import rocks.process.acrm.data.domain.Agent;
import rocks.process.acrm.data.repository.AgentRepository;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import javax.validation.Validator;

@Service
@Validated
public class AgentService {

    @Autowired
    private AgentRepository agentRepository;
    @Autowired
    Validator validator;

    public void saveAgent(@Valid Agent agent) throws Exception {
        if (agent.getId() == null) {
            if (agentRepository.findByEmail(agent.getEmail()) != null) {
                throw new Exception("Email address " + agent.getEmail() + " already assigned another agent.");
            }
        } else if (agentRepository.findByEmailAndIdNot(agent.getEmail(), agent.getId()) != null) {
            throw new Exception("Email address " + agent.getEmail() + " already assigned another agent.");
        }
        agentRepository.save(agent);
    }

    public Agent getCurrentAgent() {
        String userEmail = "demo@demo.ch";
        return agentRepository.findByEmail(userEmail);
    }

    @PostConstruct
    private void init() throws Exception {
        Agent agent = new Agent();
        agent.setName("Demo");
        agent.setEmail("demo@demo.ch");
        agent.setPassword("password");
        this.saveAgent(agent);
    }
}
