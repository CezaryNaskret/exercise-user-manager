package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.WebsiteUser;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the WebsiteUser entity.
 */
@Repository
public interface WebsiteUserRepository extends JpaRepository<WebsiteUser, Long>, JpaSpecificationExecutor<WebsiteUser> {
    @Query(
        value = "select distinct websiteUser from WebsiteUser websiteUser left join fetch websiteUser.users",
        countQuery = "select count(distinct websiteUser) from WebsiteUser websiteUser"
    )
    Page<WebsiteUser> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct websiteUser from WebsiteUser websiteUser left join fetch websiteUser.users")
    List<WebsiteUser> findAllWithEagerRelationships();

    @Query("select websiteUser from WebsiteUser websiteUser left join fetch websiteUser.users where websiteUser.id =:id")
    Optional<WebsiteUser> findOneWithEagerRelationships(@Param("id") Long id);
}
