/*
 * Copyright (c) 2020. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package rocks.process.acrm.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rocks.process.acrm.data.domain.Agent;

@Repository
public interface AgentRepository extends JpaRepository<Agent, Long> {
	Agent findByEmail(String email);
	Agent findByEmailAndIdNot(String email, Long agentId);
}
