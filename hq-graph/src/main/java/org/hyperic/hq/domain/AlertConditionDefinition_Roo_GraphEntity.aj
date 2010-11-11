// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.hyperic.hq.domain;

import java.lang.Iterable;
import java.lang.Long;
import javax.annotation.Resource;
import org.hyperic.hq.domain.AlertConditionDefinition;
import org.neo4j.graphdb.Node;
import org.springframework.datastore.graph.neo4j.finder.Finder;
import org.springframework.datastore.graph.neo4j.finder.FinderFactory;

privileged aspect AlertConditionDefinition_Roo_GraphEntity {
    
    @Resource
    private FinderFactory AlertConditionDefinition.finderFactory;
    
    public AlertConditionDefinition.new(Node n) {
        setUnderlyingState(n);
    }

    public AlertConditionDefinition AlertConditionDefinition.findById(Long id) {
        return finderFactory.getFinderForClass(AlertConditionDefinition.class).findById(id);

    }
    
    public long AlertConditionDefinition.count() {
        return finderFactory.getFinderForClass(AlertConditionDefinition.class).count();

    }
    
    public Iterable<AlertConditionDefinition> AlertConditionDefinition.findAll() {
        return finderFactory.getFinderForClass(AlertConditionDefinition.class).findAll();

    }
    
}
