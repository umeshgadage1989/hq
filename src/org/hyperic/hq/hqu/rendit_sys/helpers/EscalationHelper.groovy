package org.hyperic.hq.hqu.rendit.helpers

import java.util.Collection

import org.hyperic.hq.authz.server.session.AuthzSubject
import org.hyperic.hq.escalation.server.session.EscalationManagerEJBImpl as EscMan
import org.hyperic.hq.escalation.server.session.Escalation

class EscalationHelper extends BaseHelper {
    private escMan = EscMan.one

    EscalationHelper(AuthzSubject user) {
        super(user)
    }

    /**
     * Find Escalation by ID or name
     *
     * @param id the ID of the escalation (optional if searching by name)
     * @param name the name of the escalation (optional if searching by ID)
     * @return Escalation
     */
    Escalation getEscalation(int id, String name) {
        if (id) {
            return escMan.findById(id)
        }
        else {
            return escMan.findByName(name)
        }
    }

    /**
     * Return a list of all Escalations
     * @return a list of escalations
     */
    Collection getAllEscalations() {
        escMan.findAll(user)
    }

    /**
     * Delete an Escalation by ID
     * @param id the ID of the escalation (optional if searching by name)
     */
    def deleteEscalation(int id) {
        def esc = escMan.findById(id)
        if (esc) {
            escMan.deleteEscalation(user, esc)
        }
    }

    /**
     * Create an Escalation
     */
     Escalation createEscalation(String name, String description,
                                 boolean pauseAllowed, long maxWaitTime,
                                 boolean notifyAll, boolean repeat) {
        escMan.createEscalation(name, description, pauseAllowed, maxWaitTime,
                                notifyAll, repeat)
    }

    /**
     * Update an Escalation
     */
     Escalation updateEscalation(Escalation esc, String name,
                                 String description, boolean pauseAllowed,
                                 long maxWaitTime, boolean notifyAll,
                                 boolean repeat) {
        escMan.updateEscalation(user, esc, name, description, pauseAllowed,
                                maxWaitTime, notifyAll, repeat)
    }
}
