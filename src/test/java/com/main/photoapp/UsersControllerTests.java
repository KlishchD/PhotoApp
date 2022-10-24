package com.main.photoapp;


import com.main.photoapp.Utils.UsersUtils;
import com.main.photoapp.models.User;
import com.main.photoapp.repositories.UsersRepository;
import com.main.photoapp.utils.NicknameChecker;
import com.main.photoapp.utils.PasswordChecker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Random;

import static com.main.photoapp.Utils.RandomTextGenerator.*;
import static com.main.photoapp.Utils.UsersUtils.*;
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
    private final String NON_EXISTING_NICKNAME = "100000000";
    private final String NICKNAME_INCORRECT_FORMAT = "-100";
    private final String PASSWORD_INCORRECT_FORMAT = "a".repeat(PASSWORD_MAXIMAL_SIZE + 1);

    @BeforeEach
    public void setUp() {
        random = new Random(0);
        repository.deleteAll();
    }

    @Test
    public void updateUserPassword_correctInput_updatesUser() throws Exception {
        String nickname = getRandomNickname(random);
        String email = getRandomEmail(random);
        String password = getRandomPassword(random);
        String newPassword = getRandomPassword(random);

        String id = usersUtils.createUser(nickname, email, password);

        mockMvc.perform(post("/user/update/password").param("id", id).param("password", newPassword)).andExpect(status().isOk());
        assertEquals(newPassword, repository.findById(Integer.valueOf(id)).get().getPassword());
    }

    @Test
    public void updateUserPassword_nonExistingUser_updatesUser() throws Exception {
        String newPassword = getRandomPassword(random);
        usersUtils.createUser(random);

        mockMvc.perform(post("/user/update/password").param("id", NON_EXISTING_ID).param("password", newPassword))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("User not found"));
    }

    @Test
    public void updateUserPassword_incorrectPasswordFormat_updatesUser() throws Exception {
        String id = usersUtils.createUser(random);

        mockMvc.perform(post("/user/update/password").param("id", id).param("password", PASSWORD_INCORRECT_FORMAT))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("Password is in incorrect format"));
    }

    @Test
    public void updateUserEmail_correctInput_updatesUser() throws Exception {
        String nickname = getRandomNickname(random);
        String email = getRandomEmail(random);
        String newEmail = getRandomEmail(random);
        String password = getRandomPassword(random);

        String id = usersUtils.createUser(nickname, email, password);

        mockMvc.perform(post("/user/update/email").param("id", id).param("email", newEmail)).andExpect(status().isOk());
        assertEquals(newEmail, repository.findById(Integer.valueOf(id)).get().getEmail());
    }

    @Test
    public void updateUserEmail_nonExistingUser_updatesUser() throws Exception {
        String newEmail = getRandomEmail(random);
        usersUtils.createUser(random);

        mockMvc.perform(post("/user/update/email").param("id", NON_EXISTING_ID).param("email", newEmail))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("User not found"));
    }

    @Test
    public void updateUserEmail_incorrectEmailFormat_updatesUser() throws Exception {
        usersUtils.createUser(random);

        mockMvc.perform(post("/user/update/email").param("id", NON_EXISTING_ID).param("email", EMAIL_INCORRECT_FORMAT))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("User not found"));
    }


    @Test
    public void removeUser_correctInput_removesUser() throws Exception {
        String id = usersUtils.createUser(random);

        mockMvc.perform(post("/user/remove").param("id", id)).andExpect(status().isOk());
    }

    @Test
    public void removeUser_nonExistingId_throwException() throws Exception {
        mockMvc.perform(post("/user/remove").param("id", NON_EXISTING_ID)).andExpect(status().is5xxServerError()).andExpect(status().reason("User not found"));
    }

    @Test
    public void getUserById_correctInput_returnsUser() throws Exception {
        String nickname = getRandomNickname(random);
        String email = getRandomEmail(random);
        String password = getRandomPassword(random);

        String id = usersUtils.createUser(nickname, email, password);

        MvcResult result = mockMvc.perform(get("/user/find/by/id").param("id", id)).andExpect(status().isOk()).andReturn();
        assertEquals("{\"id\":" + id + ",\"nickname\":\"" + nickname + "\",\"email\":\"" + email + "\",\"password\":\"" + password + "\"}", result.getResponse().getContentAsString());
    }

    @Test
    public void getUserById_nonExistingUser_throwException() throws Exception {
        mockMvc.perform(get("/user/find/by/id").param("id", NON_EXISTING_ID))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("User not found"));
    }

    @Test
    public void getUserByNickname_correctInput_returnsUser() throws Exception {
        String nickname = getRandomNickname(random);
        String email = getRandomEmail(random);
        String password = getRandomPassword(random);

        String id = usersUtils.createUser(nickname, email, password);

        MvcResult result = mockMvc.perform(get("/user/find/by/nickname").param("nickname", nickname)).andExpect(status().isOk()).andReturn();
        assertEquals("{\"id\":" + id + ",\"nickname\":\"" + nickname + "\",\"email\":\"" + email + "\",\"password\":\"" + password + "\"}", result.getResponse().getContentAsString());
    }

    @Test
    public void getUserByNickname_nonExistingUser_throwException() throws Exception {
        mockMvc.perform(get("/user/find/by/nickname").param("nickname", NON_EXISTING_NICKNAME))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("User not found"));
    }

    @Test
    public void getUserByNickname_incorrectNicknameFormat_throwException() throws Exception {
        mockMvc.perform(get("/user/find/by/nickname").param("nickname", NICKNAME_INCORRECT_FORMAT))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("Nickname is in incorrect format"));
    }


    @Test
    public void createUser_correctInput_createsUser() throws Exception {
        String nickname = getRandomNickname(random);
        String email = getRandomEmail(random);
        String password = getRandomPassword(random);

        MvcResult result = mockMvc.perform(post("/user/create")
                        .param("nickname", nickname)
                        .param("email", email)
                        .param("password", password))
                .andExpect(status().isOk())
                .andReturn();

        int id = Integer.parseInt(result.getResponse().getContentAsString());
        User user = repository.findById(id).get();
        assertTrue(repository.existsById(id));
        assertEquals(password, user.getPassword());
        assertEquals(nickname, user.getNickname());
        assertEquals(email, user.getEmail());
    }

    @Test
    public void createUser_emailIsTaken_throwException() throws Exception {
        String nickname = getRandomNickname(random);
        String email = getRandomEmail(random);
        String password = getRandomPassword(random);

        usersUtils.createUser(nickname, email, password);

        mockMvc.perform(post("/user/create")
                        .param("nickname", getRandomNickname(random))
                        .param("email", email)
                        .param("password", password))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("Email is already taken"));
    }

    @Test
    public void createUser_nicknameIsTaken_throwException() throws Exception {
        String nickname = getRandomNickname(random);
        String email = getRandomEmail(random);
        String password = getRandomPassword(random);

        usersUtils.createUser(nickname, email, password);

        mockMvc.perform(post("/user/create")
                        .param("nickname", nickname)
                        .param("email", getRandomEmail(random))
                        .param("password", password))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("Nickname is already taken"));
    }

    @Test
    public void createUser_incorrectEmailFormat_throwException() throws Exception {
        mockMvc.perform(post("/user/create")
                        .param("nickname", getRandomNickname(random))
                        .param("email", EMAIL_INCORRECT_FORMAT)
                        .param("password", getRandomPassword(random)))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("Email is in incorrect format"));
    }

    @Test
    public void createUser_incorrectPasswordFormat_throwException() throws Exception {
        mockMvc.perform(post("/user/create")
                        .param("nickname", getRandomNickname(random))
                        .param("email", getRandomEmail(random))
                        .param("password", PASSWORD_INCORRECT_FORMAT))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("Password is in incorrect format"));
    }

    @Test
    public void createUser_incorrectNicknameFormat_throwException() throws Exception {
        mockMvc.perform(post("/user/create")
                        .param("nickname", NICKNAME_INCORRECT_FORMAT)
                        .param("email", getRandomEmail(random))
                        .param("password", getRandomPassword(random)))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("Nickname is in incorrect format"));
    }

}
