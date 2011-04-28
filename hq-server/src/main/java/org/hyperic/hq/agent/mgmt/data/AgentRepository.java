package org.hyperic.hq.agent.mgmt.data;

import java.util.List;

import javax.persistence.QueryHint;

import org.hyperic.hq.agent.mgmt.domain.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface AgentRepository extends JpaRepository<Agent, Integer>, AgentRepositoryCustom {

    @Transactional(readOnly = true)
    @Query("select a from Agent a where a.address = :address")
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true"),
                 @QueryHint(name = "org.hibernate.cacheRegion", value = "Agent.findByAddress") })
    List<Agent> findByAddress(@Param("address") String address);

    Agent findByAddressAndPort(String address, int port);

    @Transactional(readOnly = true)
    @Query("select a from Agent a where a.agentToken = :agentToken")
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true"),
                 @QueryHint(name = "org.hibernate.cacheRegion", value = "Agent.findByAgentToken") })
    Agent findByAgentToken(@Param("agentToken") String token);

}