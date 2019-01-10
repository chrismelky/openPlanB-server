package org.openplanrep.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A OrgUnitGroup.
 */
@Entity
@Table(name = "org_unit_group")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class OrgUnitGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "jhi_uid")
    private String uid;

    @Column(name = "code")
    private String code;

    @NotNull
    @Size(max = 50)
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "created")
    private Instant created;

    @Column(name = "last_update")
    private Instant lastUpdate;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private OrgUnitGroupSet orgUnitGroupSet;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "org_unit_group_attribute_values",
               joinColumns = @JoinColumn(name = "org_unit_groups_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "attribute_values_id", referencedColumnName = "id"))
    private Set<AttributeValue> attributeValues = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public OrgUnitGroup uid(String uid) {
        this.uid = uid;
        return this;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCode() {
        return code;
    }

    public OrgUnitGroup code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public OrgUnitGroup name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getCreated() {
        return created;
    }

    public OrgUnitGroup created(Instant created) {
        this.created = created;
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getLastUpdate() {
        return lastUpdate;
    }

    public OrgUnitGroup lastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
        return this;
    }

    public void setLastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public OrgUnitGroupSet getOrgUnitGroupSet() {
        return orgUnitGroupSet;
    }

    public OrgUnitGroup orgUnitGroupSet(OrgUnitGroupSet orgUnitGroupSet) {
        this.orgUnitGroupSet = orgUnitGroupSet;
        return this;
    }

    public void setOrgUnitGroupSet(OrgUnitGroupSet orgUnitGroupSet) {
        this.orgUnitGroupSet = orgUnitGroupSet;
    }

    public Set<AttributeValue> getAttributeValues() {
        return attributeValues;
    }

    public OrgUnitGroup attributeValues(Set<AttributeValue> attributeValues) {
        this.attributeValues = attributeValues;
        return this;
    }

    public OrgUnitGroup addAttributeValues(AttributeValue attributeValue) {
        this.attributeValues.add(attributeValue);
        return this;
    }

    public OrgUnitGroup removeAttributeValues(AttributeValue attributeValue) {
        this.attributeValues.remove(attributeValue);
        return this;
    }

    public void setAttributeValues(Set<AttributeValue> attributeValues) {
        this.attributeValues = attributeValues;
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
        OrgUnitGroup orgUnitGroup = (OrgUnitGroup) o;
        if (orgUnitGroup.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), orgUnitGroup.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OrgUnitGroup{" +
            "id=" + getId() +
            ", uid='" + getUid() + "'" +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", created='" + getCreated() + "'" +
            ", lastUpdate='" + getLastUpdate() + "'" +
            "}";
    }
}
