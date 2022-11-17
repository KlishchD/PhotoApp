package com.main.photoapp;

import com.main.photoapp.Utils.DesksUtils;
import com.main.photoapp.Utils.UsersUtils;
import com.main.photoapp.exceptions.IncorrectUsernameFormat;
import com.main.photoapp.exceptions.UserNotFoundException;
import com.main.photoapp.models.Desk.OwnersMapping.DeskOwnerMapping;
import com.main.photoapp.repositories.DesksOwnerRepository;
import com.main.photoapp.repositories.DesksRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Random;

import static com.main.photoapp.Utils.RandomTextGenerator.getRandomEmail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DesksOwnersControllerTests {
    @Autowired
    private DesksRepository desksRepository;

    @Autowired
    private DesksOwnerRepository desksOwnerRepository;

    @Autowired
    private DesksUtils desksUtils;

    @Autowired
    private UsersUtils usersUtils;

    @Autowired
    private MockMvc mockMvc;

    private Random random;

    private final String NON_EXISTING_ID = "-100";

    private static final String USERNAME = "ADMINISTRATOR";

    private static final String PASSWORD = "ADMINISTRATOR";


    @BeforeEach
    public void setUp() throws Exception {
        random = new Random(0);
        desksRepository.deleteAll();
        desksOwnerRepository.deleteAll();
        usersUtils.clearRepository();
        usersUtils.createUser(USERNAME, getRandomEmail(random), PASSWORD);
    }

    @AfterEach
    public void cleanUp() throws UserNotFoundException, IncorrectUsernameFormat {
        usersUtils.removeUser(USERNAME);
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    public void addOwnerToDesk_correctInput_addsOwnerToDesk() throws Exception {
        String creatorId = usersUtils.createUser(random);
        String deskId = desksUtils.createPublicDesk(creatorId, random);

        String userId = usersUtils.createUser(random);

        mockMvc.perform(post("/desk/add/owner")
                .param("deskId", deskId)
                .param("userId", userId)
                .param("permission", String.valueOf(DeskOwnerMapping.Permission.VIEW_ONLY_PERMISSION))
                .param("adderId", creatorId)).andExpect(status().isOk());

        assertTrue(desksOwnerRepository.existsByDeskIdAndUserId(Integer.parseInt(deskId), Integer.parseInt(userId)));

        DeskOwnerMapping.Permission permission = desksOwnerRepository.findByDeskIdAndUserId(Integer.parseInt(deskId), Integer.parseInt(userId)).get().getPermission();

        assertEquals(DeskOwnerMapping.Permission.VIEW_ONLY_PERMISSION, permission);
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    public void addOwnerToDesk_nonExistingDesk_throwsException() throws Exception {
        String creatorId = usersUtils.createUser(random);
        String userId = usersUtils.createUser(random);

        mockMvc.perform(post("/desk/add/owner")
                        .param("deskId", NON_EXISTING_ID)
                        .param("userId", userId)
                        .param("permission", String.valueOf(DeskOwnerMapping.Permission.VIEW_ONLY_PERMISSION))
                        .param("adderId", creatorId))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("Desk not found"));
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    public void addOwnerToDesk_nonExistingUser_throwsException() throws Exception {
        String creatorId = usersUtils.createUser(random);
        String deskId = desksUtils.createPublicDesk(creatorId, random);

        mockMvc.perform(post("/desk/add/owner")
                        .param("deskId", deskId)
                        .param("userId", NON_EXISTING_ID)
                        .param("permission", String.valueOf(DeskOwnerMapping.Permission.VIEW_ONLY_PERMISSION))
                        .param("adderId", creatorId))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("User not found"));
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    public void addOwnerToDesk_nonExistingAdder_throwsException() throws Exception {
        String deskId = desksUtils.createPublicDesk(random);
        String userId = usersUtils.createUser(random);

        mockMvc.perform(post("/desk/add/owner")
                        .param("deskId", deskId)
                        .param("userId", userId)
                        .param("permission", String.valueOf(DeskOwnerMapping.Permission.VIEW_ONLY_PERMISSION))
                        .param("adderId", NON_EXISTING_ID))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("User not found"));
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    public void addOwnerToDesk_creatorTriesToGiveCreatorPermission_throwException() throws Exception {
        String creatorId = usersUtils.createUser(random);
        String deskId = desksUtils.createPublicDesk(creatorId, random);

        String userId = usersUtils.createUser(random);

        mockMvc.perform(post("/desk/add/owner")
                        .param("deskId", deskId)
                        .param("userId", userId)
                        .param("permission", String.valueOf(DeskOwnerMapping.Permission.CREATOR_PERMISSION))
                        .param("adderId", creatorId))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("Only original creator of a desk can have creator permission"));
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    public void addOwnerToDesk_userIsAnOwner_throwException() throws Exception {
        String creatorId = usersUtils.createUser(random);
        String deskId = desksUtils.createPublicDesk(creatorId, random);
        String userId = usersUtils.createUser(random);

        desksUtils.addOwner(deskId, userId, DeskOwnerMapping.Permission.FULL_PERMISSION, creatorId);

        mockMvc.perform(post("/desk/add/owner")
                        .param("deskId", deskId)
                        .param("userId", userId)
                        .param("permission", String.valueOf(DeskOwnerMapping.Permission.FULL_PERMISSION))
                        .param("adderId", creatorId))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("User is already an owner of a desk"));
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    public void addOwnerToDesk_ownerTriesToAddOwnerWithEqualPermissions_throwException() throws Exception {
        String creatorId = usersUtils.createUser(random);
        String deskId = desksUtils.createPublicDesk(creatorId, random);
        String userId = usersUtils.createUser(random);
        String userId1 = usersUtils.createUser(random);

        desksUtils.addOwner(deskId, userId, DeskOwnerMapping.Permission.FULL_PERMISSION, creatorId);

        mockMvc.perform(post("/desk/add/owner")
                        .param("deskId", deskId)
                        .param("userId", userId1)
                        .param("permission", String.valueOf(DeskOwnerMapping.Permission.FULL_PERMISSION))
                        .param("adderId", userId))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("Not enough permissions"));
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    public void addOwnerToDesk_adderIdNotOwner_throwException() throws Exception {
        String creatorId = usersUtils.createUser(random);
        String deskId = desksUtils.createPublicDesk(creatorId, random);

        String userId1 = usersUtils.createUser(random);
        String userId2 = usersUtils.createUser(random);

        mockMvc.perform(post("/desk/add/owner")
                        .param("deskId", deskId)
                        .param("userId", userId1)
                        .param("permission", String.valueOf(DeskOwnerMapping.Permission.VIEW_ONLY_PERMISSION))
                        .param("adderId", userId2))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("Not enough permissions"));
    }


    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    public void getOwners_correctInput_returnsOwners() throws Exception {
        String creatorId = usersUtils.createUser(random);
        String userId = usersUtils.createUser(random);
        String deskId = desksUtils.createPublicDesk(creatorId, random);
        desksUtils.addOwner(deskId, userId, DeskOwnerMapping.Permission.FULL_PERMISSION, creatorId);

        MvcResult result = mockMvc.perform(get("/desk/get/owners")
                        .param("deskId", deskId)
                        .param("userId", creatorId))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("[{\"deskId\":" + deskId + ",\"userId\":" + creatorId + ",\"permission\":\"CREATOR_PERMISSION\"},{\"deskId\":" + deskId + ",\"userId\":" + userId + ",\"permission\":\"FULL_PERMISSION\"}]", result.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    public void getOwners_nonExistingDesk_throwsException() throws Exception {
        String userId = usersUtils.createUser(random);

        mockMvc.perform(get("/desk/get/owners")
                        .param("deskId", NON_EXISTING_ID)
                        .param("userId", userId))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("Desk not found"));
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    public void getOwners_nonExistingUser_throwsException() throws Exception {
        String creatorId = usersUtils.createUser(random);
        String deskId = desksUtils.createPublicDesk(creatorId, random);

        mockMvc.perform(get("/desk/get/owners")
                        .param("deskId", deskId)
                        .param("userId", NON_EXISTING_ID))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("User not found"));
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    public void getOwners_publicDeskAndUserIsNotOwner_throwException() throws Exception {
        String creatorId = usersUtils.createUser(random);
        String userId = usersUtils.createUser(random);
        String deskId = desksUtils.createPublicDesk(creatorId, random);

        MvcResult result = mockMvc.perform(get("/desk/get/owners")
                        .param("deskId", deskId)
                        .param("userId", userId))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("[{\"deskId\":" + deskId + ",\"userId\":" + creatorId + ",\"permission\":\"CREATOR_PERMISSION\"}]", result.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    public void getOwners_privateDeskAndUserIsNotOwner_throwException() throws Exception {
        String userId = usersUtils.createUser(random);
        String deskId = desksUtils.createPrivateDesk(random);

        mockMvc.perform(get("/desk/get/owners")
                        .param("deskId", deskId)
                        .param("userId", userId))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("Not enough permissions"));
    }


    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    public void getOwnerPermission_correctInput_returnsOwnerPermission() throws Exception {
        String creatorId = usersUtils.createUser(random);
        String userId = usersUtils.createUser(random);
        String deskId = desksUtils.createPublicDesk(creatorId, random);
        desksUtils.addOwner(deskId, userId, DeskOwnerMapping.Permission.FULL_PERMISSION, creatorId);

        MvcResult result = mockMvc.perform(get("/desk/get/owner_permission")
                        .param("deskId", deskId)
                        .param("userId", userId)
                        .param("userIdWhoAsks", creatorId))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("\"FULL_PERMISSION\"", result.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    public void getOwnerPermission_nonExistingUser_throwsException() throws Exception {
        String creatorId = usersUtils.createUser(random);
        String deskId = desksUtils.createPublicDesk(creatorId, random);

        mockMvc.perform(get("/desk/get/owner_permission")
                        .param("deskId", deskId)
                        .param("userId", NON_EXISTING_ID)
                        .param("userIdWhoAsks", creatorId))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("User not found"));
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    public void getOwnerPermission_userNotOwner_returnsNO_PERMISSIONS() throws Exception {
        String creatorId = usersUtils.createUser(random);
        String userId = usersUtils.createUser(random);
        String deskId = desksUtils.createPublicDesk(creatorId, random);

        MvcResult result = mockMvc.perform(get("/desk/get/owner_permission")
                        .param("deskId", deskId)
                        .param("userId", userId)
                        .param("userIdWhoAsks", creatorId))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("\"NO_PERMISSIONS\"", result.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    public void getOwnerPermission_nonExistingUserIdWhoAsks_throwException() throws Exception {
        String creatorId = usersUtils.createUser(random);
        String userId = usersUtils.createUser(random);
        String deskId = desksUtils.createPublicDesk(creatorId, random);

        mockMvc.perform(get("/desk/get/owner_permission")
                        .param("deskId", deskId)
                        .param("userId", userId)
                        .param("userIdWhoAsks", NON_EXISTING_ID))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("User not found"));
    }


    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    public void getOwnersWithPermission_publicDeskAndUserIsOwner_returnsOwners() throws Exception {
        String creatorId = usersUtils.createUser(random);
        String userId = usersUtils.createUser(random);
        String deskId = desksUtils.createPublicDesk(creatorId, random);
        desksUtils.addOwner(deskId, userId, DeskOwnerMapping.Permission.FULL_PERMISSION, creatorId);

        MvcResult result = mockMvc.perform(get("/desk/get/owners_with_permission")
                        .param("deskId", deskId)
                        .param("userId", userId)
                        .param("permission", "FULL_PERMISSION"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("[" + userId + "]", result.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    public void getOwnersWithPermission_publicDeskAndUserIsNotOwner_returnsOwners() throws Exception {
        String creatorId = usersUtils.createUser(random);
        String userId1 = usersUtils.createUser(random);
        String userId2 = usersUtils.createUser(random);
        String deskId = desksUtils.createPublicDesk(creatorId, random);
        desksUtils.addOwner(deskId, userId1, DeskOwnerMapping.Permission.FULL_PERMISSION, creatorId);

        MvcResult result = mockMvc.perform(get("/desk/get/owners_with_permission")
                        .param("deskId", deskId)
                        .param("userId", userId2)
                        .param("permission", "FULL_PERMISSION"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("[" + userId1 + "]", result.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    public void getOwnersWithPermission_privateDeskAndUserIsOwner_returnsOwners() throws Exception {
        String creatorId = usersUtils.createUser(random);
        String userId = usersUtils.createUser(random);
        String deskId = desksUtils.createPrivateDesk(creatorId, random);
        desksUtils.addOwner(deskId, userId, DeskOwnerMapping.Permission.FULL_PERMISSION, creatorId);

        MvcResult result = mockMvc.perform(get("/desk/get/owners_with_permission")
                        .param("deskId", deskId)
                        .param("userId", userId)
                        .param("permission", "FULL_PERMISSION"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("[" + userId + "]", result.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    public void getOwnersWithPermission_privateDeskAndUserIsNotOwner_throwsException() throws Exception {
        String creatorId = usersUtils.createUser(random);
        String userId2 = usersUtils.createUser(random);
        String deskId = desksUtils.createPrivateDesk(creatorId, random);

        mockMvc.perform(get("/desk/get/owners_with_permission")
                        .param("deskId", deskId)
                        .param("userId", userId2)
                        .param("permission", "FULL_PERMISSION"))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("Not enough permissions"));
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    public void getOwnersWithPermission_nonExistingUser_throwsException() throws Exception {
        String creatorId = usersUtils.createUser(random);
        String deskId = desksUtils.createPublicDesk(creatorId, random);

        mockMvc.perform(get("/desk/get/owners_with_permission")
                        .param("deskId", deskId)
                        .param("userId", NON_EXISTING_ID)
                        .param("permission", "FULL_PERMISSION"))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("User not found"));
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    public void getOwnersWithPermission_nonExistingDesk_throwsException() throws Exception {
        String userId = usersUtils.createUser(random);

        mockMvc.perform(get("/desk/get/owners_with_permission")
                        .param("deskId", NON_EXISTING_ID)
                        .param("userId", userId)
                        .param("permission", "FULL_PERMISSION"))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("Desk not found"));
    }


    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    public void getCreator_publicDeskAndUserIsOwner_returnsCreator() throws Exception {
        String creatorId = usersUtils.createUser(random);
        String userId = usersUtils.createUser(random);
        String deskId = desksUtils.createPublicDesk(creatorId, random);
        desksUtils.addOwner(deskId, userId, DeskOwnerMapping.Permission.FULL_PERMISSION, creatorId);

        MvcResult result = mockMvc.perform(get("/desk/get/creator")
                        .param("deskId", deskId)
                        .param("userId", userId))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(creatorId, result.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    public void getCreator_publicDeskAndUserIsNotOwner_returnsCreator() throws Exception {
        String creatorId = usersUtils.createUser(random);
        String userId2 = usersUtils.createUser(random);
        String deskId = desksUtils.createPublicDesk(creatorId, random);

        MvcResult result = mockMvc.perform(get("/desk/get/creator")
                        .param("deskId", deskId)
                        .param("userId", userId2))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(creatorId, result.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    public void getCreator_privateDeskAndUserIsOwner_returnsCreator() throws Exception {
        String creatorId = usersUtils.createUser(random);
        String userId = usersUtils.createUser(random);
        String deskId = desksUtils.createPrivateDesk(creatorId, random);
        desksUtils.addOwner(deskId, userId, DeskOwnerMapping.Permission.FULL_PERMISSION, creatorId);

        MvcResult result = mockMvc.perform(get("/desk/get/creator")
                        .param("deskId", deskId)
                        .param("userId", userId))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(creatorId, result.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    public void getCreator_privateDeskAndUserIsNotOwner_throwsException() throws Exception {
        String creatorId = usersUtils.createUser(random);
        String userId2 = usersUtils.createUser(random);
        String deskId = desksUtils.createPrivateDesk(creatorId, random);

        mockMvc.perform(get("/desk/get/creator")
                        .param("deskId", deskId)
                        .param("userId", userId2))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("Not enough permissions"));
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    public void getCreator_nonExistingUser_throwsException() throws Exception {
        String creatorId = usersUtils.createUser(random);
        String deskId = desksUtils.createPublicDesk(creatorId, random);

        mockMvc.perform(get("/desk/get/creator")
                        .param("deskId", deskId)
                        .param("userId", NON_EXISTING_ID))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("User not found"));
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    public void getCreator_nonExistingDesk_throwsException() throws Exception {
        String userId = usersUtils.createUser(random);

        mockMvc.perform(get("/desk/get/creator")
                        .param("deskId", NON_EXISTING_ID)
                        .param("userId", userId))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("Desk not found"));
    }

}
