package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.WebsiteUser;
import com.mycompany.myapp.repository.WebsiteUserRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link WebsiteUser}.
 */
@Service
@Transactional
public class WebsiteUserService {

    private final Logger log = LoggerFactory.getLogger(WebsiteUserService.class);

    private final WebsiteUserRepository websiteUserRepository;

    public WebsiteUserService(WebsiteUserRepository websiteUserRepository) {
        this.websiteUserRepository = websiteUserRepository;
    }

    /**
     * Save a websiteUser.
     *
     * @param websiteUser the entity to save.
     * @return the persisted entity.
     */
    public WebsiteUser save(WebsiteUser websiteUser) {
        log.debug("Request to save WebsiteUser : {}", websiteUser);
        return websiteUserRepository.save(websiteUser);
    }

    /**
     * Partially update a websiteUser.
     *
     * @param websiteUser the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<WebsiteUser> partialUpdate(WebsiteUser websiteUser) {
        log.debug("Request to partially update WebsiteUser : {}", websiteUser);

        return websiteUserRepository
            .findById(websiteUser.getId())
            .map(existingWebsiteUser -> {
                if (websiteUser.getLogin() != null) {
                    existingWebsiteUser.setLogin(websiteUser.getLogin());
                }
                if (websiteUser.getFirstName() != null) {
                    existingWebsiteUser.setFirstName(websiteUser.getFirstName());
                }
                if (websiteUser.getLastName() != null) {
                    existingWebsiteUser.setLastName(websiteUser.getLastName());
                }
                if (websiteUser.getEmail() != null) {
                    existingWebsiteUser.setEmail(websiteUser.getEmail());
                }
                if (websiteUser.getLanguage() != null) {
                    existingWebsiteUser.setLanguage(websiteUser.getLanguage());
                }
                if (websiteUser.getProfiles() != null) {
                    existingWebsiteUser.setProfiles(websiteUser.getProfiles());
                }
                if (websiteUser.getCreatedDate() != null) {
                    existingWebsiteUser.setCreatedDate(websiteUser.getCreatedDate());
                }
                if (websiteUser.getModifiedDate() != null) {
                    existingWebsiteUser.setModifiedDate(websiteUser.getModifiedDate());
                }
                if (websiteUser.getActivated() != null) {
                    existingWebsiteUser.setActivated(websiteUser.getActivated());
                }
                if (websiteUser.getPassword() != null) {
                    existingWebsiteUser.setPassword(websiteUser.getPassword());
                }

                return existingWebsiteUser;
            })
            .map(websiteUserRepository::save);
    }

    /**
     * Get all the websiteUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<WebsiteUser> findAll(Pageable pageable) {
        log.debug("Request to get all WebsiteUsers");
        return websiteUserRepository.findAll(pageable);
    }

    /**
     * Get all the websiteUsers with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<WebsiteUser> findAllWithEagerRelationships(Pageable pageable) {
        return websiteUserRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one websiteUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WebsiteUser> findOne(Long id) {
        log.debug("Request to get WebsiteUser : {}", id);
        return websiteUserRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the websiteUser by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete WebsiteUser : {}", id);
        websiteUserRepository.deleteById(id);
    }
}
