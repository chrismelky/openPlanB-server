package org.openplanrep.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Attribute.
 */
@Entity
@Table(name = "attribute")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Attribute implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "code")
    private String code;

    @NotNull
    @Size(max = 100)
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @NotNull
    @Column(name = "value_type", nullable = false)
    private String valueType;

    @NotNull
    @Column(name = "mandatory", nullable = false)
    private Boolean mandatory;

    @Column(name = "org_unit_group_set_attribute")
    private Boolean orgUnitGroupSetAttribute;

    @Column(name = "org_unit_group_attribute")
    private Boolean orgUnitGroupAttribute;

    @Column(name = "org_unit_attribute")
    private Boolean orgUnitAttribute;

    @Column(name = "user_attribute")
    private Boolean userAttribute;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @ManyToOne
    @JsonIgnoreProperties("")
    private OptionSet optionSet;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public Attribute uuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCode() {
        return code;
    }

    public Attribute code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public Attribute name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValueType() {
        return valueType;
    }

    public Attribute valueType(String valueType) {
        this.valueType = valueType;
        return this;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public Boolean isMandatory() {
        return mandatory;
    }

    public Attribute mandatory(Boolean mandatory) {
        this.mandatory = mandatory;
        return this;
    }

    public void setMandatory(Boolean mandatory) {
        this.mandatory = mandatory;
    }

    public Boolean isOrgUnitGroupSetAttribute() {
        return orgUnitGroupSetAttribute;
    }

    public Attribute orgUnitGroupSetAttribute(Boolean orgUnitGroupSetAttribute) {
        this.orgUnitGroupSetAttribute = orgUnitGroupSetAttribute;
        return this;
    }

    public void setOrgUnitGroupSetAttribute(Boolean orgUnitGroupSetAttribute) {
        this.orgUnitGroupSetAttribute = orgUnitGroupSetAttribute;
    }

    public Boolean isOrgUnitGroupAttribute() {
        return orgUnitGroupAttribute;
    }

    public Attribute orgUnitGroupAttribute(Boolean orgUnitGroupAttribute) {
        this.orgUnitGroupAttribute = orgUnitGroupAttribute;
        return this;
    }

    public void setOrgUnitGroupAttribute(Boolean orgUnitGroupAttribute) {
        this.orgUnitGroupAttribute = orgUnitGroupAttribute;
    }

    public Boolean isOrgUnitAttribute() {
        return orgUnitAttribute;
    }

    public Attribute orgUnitAttribute(Boolean orgUnitAttribute) {
        this.orgUnitAttribute = orgUnitAttribute;
        return this;
    }

    public void setOrgUnitAttribute(Boolean orgUnitAttribute) {
        this.orgUnitAttribute = orgUnitAttribute;
    }

    public Boolean isUserAttribute() {
        return userAttribute;
    }

    public Attribute userAttribute(Boolean userAttribute) {
        this.userAttribute = userAttribute;
        return this;
    }

    public void setUserAttribute(Boolean userAttribute) {
        this.userAttribute = userAttribute;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public Attribute sortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
        return this;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public OptionSet getOptionSet() {
        return optionSet;
    }

    public Attribute optionSet(OptionSet optionSet) {
        this.optionSet = optionSet;
        return this;
    }

    public void setOptionSet(OptionSet optionSet) {
        this.optionSet = optionSet;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Attribute attribute = (Attribute) o;
        if (attribute.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), attribute.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Attribute{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", valueType='" + getValueType() + "'" +
            ", mandatory='" + isMandatory() + "'" +
            ", orgUnitGroupSetAttribute='" + isOrgUnitGroupSetAttribute() + "'" +
            ", orgUnitGroupAttribute='" + isOrgUnitGroupAttribute() + "'" +
            ", orgUnitAttribute='" + isOrgUnitAttribute() + "'" +
            ", userAttribute='" + isUserAttribute() + "'" +
            ", sortOrder=" + getSortOrder() +
            "}";
    }
}
