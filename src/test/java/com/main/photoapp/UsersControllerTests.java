package com.main.photoapp;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.main.photoapp.Utils.UsersUtils;
import com.main.photoapp.exceptions.IncorrectUsernameFormat;
import com.main.photoapp.exceptions.UserNotFoundException;
import com.main.photoapp.models.User;
import com.main.photoapp.repositories.UsersRepository;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.main.photoapp.Utils.RandomTextGenerator.*;
import static com.main.photoapp.utils.PasswordChecker.PASSWORD_MAXIMAL_SIZE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class UsersControllerTests {

    @Autowired
    private UsersRepository repository;

    @Autowired
    private UsersUtils usersUtils;

    @Autowired
    private MockMvc mockMvc;
    private Random random;
    private final String NON_EXISTING_ID = "-100";
    private final String EMAIL_INCORRECT_FORMAT = "-12";
    private final String NON_EXISTING_USERNAME = "100000000";
    private final String USERNAME_INCORRECT_FORMAT = "-100";
    private final String PASSWORD_INCORRECT_FORMAT = "a".repeat(PASSWORD_MAXIMAL_SIZE + 1);

    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    private static final String USERNAME = "ADMINISTRATOR";

    private static final String PASSWORD = "ADMINISTRATOR";

    @BeforeEach
    public void setUp() throws Exception {
        random = new Random(0);
        repository.deleteAll();
        usersUtils.createUser(USERNAME, getRandomEmail(random), PASSWORD);
    }

    @AfterEach
    public void cleanUp() throws UserNotFoundException, IncorrectUsernameFormat {
        usersUtils.removeUser(USERNAME);
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    public void updateUserPassword_correctInput_updatesUser() throws Exception {
        String username = getRandomUsername(random);
        String email = getRandomEmail(random);
        String password = getRandomPassword(random);
        String newPassword = getRandomPassword(random);

        String id = usersUtils.createUser(username, email, password);

        mockMvc.perform(post("/user/update/password").param("id", id).param("password", newPassword)).andExpect(status().isOk());
        String actualPassword = repository.findById(Integer.valueOf(id)).get().getPassword();
        assertTrue(encoder.matches(newPassword, actualPassword));
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    public void updateUserPassword_nonExistingUser_updatesUser() throws Exception {
        String newPassword = getRandomPassword(random);
        usersUtils.createUser(random);

        mockMvc.perform(post("/user/update/password").param("id", NON_EXISTING_ID).param("password", newPassword))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("User not found"));
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    public void updateUserPassword_incorrectPasswordFormat_updatesUser() throws Exception {
        String id = usersUtils.createUser(random);

        mockMvc.perform(post("/user/update/password").param("id", id).param("password", PASSWORD_INCORRECT_FORMAT))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("Password is in incorrect format"));
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    public void updateUserEmail_correctInput_updatesUser() throws Exception {
        String username = getRandomUsername(random);
        String email = getRandomEmail(random);
        String newEmail = getRandomEmail(random);
        String password = getRandomPassword(random);

        String id = usersUtils.createUser(username, email, password);

        mockMvc.perform(post("/user/update/email").param("id", id).param("email", newEmail)).andExpect(status().isOk());
        assertEquals(newEmail, repository.findById(Integer.valueOf(id)).get().getEmail());
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    public void updateUserEmail_nonExistingUser_updatesUser() throws Exception {
        String newEmail = getRandomEmail(random);
        usersUtils.createUser(random);

        mockMvc.perform(post("/user/update/email").param("id", NON_EXISTING_ID).param("email", newEmail))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("User not found"));
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    public void updateUserEmail_incorrectEmailFormat_updatesUser() throws Exception {
        usersUtils.createUser(random);

        mockMvc.perform(post("/user/update/email").param("id", NON_EXISTING_ID).param("email", EMAIL_INCORRECT_FORMAT))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("User not found"));
    }


    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    public void removeUser_correctInput_removesUser() throws Exception {
        String id = usersUtils.createUser(random);

        mockMvc.perform(post("/user/remove").param("id", id)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    public void removeUser_nonExistingId_throwException() throws Exception {
        mockMvc.perform(post("/user/remove").param("id", NON_EXISTING_ID)).andExpect(status().is5xxServerError()).andExpect(status().reason("User not found"));
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    public void getUserById_correctInput_returnsUser() throws Exception {
        String username = getRandomUsername(random);
        String email = getRandomEmail(random);
        String password = getRandomPassword(random);

        String id = usersUtils.createUser(username, email, password);

        MvcResult result = mockMvc.perform(get("/user/find/by/id").param("id", id).contentType("JSON")).andExpect(status().isOk()).andReturn();

        Map<String, String> map = new ObjectMapper().readValue(result.getResponse().getContentAsString(), HashMap.class);

        assertEquals(Integer.parseInt(id), map.get("id"));
        assertEquals(username, map.get("username"));
        assertEquals(email, map.get("email"));
        assertTrue(encoder.matches(password, map.get("password")));

        //assertEquals("{\"id\":" + id + ",\"username\":\"" + username + "\",\"email\":\"" + email + "\",\"password\":\"" + encoder.encode(password) + "\",\"enabled\":true}", );
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    public void getUserById_nonExistingUser_throwException() throws Exception {
        mockMvc.perform(get("/user/find/by/id").param("id", NON_EXISTING_ID))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("User not found"));
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    public void getUserByUsername_correctInput_returnsUser() throws Exception {
        String username = getRandomUsername(random);
        String email = getRandomEmail(random);
        String password = getRandomPassword(random);

        String id = usersUtils.createUser(username, email, password);

        MvcResult result = mockMvc.perform(get("/user/find/by/username").param("username", username)).andExpect(status().isOk()).andReturn();

        Map<String, String> map = new ObjectMapper().readValue(result.getResponse().getContentAsString(), HashMap.class);

        assertEquals(Integer.parseInt(id), map.get("id"));
        assertEquals(username, map.get("username"));
        assertEquals(email, map.get("email"));
        assertTrue(encoder.matches(password, map.get("password")));


        //assertEquals("{\"id\":" + id + ",\"username\":\"" + username + "\",\"email\":\"" + email + "\",\"password\":\"" + encoder.encode(password) + "\",\"enabled\":true}", result.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    public void getUserByUsername_nonExistingUser_throwException() throws Exception {
        mockMvc.perform(get("/user/find/by/username").param("username", NON_EXISTING_USERNAME))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("User not found"));
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    public void getUserByUsername_incorrectUsernameFormat_throwException() throws Exception {
        mockMvc.perform(get("/user/find/by/username").param("username", USERNAME_INCORRECT_FORMAT))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("Username is in incorrect format"));
    }


    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    public void createUser_correctInput_createsUser() throws Exception {
        String username = getRandomUsername(random);
        String email = getRandomEmail(random);
        String password = getRandomPassword(random);

        MvcResult result = mockMvc.perform(post("/user/create")
                        .param("username", username)
                        .param("email", email)
                        .param("password", password))
                .andExpect(status().isOk())
                .andReturn();

        int id = Integer.parseInt(result.getResponse().getContentAsString());
        User user = repository.findById(id).get();
        assertTrue(repository.existsById(id));
        assertTrue(encoder.matches(password, user.getPassword()));
        assertEquals(username, user.getUsername());
        assertEquals(email, user.getEmail());
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    public void createUser_emailIsTaken_throwException() throws Exception {
        String username = getRandomUsername(random);
        String email = getRandomEmail(random);
        String password = getRandomPassword(random);

        usersUtils.createUser(username, email, password);

        mockMvc.perform(post("/user/create")
                        .param("username", getRandomUsername(random))
                        .param("email", email)
                        .param("password", password))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("Email is already taken"));
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    public void createUser_usernameIsTaken_throwException() throws Exception {
        String username = getRandomUsername(random);
        String email = getRandomEmail(random);
        String password = getRandomPassword(random);

        usersUtils.createUser(username, email, password);

        mockMvc.perform(post("/user/create")
                        .param("username", username)
                        .param("email", getRandomEmail(random))
                        .param("password", password))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("Username is already taken"));
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    public void createUser_incorrectEmailFormat_throwException() throws Exception {
        mockMvc.perform(post("/user/create")
                        .param("username", getRandomUsername(random))
                        .param("email", EMAIL_INCORRECT_FORMAT)
                        .param("password", getRandomPassword(random)))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("Email is in incorrect format"));
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    public void createUser_incorrectPasswordFormat_throwException() throws Exception {
        mockMvc.perform(post("/user/create")
                        .param("username", getRandomUsername(random))
                        .param("email", getRandomEmail(random))
                        .param("password", PASSWORD_INCORRECT_FORMAT))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("Password is in incorrect format"));
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    public void createUser_incorrectUsernameFormat_throwException() throws Exception {
        mockMvc.perform(post("/user/create")
                        .param("username", USERNAME_INCORRECT_FORMAT)
                        .param("email", getRandomEmail(random))
                        .param("password", getRandomPassword(random)))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("Username is in incorrect format"));
    }

}
