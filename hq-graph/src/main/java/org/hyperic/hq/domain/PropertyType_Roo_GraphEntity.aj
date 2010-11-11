// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.hyperic.hq.domain;

import java.lang.Iterable;
import java.lang.Long;
import javax.annotation.Resource;
import org.hyperic.hq.domain.PropertyType;
import org.neo4j.graphdb.Node;
import org.springframework.datastore.graph.neo4j.finder.Finder;
import org.springframework.datastore.graph.neo4j.finder.FinderFactory;

privileged aspect PropertyType_Roo_GraphEntity {
    
    @Resource
    private FinderFactory PropertyType.finderFactory;
    
    public PropertyType.new(Node n) {
        setUnderlyingState(n);
    }

    public PropertyType PropertyType.findById(Long id) {
        return finderFactory.getFinderForClass(PropertyType.class).findById(id);

    }
    
    public long PropertyType.count() {
        return finderFactory.getFinderForClass(PropertyType.class).count();

    }
    
    public Iterable<PropertyType> PropertyType.findAll() {
        return finderFactory.getFinderForClass(PropertyType.class).findAll();

    }
    
}
