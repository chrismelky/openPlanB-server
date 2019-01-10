package org.openplanrep.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the Attribute entity. This class is used in AttributeResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /attributes?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AttributeCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter uuid;

    private StringFilter code;

    private StringFilter name;

    private StringFilter valueType;

    private BooleanFilter mandatory;

    private BooleanFilter orgUnitGroupSetAttribute;

    private BooleanFilter orgUnitGroupAttribute;

    private BooleanFilter orgUnitAttribute;

    private BooleanFilter userAttribute;

    private IntegerFilter sortOrder;

    private LongFilter optionSetId;

    public AttributeCriteria() {
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

    public StringFilter getValueType() {
        return valueType;
    }

    public void setValueType(StringFilter valueType) {
        this.valueType = valueType;
    }

    public BooleanFilter getMandatory() {
        return mandatory;
    }

    public void setMandatory(BooleanFilter mandatory) {
        this.mandatory = mandatory;
    }

    public BooleanFilter getOrgUnitGroupSetAttribute() {
        return orgUnitGroupSetAttribute;
    }

    public void setOrgUnitGroupSetAttribute(BooleanFilter orgUnitGroupSetAttribute) {
        this.orgUnitGroupSetAttribute = orgUnitGroupSetAttribute;
    }

    public BooleanFilter getOrgUnitGroupAttribute() {
        return orgUnitGroupAttribute;
    }

    public void setOrgUnitGroupAttribute(BooleanFilter orgUnitGroupAttribute) {
        this.orgUnitGroupAttribute = orgUnitGroupAttribute;
    }

    public BooleanFilter getOrgUnitAttribute() {
        return orgUnitAttribute;
    }

    public void setOrgUnitAttribute(BooleanFilter orgUnitAttribute) {
        this.orgUnitAttribute = orgUnitAttribute;
    }

    public BooleanFilter getUserAttribute() {
        return userAttribute;
    }

    public void setUserAttribute(BooleanFilter userAttribute) {
        this.userAttribute = userAttribute;
    }

    public IntegerFilter getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(IntegerFilter sortOrder) {
        this.sortOrder = sortOrder;
    }

    public LongFilter getOptionSetId() {
        return optionSetId;
    }

    public void setOptionSetId(LongFilter optionSetId) {
        this.optionSetId = optionSetId;
    }

    @Override
    public String toString() {
        return "AttributeCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (uuid != null ? "uuid=" + uuid + ", " : "") +
                (code != null ? "code=" + code + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (valueType != null ? "valueType=" + valueType + ", " : "") +
                (mandatory != null ? "mandatory=" + mandatory + ", " : "") +
                (orgUnitGroupSetAttribute != null ? "orgUnitGroupSetAttribute=" + orgUnitGroupSetAttribute + ", " : "") +
                (orgUnitGroupAttribute != null ? "orgUnitGroupAttribute=" + orgUnitGroupAttribute + ", " : "") +
                (orgUnitAttribute != null ? "orgUnitAttribute=" + orgUnitAttribute + ", " : "") +
                (userAttribute != null ? "userAttribute=" + userAttribute + ", " : "") +
                (sortOrder != null ? "sortOrder=" + sortOrder + ", " : "") +
                (optionSetId != null ? "optionSetId=" + optionSetId + ", " : "") +
            "}";
    }

}
