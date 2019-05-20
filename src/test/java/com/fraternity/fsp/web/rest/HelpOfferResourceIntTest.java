package com.fraternity.fsp.web.rest;

import com.fraternity.fsp.FraternityApp;

import com.fraternity.fsp.domain.HelpOffer;
import com.fraternity.fsp.domain.HelpAction;
import com.fraternity.fsp.domain.User;
import com.fraternity.fsp.domain.Category;
import com.fraternity.fsp.repository.HelpOfferRepository;
import com.fraternity.fsp.service.HelpOfferService;
import com.fraternity.fsp.web.rest.errors.ExceptionTranslator;
import com.fraternity.fsp.service.dto.HelpOfferCriteria;
import com.fraternity.fsp.service.HelpOfferQueryService;

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
 * Test class for the HelpOfferResource REST controller.
 *
 * @see HelpOfferResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FraternityApp.class)
public class HelpOfferResourceIntTest {

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
    private HelpOfferRepository helpOfferRepository;

    @Autowired
    private HelpOfferService helpOfferService;

    @Autowired
    private HelpOfferQueryService helpOfferQueryService;

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

    private MockMvc restHelpOfferMockMvc;

    private HelpOffer helpOffer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final HelpOfferResource helpOfferResource = new HelpOfferResource(helpOfferService, helpOfferQueryService);
        this.restHelpOfferMockMvc = MockMvcBuilders.standaloneSetup(helpOfferResource)
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
    public static HelpOffer createEntity(EntityManager em) {
        HelpOffer helpOffer = new HelpOffer()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .datePost(DEFAULT_DATE_POST)
            .dateStart(DEFAULT_DATE_START)
            .dateEnd(DEFAULT_DATE_END);
        return helpOffer;
    }

    @Before
    public void initTest() {
        helpOffer = createEntity(em);
    }

