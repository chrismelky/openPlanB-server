package org.openplanrep.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

import io.github.jhipster.service.filter.InstantFilter;




/**
 * Criteria class for the OrgUnitGroup entity. This class is used in OrgUnitGroupResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /org-unit-groups?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class OrgUnitGroupCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter uid;

    private StringFilter code;

    private StringFilter name;

    private InstantFilter created;

    private InstantFilter lastUpdate;

    private LongFilter orgUnitGroupSetId;

    private LongFilter attributeValuesId;

    public OrgUnitGroupCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getUid() {
        return uid;
    }

    public void setUid(StringFilter uid) {
        this.uid = uid;
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

    public InstantFilter getCreated() {
        return created;
    }

    public void setCreated(InstantFilter created) {
        this.created = created;
    }

    public InstantFilter getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(InstantFilter lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public LongFilter getOrgUnitGroupSetId() {
        return orgUnitGroupSetId;
    }

    public void setOrgUnitGroupSetId(LongFilter orgUnitGroupSetId) {
        this.orgUnitGroupSetId = orgUnitGroupSetId;
    }

    public LongFilter getAttributeValuesId() {
        return attributeValuesId;
    }

    public void setAttributeValuesId(LongFilter attributeValuesId) {
        this.attributeValuesId = attributeValuesId;
    }

    @Override
    public String toString() {
        return "OrgUnitGroupCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (uid != null ? "uid=" + uid + ", " : "") +
                (code != null ? "code=" + code + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (created != null ? "created=" + created + ", " : "") +
                (lastUpdate != null ? "lastUpdate=" + lastUpdate + ", " : "") +
                (orgUnitGroupSetId != null ? "orgUnitGroupSetId=" + orgUnitGroupSetId + ", " : "") +
                (attributeValuesId != null ? "attributeValuesId=" + attributeValuesId + ", " : "") +
            "}";
    }

}
