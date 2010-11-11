// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.hyperic.hq.domain;

import java.lang.Iterable;
import java.lang.Long;
import javax.annotation.Resource;
import org.hyperic.hq.domain.EscalationDefinition;
import org.neo4j.graphdb.Node;
import org.springframework.datastore.graph.neo4j.finder.Finder;
import org.springframework.datastore.graph.neo4j.finder.FinderFactory;

privileged aspect EscalationDefinition_Roo_GraphEntity {
    
    @Resource
    private FinderFactory EscalationDefinition.finderFactory;
    
    public EscalationDefinition.new(Node n) {
        setUnderlyingState(n);
    }

    public EscalationDefinition EscalationDefinition.findById(Long id) {
        return finderFactory.getFinderForClass(EscalationDefinition.class).findById(id);

    }
    
    public long EscalationDefinition.count() {
        return finderFactory.getFinderForClass(EscalationDefinition.class).count();

    }
    
    public Iterable<EscalationDefinition> EscalationDefinition.findAll() {
        return finderFactory.getFinderForClass(EscalationDefinition.class).findAll();

    }
    
}
