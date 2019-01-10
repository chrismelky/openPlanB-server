package org.openplanrep.repository;

import org.openplanrep.domain.OrganisationUnit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the OrganisationUnit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrganisationUnitRepository extends JpaRepository<OrganisationUnit, Long>, JpaSpecificationExecutor<OrganisationUnit> {

    @Query(value = "select distinct organisation_unit from OrganisationUnit organisation_unit left join fetch organisation_unit.attributeValues",
        countQuery = "select count(distinct organisation_unit) from OrganisationUnit organisation_unit")
    Page<OrganisationUnit> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct organisation_unit from OrganisationUnit organisation_unit left join fetch organisation_unit.attributeValues")
    List<OrganisationUnit> findAllWithEagerRelationships();

    @Query("select organisation_unit from OrganisationUnit organisation_unit left join fetch organisation_unit.attributeValues where organisation_unit.id =:id")
    Optional<OrganisationUnit> findOneWithEagerRelationships(@Param("id") Long id);

}
