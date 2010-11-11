// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.hyperic.hq.domain;

import java.lang.Integer;
import java.lang.Long;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Version;
import org.hyperic.hq.domain.Relation;
import org.springframework.transaction.annotation.Transactional;

privileged aspect Relation_Roo_Entity {
    
    declare @type: Relation: @Entity;
    
    @PersistenceContext
    transient EntityManager Relation.entityManager;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long Relation.id;
    
    @Version
    @Column(name = "version")
    private Integer Relation.version;
    
    public Long Relation.getId() {
        return this.id;
    }
    
    public void Relation.setId(Long id) {
        this.id = id;
    }
    
    public Integer Relation.getVersion() {
        return this.version;
    }
    
    public void Relation.setVersion(Integer version) {
        this.version = version;
    }
    
    @Transactional
    public void Relation.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void Relation.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Relation attached = this.entityManager.find(this.getClass(), this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void Relation.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public Relation Relation.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Relation merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
    public static final EntityManager Relation.entityManager() {
        EntityManager em = new Relation().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long Relation.countRelations() {
        return entityManager().createQuery("select count(o) from Relation o", Long.class).getSingleResult();
    }
    
    public static List<Relation> Relation.findAllRelations() {
        return entityManager().createQuery("select o from Relation o", Relation.class).getResultList();
    }
    
    public static Relation Relation.findRelation(Long id) {
        if (id == null) return null;
        return entityManager().find(Relation.class, id);
    }
    
    public static List<Relation> Relation.findRelationEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("select o from Relation o", Relation.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
}
