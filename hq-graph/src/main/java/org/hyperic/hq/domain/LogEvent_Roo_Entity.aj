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
import org.hyperic.hq.domain.LogEvent;
import org.springframework.transaction.annotation.Transactional;

privileged aspect LogEvent_Roo_Entity {
    
    declare @type: LogEvent: @Entity;
    
    @PersistenceContext
    transient EntityManager LogEvent.entityManager;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long LogEvent.id;
    
    @Version
    @Column(name = "version")
    private Integer LogEvent.version;
    
    public Long LogEvent.getId() {
        return this.id;
    }
    
    public void LogEvent.setId(Long id) {
        this.id = id;
    }
    
    public Integer LogEvent.getVersion() {
        return this.version;
    }
    
    public void LogEvent.setVersion(Integer version) {
        this.version = version;
    }
    
    @Transactional
    public void LogEvent.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void LogEvent.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            LogEvent attached = this.entityManager.find(this.getClass(), this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void LogEvent.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public LogEvent LogEvent.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        LogEvent merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
    public static final EntityManager LogEvent.entityManager() {
        EntityManager em = new LogEvent().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long LogEvent.countLogEvents() {
        return entityManager().createQuery("select count(o) from LogEvent o", Long.class).getSingleResult();
    }
    
    public static List<LogEvent> LogEvent.findAllLogEvents() {
        return entityManager().createQuery("select o from LogEvent o", LogEvent.class).getResultList();
    }
    
    public static LogEvent LogEvent.findLogEvent(Long id) {
        if (id == null) return null;
        return entityManager().find(LogEvent.class, id);
    }
    
    public static List<LogEvent> LogEvent.findLogEventEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("select o from LogEvent o", LogEvent.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
}
