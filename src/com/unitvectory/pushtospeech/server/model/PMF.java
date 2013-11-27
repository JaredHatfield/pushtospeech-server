package com.unitvectory.pushtospeech.server.model;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

/**
 * The persistence manager factory helper class.
 * 
 * @author Jared Hatfield
 * 
 */
public final class PMF {

    /**
     * The persistence manager factory.
     */
    private static final PersistenceManagerFactory pmfInstance = JDOHelper
            .getPersistenceManagerFactory("transactions-optional");

    /**
     * Initializes a new instance of the PMF class.
     */
    private PMF() {
    }

    /**
     * Returns the PersistenceManagerFactory instance.
     * 
     * @return The PMF instance.
     */
    public static PersistenceManagerFactory get() {
        return pmfInstance;
    }
}
