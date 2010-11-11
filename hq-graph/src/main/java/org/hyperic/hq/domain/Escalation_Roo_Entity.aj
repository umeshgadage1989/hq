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
import org.hyperic.hq.domain.Escalation;
import org.springframework.transaction.annotation.Transactional;

privileged aspect Escalation_Roo_Entity {
    
    declare @type: Escalation: @Entity;
    
    @PersistenceContext
    transient EntityManager Escalation.entityManager;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long Escalation.id;
    
    @Version
    @Column(name = "version")
    private Integer Escalation.version;
    
    public Long Escalation.getId() {
        return this.id;
    }
    
    public void Escalation.setId(Long id) {
        this.id = id;
    }
    
    public Integer Escalation.getVersion() {
        return this.version;
    }
    
    public void Escalation.setVersion(Integer version) {
        this.version = version;
    }
    
    @Transactional
    public void Escalation.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void Escalation.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Escalation attached = this.entityManager.find(this.getClass(), this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void Escalation.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public Escalation Escalation.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Escalation merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
    public static final EntityManager Escalation.entityManager() {
        EntityManager em = new Escalation().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long Escalation.countEscalations() {
        return entityManager().createQuery("select count(o) from Escalation o", Long.class).getSingleResult();
    }
    
    public static List<Escalation> Escalation.findAllEscalations() {
        return entityManager().createQuery("select o from Escalation o", Escalation.class).getResultList();
    }
    
    public static Escalation Escalation.findEscalation(Long id) {
        if (id == null) return null;
        return entityManager().find(Escalation.class, id);
    }
    
    public static List<Escalation> Escalation.findEscalationEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("select o from Escalation o", Escalation.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
}