    @Test
    @Transactional
    public void createHelpOffer() throws Exception {
        int databaseSizeBeforeCreate = helpOfferRepository.findAll().size();

        // Create the HelpOffer
        restHelpOfferMockMvc.perform(post("/api/help-offers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(helpOffer)))
            .andExpect(status().isCreated());

        // Validate the HelpOffer in the database
        List<HelpOffer> helpOfferList = helpOfferRepository.findAll();
        assertThat(helpOfferList).hasSize(databaseSizeBeforeCreate + 1);
        HelpOffer testHelpOffer = helpOfferList.get(helpOfferList.size() - 1);
        assertThat(testHelpOffer.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testHelpOffer.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testHelpOffer.getDatePost()).isEqualTo(DEFAULT_DATE_POST);
        assertThat(testHelpOffer.getDateStart()).isEqualTo(DEFAULT_DATE_START);
        assertThat(testHelpOffer.getDateEnd()).isEqualTo(DEFAULT_DATE_END);
    }

    @Test
    @Transactional
    public void createHelpOfferWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = helpOfferRepository.findAll().size();

        // Create the HelpOffer with an existing ID
        helpOffer.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHelpOfferMockMvc.perform(post("/api/help-offers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(helpOffer)))
            .andExpect(status().isBadRequest());

        // Validate the HelpOffer in the database
        List<HelpOffer> helpOfferList = helpOfferRepository.findAll();
        assertThat(helpOfferList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllHelpOffers() throws Exception {
        // Initialize the database
        helpOfferRepository.saveAndFlush(helpOffer);

        // Get all the helpOfferList
        restHelpOfferMockMvc.perform(get("/api/help-offers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(helpOffer.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].datePost").value(hasItem(DEFAULT_DATE_POST.toString())))
            .andExpect(jsonPath("$.[*].dateStart").value(hasItem(DEFAULT_DATE_START.toString())))
            .andExpect(jsonPath("$.[*].dateEnd").value(hasItem(DEFAULT_DATE_END.toString())));
    }
    
    @Test
    @Transactional
    public void getHelpOffer() throws Exception {
        // Initialize the database
        helpOfferRepository.saveAndFlush(helpOffer);

        // Get the helpOffer
        restHelpOfferMockMvc.perform(get("/api/help-offers/{id}", helpOffer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(helpOffer.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.datePost").value(DEFAULT_DATE_POST.toString()))
            .andExpect(jsonPath("$.dateStart").value(DEFAULT_DATE_START.toString()))
            .andExpect(jsonPath("$.dateEnd").value(DEFAULT_DATE_END.toString()));
    }

    @Test
    @Transactional
    public void getAllHelpOffersByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        helpOfferRepository.saveAndFlush(helpOffer);

        // Get all the helpOfferList where title equals to DEFAULT_TITLE
        defaultHelpOfferShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the helpOfferList where title equals to UPDATED_TITLE
        defaultHelpOfferShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllHelpOffersByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        helpOfferRepository.saveAndFlush(helpOffer);

        // Get all the helpOfferList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultHelpOfferShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the helpOfferList where title equals to UPDATED_TITLE
        defaultHelpOfferShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllHelpOffersByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        helpOfferRepository.saveAndFlush(helpOffer);

        // Get all the helpOfferList where title is not null
        defaultHelpOfferShouldBeFound("title.specified=true");

        // Get all the helpOfferList where title is null
        defaultHelpOfferShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    public void getAllHelpOffersByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        helpOfferRepository.saveAndFlush(helpOffer);

        // Get all the helpOfferList where description equals to DEFAULT_DESCRIPTION
        defaultHelpOfferShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the helpOfferList where description equals to UPDATED_DESCRIPTION
        defaultHelpOfferShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllHelpOffersByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        helpOfferRepository.saveAndFlush(helpOffer);

        // Get all the helpOfferList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultHelpOfferShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the helpOfferList where description equals to UPDATED_DESCRIPTION
        defaultHelpOfferShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllHelpOffersByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        helpOfferRepository.saveAndFlush(helpOffer);

        // Get all the helpOfferList where description is not null
        defaultHelpOfferShouldBeFound("description.specified=true");

        // Get all the helpOfferList where description is null
        defaultHelpOfferShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllHelpOffersByDatePostIsEqualToSomething() throws Exception {
        // Initialize the database
        helpOfferRepository.saveAndFlush(helpOffer);

        // Get all the helpOfferList where datePost equals to DEFAULT_DATE_POST
        defaultHelpOfferShouldBeFound("datePost.equals=" + DEFAULT_DATE_POST);

        // Get all the helpOfferList where datePost equals to UPDATED_DATE_POST
        defaultHelpOfferShouldNotBeFound("datePost.equals=" + UPDATED_DATE_POST);
    }

    @Test
    @Transactional
    public void getAllHelpOffersByDatePostIsInShouldWork() throws Exception {
        // Initialize the database
        helpOfferRepository.saveAndFlush(helpOffer);

        // Get all the helpOfferList where datePost in DEFAULT_DATE_POST or UPDATED_DATE_POST
        defaultHelpOfferShouldBeFound("datePost.in=" + DEFAULT_DATE_POST + "," + UPDATED_DATE_POST);

        // Get all the helpOfferList where datePost equals to UPDATED_DATE_POST
        defaultHelpOfferShouldNotBeFound("datePost.in=" + UPDATED_DATE_POST);
    }

    @Test
    @Transactional
    public void getAllHelpOffersByDatePostIsNullOrNotNull() throws Exception {
        // Initialize the database
        helpOfferRepository.saveAndFlush(helpOffer);

        // Get all the helpOfferList where datePost is not null
        defaultHelpOfferShouldBeFound("datePost.specified=true");

        // Get all the helpOfferList where datePost is null
        defaultHelpOfferShouldNotBeFound("datePost.specified=false");
    }

    @Test
    @Transactional
    public void getAllHelpOffersByDatePostIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        helpOfferRepository.saveAndFlush(helpOffer);

        // Get all the helpOfferList where datePost greater than or equals to DEFAULT_DATE_POST
        defaultHelpOfferShouldBeFound("datePost.greaterOrEqualThan=" + DEFAULT_DATE_POST);

        // Get all the helpOfferList where datePost greater than or equals to UPDATED_DATE_POST
        defaultHelpOfferShouldNotBeFound("datePost.greaterOrEqualThan=" + UPDATED_DATE_POST);
    }

    @Test
    @Transactional
    public void getAllHelpOffersByDatePostIsLessThanSomething() throws Exception {
        // Initialize the database
        helpOfferRepository.saveAndFlush(helpOffer);

        // Get all the helpOfferList where datePost less than or equals to DEFAULT_DATE_POST
        defaultHelpOfferShouldNotBeFound("datePost.lessThan=" + DEFAULT_DATE_POST);

        // Get all the helpOfferList where datePost less than or equals to UPDATED_DATE_POST
        defaultHelpOfferShouldBeFound("datePost.lessThan=" + UPDATED_DATE_POST);
    }


    @Test
    @Transactional
    public void getAllHelpOffersByDateStartIsEqualToSomething() throws Exception {
        // Initialize the database
        helpOfferRepository.saveAndFlush(helpOffer);

        // Get all the helpOfferList where dateStart equals to DEFAULT_DATE_START
        defaultHelpOfferShouldBeFound("dateStart.equals=" + DEFAULT_DATE_START);

        // Get all the helpOfferList where dateStart equals to UPDATED_DATE_START
        defaultHelpOfferShouldNotBeFound("dateStart.equals=" + UPDATED_DATE_START);
    }

    @Test
    @Transactional
    public void getAllHelpOffersByDateStartIsInShouldWork() throws Exception {
        // Initialize the database
        helpOfferRepository.saveAndFlush(helpOffer);

        // Get all the helpOfferList where dateStart in DEFAULT_DATE_START or UPDATED_DATE_START
        defaultHelpOfferShouldBeFound("dateStart.in=" + DEFAULT_DATE_START + "," + UPDATED_DATE_START);

        // Get all the helpOfferList where dateStart equals to UPDATED_DATE_START
        defaultHelpOfferShouldNotBeFound("dateStart.in=" + UPDATED_DATE_START);
    }

    @Test
    @Transactional
    public void getAllHelpOffersByDateStartIsNullOrNotNull() throws Exception {
        // Initialize the database
        helpOfferRepository.saveAndFlush(helpOffer);

        // Get all the helpOfferList where dateStart is not null
        defaultHelpOfferShouldBeFound("dateStart.specified=true");

        // Get all the helpOfferList where dateStart is null
        defaultHelpOfferShouldNotBeFound("dateStart.specified=false");
    }

    @Test
    @Transactional
    public void getAllHelpOffersByDateStartIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        helpOfferRepository.saveAndFlush(helpOffer);

        // Get all the helpOfferList where dateStart greater than or equals to DEFAULT_DATE_START
        defaultHelpOfferShouldBeFound("dateStart.greaterOrEqualThan=" + DEFAULT_DATE_START);

        // Get all the helpOfferList where dateStart greater than or equals to UPDATED_DATE_START
        defaultHelpOfferShouldNotBeFound("dateStart.greaterOrEqualThan=" + UPDATED_DATE_START);
    }

    @Test
    @Transactional
    public void getAllHelpOffersByDateStartIsLessThanSomething() throws Exception {
        // Initialize the database
        helpOfferRepository.saveAndFlush(helpOffer);

        // Get all the helpOfferList where dateStart less than or equals to DEFAULT_DATE_START
        defaultHelpOfferShouldNotBeFound("dateStart.lessThan=" + DEFAULT_DATE_START);

        // Get all the helpOfferList where dateStart less than or equals to UPDATED_DATE_START
        defaultHelpOfferShouldBeFound("dateStart.lessThan=" + UPDATED_DATE_START);
    }


    @Test
    @Transactional
    public void getAllHelpOffersByDateEndIsEqualToSomething() throws Exception {
        // Initialize the database
        helpOfferRepository.saveAndFlush(helpOffer);

        // Get all the helpOfferList where dateEnd equals to DEFAULT_DATE_END
        defaultHelpOfferShouldBeFound("dateEnd.equals=" + DEFAULT_DATE_END);

        // Get all the helpOfferList where dateEnd equals to UPDATED_DATE_END
        defaultHelpOfferShouldNotBeFound("dateEnd.equals=" + UPDATED_DATE_END);
    }

    @Test
    @Transactional
    public void getAllHelpOffersByDateEndIsInShouldWork() throws Exception {
        // Initialize the database
        helpOfferRepository.saveAndFlush(helpOffer);

        // Get all the helpOfferList where dateEnd in DEFAULT_DATE_END or UPDATED_DATE_END
        defaultHelpOfferShouldBeFound("dateEnd.in=" + DEFAULT_DATE_END + "," + UPDATED_DATE_END);

        // Get all the helpOfferList where dateEnd equals to UPDATED_DATE_END
        defaultHelpOfferShouldNotBeFound("dateEnd.in=" + UPDATED_DATE_END);
    }

    @Test
    @Transactional
    public void getAllHelpOffersByDateEndIsNullOrNotNull() throws Exception {
        // Initialize the database
        helpOfferRepository.saveAndFlush(helpOffer);

        // Get all the helpOfferList where dateEnd is not null
        defaultHelpOfferShouldBeFound("dateEnd.specified=true");

        // Get all the helpOfferList where dateEnd is null
        defaultHelpOfferShouldNotBeFound("dateEnd.specified=false");
    }

    @Test
    @Transactional
    public void getAllHelpOffersByDateEndIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        helpOfferRepository.saveAndFlush(helpOffer);

        // Get all the helpOfferList where dateEnd greater than or equals to DEFAULT_DATE_END
        defaultHelpOfferShouldBeFound("dateEnd.greaterOrEqualThan=" + DEFAULT_DATE_END);

        // Get all the helpOfferList where dateEnd greater than or equals to UPDATED_DATE_END
        defaultHelpOfferShouldNotBeFound("dateEnd.greaterOrEqualThan=" + UPDATED_DATE_END);
    }

    @Test
    @Transactional
    public void getAllHelpOffersByDateEndIsLessThanSomething() throws Exception {
        // Initialize the database
        helpOfferRepository.saveAndFlush(helpOffer);

        // Get all the helpOfferList where dateEnd less than or equals to DEFAULT_DATE_END
        defaultHelpOfferShouldNotBeFound("dateEnd.lessThan=" + DEFAULT_DATE_END);

        // Get all the helpOfferList where dateEnd less than or equals to UPDATED_DATE_END
        defaultHelpOfferShouldBeFound("dateEnd.lessThan=" + UPDATED_DATE_END);
    }


    @Test
    @Transactional
    public void getAllHelpOffersByHelpOIsEqualToSomething() throws Exception {
        // Initialize the database
        HelpAction helpO = HelpActionResourceIntTest.createEntity(em);
        em.persist(helpO);
        em.flush();
        helpOffer.setHelpO(helpO);
        helpOfferRepository.saveAndFlush(helpOffer);
        Long helpOId = helpO.getId();

        // Get all the helpOfferList where helpO equals to helpOId
        defaultHelpOfferShouldBeFound("helpOId.equals=" + helpOId);

        // Get all the helpOfferList where helpO equals to helpOId + 1
        defaultHelpOfferShouldNotBeFound("helpOId.equals=" + (helpOId + 1));
    }


    @Test
    @Transactional
    public void getAllHelpOffersByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        helpOffer.setUser(user);
        helpOfferRepository.saveAndFlush(helpOffer);
        Long userId = user.getId();

        // Get all the helpOfferList where user equals to userId
        defaultHelpOfferShouldBeFound("userId.equals=" + userId);

        // Get all the helpOfferList where user equals to userId + 1
        defaultHelpOfferShouldNotBeFound("userId.equals=" + (userId + 1));
    }


    @Test
    @Transactional
    public void getAllHelpOffersByCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        Category category = CategoryResourceIntTest.createEntity(em);
        em.persist(category);
        em.flush();
        helpOffer.setCategory(category);
        helpOfferRepository.saveAndFlush(helpOffer);
        Long categoryId = category.getId();

        // Get all the helpOfferList where category equals to categoryId
        defaultHelpOfferShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the helpOfferList where category equals to categoryId + 1
        defaultHelpOfferShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultHelpOfferShouldBeFound(String filter) throws Exception {
        restHelpOfferMockMvc.perform(get("/api/help-offers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(helpOffer.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].datePost").value(hasItem(DEFAULT_DATE_POST.toString())))
            .andExpect(jsonPath("$.[*].dateStart").value(hasItem(DEFAULT_DATE_START.toString())))
            .andExpect(jsonPath("$.[*].dateEnd").value(hasItem(DEFAULT_DATE_END.toString())));

        // Check, that the count call also returns 1
        restHelpOfferMockMvc.perform(get("/api/help-offers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultHelpOfferShouldNotBeFound(String filter) throws Exception {
        restHelpOfferMockMvc.perform(get("/api/help-offers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHelpOfferMockMvc.perform(get("/api/help-offers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingHelpOffer() throws Exception {
        // Get the helpOffer
        restHelpOfferMockMvc.perform(get("/api/help-offers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHelpOffer() throws Exception {
        // Initialize the database
        helpOfferService.save(helpOffer);

        int databaseSizeBeforeUpdate = helpOfferRepository.findAll().size();

        // Update the helpOffer
        HelpOffer updatedHelpOffer = helpOfferRepository.findById(helpOffer.getId()).get();
        // Disconnect from session so that the updates on updatedHelpOffer are not directly saved in db
        em.detach(updatedHelpOffer);
        updatedHelpOffer
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .datePost(UPDATED_DATE_POST)
            .dateStart(UPDATED_DATE_START)
            .dateEnd(UPDATED_DATE_END);

        restHelpOfferMockMvc.perform(put("/api/help-offers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedHelpOffer)))
            .andExpect(status().isOk());

        // Validate the HelpOffer in the database
        List<HelpOffer> helpOfferList = helpOfferRepository.findAll();
        assertThat(helpOfferList).hasSize(databaseSizeBeforeUpdate);
        HelpOffer testHelpOffer = helpOfferList.get(helpOfferList.size() - 1);
        assertThat(testHelpOffer.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testHelpOffer.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testHelpOffer.getDatePost()).isEqualTo(UPDATED_DATE_POST);
        assertThat(testHelpOffer.getDateStart()).isEqualTo(UPDATED_DATE_START);
        assertThat(testHelpOffer.getDateEnd()).isEqualTo(UPDATED_DATE_END);
    }

    @Test
    @Transactional
    public void updateNonExistingHelpOffer() throws Exception {
        int databaseSizeBeforeUpdate = helpOfferRepository.findAll().size();

        // Create the HelpOffer

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHelpOfferMockMvc.perform(put("/api/help-offers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(helpOffer)))
            .andExpect(status().isBadRequest());

        // Validate the HelpOffer in the database
        List<HelpOffer> helpOfferList = helpOfferRepository.findAll();
        assertThat(helpOfferList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteHelpOffer() throws Exception {
        // Initialize the database
        helpOfferService.save(helpOffer);

        int databaseSizeBeforeDelete = helpOfferRepository.findAll().size();

        // Delete the helpOffer
        restHelpOfferMockMvc.perform(delete("/api/help-offers/{id}", helpOffer.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<HelpOffer> helpOfferList = helpOfferRepository.findAll();
        assertThat(helpOfferList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HelpOffer.class);
        HelpOffer helpOffer1 = new HelpOffer();
        helpOffer1.setId(1L);
        HelpOffer helpOffer2 = new HelpOffer();
        helpOffer2.setId(helpOffer1.getId());
        assertThat(helpOffer1).isEqualTo(helpOffer2);
        helpOffer2.setId(2L);
        assertThat(helpOffer1).isNotEqualTo(helpOffer2);
        helpOffer1.setId(null);
        assertThat(helpOffer1).isNotEqualTo(helpOffer2);
    }
}
