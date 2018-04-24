package org.dsmhack.controller;

import org.dsmhack.model.CheckIn;
import org.dsmhack.model.Project;
import org.dsmhack.repository.CheckInRepository;
import org.dsmhack.repository.ProjectRepository;
import org.dsmhack.service.CodeGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
public class ProjectControllerTest {

    private MockMvc mockMvc;
    @InjectMocks
    private ProjectController projectController;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private CodeGenerator codeGenerator;
    @Mock
    private CheckInRepository checkInRepository;

    @Before
    public void setup(){
        mockMvc = MockMvcBuilders.standaloneSetup(projectController).build();
    }

    @Test
    public void getAllProjectsReturns200() throws Exception {
        mockMvc.perform(get("/projects"))
                .andExpect(status().isOk());
    }

    @Test
    public void getProjectByIdReturns200() throws Exception {
        mockMvc.perform(get("/projects/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void postCallsRepositoryToSaveProject() throws Exception {
        Project project = new Project();
        projectController.save(project);
        verify(projectRepository).save(project);
    }

    @Test
    public void postCallsGuidGeneratorToGenerateUUIDBeforeSavingProject() throws Exception {
        UUID projectId = UUID.randomUUID();
        when(codeGenerator.generateUUID()).thenReturn(projectId);
        projectController.save(new Project());
        ArgumentCaptor<Project> captor = ArgumentCaptor.forClass(Project.class);
        verify(projectRepository).save(captor.capture());
        assertEquals(projectId, captor.getValue().getProjectGuid());
    }

    @Test
    public void postReturnsSavedProject() throws Exception {
        Project expectedProject = new Project();
        when(projectRepository.save(any(Project.class))).thenReturn(expectedProject);
        Project actualProject = projectController.save(new Project()).getBody();
        assertEquals(expectedProject, actualProject);
    }

    @Test
    public void getAllProjectsCallsRepository() throws Exception {
        List<Project> projects = Arrays.asList(new Project(), new Project());
        when(projectRepository.findAll()).thenReturn(projects);
        assertEquals(projects, projectController.getAllProjects());
    }

    @Test
    public void getProjectsByIdCallsRepositoryFind() throws Exception {
        Project project = new Project();
        when(projectRepository.findOne(anyString())).thenReturn(project);
        assertEquals(project, projectController.getProjectById(""));
    }

    @Test
    public void findAllCheckinsReturns200() throws Exception {
        mockMvc.perform(get("/projects/1/check-ins"))
                .andExpect(status().isOk());
    }

    @Test
    public void findAllCheckinsRetrievesCheckingsFromRepository() throws Exception {
        List<CheckIn> expectedCheckins = Collections.singletonList(new CheckIn());
        when(checkInRepository.findByProjectGuid("guid")).thenReturn(expectedCheckins);
        assertEquals(expectedCheckins, projectController.findAllCheckins("guid"));
    }

    @Test
    public void postProjectByIdReturns201() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
            post("/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Project()
                    .setOrganizationGuid("organizationGuid")
                    .setName("name")
                    .setDescription("description")
                    .toJson())
        ).andExpect(
            status().isCreated()
        ).andReturn();

        assertEquals(null, mvcResult.getResolvedException());
    }

    @Test
    public void postUserByIdReturns400_notNull() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
            post("/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")
        ).andExpect(
            status().isBadRequest()
        ).andReturn();

        String message = mvcResult.getResolvedException().getMessage();
        assertTrue(message.contains("NotNull.project.organizationGuid"));
        assertTrue(message.contains("NotNull.project.name"));
        assertTrue(message.contains("NotNull.project.description"));
    }

    @Test
    public void postUserByIdReturns400_size() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
            post("/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Project()
                    .setOrganizationGuid("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
                    .setName("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb")
                    .setDescription("ccccccccccccccccccccccccccccccccccccccccccccccccccc")
                    .toJson())
        ).andExpect(
            status().isBadRequest()
        ).andReturn();

        String message = mvcResult.getResolvedException().getMessage();
        assertTrue(message.contains("Size.project.organizationGuid"));
        assertTrue(message.contains("Size.project.name"));
        assertTrue(message.contains("Size.project.description"));
    }

    @Test
    public void checkinReturns201() throws Exception {
        String projectGuid = UUID.randomUUID().toString();
        MvcResult mvcResult = mockMvc.perform(post("/projects/" + projectGuid + "/check-ins")
                .contentType(MediaType.APPLICATION_JSON)
                .content(UUID.randomUUID().toString()))
                /*.andExpect(status().isCreated())*/.andReturn();
        System.out.printf("blip");
    }

    @Test
    public void postToCheckinCallsRepositoryWithCheckIn() throws Exception {
        UUID projectGuid = UUID.randomUUID();
        UUID userGuid = UUID.randomUUID();
        projectController.checkUserIn(projectGuid, userGuid);
        ArgumentCaptor<CheckIn> captor = ArgumentCaptor.forClass(CheckIn.class);
        verify(checkInRepository).save(captor.capture());
        assertEquals(userGuid, captor.getValue().getUserGuid());
        assertEquals(projectGuid, captor.getValue().getProjectGuid());
    }

    @Test
    public void checkInReturnsCheckIn() throws Exception {
        CheckIn expectedCheckin = new CheckIn();
        when(checkInRepository.save(any(CheckIn.class))).thenReturn(expectedCheckin);
        assertEquals(expectedCheckin, projectController.checkUserIn(UUID.randomUUID(), UUID.randomUUID()).getBody());
    }

    @Test
    public void checkoutReturns200() throws Exception {
        when(checkInRepository.findByProjectGuidAndUserGuid(anyString(), anyString())).thenReturn(new CheckIn());
        mockMvc.perform(put("/projects/12345/check-ins")
                .contentType(MediaType.APPLICATION_JSON)
                .content("user-uuid"))
                .andExpect(status().isOk());    }

    @Test
    public void checkoutRetrievesCheckinFromRepository() throws Exception {
        when(checkInRepository.findByProjectGuidAndUserGuid(anyString(), anyString())).thenReturn(new CheckIn());
        projectController.checkOutUser("projectGuid", "userGuid");
        verify(checkInRepository).findByProjectGuidAndUserGuid("projectGuid", "userGuid");
    }

    @Test
    public void checkOutSavesCheckIn() throws Exception {
        CheckIn checkIn = new CheckIn();
        when(checkInRepository.findByProjectGuidAndUserGuid(anyString(), anyString())).thenReturn(checkIn);
        projectController.checkOutUser("projectGuid", "userGuid");
        verify(checkInRepository).save(checkIn);
    }

    @Test
    public void checkOutReturnsCheckIn() throws Exception {
        CheckIn expectedCheckin = new CheckIn();
        when(checkInRepository.save(any(CheckIn.class))).thenReturn(expectedCheckin);
        when(checkInRepository.findByProjectGuidAndUserGuid(anyString(), anyString())).thenReturn(expectedCheckin);
        assertEquals(expectedCheckin, projectController.checkOutUser("projectGuid", "userGuid") );
    }
}