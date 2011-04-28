package org.hyperic.hq.inventory.domain;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.graph.annotation.GraphProperty;
import org.springframework.data.graph.annotation.NodeEntity;
import org.springframework.data.graph.neo4j.support.GraphDatabaseContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

/**
 * Represents a property that can be set against Resources of the associated
 * ResourceType
 * @author jhickey
 * @author dcrutchfield
 * 
 */
@Configurable
@NodeEntity
public class PropertyType {

    @GraphProperty
    private Object defaultValue;

    @NotNull
    @GraphProperty
    private String description;

    @GraphProperty
    private boolean hidden;

    @GraphProperty
    private boolean indexed;

    @GraphProperty
    @NotNull
    private String name;

    @GraphProperty
    private boolean secret;

    @Autowired
    private transient GraphDatabaseContext graphDatabaseContext;

    private transient Validator propertyValidator;

    public PropertyType() {
    }

    /**
     * 
     * @param name The name of the property
     * @param description The description of the property
     */
    public PropertyType(String name, String description) {
        this.description = description;
        this.name = name;
    }

    // TODO remove
    public PropertyType(String name, Class<?> type) {
        this.name = name;
    }

    /**
     * 
     * @return The default value for the property
     */
    public Object getDefaultValue() {
        return this.defaultValue;
    }

    /**
     * 
     * @return The property description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * 
     * @return The property name
     */
    public String getName() {
        return this.name;
    }

    /**
     * 
     * @return true if the property should be hidden from users
     */
    public boolean isHidden() {
        return hidden;
    }

    /**
     * 
     * @return true if the property should be indexed for lookup when set
     */
    public boolean isIndexed() {
        return indexed;
    }

    /**
     * 
     * @return true if value should be obscured (like a password)
     */
    public boolean isSecret() {
        return this.secret;
    }

    /**
     * Removes this PropertyType. Only supported as part of ResourceType removal
     */
    @Transactional("neoTxManager")
    public void remove() {
        // TODO remove property instances?
        graphDatabaseContext.removeNodeEntity(this);
    }

    /**
     * 
     * @param defaultValue The default value for the property
     */
    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * 
     * @param description The property description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 
     * @param hidden true if the property should be hidden from users
     */
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    /**
     * 
     * @param indexed true if the property should be indexed for lookup when set
     */
    public void setIndexed(boolean indexed) {
        this.indexed = indexed;
    }

    /**
     * 
     * @param secret true if the property value should be obscured (like a
     *        password)
     */
    public void setSecret(boolean secret) {
        this.secret = secret;
    }

    public Validator getPropertyValidator() {
        return propertyValidator;
    }

    public void setPropertyValidator(Validator propertyValidator) {
        this.propertyValidator = propertyValidator;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PropertyType[");
        sb.append("Name: ").append(getName()).append(", ");
        sb.append("Description: ").append(getDescription()).append(", ");
        sb.append("Secret: ").append(isSecret()).append(", ");
        sb.append("DefaultValue: ").append(getDefaultValue()).append(", ");
        sb.append("Hidden: ").append(isHidden()).append("]");
        return sb.toString();
    }
}