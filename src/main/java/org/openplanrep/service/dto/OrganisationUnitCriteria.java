package org.openplanrep.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.BigDecimalFilter;

import io.github.jhipster.service.filter.LocalDateFilter;



/**
 * Criteria class for the OrganisationUnit entity. This class is used in OrganisationUnitResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /organisation-units?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class OrganisationUnitCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter uuid;

    private StringFilter code;

    private StringFilter name;

    private LocalDateFilter openingDate;

    private LocalDateFilter closedDate;

    private StringFilter url;

    private BigDecimalFilter latitude;

    private BigDecimalFilter longitude;

    private StringFilter address;

    private StringFilter email;

    private StringFilter phoneNumner;

    private LongFilter parentId;

    private LongFilter orgUnitGroupId;

    private LongFilter attributeValuesId;

    public OrganisationUnitCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getUuid() {
        return uuid;
    }

    public void setUuid(StringFilter uuid) {
        this.uuid = uuid;
    }

    public StringFilter getCode() {
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public LocalDateFilter getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(LocalDateFilter openingDate) {
        this.openingDate = openingDate;
    }

    public LocalDateFilter getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(LocalDateFilter closedDate) {
        this.closedDate = closedDate;
    }

    public StringFilter getUrl() {
        return url;
    }

    public void setUrl(StringFilter url) {
        this.url = url;
    }

    public BigDecimalFilter getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimalFilter latitude) {
        this.latitude = latitude;
    }

    public BigDecimalFilter getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimalFilter longitude) {
        this.longitude = longitude;
    }

    public StringFilter getAddress() {
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public StringFilter getEmail() {
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getPhoneNumner() {
        return phoneNumner;
    }

    public void setPhoneNumner(StringFilter phoneNumner) {
        this.phoneNumner = phoneNumner;
    }

    public LongFilter getParentId() {
        return parentId;
    }

    public void setParentId(LongFilter parentId) {
        this.parentId = parentId;
    }

    public LongFilter getOrgUnitGroupId() {
        return orgUnitGroupId;
    }

    public void setOrgUnitGroupId(LongFilter orgUnitGroupId) {
        this.orgUnitGroupId = orgUnitGroupId;
    }

    public LongFilter getAttributeValuesId() {
        return attributeValuesId;
    }

    public void setAttributeValuesId(LongFilter attributeValuesId) {
        this.attributeValuesId = attributeValuesId;
    }

    @Override
    public String toString() {
        return "OrganisationUnitCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (uuid != null ? "uuid=" + uuid + ", " : "") +
                (code != null ? "code=" + code + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (openingDate != null ? "openingDate=" + openingDate + ", " : "") +
                (closedDate != null ? "closedDate=" + closedDate + ", " : "") +
                (url != null ? "url=" + url + ", " : "") +
                (latitude != null ? "latitude=" + latitude + ", " : "") +
                (longitude != null ? "longitude=" + longitude + ", " : "") +
                (address != null ? "address=" + address + ", " : "") +
                (email != null ? "email=" + email + ", " : "") +
                (phoneNumner != null ? "phoneNumner=" + phoneNumner + ", " : "") +
                (parentId != null ? "parentId=" + parentId + ", " : "") +
                (orgUnitGroupId != null ? "orgUnitGroupId=" + orgUnitGroupId + ", " : "") +
                (attributeValuesId != null ? "attributeValuesId=" + attributeValuesId + ", " : "") +
            "}";
    }

}
