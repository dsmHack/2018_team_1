package org.dsmhack.controller;

import org.dsmhack.model.User;
import org.dsmhack.repository.UserRepository;
import org.dsmhack.service.CodeGenerator;
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
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserControllerTest {

    private MockMvc mockMvc;
    @InjectMocks
    private UserController userController;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CodeGenerator codeGenerator;

    @Test
    public void getUserByIdReturns200() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void postCallsRepositoryToSaveUser() throws Exception {
        User user = new User();
        userController.save(user);
        verify(userRepository).save(user);
    }

    //todo: modify this to return a 201 rather than a 200
    @Test
    public void postUserByIdReturns200() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        MvcResult mvcResult = mockMvc.perform(
            post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"jdoe@example.com\",\"role\":\"admin\"}")
        ).andExpect(
            status().isOk()
        ).andReturn();

        assertEquals(null, mvcResult.getResolvedException());
    }

    @Test
    public void postUserByIdReturns400() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        MvcResult mvcResult = mockMvc.perform(
            post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")
        ).andExpect(
            status().isBadRequest()
        ).andReturn();

        assertTrue(mvcResult.getResolvedException().getMessage().contains("NotNull.user.firstName"));
        assertTrue(mvcResult.getResolvedException().getMessage().contains("NotNull.user.lastName"));
        assertTrue(mvcResult.getResolvedException().getMessage().contains("NotNull.user.email"));
        assertTrue(mvcResult.getResolvedException().getMessage().contains("NotNull.user.role"));
    }

    //todo: need to add tests specifically asserting "First Name is required.", "First Name cannot be larger than x and less than y", etc. This will require figuring out how to get better text output.

    //todo: need to pull in the gson library to create a toJson method in User something like new Gson().toJson(this); Might consider working into a factory concept to keep these test files small

    @Test
    public void postCallsGuidGeneratorToGenerateUUIDBeforeSavingUser() throws Exception {
        String userId = "uuid";
        when(codeGenerator.generateUUID()).thenReturn(userId);
        userController.save(new User());
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertEquals(userId, captor.getValue().getUserGuid());
    }

    @Test
    public void postReturnsSavedUser() throws Exception {
        User expectedUser = new User();
        when(userRepository.save(any(User.class))).thenReturn(expectedUser);
        User actualUser = userController.save(new User());
        assertEquals(expectedUser, actualUser);
    }

    @Test
    public void getAllUsersReturnsUsersFromRepository() throws Exception {
        List<User> users = Arrays.asList(new User(), new User());
        when(userRepository.findAll()).thenReturn(users);
        assertEquals(users, userController.getAllUsers());
    }

    @Test
    public void getUserByIdReturnsUserFromRepository() throws Exception {
        User user = new User();
        when(userRepository.findOne(anyString())).thenReturn(user);
        assertEquals(user, userController.getUserById(""));
    }
}