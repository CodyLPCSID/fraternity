package com.fraternity.fsp.web.rest;

import com.fraternity.fsp.FraternityApp;

import com.fraternity.fsp.domain.HelpRequest;
import com.fraternity.fsp.domain.User;
import com.fraternity.fsp.domain.Category;
import com.fraternity.fsp.repository.HelpRequestRepository;
import com.fraternity.fsp.service.HelpRequestService;
import com.fraternity.fsp.web.rest.errors.ExceptionTranslator;
import com.fraternity.fsp.service.dto.HelpRequestCriteria;
import com.fraternity.fsp.service.HelpRequestQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;


import static com.fraternity.fsp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the HelpRequestResource REST controller.
 *
 * @see HelpRequestResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FraternityApp.class)
public class HelpRequestResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_POST = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_POST = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_START = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_START = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_END = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_END = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private HelpRequestRepository helpRequestRepository;

    @Autowired
    private HelpRequestService helpRequestService;

    @Autowired
    private HelpRequestQueryService helpRequestQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restHelpRequestMockMvc;

    private HelpRequest helpRequest;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final HelpRequestResource helpRequestResource = new HelpRequestResource(helpRequestService, helpRequestQueryService);
        this.restHelpRequestMockMvc = MockMvcBuilders.standaloneSetup(helpRequestResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HelpRequest createEntity(EntityManager em) {
        HelpRequest helpRequest = new HelpRequest()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .datePost(DEFAULT_DATE_POST)
            .dateStart(DEFAULT_DATE_START)
            .dateEnd(DEFAULT_DATE_END);
        return helpRequest;
    }

    @Before
    public void initTest() {
        helpRequest = createEntity(em);
    }

    @Test
    @Transactional
    public void createHelpRequest() throws Exception {
        int databaseSizeBeforeCreate = helpRequestRepository.findAll().size();

        // Create the HelpRequest
        restHelpRequestMockMvc.perform(post("/api/help-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(helpRequest)))
            .andExpect(status().isCreated());

        // Validate the HelpRequest in the database
        List<HelpRequest> helpRequestList = helpRequestRepository.findAll();
        assertThat(helpRequestList).hasSize(databaseSizeBeforeCreate + 1);
        HelpRequest testHelpRequest = helpRequestList.get(helpRequestList.size() - 1);
        assertThat(testHelpRequest.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testHelpRequest.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testHelpRequest.getDatePost()).isEqualTo(DEFAULT_DATE_POST);
        assertThat(testHelpRequest.getDateStart()).isEqualTo(DEFAULT_DATE_START);
        assertThat(testHelpRequest.getDateEnd()).isEqualTo(DEFAULT_DATE_END);
    }

    @Test
    @Transactional
    public void createHelpRequestWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = helpRequestRepository.findAll().size();

        // Create the HelpRequest with an existing ID
        helpRequest.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHelpRequestMockMvc.perform(post("/api/help-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(helpRequest)))
            .andExpect(status().isBadRequest());

        // Validate the HelpRequest in the database
        List<HelpRequest> helpRequestList = helpRequestRepository.findAll();
        assertThat(helpRequestList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllHelpRequests() throws Exception {
        // Initialize the database
        helpRequestRepository.saveAndFlush(helpRequest);

        // Get all the helpRequestList
        restHelpRequestMockMvc.perform(get("/api/help-requests?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(helpRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].datePost").value(hasItem(DEFAULT_DATE_POST.toString())))
            .andExpect(jsonPath("$.[*].dateStart").value(hasItem(DEFAULT_DATE_START.toString())))
            .andExpect(jsonPath("$.[*].dateEnd").value(hasItem(DEFAULT_DATE_END.toString())));
    }
    
    @Test
    @Transactional
    public void getHelpRequest() throws Exception {
        // Initialize the database
        helpRequestRepository.saveAndFlush(helpRequest);

        // Get the helpRequest
        restHelpRequestMockMvc.perform(get("/api/help-requests/{id}", helpRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(helpRequest.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.datePost").value(DEFAULT_DATE_POST.toString()))
            .andExpect(jsonPath("$.dateStart").value(DEFAULT_DATE_START.toString()))
            .andExpect(jsonPath("$.dateEnd").value(DEFAULT_DATE_END.toString()));
    }

    @Test
    @Transactional
    public void getAllHelpRequestsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        helpRequestRepository.saveAndFlush(helpRequest);

        // Get all the helpRequestList where title equals to DEFAULT_TITLE
        defaultHelpRequestShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the helpRequestList where title equals to UPDATED_TITLE
        defaultHelpRequestShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllHelpRequestsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        helpRequestRepository.saveAndFlush(helpRequest);

        // Get all the helpRequestList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultHelpRequestShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the helpRequestList where title equals to UPDATED_TITLE
        defaultHelpRequestShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllHelpRequestsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        helpRequestRepository.saveAndFlush(helpRequest);

        // Get all the helpRequestList where title is not null
        defaultHelpRequestShouldBeFound("title.specified=true");

        // Get all the helpRequestList where title is null
        defaultHelpRequestShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    public void getAllHelpRequestsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        helpRequestRepository.saveAndFlush(helpRequest);

        // Get all the helpRequestList where description equals to DEFAULT_DESCRIPTION
        defaultHelpRequestShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the helpRequestList where description equals to UPDATED_DESCRIPTION
        defaultHelpRequestShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllHelpRequestsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        helpRequestRepository.saveAndFlush(helpRequest);

        // Get all the helpRequestList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultHelpRequestShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the helpRequestList where description equals to UPDATED_DESCRIPTION
        defaultHelpRequestShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllHelpRequestsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        helpRequestRepository.saveAndFlush(helpRequest);

        // Get all the helpRequestList where description is not null
        defaultHelpRequestShouldBeFound("description.specified=true");

        // Get all the helpRequestList where description is null
        defaultHelpRequestShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllHelpRequestsByDatePostIsEqualToSomething() throws Exception {
        // Initialize the database
        helpRequestRepository.saveAndFlush(helpRequest);

        // Get all the helpRequestList where datePost equals to DEFAULT_DATE_POST
        defaultHelpRequestShouldBeFound("datePost.equals=" + DEFAULT_DATE_POST);

        // Get all the helpRequestList where datePost equals to UPDATED_DATE_POST
        defaultHelpRequestShouldNotBeFound("datePost.equals=" + UPDATED_DATE_POST);
    }

    @Test
    @Transactional
    public void getAllHelpRequestsByDatePostIsInShouldWork() throws Exception {
        // Initialize the database
        helpRequestRepository.saveAndFlush(helpRequest);

        // Get all the helpRequestList where datePost in DEFAULT_DATE_POST or UPDATED_DATE_POST
        defaultHelpRequestShouldBeFound("datePost.in=" + DEFAULT_DATE_POST + "," + UPDATED_DATE_POST);

        // Get all the helpRequestList where datePost equals to UPDATED_DATE_POST
        defaultHelpRequestShouldNotBeFound("datePost.in=" + UPDATED_DATE_POST);
    }

    @Test
    @Transactional
    public void getAllHelpRequestsByDatePostIsNullOrNotNull() throws Exception {
        // Initialize the database
        helpRequestRepository.saveAndFlush(helpRequest);

        // Get all the helpRequestList where datePost is not null
        defaultHelpRequestShouldBeFound("datePost.specified=true");

        // Get all the helpRequestList where datePost is null
        defaultHelpRequestShouldNotBeFound("datePost.specified=false");
    }

    @Test
    @Transactional
    public void getAllHelpRequestsByDatePostIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        helpRequestRepository.saveAndFlush(helpRequest);

        // Get all the helpRequestList where datePost greater than or equals to DEFAULT_DATE_POST
        defaultHelpRequestShouldBeFound("datePost.greaterOrEqualThan=" + DEFAULT_DATE_POST);

        // Get all the helpRequestList where datePost greater than or equals to UPDATED_DATE_POST
        defaultHelpRequestShouldNotBeFound("datePost.greaterOrEqualThan=" + UPDATED_DATE_POST);
    }

    @Test
    @Transactional
    public void getAllHelpRequestsByDatePostIsLessThanSomething() throws Exception {
        // Initialize the database
        helpRequestRepository.saveAndFlush(helpRequest);

        // Get all the helpRequestList where datePost less than or equals to DEFAULT_DATE_POST
        defaultHelpRequestShouldNotBeFound("datePost.lessThan=" + DEFAULT_DATE_POST);

        // Get all the helpRequestList where datePost less than or equals to UPDATED_DATE_POST
        defaultHelpRequestShouldBeFound("datePost.lessThan=" + UPDATED_DATE_POST);
    }


    @Test
    @Transactional
    public void getAllHelpRequestsByDateStartIsEqualToSomething() throws Exception {
        // Initialize the database
        helpRequestRepository.saveAndFlush(helpRequest);

        // Get all the helpRequestList where dateStart equals to DEFAULT_DATE_START
        defaultHelpRequestShouldBeFound("dateStart.equals=" + DEFAULT_DATE_START);

        // Get all the helpRequestList where dateStart equals to UPDATED_DATE_START
        defaultHelpRequestShouldNotBeFound("dateStart.equals=" + UPDATED_DATE_START);
    }

    @Test
    @Transactional
    public void getAllHelpRequestsByDateStartIsInShouldWork() throws Exception {
        // Initialize the database
        helpRequestRepository.saveAndFlush(helpRequest);

        // Get all the helpRequestList where dateStart in DEFAULT_DATE_START or UPDATED_DATE_START
        defaultHelpRequestShouldBeFound("dateStart.in=" + DEFAULT_DATE_START + "," + UPDATED_DATE_START);

        // Get all the helpRequestList where dateStart equals to UPDATED_DATE_START
        defaultHelpRequestShouldNotBeFound("dateStart.in=" + UPDATED_DATE_START);
    }

    @Test
    @Transactional
    public void getAllHelpRequestsByDateStartIsNullOrNotNull() throws Exception {
        // Initialize the database
        helpRequestRepository.saveAndFlush(helpRequest);

        // Get all the helpRequestList where dateStart is not null
        defaultHelpRequestShouldBeFound("dateStart.specified=true");

        // Get all the helpRequestList where dateStart is null
        defaultHelpRequestShouldNotBeFound("dateStart.specified=false");
    }

    @Test
    @Transactional
    public void getAllHelpRequestsByDateStartIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        helpRequestRepository.saveAndFlush(helpRequest);

        // Get all the helpRequestList where dateStart greater than or equals to DEFAULT_DATE_START
        defaultHelpRequestShouldBeFound("dateStart.greaterOrEqualThan=" + DEFAULT_DATE_START);

        // Get all the helpRequestList where dateStart greater than or equals to UPDATED_DATE_START
        defaultHelpRequestShouldNotBeFound("dateStart.greaterOrEqualThan=" + UPDATED_DATE_START);
    }

    @Test
    @Transactional
    public void getAllHelpRequestsByDateStartIsLessThanSomething() throws Exception {
        // Initialize the database
        helpRequestRepository.saveAndFlush(helpRequest);

        // Get all the helpRequestList where dateStart less than or equals to DEFAULT_DATE_START
        defaultHelpRequestShouldNotBeFound("dateStart.lessThan=" + DEFAULT_DATE_START);

        // Get all the helpRequestList where dateStart less than or equals to UPDATED_DATE_START
        defaultHelpRequestShouldBeFound("dateStart.lessThan=" + UPDATED_DATE_START);
    }


    @Test
    @Transactional
    public void getAllHelpRequestsByDateEndIsEqualToSomething() throws Exception {
        // Initialize the database
        helpRequestRepository.saveAndFlush(helpRequest);

        // Get all the helpRequestList where dateEnd equals to DEFAULT_DATE_END
        defaultHelpRequestShouldBeFound("dateEnd.equals=" + DEFAULT_DATE_END);

        // Get all the helpRequestList where dateEnd equals to UPDATED_DATE_END
        defaultHelpRequestShouldNotBeFound("dateEnd.equals=" + UPDATED_DATE_END);
    }

    @Test
    @Transactional
    public void getAllHelpRequestsByDateEndIsInShouldWork() throws Exception {
        // Initialize the database
        helpRequestRepository.saveAndFlush(helpRequest);

        // Get all the helpRequestList where dateEnd in DEFAULT_DATE_END or UPDATED_DATE_END
        defaultHelpRequestShouldBeFound("dateEnd.in=" + DEFAULT_DATE_END + "," + UPDATED_DATE_END);

        // Get all the helpRequestList where dateEnd equals to UPDATED_DATE_END
        defaultHelpRequestShouldNotBeFound("dateEnd.in=" + UPDATED_DATE_END);
    }

    @Test
    @Transactional
    public void getAllHelpRequestsByDateEndIsNullOrNotNull() throws Exception {
        // Initialize the database
        helpRequestRepository.saveAndFlush(helpRequest);

        // Get all the helpRequestList where dateEnd is not null
        defaultHelpRequestShouldBeFound("dateEnd.specified=true");

        // Get all the helpRequestList where dateEnd is null
        defaultHelpRequestShouldNotBeFound("dateEnd.specified=false");
    }

    @Test
    @Transactional
    public void getAllHelpRequestsByDateEndIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        helpRequestRepository.saveAndFlush(helpRequest);

        // Get all the helpRequestList where dateEnd greater than or equals to DEFAULT_DATE_END
        defaultHelpRequestShouldBeFound("dateEnd.greaterOrEqualThan=" + DEFAULT_DATE_END);

        // Get all the helpRequestList where dateEnd greater than or equals to UPDATED_DATE_END
        defaultHelpRequestShouldNotBeFound("dateEnd.greaterOrEqualThan=" + UPDATED_DATE_END);
    }

    @Test
    @Transactional
    public void getAllHelpRequestsByDateEndIsLessThanSomething() throws Exception {
        // Initialize the database
        helpRequestRepository.saveAndFlush(helpRequest);

        // Get all the helpRequestList where dateEnd less than or equals to DEFAULT_DATE_END
        defaultHelpRequestShouldNotBeFound("dateEnd.lessThan=" + DEFAULT_DATE_END);

        // Get all the helpRequestList where dateEnd less than or equals to UPDATED_DATE_END
        defaultHelpRequestShouldBeFound("dateEnd.lessThan=" + UPDATED_DATE_END);
    }


    @Test
    @Transactional
    public void getAllHelpRequestsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        helpRequest.setUser(user);
        helpRequestRepository.saveAndFlush(helpRequest);
        Long userId = user.getId();

        // Get all the helpRequestList where user equals to userId
        defaultHelpRequestShouldBeFound("userId.equals=" + userId);

        // Get all the helpRequestList where user equals to userId + 1
        defaultHelpRequestShouldNotBeFound("userId.equals=" + (userId + 1));
    }


    @Test
    @Transactional
    public void getAllHelpRequestsByCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        Category category = CategoryResourceIntTest.createEntity(em);
        em.persist(category);
        em.flush();
        helpRequest.setCategory(category);
        helpRequestRepository.saveAndFlush(helpRequest);
        Long categoryId = category.getId();

        // Get all the helpRequestList where category equals to categoryId
        defaultHelpRequestShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the helpRequestList where category equals to categoryId + 1
        defaultHelpRequestShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultHelpRequestShouldBeFound(String filter) throws Exception {
        restHelpRequestMockMvc.perform(get("/api/help-requests?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(helpRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].datePost").value(hasItem(DEFAULT_DATE_POST.toString())))
            .andExpect(jsonPath("$.[*].dateStart").value(hasItem(DEFAULT_DATE_START.toString())))
            .andExpect(jsonPath("$.[*].dateEnd").value(hasItem(DEFAULT_DATE_END.toString())));

        // Check, that the count call also returns 1
        restHelpRequestMockMvc.perform(get("/api/help-requests/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultHelpRequestShouldNotBeFound(String filter) throws Exception {
        restHelpRequestMockMvc.perform(get("/api/help-requests?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHelpRequestMockMvc.perform(get("/api/help-requests/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingHelpRequest() throws Exception {
        // Get the helpRequest
        restHelpRequestMockMvc.perform(get("/api/help-requests/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHelpRequest() throws Exception {
        // Initialize the database
        helpRequestService.save(helpRequest);

        int databaseSizeBeforeUpdate = helpRequestRepository.findAll().size();

        // Update the helpRequest
        HelpRequest updatedHelpRequest = helpRequestRepository.findById(helpRequest.getId()).get();
        // Disconnect from session so that the updates on updatedHelpRequest are not directly saved in db
        em.detach(updatedHelpRequest);
        updatedHelpRequest
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .datePost(UPDATED_DATE_POST)
            .dateStart(UPDATED_DATE_START)
            .dateEnd(UPDATED_DATE_END);

        restHelpRequestMockMvc.perform(put("/api/help-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedHelpRequest)))
            .andExpect(status().isOk());

        // Validate the HelpRequest in the database
        List<HelpRequest> helpRequestList = helpRequestRepository.findAll();
        assertThat(helpRequestList).hasSize(databaseSizeBeforeUpdate);
        HelpRequest testHelpRequest = helpRequestList.get(helpRequestList.size() - 1);
        assertThat(testHelpRequest.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testHelpRequest.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testHelpRequest.getDatePost()).isEqualTo(UPDATED_DATE_POST);
        assertThat(testHelpRequest.getDateStart()).isEqualTo(UPDATED_DATE_START);
        assertThat(testHelpRequest.getDateEnd()).isEqualTo(UPDATED_DATE_END);
    }

    @Test
    @Transactional
    public void updateNonExistingHelpRequest() throws Exception {
        int databaseSizeBeforeUpdate = helpRequestRepository.findAll().size();

        // Create the HelpRequest

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHelpRequestMockMvc.perform(put("/api/help-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(helpRequest)))
            .andExpect(status().isBadRequest());

        // Validate the HelpRequest in the database
        List<HelpRequest> helpRequestList = helpRequestRepository.findAll();
        assertThat(helpRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteHelpRequest() throws Exception {
        // Initialize the database
        helpRequestService.save(helpRequest);

        int databaseSizeBeforeDelete = helpRequestRepository.findAll().size();

        // Delete the helpRequest
        restHelpRequestMockMvc.perform(delete("/api/help-requests/{id}", helpRequest.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<HelpRequest> helpRequestList = helpRequestRepository.findAll();
        assertThat(helpRequestList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HelpRequest.class);
        HelpRequest helpRequest1 = new HelpRequest();
        helpRequest1.setId(1L);
        HelpRequest helpRequest2 = new HelpRequest();
        helpRequest2.setId(helpRequest1.getId());
        assertThat(helpRequest1).isEqualTo(helpRequest2);
        helpRequest2.setId(2L);
        assertThat(helpRequest1).isNotEqualTo(helpRequest2);
        helpRequest1.setId(null);
        assertThat(helpRequest1).isNotEqualTo(helpRequest2);
    }
}
