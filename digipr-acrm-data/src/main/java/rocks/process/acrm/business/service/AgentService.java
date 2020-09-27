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

import javax.validation.Valid;
import javax.validation.Validator;

@Service
@Validated
public class AgentService {

    @Autowired
    private AgentRepository agentRepository;
    @Autowired
    Validator validator;

    public Agent saveAgent(@Valid Agent agent) throws Exception {
        if (agent.getId() == null) {
            if (agentRepository.findByEmail(agent.getEmail()) != null) {
                throw new Exception("Email address " + agent.getEmail() + " already assigned another agent.");
            }
        } else if (agentRepository.findByEmailAndIdNot(agent.getEmail(), agent.getId()) != null) {
            throw new Exception("Email address " + agent.getEmail() + " already assigned another agent.");
        }
        return agentRepository.save(agent);
    }

}
