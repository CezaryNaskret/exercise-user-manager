package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.WebsiteUser;
import com.mycompany.myapp.repository.WebsiteUserRepository;
import com.mycompany.myapp.service.WebsiteUserQueryService;
import com.mycompany.myapp.service.WebsiteUserService;
import com.mycompany.myapp.service.criteria.WebsiteUserCriteria;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.WebsiteUser}.
 */
@RestController
@RequestMapping("/api")
public class WebsiteUserResource {

    private final Logger log = LoggerFactory.getLogger(WebsiteUserResource.class);

    private static final String ENTITY_NAME = "websiteUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WebsiteUserService websiteUserService;

    private final WebsiteUserRepository websiteUserRepository;

    private final WebsiteUserQueryService websiteUserQueryService;

    public WebsiteUserResource(
        WebsiteUserService websiteUserService,
        WebsiteUserRepository websiteUserRepository,
        WebsiteUserQueryService websiteUserQueryService
    ) {
        this.websiteUserService = websiteUserService;
        this.websiteUserRepository = websiteUserRepository;
        this.websiteUserQueryService = websiteUserQueryService;
    }

    /**
     * {@code POST  /website-users} : Create a new websiteUser.
     *
     * @param websiteUser the websiteUser to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new websiteUser, or with status {@code 400 (Bad Request)} if the websiteUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/website-users")
    public ResponseEntity<WebsiteUser> createWebsiteUser(@Valid @RequestBody WebsiteUser websiteUser) throws URISyntaxException {
        log.debug("REST request to save WebsiteUser : {}", websiteUser);
        if (websiteUser.getId() != null) {
            throw new BadRequestAlertException("A new websiteUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WebsiteUser result = websiteUserService.save(websiteUser);
        return ResponseEntity
            .created(new URI("/api/website-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /website-users/:id} : Updates an existing websiteUser.
     *
     * @param id the id of the websiteUser to save.
     * @param websiteUser the websiteUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated websiteUser,
     * or with status {@code 400 (Bad Request)} if the websiteUser is not valid,
     * or with status {@code 500 (Internal Server Error)} if the websiteUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/website-users/{id}")
    public ResponseEntity<WebsiteUser> updateWebsiteUser(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WebsiteUser websiteUser
    ) throws URISyntaxException {
        log.debug("REST request to update WebsiteUser : {}, {}", id, websiteUser);
        if (websiteUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, websiteUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!websiteUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WebsiteUser result = websiteUserService.save(websiteUser);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, websiteUser.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /website-users/:id} : Partial updates given fields of an existing websiteUser, field will ignore if it is null
     *
     * @param id the id of the websiteUser to save.
     * @param websiteUser the websiteUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated websiteUser,
     * or with status {@code 400 (Bad Request)} if the websiteUser is not valid,
     * or with status {@code 404 (Not Found)} if the websiteUser is not found,
     * or with status {@code 500 (Internal Server Error)} if the websiteUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/website-users/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WebsiteUser> partialUpdateWebsiteUser(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WebsiteUser websiteUser
    ) throws URISyntaxException {
        log.debug("REST request to partial update WebsiteUser partially : {}, {}", id, websiteUser);
        if (websiteUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, websiteUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!websiteUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WebsiteUser> result = websiteUserService.partialUpdate(websiteUser);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, websiteUser.getId().toString())
        );
    }

    /**
     * {@code GET  /website-users} : get all the websiteUsers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of websiteUsers in body.
     */
    @GetMapping("/website-users")
    public ResponseEntity<List<WebsiteUser>> getAllWebsiteUsers(
        WebsiteUserCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get WebsiteUsers by criteria: {}", criteria);
        Page<WebsiteUser> page = websiteUserQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /website-users/count} : count all the websiteUsers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/website-users/count")
    public ResponseEntity<Long> countWebsiteUsers(WebsiteUserCriteria criteria) {
        log.debug("REST request to count WebsiteUsers by criteria: {}", criteria);
        return ResponseEntity.ok().body(websiteUserQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /website-users/:id} : get the "id" websiteUser.
     *
     * @param id the id of the websiteUser to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the websiteUser, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/website-users/{id}")
    public ResponseEntity<WebsiteUser> getWebsiteUser(@PathVariable Long id) {
        log.debug("REST request to get WebsiteUser : {}", id);
        Optional<WebsiteUser> websiteUser = websiteUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(websiteUser);
    }

    /**
     * {@code DELETE  /website-users/:id} : delete the "id" websiteUser.
     *
     * @param id the id of the websiteUser to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/website-users/{id}")
    public ResponseEntity<Void> deleteWebsiteUser(@PathVariable Long id) {
        log.debug("REST request to delete WebsiteUser : {}", id);
        websiteUserService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
