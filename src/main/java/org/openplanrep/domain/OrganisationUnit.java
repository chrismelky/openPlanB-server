package org.openplanrep.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A OrganisationUnit.
 */
@Entity
@Table(name = "organisation_unit")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class OrganisationUnit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "uuid")
    private String uuid;

    @Size(max = 50)
    @Column(name = "code", length = 50)
    private String code;

    @NotNull
    @Size(max = 100)
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "opening_date")
    private LocalDate openingDate;

    @Column(name = "closed_date")
    private LocalDate closedDate;

    @Column(name = "url")
    private String url;

    @Column(name = "latitude", precision = 10, scale = 2)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 10, scale = 2)
    private BigDecimal longitude;

    @Column(name = "address")
    private String address;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_numner")
    private String phoneNumner;

    @OneToOne
    @JoinColumn(unique = true)
    private OrganisationUnit parent;

    @ManyToOne
    @JsonIgnoreProperties("")
    private OrgUnitGroup orgUnitGroup;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "organisation_unit_attribute_values",
               joinColumns = @JoinColumn(name = "organisation_units_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "attribute_values_id", referencedColumnName = "id"))
    private Set<AttributeValue> attributeValues = new HashSet<>();

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

    public OrganisationUnit uuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCode() {
        return code;
    }

    public OrganisationUnit code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public OrganisationUnit name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getOpeningDate() {
        return openingDate;
    }

    public OrganisationUnit openingDate(LocalDate openingDate) {
        this.openingDate = openingDate;
        return this;
    }

    public void setOpeningDate(LocalDate openingDate) {
        this.openingDate = openingDate;
    }

    public LocalDate getClosedDate() {
        return closedDate;
    }

    public OrganisationUnit closedDate(LocalDate closedDate) {
        this.closedDate = closedDate;
        return this;
    }

    public void setClosedDate(LocalDate closedDate) {
        this.closedDate = closedDate;
    }

    public String getUrl() {
        return url;
    }

    public OrganisationUnit url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public OrganisationUnit latitude(BigDecimal latitude) {
        this.latitude = latitude;
        return this;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public OrganisationUnit longitude(BigDecimal longitude) {
        this.longitude = longitude;
        return this;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public OrganisationUnit address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public OrganisationUnit email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumner() {
        return phoneNumner;
    }

    public OrganisationUnit phoneNumner(String phoneNumner) {
        this.phoneNumner = phoneNumner;
        return this;
    }

    public void setPhoneNumner(String phoneNumner) {
        this.phoneNumner = phoneNumner;
    }

    public OrganisationUnit getParent() {
        return parent;
    }

    public OrganisationUnit parent(OrganisationUnit organisationUnit) {
        this.parent = organisationUnit;
        return this;
    }

    public void setParent(OrganisationUnit organisationUnit) {
        this.parent = organisationUnit;
    }

    public OrgUnitGroup getOrgUnitGroup() {
        return orgUnitGroup;
    }

    public OrganisationUnit orgUnitGroup(OrgUnitGroup orgUnitGroup) {
        this.orgUnitGroup = orgUnitGroup;
        return this;
    }

    public void setOrgUnitGroup(OrgUnitGroup orgUnitGroup) {
        this.orgUnitGroup = orgUnitGroup;
    }

    public Set<AttributeValue> getAttributeValues() {
        return attributeValues;
    }

    public OrganisationUnit attributeValues(Set<AttributeValue> attributeValues) {
        this.attributeValues = attributeValues;
        return this;
    }

    public OrganisationUnit addAttributeValues(AttributeValue attributeValue) {
        this.attributeValues.add(attributeValue);
        return this;
    }

    public OrganisationUnit removeAttributeValues(AttributeValue attributeValue) {
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
        OrganisationUnit organisationUnit = (OrganisationUnit) o;
        if (organisationUnit.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), organisationUnit.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OrganisationUnit{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", openingDate='" + getOpeningDate() + "'" +
            ", closedDate='" + getClosedDate() + "'" +
            ", url='" + getUrl() + "'" +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            ", address='" + getAddress() + "'" +
            ", email='" + getEmail() + "'" +
            ", phoneNumner='" + getPhoneNumner() + "'" +
            "}";
    }
}
