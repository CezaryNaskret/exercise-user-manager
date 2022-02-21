package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.WebsiteUser;
import com.mycompany.myapp.repository.WebsiteUserRepository;
import com.mycompany.myapp.service.criteria.WebsiteUserCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link WebsiteUser} entities in the database.
 * The main input is a {@link WebsiteUserCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link WebsiteUser} or a {@link Page} of {@link WebsiteUser} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WebsiteUserQueryService extends QueryService<WebsiteUser> {

    private final Logger log = LoggerFactory.getLogger(WebsiteUserQueryService.class);

    private final WebsiteUserRepository websiteUserRepository;

    public WebsiteUserQueryService(WebsiteUserRepository websiteUserRepository) {
        this.websiteUserRepository = websiteUserRepository;
    }

    /**
     * Return a {@link List} of {@link WebsiteUser} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<WebsiteUser> findByCriteria(WebsiteUserCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<WebsiteUser> specification = createSpecification(criteria);
        return websiteUserRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link WebsiteUser} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<WebsiteUser> findByCriteria(WebsiteUserCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<WebsiteUser> specification = createSpecification(criteria);
        return websiteUserRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(WebsiteUserCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<WebsiteUser> specification = createSpecification(criteria);
        return websiteUserRepository.count(specification);
    }

    /**
     * Function to convert {@link WebsiteUserCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<WebsiteUser> createSpecification(WebsiteUserCriteria criteria) {
        Specification<WebsiteUser> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), WebsiteUser_.id));
            }
            if (criteria.getLogin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLogin(), WebsiteUser_.login));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), WebsiteUser_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), WebsiteUser_.lastName));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), WebsiteUser_.email));
            }
            if (criteria.getLanguage() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLanguage(), WebsiteUser_.language));
            }
            if (criteria.getProfiles() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProfiles(), WebsiteUser_.profiles));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), WebsiteUser_.createdDate));
            }
            if (criteria.getModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModifiedDate(), WebsiteUser_.modifiedDate));
            }
            if (criteria.getActivated() != null) {
                specification = specification.and(buildSpecification(criteria.getActivated(), WebsiteUser_.activated));
            }
            if (criteria.getPassword() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPassword(), WebsiteUser_.password));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(WebsiteUser_.users, JoinType.LEFT).get(User_.id))
                    );
            }
        }
        return specification;
    }
}
