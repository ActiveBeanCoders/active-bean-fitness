package com.activebeancoders.fitness.web.rest;

import com.activebeancoders.fitness.ActiveBeanFitnessApp;
import com.activebeancoders.fitness.domain.Action;
import com.activebeancoders.fitness.repository.ActionRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.activebeancoders.fitness.domain.enumeration.ActionType;

/**
 * Test class for the ActionResource REST controller.
 *
 * @see ActionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ActiveBeanFitnessApp.class)
@WebAppConfiguration
@IntegrationTest
public class ActionResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    private static final ZonedDateTime DEFAULT_START = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_START = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_START_STR = dateTimeFormatter.format(DEFAULT_START);

    private static final Integer DEFAULT_DURATION = 1;
    private static final Integer UPDATED_DURATION = 2;
    private static final String DEFAULT_UNIT = "AAAAA";
    private static final String UPDATED_UNIT = "BBBBB";

    private static final Double DEFAULT_DISTANCE = 0D;
    private static final Double UPDATED_DISTANCE = 1D;
    private static final String DEFAULT_COMMENT = "AAAAA";
    private static final String UPDATED_COMMENT = "BBBBB";

    private static final ActionType DEFAULT_TYPE = ActionType.Running;
    private static final ActionType UPDATED_TYPE = ActionType.Walking;

    @Inject
    private ActionRepository actionRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restActionMockMvc;

    private Action action;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ActionResource actionResource = new ActionResource();
        ReflectionTestUtils.setField(actionResource, "actionRepository", actionRepository);
        this.restActionMockMvc = MockMvcBuilders.standaloneSetup(actionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        actionRepository.deleteAll();
        action = new Action();
        action.setUserId(DEFAULT_USER_ID);
        action.setStart(DEFAULT_START);
        action.setDuration(DEFAULT_DURATION);
        action.setUnit(DEFAULT_UNIT);
        action.setDistance(DEFAULT_DISTANCE);
        action.setComment(DEFAULT_COMMENT);
        action.setType(DEFAULT_TYPE);
    }

    @Test
    public void createAction() throws Exception {
        int databaseSizeBeforeCreate = actionRepository.findAll().size();

        // Create the Action

        restActionMockMvc.perform(post("/api/actions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(action)))
                .andExpect(status().isCreated());

        // Validate the Action in the database
        List<Action> actions = actionRepository.findAll();
        assertThat(actions).hasSize(databaseSizeBeforeCreate + 1);
        Action testAction = actions.get(actions.size() - 1);
        assertThat(testAction.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testAction.getStart()).isEqualTo(DEFAULT_START);
        assertThat(testAction.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testAction.getUnit()).isEqualTo(DEFAULT_UNIT);
        assertThat(testAction.getDistance()).isEqualTo(DEFAULT_DISTANCE);
        assertThat(testAction.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testAction.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    public void getAllActions() throws Exception {
        // Initialize the database
        actionRepository.save(action);

        // Get all the actions
        restActionMockMvc.perform(get("/api/actions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(action.getId())))
                .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
                .andExpect(jsonPath("$.[*].start").value(hasItem(DEFAULT_START_STR)))
                .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)))
                .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT.toString())))
                .andExpect(jsonPath("$.[*].distance").value(hasItem(DEFAULT_DISTANCE.doubleValue())))
                .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    public void getAction() throws Exception {
        // Initialize the database
        actionRepository.save(action);

        // Get the action
        restActionMockMvc.perform(get("/api/actions/{id}", action.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(action.getId()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.start").value(DEFAULT_START_STR))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION))
            .andExpect(jsonPath("$.unit").value(DEFAULT_UNIT.toString()))
            .andExpect(jsonPath("$.distance").value(DEFAULT_DISTANCE.doubleValue()))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    public void getNonExistingAction() throws Exception {
        // Get the action
        restActionMockMvc.perform(get("/api/actions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateAction() throws Exception {
        // Initialize the database
        actionRepository.save(action);
        int databaseSizeBeforeUpdate = actionRepository.findAll().size();

        // Update the action
        Action updatedAction = new Action();
        updatedAction.setId(action.getId());
        updatedAction.setUserId(UPDATED_USER_ID);
        updatedAction.setStart(UPDATED_START);
        updatedAction.setDuration(UPDATED_DURATION);
        updatedAction.setUnit(UPDATED_UNIT);
        updatedAction.setDistance(UPDATED_DISTANCE);
        updatedAction.setComment(UPDATED_COMMENT);
        updatedAction.setType(UPDATED_TYPE);

        restActionMockMvc.perform(put("/api/actions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedAction)))
                .andExpect(status().isOk());

        // Validate the Action in the database
        List<Action> actions = actionRepository.findAll();
        assertThat(actions).hasSize(databaseSizeBeforeUpdate);
        Action testAction = actions.get(actions.size() - 1);
        assertThat(testAction.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testAction.getStart()).isEqualTo(UPDATED_START);
        assertThat(testAction.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testAction.getUnit()).isEqualTo(UPDATED_UNIT);
        assertThat(testAction.getDistance()).isEqualTo(UPDATED_DISTANCE);
        assertThat(testAction.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testAction.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    public void deleteAction() throws Exception {
        // Initialize the database
        actionRepository.save(action);
        int databaseSizeBeforeDelete = actionRepository.findAll().size();

        // Get the action
        restActionMockMvc.perform(delete("/api/actions/{id}", action.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Action> actions = actionRepository.findAll();
        assertThat(actions).hasSize(databaseSizeBeforeDelete - 1);
    }
}
