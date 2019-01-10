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
 * Criteria class for the OrgUnitGroupSet entity. This class is used in OrgUnitGroupSetResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /org-unit-group-sets?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class OrgUnitGroupSetCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter uuid;

    private StringFilter code;

    private StringFilter name;

    private StringFilter description;

    private InstantFilter created;

    private InstantFilter lastUpdate;

    private LongFilter attributeValuesId;

    public OrgUnitGroupSetCriteria() {
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

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
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

    public LongFilter getAttributeValuesId() {
        return attributeValuesId;
    }

    public void setAttributeValuesId(LongFilter attributeValuesId) {
        this.attributeValuesId = attributeValuesId;
    }

    @Override
    public String toString() {
        return "OrgUnitGroupSetCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (uuid != null ? "uuid=" + uuid + ", " : "") +
                (code != null ? "code=" + code + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (created != null ? "created=" + created + ", " : "") +
                (lastUpdate != null ? "lastUpdate=" + lastUpdate + ", " : "") +
                (attributeValuesId != null ? "attributeValuesId=" + attributeValuesId + ", " : "") +
            "}";
    }

}
