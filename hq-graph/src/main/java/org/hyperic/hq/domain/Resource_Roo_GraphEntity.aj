// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.hyperic.hq.domain;

import java.lang.Iterable;
import java.lang.Long;
import org.hyperic.hq.domain.Resource;
import org.neo4j.graphdb.Node;
import org.springframework.datastore.graph.neo4j.finder.Finder;
import org.springframework.datastore.graph.neo4j.finder.FinderFactory;

privileged aspect Resource_Roo_GraphEntity {
    
    @javax.annotation.Resource
    private FinderFactory Resource.finderFactory;
    
    public Resource.new(Node n) {
        setUnderlyingState(n);
    }

    public Resource Resource.findById(Long id) {
        return finderFactory.getFinderForClass(Resource.class).findById(id);

    }
    
    public long Resource.count() {
        return finderFactory.getFinderForClass(Resource.class).count();

    }
    
    public Iterable<Resource> Resource.findAll() {
        return finderFactory.getFinderForClass(Resource.class).findAll();

    }
    
}
