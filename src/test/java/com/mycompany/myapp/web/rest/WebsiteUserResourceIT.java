package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.domain.WebsiteUser;
import com.mycompany.myapp.repository.WebsiteUserRepository;
import com.mycompany.myapp.service.WebsiteUserService;
import com.mycompany.myapp.service.criteria.WebsiteUserCriteria;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link WebsiteUserResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class WebsiteUserResourceIT {

    private static final String DEFAULT_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_LOGIN = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_LANGUAGE = "AAAAAAAAAA";
    private static final String UPDATED_LANGUAGE = "BBBBBBBBBB";

    private static final String DEFAULT_PROFILES = "AAAAAAAAAA";
    private static final String UPDATED_PROFILES = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATED_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_MODIFIED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MODIFIED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_MODIFIED_DATE = LocalDate.ofEpochDay(-1L);

    private static final Boolean DEFAULT_ACTIVATED = false;
    private static final Boolean UPDATED_ACTIVATED = true;

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/website-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WebsiteUserRepository websiteUserRepository;

    @Mock
    private WebsiteUserRepository websiteUserRepositoryMock;

    @Mock
    private WebsiteUserService websiteUserServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWebsiteUserMockMvc;

    private WebsiteUser websiteUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WebsiteUser createEntity(EntityManager em) {
        WebsiteUser websiteUser = new WebsiteUser()
            .login(DEFAULT_LOGIN)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .language(DEFAULT_LANGUAGE)
            .profiles(DEFAULT_PROFILES)
            .createdDate(DEFAULT_CREATED_DATE)
            .modifiedDate(DEFAULT_MODIFIED_DATE)
            .activated(DEFAULT_ACTIVATED)
            .password(DEFAULT_PASSWORD);
        return websiteUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WebsiteUser createUpdatedEntity(EntityManager em) {
        WebsiteUser websiteUser = new WebsiteUser()
            .login(UPDATED_LOGIN)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .language(UPDATED_LANGUAGE)
            .profiles(UPDATED_PROFILES)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE)
            .activated(UPDATED_ACTIVATED)
            .password(UPDATED_PASSWORD);
        return websiteUser;
    }

    @BeforeEach
    public void initTest() {
        websiteUser = createEntity(em);
    }

    @Test
    @Transactional
    void createWebsiteUser() throws Exception {
        int databaseSizeBeforeCreate = websiteUserRepository.findAll().size();
        // Create the WebsiteUser
        restWebsiteUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(websiteUser)))
            .andExpect(status().isCreated());

        // Validate the WebsiteUser in the database
        List<WebsiteUser> websiteUserList = websiteUserRepository.findAll();
        assertThat(websiteUserList).hasSize(databaseSizeBeforeCreate + 1);
        WebsiteUser testWebsiteUser = websiteUserList.get(websiteUserList.size() - 1);
        assertThat(testWebsiteUser.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(testWebsiteUser.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testWebsiteUser.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testWebsiteUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testWebsiteUser.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
        assertThat(testWebsiteUser.getProfiles()).isEqualTo(DEFAULT_PROFILES);
        assertThat(testWebsiteUser.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testWebsiteUser.getModifiedDate()).isEqualTo(DEFAULT_MODIFIED_DATE);
        assertThat(testWebsiteUser.getActivated()).isEqualTo(DEFAULT_ACTIVATED);
        assertThat(testWebsiteUser.getPassword()).isEqualTo(DEFAULT_PASSWORD);
    }

    @Test
    @Transactional
    void createWebsiteUserWithExistingId() throws Exception {
        // Create the WebsiteUser with an existing ID
        websiteUser.setId(1L);

        int databaseSizeBeforeCreate = websiteUserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWebsiteUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(websiteUser)))
            .andExpect(status().isBadRequest());

        // Validate the WebsiteUser in the database
        List<WebsiteUser> websiteUserList = websiteUserRepository.findAll();
        assertThat(websiteUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLoginIsRequired() throws Exception {
        int databaseSizeBeforeTest = websiteUserRepository.findAll().size();
        // set the field null
        websiteUser.setLogin(null);

        // Create the WebsiteUser, which fails.

        restWebsiteUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(websiteUser)))
            .andExpect(status().isBadRequest());

        List<WebsiteUser> websiteUserList = websiteUserRepository.findAll();
        assertThat(websiteUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPasswordIsRequired() throws Exception {
        int databaseSizeBeforeTest = websiteUserRepository.findAll().size();
        // set the field null
        websiteUser.setPassword(null);

        // Create the WebsiteUser, which fails.

        restWebsiteUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(websiteUser)))
            .andExpect(status().isBadRequest());

        List<WebsiteUser> websiteUserList = websiteUserRepository.findAll();
        assertThat(websiteUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWebsiteUsers() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList
        restWebsiteUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(websiteUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE)))
            .andExpect(jsonPath("$.[*].profiles").value(hasItem(DEFAULT_PROFILES)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED.booleanValue())))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWebsiteUsersWithEagerRelationshipsIsEnabled() throws Exception {
        when(websiteUserServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restWebsiteUserMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(websiteUserServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWebsiteUsersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(websiteUserServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restWebsiteUserMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(websiteUserServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getWebsiteUser() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get the websiteUser
        restWebsiteUserMockMvc
            .perform(get(ENTITY_API_URL_ID, websiteUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(websiteUser.getId().intValue()))
            .andExpect(jsonPath("$.login").value(DEFAULT_LOGIN))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE))
            .andExpect(jsonPath("$.profiles").value(DEFAULT_PROFILES))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.activated").value(DEFAULT_ACTIVATED.booleanValue()))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD));
    }

    @Test
    @Transactional
    void getWebsiteUsersByIdFiltering() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        Long id = websiteUser.getId();

        defaultWebsiteUserShouldBeFound("id.equals=" + id);
        defaultWebsiteUserShouldNotBeFound("id.notEquals=" + id);

        defaultWebsiteUserShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultWebsiteUserShouldNotBeFound("id.greaterThan=" + id);

        defaultWebsiteUserShouldBeFound("id.lessThanOrEqual=" + id);
        defaultWebsiteUserShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByLoginIsEqualToSomething() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList where login equals to DEFAULT_LOGIN
        defaultWebsiteUserShouldBeFound("login.equals=" + DEFAULT_LOGIN);

        // Get all the websiteUserList where login equals to UPDATED_LOGIN
        defaultWebsiteUserShouldNotBeFound("login.equals=" + UPDATED_LOGIN);
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByLoginIsNotEqualToSomething() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList where login not equals to DEFAULT_LOGIN
        defaultWebsiteUserShouldNotBeFound("login.notEquals=" + DEFAULT_LOGIN);

        // Get all the websiteUserList where login not equals to UPDATED_LOGIN
        defaultWebsiteUserShouldBeFound("login.notEquals=" + UPDATED_LOGIN);
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByLoginIsInShouldWork() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList where login in DEFAULT_LOGIN or UPDATED_LOGIN
        defaultWebsiteUserShouldBeFound("login.in=" + DEFAULT_LOGIN + "," + UPDATED_LOGIN);

        // Get all the websiteUserList where login equals to UPDATED_LOGIN
        defaultWebsiteUserShouldNotBeFound("login.in=" + UPDATED_LOGIN);
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByLoginIsNullOrNotNull() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList where login is not null
        defaultWebsiteUserShouldBeFound("login.specified=true");

        // Get all the websiteUserList where login is null
        defaultWebsiteUserShouldNotBeFound("login.specified=false");
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByLoginContainsSomething() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList where login contains DEFAULT_LOGIN
        defaultWebsiteUserShouldBeFound("login.contains=" + DEFAULT_LOGIN);

        // Get all the websiteUserList where login contains UPDATED_LOGIN
        defaultWebsiteUserShouldNotBeFound("login.contains=" + UPDATED_LOGIN);
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByLoginNotContainsSomething() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList where login does not contain DEFAULT_LOGIN
        defaultWebsiteUserShouldNotBeFound("login.doesNotContain=" + DEFAULT_LOGIN);

        // Get all the websiteUserList where login does not contain UPDATED_LOGIN
        defaultWebsiteUserShouldBeFound("login.doesNotContain=" + UPDATED_LOGIN);
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList where firstName equals to DEFAULT_FIRST_NAME
        defaultWebsiteUserShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the websiteUserList where firstName equals to UPDATED_FIRST_NAME
        defaultWebsiteUserShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByFirstNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList where firstName not equals to DEFAULT_FIRST_NAME
        defaultWebsiteUserShouldNotBeFound("firstName.notEquals=" + DEFAULT_FIRST_NAME);

        // Get all the websiteUserList where firstName not equals to UPDATED_FIRST_NAME
        defaultWebsiteUserShouldBeFound("firstName.notEquals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultWebsiteUserShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the websiteUserList where firstName equals to UPDATED_FIRST_NAME
        defaultWebsiteUserShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList where firstName is not null
        defaultWebsiteUserShouldBeFound("firstName.specified=true");

        // Get all the websiteUserList where firstName is null
        defaultWebsiteUserShouldNotBeFound("firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList where firstName contains DEFAULT_FIRST_NAME
        defaultWebsiteUserShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the websiteUserList where firstName contains UPDATED_FIRST_NAME
        defaultWebsiteUserShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList where firstName does not contain DEFAULT_FIRST_NAME
        defaultWebsiteUserShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the websiteUserList where firstName does not contain UPDATED_FIRST_NAME
        defaultWebsiteUserShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList where lastName equals to DEFAULT_LAST_NAME
        defaultWebsiteUserShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the websiteUserList where lastName equals to UPDATED_LAST_NAME
        defaultWebsiteUserShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByLastNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList where lastName not equals to DEFAULT_LAST_NAME
        defaultWebsiteUserShouldNotBeFound("lastName.notEquals=" + DEFAULT_LAST_NAME);

        // Get all the websiteUserList where lastName not equals to UPDATED_LAST_NAME
        defaultWebsiteUserShouldBeFound("lastName.notEquals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultWebsiteUserShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the websiteUserList where lastName equals to UPDATED_LAST_NAME
        defaultWebsiteUserShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList where lastName is not null
        defaultWebsiteUserShouldBeFound("lastName.specified=true");

        // Get all the websiteUserList where lastName is null
        defaultWebsiteUserShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByLastNameContainsSomething() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList where lastName contains DEFAULT_LAST_NAME
        defaultWebsiteUserShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the websiteUserList where lastName contains UPDATED_LAST_NAME
        defaultWebsiteUserShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList where lastName does not contain DEFAULT_LAST_NAME
        defaultWebsiteUserShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the websiteUserList where lastName does not contain UPDATED_LAST_NAME
        defaultWebsiteUserShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList where email equals to DEFAULT_EMAIL
        defaultWebsiteUserShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the websiteUserList where email equals to UPDATED_EMAIL
        defaultWebsiteUserShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList where email not equals to DEFAULT_EMAIL
        defaultWebsiteUserShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the websiteUserList where email not equals to UPDATED_EMAIL
        defaultWebsiteUserShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultWebsiteUserShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the websiteUserList where email equals to UPDATED_EMAIL
        defaultWebsiteUserShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList where email is not null
        defaultWebsiteUserShouldBeFound("email.specified=true");

        // Get all the websiteUserList where email is null
        defaultWebsiteUserShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByEmailContainsSomething() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList where email contains DEFAULT_EMAIL
        defaultWebsiteUserShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the websiteUserList where email contains UPDATED_EMAIL
        defaultWebsiteUserShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList where email does not contain DEFAULT_EMAIL
        defaultWebsiteUserShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the websiteUserList where email does not contain UPDATED_EMAIL
        defaultWebsiteUserShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByLanguageIsEqualToSomething() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList where language equals to DEFAULT_LANGUAGE
        defaultWebsiteUserShouldBeFound("language.equals=" + DEFAULT_LANGUAGE);

        // Get all the websiteUserList where language equals to UPDATED_LANGUAGE
        defaultWebsiteUserShouldNotBeFound("language.equals=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByLanguageIsNotEqualToSomething() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList where language not equals to DEFAULT_LANGUAGE
        defaultWebsiteUserShouldNotBeFound("language.notEquals=" + DEFAULT_LANGUAGE);

        // Get all the websiteUserList where language not equals to UPDATED_LANGUAGE
        defaultWebsiteUserShouldBeFound("language.notEquals=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByLanguageIsInShouldWork() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList where language in DEFAULT_LANGUAGE or UPDATED_LANGUAGE
        defaultWebsiteUserShouldBeFound("language.in=" + DEFAULT_LANGUAGE + "," + UPDATED_LANGUAGE);

        // Get all the websiteUserList where language equals to UPDATED_LANGUAGE
        defaultWebsiteUserShouldNotBeFound("language.in=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByLanguageIsNullOrNotNull() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList where language is not null
        defaultWebsiteUserShouldBeFound("language.specified=true");

        // Get all the websiteUserList where language is null
        defaultWebsiteUserShouldNotBeFound("language.specified=false");
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByLanguageContainsSomething() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList where language contains DEFAULT_LANGUAGE
        defaultWebsiteUserShouldBeFound("language.contains=" + DEFAULT_LANGUAGE);

        // Get all the websiteUserList where language contains UPDATED_LANGUAGE
        defaultWebsiteUserShouldNotBeFound("language.contains=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByLanguageNotContainsSomething() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList where language does not contain DEFAULT_LANGUAGE
        defaultWebsiteUserShouldNotBeFound("language.doesNotContain=" + DEFAULT_LANGUAGE);

        // Get all the websiteUserList where language does not contain UPDATED_LANGUAGE
        defaultWebsiteUserShouldBeFound("language.doesNotContain=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByProfilesIsEqualToSomething() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList where profiles equals to DEFAULT_PROFILES
        defaultWebsiteUserShouldBeFound("profiles.equals=" + DEFAULT_PROFILES);

        // Get all the websiteUserList where profiles equals to UPDATED_PROFILES
        defaultWebsiteUserShouldNotBeFound("profiles.equals=" + UPDATED_PROFILES);
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByProfilesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList where profiles not equals to DEFAULT_PROFILES
        defaultWebsiteUserShouldNotBeFound("profiles.notEquals=" + DEFAULT_PROFILES);

        // Get all the websiteUserList where profiles not equals to UPDATED_PROFILES
        defaultWebsiteUserShouldBeFound("profiles.notEquals=" + UPDATED_PROFILES);
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByProfilesIsInShouldWork() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList where profiles in DEFAULT_PROFILES or UPDATED_PROFILES
        defaultWebsiteUserShouldBeFound("profiles.in=" + DEFAULT_PROFILES + "," + UPDATED_PROFILES);

        // Get all the websiteUserList where profiles equals to UPDATED_PROFILES
        defaultWebsiteUserShouldNotBeFound("profiles.in=" + UPDATED_PROFILES);
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByProfilesIsNullOrNotNull() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList where profiles is not null
        defaultWebsiteUserShouldBeFound("profiles.specified=true");

        // Get all the websiteUserList where profiles is null
        defaultWebsiteUserShouldNotBeFound("profiles.specified=false");
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByProfilesContainsSomething() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList where profiles contains DEFAULT_PROFILES
        defaultWebsiteUserShouldBeFound("profiles.contains=" + DEFAULT_PROFILES);

        // Get all the websiteUserList where profiles contains UPDATED_PROFILES
        defaultWebsiteUserShouldNotBeFound("profiles.contains=" + UPDATED_PROFILES);
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByProfilesNotContainsSomething() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList where profiles does not contain DEFAULT_PROFILES
        defaultWebsiteUserShouldNotBeFound("profiles.doesNotContain=" + DEFAULT_PROFILES);

        // Get all the websiteUserList where profiles does not contain UPDATED_PROFILES
        defaultWebsiteUserShouldBeFound("profiles.doesNotContain=" + UPDATED_PROFILES);
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList where createdDate equals to DEFAULT_CREATED_DATE
        defaultWebsiteUserShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the websiteUserList where createdDate equals to UPDATED_CREATED_DATE
        defaultWebsiteUserShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByCreatedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList where createdDate not equals to DEFAULT_CREATED_DATE
        defaultWebsiteUserShouldNotBeFound("createdDate.notEquals=" + DEFAULT_CREATED_DATE);

        // Get all the websiteUserList where createdDate not equals to UPDATED_CREATED_DATE
        defaultWebsiteUserShouldBeFound("createdDate.notEquals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultWebsiteUserShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the websiteUserList where createdDate equals to UPDATED_CREATED_DATE
        defaultWebsiteUserShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList where createdDate is not null
        defaultWebsiteUserShouldBeFound("createdDate.specified=true");

        // Get all the websiteUserList where createdDate is null
        defaultWebsiteUserShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByCreatedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList where createdDate is greater than or equal to DEFAULT_CREATED_DATE
        defaultWebsiteUserShouldBeFound("createdDate.greaterThanOrEqual=" + DEFAULT_CREATED_DATE);

        // Get all the websiteUserList where createdDate is greater than or equal to UPDATED_CREATED_DATE
        defaultWebsiteUserShouldNotBeFound("createdDate.greaterThanOrEqual=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByCreatedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList where createdDate is less than or equal to DEFAULT_CREATED_DATE
        defaultWebsiteUserShouldBeFound("createdDate.lessThanOrEqual=" + DEFAULT_CREATED_DATE);

        // Get all the websiteUserList where createdDate is less than or equal to SMALLER_CREATED_DATE
        defaultWebsiteUserShouldNotBeFound("createdDate.lessThanOrEqual=" + SMALLER_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByCreatedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList where createdDate is less than DEFAULT_CREATED_DATE
        defaultWebsiteUserShouldNotBeFound("createdDate.lessThan=" + DEFAULT_CREATED_DATE);

        // Get all the websiteUserList where createdDate is less than UPDATED_CREATED_DATE
        defaultWebsiteUserShouldBeFound("createdDate.lessThan=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByCreatedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList where createdDate is greater than DEFAULT_CREATED_DATE
        defaultWebsiteUserShouldNotBeFound("createdDate.greaterThan=" + DEFAULT_CREATED_DATE);

        // Get all the websiteUserList where createdDate is greater than SMALLER_CREATED_DATE
        defaultWebsiteUserShouldBeFound("createdDate.greaterThan=" + SMALLER_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList where modifiedDate equals to DEFAULT_MODIFIED_DATE
        defaultWebsiteUserShouldBeFound("modifiedDate.equals=" + DEFAULT_MODIFIED_DATE);

        // Get all the websiteUserList where modifiedDate equals to UPDATED_MODIFIED_DATE
        defaultWebsiteUserShouldNotBeFound("modifiedDate.equals=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByModifiedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList where modifiedDate not equals to DEFAULT_MODIFIED_DATE
        defaultWebsiteUserShouldNotBeFound("modifiedDate.notEquals=" + DEFAULT_MODIFIED_DATE);

        // Get all the websiteUserList where modifiedDate not equals to UPDATED_MODIFIED_DATE
        defaultWebsiteUserShouldBeFound("modifiedDate.notEquals=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList where modifiedDate in DEFAULT_MODIFIED_DATE or UPDATED_MODIFIED_DATE
        defaultWebsiteUserShouldBeFound("modifiedDate.in=" + DEFAULT_MODIFIED_DATE + "," + UPDATED_MODIFIED_DATE);

        // Get all the websiteUserList where modifiedDate equals to UPDATED_MODIFIED_DATE
        defaultWebsiteUserShouldNotBeFound("modifiedDate.in=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList where modifiedDate is not null
        defaultWebsiteUserShouldBeFound("modifiedDate.specified=true");

        // Get all the websiteUserList where modifiedDate is null
        defaultWebsiteUserShouldNotBeFound("modifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByModifiedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList where modifiedDate is greater than or equal to DEFAULT_MODIFIED_DATE
        defaultWebsiteUserShouldBeFound("modifiedDate.greaterThanOrEqual=" + DEFAULT_MODIFIED_DATE);

        // Get all the websiteUserList where modifiedDate is greater than or equal to UPDATED_MODIFIED_DATE
        defaultWebsiteUserShouldNotBeFound("modifiedDate.greaterThanOrEqual=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByModifiedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList where modifiedDate is less than or equal to DEFAULT_MODIFIED_DATE
        defaultWebsiteUserShouldBeFound("modifiedDate.lessThanOrEqual=" + DEFAULT_MODIFIED_DATE);

        // Get all the websiteUserList where modifiedDate is less than or equal to SMALLER_MODIFIED_DATE
        defaultWebsiteUserShouldNotBeFound("modifiedDate.lessThanOrEqual=" + SMALLER_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByModifiedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList where modifiedDate is less than DEFAULT_MODIFIED_DATE
        defaultWebsiteUserShouldNotBeFound("modifiedDate.lessThan=" + DEFAULT_MODIFIED_DATE);

        // Get all the websiteUserList where modifiedDate is less than UPDATED_MODIFIED_DATE
        defaultWebsiteUserShouldBeFound("modifiedDate.lessThan=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByModifiedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList where modifiedDate is greater than DEFAULT_MODIFIED_DATE
        defaultWebsiteUserShouldNotBeFound("modifiedDate.greaterThan=" + DEFAULT_MODIFIED_DATE);

        // Get all the websiteUserList where modifiedDate is greater than SMALLER_MODIFIED_DATE
        defaultWebsiteUserShouldBeFound("modifiedDate.greaterThan=" + SMALLER_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByActivatedIsEqualToSomething() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList where activated equals to DEFAULT_ACTIVATED
        defaultWebsiteUserShouldBeFound("activated.equals=" + DEFAULT_ACTIVATED);

        // Get all the websiteUserList where activated equals to UPDATED_ACTIVATED
        defaultWebsiteUserShouldNotBeFound("activated.equals=" + UPDATED_ACTIVATED);
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByActivatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList where activated not equals to DEFAULT_ACTIVATED
        defaultWebsiteUserShouldNotBeFound("activated.notEquals=" + DEFAULT_ACTIVATED);

        // Get all the websiteUserList where activated not equals to UPDATED_ACTIVATED
        defaultWebsiteUserShouldBeFound("activated.notEquals=" + UPDATED_ACTIVATED);
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByActivatedIsInShouldWork() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList where activated in DEFAULT_ACTIVATED or UPDATED_ACTIVATED
        defaultWebsiteUserShouldBeFound("activated.in=" + DEFAULT_ACTIVATED + "," + UPDATED_ACTIVATED);

        // Get all the websiteUserList where activated equals to UPDATED_ACTIVATED
        defaultWebsiteUserShouldNotBeFound("activated.in=" + UPDATED_ACTIVATED);
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByActivatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList where activated is not null
        defaultWebsiteUserShouldBeFound("activated.specified=true");

        // Get all the websiteUserList where activated is null
        defaultWebsiteUserShouldNotBeFound("activated.specified=false");
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByPasswordIsEqualToSomething() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList where password equals to DEFAULT_PASSWORD
        defaultWebsiteUserShouldBeFound("password.equals=" + DEFAULT_PASSWORD);

        // Get all the websiteUserList where password equals to UPDATED_PASSWORD
        defaultWebsiteUserShouldNotBeFound("password.equals=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByPasswordIsNotEqualToSomething() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList where password not equals to DEFAULT_PASSWORD
        defaultWebsiteUserShouldNotBeFound("password.notEquals=" + DEFAULT_PASSWORD);

        // Get all the websiteUserList where password not equals to UPDATED_PASSWORD
        defaultWebsiteUserShouldBeFound("password.notEquals=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByPasswordIsInShouldWork() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList where password in DEFAULT_PASSWORD or UPDATED_PASSWORD
        defaultWebsiteUserShouldBeFound("password.in=" + DEFAULT_PASSWORD + "," + UPDATED_PASSWORD);

        // Get all the websiteUserList where password equals to UPDATED_PASSWORD
        defaultWebsiteUserShouldNotBeFound("password.in=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByPasswordIsNullOrNotNull() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList where password is not null
        defaultWebsiteUserShouldBeFound("password.specified=true");

        // Get all the websiteUserList where password is null
        defaultWebsiteUserShouldNotBeFound("password.specified=false");
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByPasswordContainsSomething() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList where password contains DEFAULT_PASSWORD
        defaultWebsiteUserShouldBeFound("password.contains=" + DEFAULT_PASSWORD);

        // Get all the websiteUserList where password contains UPDATED_PASSWORD
        defaultWebsiteUserShouldNotBeFound("password.contains=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByPasswordNotContainsSomething() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        // Get all the websiteUserList where password does not contain DEFAULT_PASSWORD
        defaultWebsiteUserShouldNotBeFound("password.doesNotContain=" + DEFAULT_PASSWORD);

        // Get all the websiteUserList where password does not contain UPDATED_PASSWORD
        defaultWebsiteUserShouldBeFound("password.doesNotContain=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllWebsiteUsersByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            user = UserResourceIT.createEntity(em);
            em.persist(user);
            em.flush();
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(user);
        em.flush();
        websiteUser.addUser(user);
        websiteUserRepository.saveAndFlush(websiteUser);
        Long userId = user.getId();

        // Get all the websiteUserList where user equals to userId
        defaultWebsiteUserShouldBeFound("userId.equals=" + userId);

        // Get all the websiteUserList where user equals to (userId + 1)
        defaultWebsiteUserShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultWebsiteUserShouldBeFound(String filter) throws Exception {
        restWebsiteUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(websiteUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE)))
            .andExpect(jsonPath("$.[*].profiles").value(hasItem(DEFAULT_PROFILES)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED.booleanValue())))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)));

        // Check, that the count call also returns 1
        restWebsiteUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultWebsiteUserShouldNotBeFound(String filter) throws Exception {
        restWebsiteUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restWebsiteUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingWebsiteUser() throws Exception {
        // Get the websiteUser
        restWebsiteUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWebsiteUser() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        int databaseSizeBeforeUpdate = websiteUserRepository.findAll().size();

        // Update the websiteUser
        WebsiteUser updatedWebsiteUser = websiteUserRepository.findById(websiteUser.getId()).get();
        // Disconnect from session so that the updates on updatedWebsiteUser are not directly saved in db
        em.detach(updatedWebsiteUser);
        updatedWebsiteUser
            .login(UPDATED_LOGIN)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .language(UPDATED_LANGUAGE)
            .profiles(UPDATED_PROFILES)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE)
            .activated(UPDATED_ACTIVATED)
            .password(UPDATED_PASSWORD);

        restWebsiteUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWebsiteUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedWebsiteUser))
            )
            .andExpect(status().isOk());

        // Validate the WebsiteUser in the database
        List<WebsiteUser> websiteUserList = websiteUserRepository.findAll();
        assertThat(websiteUserList).hasSize(databaseSizeBeforeUpdate);
        WebsiteUser testWebsiteUser = websiteUserList.get(websiteUserList.size() - 1);
        assertThat(testWebsiteUser.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testWebsiteUser.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testWebsiteUser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testWebsiteUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testWebsiteUser.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testWebsiteUser.getProfiles()).isEqualTo(UPDATED_PROFILES);
        assertThat(testWebsiteUser.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testWebsiteUser.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
        assertThat(testWebsiteUser.getActivated()).isEqualTo(UPDATED_ACTIVATED);
        assertThat(testWebsiteUser.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void putNonExistingWebsiteUser() throws Exception {
        int databaseSizeBeforeUpdate = websiteUserRepository.findAll().size();
        websiteUser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWebsiteUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, websiteUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(websiteUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the WebsiteUser in the database
        List<WebsiteUser> websiteUserList = websiteUserRepository.findAll();
        assertThat(websiteUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWebsiteUser() throws Exception {
        int databaseSizeBeforeUpdate = websiteUserRepository.findAll().size();
        websiteUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWebsiteUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(websiteUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the WebsiteUser in the database
        List<WebsiteUser> websiteUserList = websiteUserRepository.findAll();
        assertThat(websiteUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWebsiteUser() throws Exception {
        int databaseSizeBeforeUpdate = websiteUserRepository.findAll().size();
        websiteUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWebsiteUserMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(websiteUser)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WebsiteUser in the database
        List<WebsiteUser> websiteUserList = websiteUserRepository.findAll();
        assertThat(websiteUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWebsiteUserWithPatch() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        int databaseSizeBeforeUpdate = websiteUserRepository.findAll().size();

        // Update the websiteUser using partial update
        WebsiteUser partialUpdatedWebsiteUser = new WebsiteUser();
        partialUpdatedWebsiteUser.setId(websiteUser.getId());

        partialUpdatedWebsiteUser
            .login(UPDATED_LOGIN)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .profiles(UPDATED_PROFILES)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restWebsiteUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWebsiteUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWebsiteUser))
            )
            .andExpect(status().isOk());

        // Validate the WebsiteUser in the database
        List<WebsiteUser> websiteUserList = websiteUserRepository.findAll();
        assertThat(websiteUserList).hasSize(databaseSizeBeforeUpdate);
        WebsiteUser testWebsiteUser = websiteUserList.get(websiteUserList.size() - 1);
        assertThat(testWebsiteUser.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testWebsiteUser.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testWebsiteUser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testWebsiteUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testWebsiteUser.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
        assertThat(testWebsiteUser.getProfiles()).isEqualTo(UPDATED_PROFILES);
        assertThat(testWebsiteUser.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testWebsiteUser.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
        assertThat(testWebsiteUser.getActivated()).isEqualTo(DEFAULT_ACTIVATED);
        assertThat(testWebsiteUser.getPassword()).isEqualTo(DEFAULT_PASSWORD);
    }

    @Test
    @Transactional
    void fullUpdateWebsiteUserWithPatch() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        int databaseSizeBeforeUpdate = websiteUserRepository.findAll().size();

        // Update the websiteUser using partial update
        WebsiteUser partialUpdatedWebsiteUser = new WebsiteUser();
        partialUpdatedWebsiteUser.setId(websiteUser.getId());

        partialUpdatedWebsiteUser
            .login(UPDATED_LOGIN)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .language(UPDATED_LANGUAGE)
            .profiles(UPDATED_PROFILES)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE)
            .activated(UPDATED_ACTIVATED)
            .password(UPDATED_PASSWORD);

        restWebsiteUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWebsiteUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWebsiteUser))
            )
            .andExpect(status().isOk());

        // Validate the WebsiteUser in the database
        List<WebsiteUser> websiteUserList = websiteUserRepository.findAll();
        assertThat(websiteUserList).hasSize(databaseSizeBeforeUpdate);
        WebsiteUser testWebsiteUser = websiteUserList.get(websiteUserList.size() - 1);
        assertThat(testWebsiteUser.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testWebsiteUser.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testWebsiteUser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testWebsiteUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testWebsiteUser.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testWebsiteUser.getProfiles()).isEqualTo(UPDATED_PROFILES);
        assertThat(testWebsiteUser.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testWebsiteUser.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
        assertThat(testWebsiteUser.getActivated()).isEqualTo(UPDATED_ACTIVATED);
        assertThat(testWebsiteUser.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void patchNonExistingWebsiteUser() throws Exception {
        int databaseSizeBeforeUpdate = websiteUserRepository.findAll().size();
        websiteUser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWebsiteUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, websiteUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(websiteUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the WebsiteUser in the database
        List<WebsiteUser> websiteUserList = websiteUserRepository.findAll();
        assertThat(websiteUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWebsiteUser() throws Exception {
        int databaseSizeBeforeUpdate = websiteUserRepository.findAll().size();
        websiteUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWebsiteUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(websiteUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the WebsiteUser in the database
        List<WebsiteUser> websiteUserList = websiteUserRepository.findAll();
        assertThat(websiteUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWebsiteUser() throws Exception {
        int databaseSizeBeforeUpdate = websiteUserRepository.findAll().size();
        websiteUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWebsiteUserMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(websiteUser))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WebsiteUser in the database
        List<WebsiteUser> websiteUserList = websiteUserRepository.findAll();
        assertThat(websiteUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWebsiteUser() throws Exception {
        // Initialize the database
        websiteUserRepository.saveAndFlush(websiteUser);

        int databaseSizeBeforeDelete = websiteUserRepository.findAll().size();

        // Delete the websiteUser
        restWebsiteUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, websiteUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WebsiteUser> websiteUserList = websiteUserRepository.findAll();
        assertThat(websiteUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
