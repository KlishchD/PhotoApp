package com.main.photoapp;

import com.main.photoapp.Utils.DesksUtils;
import com.main.photoapp.Utils.UsersUtils;
import com.main.photoapp.models.Desk.Desk;
import com.main.photoapp.repositories.DeskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Random;

import static com.main.photoapp.Utils.RandomTextGenerator.getRandomDescription;
import static com.main.photoapp.Utils.RandomTextGenerator.getRandomName;
import static com.main.photoapp.utils.DeskDescriptionChecker.DESK_DESCRIPTION_MAX_SIZE;
import static com.main.photoapp.utils.DeskNameChecker.DESK_NAME_MAX_SIZE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DesksControllerTests {

    @Autowired
    private DeskRepository desksRepository;

    @Autowired
    private DesksUtils desksUtils;

    @Autowired
    private UsersUtils usersUtils;

    @Autowired
    private MockMvc mockMvc;

    private Random random;

    private final String NON_EXISTING_ID = "-100";

    private final String DESK_NAME_INCORRECT_FORMAT = "a".repeat(DESK_NAME_MAX_SIZE + 1);
    private final String DESK_DESCRIPTION_INCORRECT_FORMAT = "a".repeat(DESK_DESCRIPTION_MAX_SIZE + 1);

    @BeforeEach
    public void setUp() {
        random = new Random(0);
        usersUtils.clearRepository();
        desksRepository.deleteAll();
    }

    @Test
    public void addDesk_publicDesk_createsPublicDesk() throws Exception {
        String name = getRandomName(random);
        String description = getRandomDescription(random);
        String creatorID = usersUtils.createUser(random);

        MvcResult result = mockMvc.perform(post("/desk/add")
                        .param("name", name)
                        .param("description", description)
                        .param("creator_id", creatorID)
                        .param("type", "PUBLIC"))
                .andExpect(status().isOk())
                .andReturn();

        int id = Integer.parseInt(result.getResponse().getContentAsString());

        assertTrue(desksRepository.existsById(id));
        Desk desk = desksRepository.findById(id).get();
        assertEquals(name, desk.getName());
        assertEquals(description, desk.getDescription());
        assertEquals(Desk.DeskType.PUBLIC, desk.getType());
    }

    @Test
    public void addDesk_privateDesk_createsPrivateDesk() throws Exception {
        String name = getRandomName(random);
        String description = getRandomDescription(random);
        String creatorID = usersUtils.createUser(random);

        MvcResult result = mockMvc.perform(post("/desk/add")
                        .param("name", name)
                        .param("description", description)
                        .param("creator_id", creatorID)
                        .param("type", "PRIVATE"))
                .andExpect(status().isOk())
                .andReturn();

        int id = Integer.parseInt(result.getResponse().getContentAsString());

        assertTrue(desksRepository.existsById(id));
        Desk desk = desksRepository.findById(id).get();
        assertEquals(name, desk.getName());
        assertEquals(description, desk.getDescription());
        assertEquals(Desk.DeskType.PRIVATE, desk.getType());
    }

    @Test
    public void addDesk_incorrectDeskNameFormat_createsDesk() throws Exception {
        mockMvc.perform(post("/desk/add")
                        .param("name", DESK_NAME_INCORRECT_FORMAT)
                        .param("description", getRandomDescription(random))
                        .param("creator_id", usersUtils.createUser(random))
                        .param("type", "PUBLIC"))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("Desk name is in incorrect format"));
    }

    @Test
    public void addDesk_incorrectDeskDescriptionFormat_createsDesk() throws Exception {
        mockMvc.perform(post("/desk/add")
                        .param("name", getRandomName(random))
                        .param("description", DESK_DESCRIPTION_INCORRECT_FORMAT)
                        .param("creator_id", usersUtils.createUser(random))
                        .param("type", "PUBLIC"))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("Desk description is in incorrect format"));
    }

    @Test
    public void addDesk_nonExistingUser_createsDesk() throws Exception {
        mockMvc.perform(post("/desk/add")
                        .param("name", getRandomName(random))
                        .param("description", getRandomDescription(random))
                        .param("creator_id", NON_EXISTING_ID)
                        .param("type", "PUBLIC"))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("User not found"));
    }


    @Test
    public void removeDesk_correctInput_removesDesk() throws Exception {
        String creatorId = usersUtils.createUser(random);
        String deskId = desksUtils.createPublicDesk(creatorId, random);

        mockMvc.perform(post("/desk/remove")
                        .param("deskId", deskId)
                        .param("userId", creatorId))
                .andExpect(status().isOk())
                .andReturn();

        desksRepository.existsById(Integer.parseInt(deskId));
    }

    @Test
    public void removeDesk_nonOwnerUser_throwsException() throws Exception {
        String creatorId = usersUtils.createUser(random);
        String deskId = desksUtils.createPublicDesk(creatorId, random);
        String userId = usersUtils.createUser(random);

        mockMvc.perform(post("/desk/remove")
                        .param("deskId", deskId)
                        .param("userId", userId))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("Not enough permissions"));
    }

    @Test
    public void removeDesk_nonExistingDesk_throwsException() throws Exception {
        String userId = usersUtils.createUser(random);

        mockMvc.perform(post("/desk/remove")
                        .param("deskId", NON_EXISTING_ID)
                        .param("userId", userId))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("Desk not found"));
    }

    @Test
    public void removeDesk_nonExistingUser_throwsException() throws Exception {
        String creatorId = usersUtils.createUser(random);
        String deskId = desksUtils.createPublicDesk(creatorId, random);

        mockMvc.perform(post("/desk/remove")
                        .param("deskId", deskId)
                        .param("userId", NON_EXISTING_ID))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("User not found"));
    }


    @Test
    public void getDeskInformation_publicDeskOwner_returnsDeskInformation() throws Exception {
        String name = getRandomName(random);
        String description = getRandomDescription(random);
        String userId = usersUtils.createUser(random);
        String deskId = desksUtils.createPublicDesk(name, description, userId);

        MvcResult result = mockMvc.perform(get("/desk/get/info")
                        .param("deskId", deskId)
                        .param("userId", userId))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("{\"id\":" + deskId + ",\"name\":\"" + name + "\",\"description\":\"" + description + "\",\"type\":\"PUBLIC\"}", result.getResponse().getContentAsString());
    }

    @Test
    public void getDeskInformation_publicDeskNonOwner_returnsDeskInformation() throws Exception {
        String name = getRandomName(random);
        String description = getRandomDescription(random);
        String userId = usersUtils.createUser(random);
        String randomUserId = usersUtils.createUser(random);
        String deskId = desksUtils.createPublicDesk(name, description, userId);

        MvcResult result = mockMvc.perform(get("/desk/get/info")
                        .param("deskId", deskId)
                        .param("userId", randomUserId))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("{\"id\":" + deskId + ",\"name\":\"" + name + "\",\"description\":\"" + description + "\",\"type\":\"PUBLIC\"}", result.getResponse().getContentAsString());
    }

    @Test
    public void getDeskInformation_privateDeskOwner_returnsDeskInformation() throws Exception {
        String name = getRandomName(random);
        String description = getRandomDescription(random);
        String userId = usersUtils.createUser(random);
        String deskId = desksUtils.createPrivateDesk(name, description, userId);

        MvcResult result = mockMvc.perform(get("/desk/get/info")
                        .param("deskId", deskId)
                        .param("userId", userId))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("{\"id\":" + deskId + ",\"name\":\"" + name + "\",\"description\":\"" + description + "\",\"type\":\"PRIVATE\"}", result.getResponse().getContentAsString());
    }

    @Test
    public void getDeskInformation_privateDeskNonOwner_throwsException() throws Exception {
        String name = getRandomName(random);
        String description = getRandomDescription(random);
        String userId = usersUtils.createUser(random);
        String randomUserId = usersUtils.createUser(random);
        String deskId = desksUtils.createPrivateDesk(name, description, userId);

        mockMvc.perform(get("/desk/get/info")
                        .param("deskId", deskId)
                        .param("userId", randomUserId))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("Not enough permissions"));
    }

    @Test
    public void getDeskInformation_nonExistingDesk_throwsException() throws Exception {
        String userId = usersUtils.createUser(random);

        mockMvc.perform(get("/desk/get/info")
                        .param("deskId", NON_EXISTING_ID)
                        .param("userId", userId))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("Desk not found"));
    }

    @Test
    public void getDeskInformation_nonExistingUser_throwsException() throws Exception {
        String deskId = desksUtils.createPublicDesk(random);

        mockMvc.perform(get("/desk/get/info")
                        .param("deskId", deskId)
                        .param("userId", NON_EXISTING_ID))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("User not found"));

    }


    @Test
    public void updateDeskName_correctInput_updatesDesksName() throws Exception {
        String creatorId = usersUtils.createUser(random);
        String deskId = desksUtils.createPublicDesk(creatorId, random);
        String newName = getRandomName(random);

        mockMvc.perform(post("/desk/update/name")
                        .param("deskId", deskId)
                        .param("name", newName)
                        .param("userId", creatorId))
                .andExpect(status().isOk());

        assertTrue(desksRepository.existsById(Integer.parseInt(deskId)));
        Desk desk = desksRepository.findById(Integer.parseInt(deskId)).get();
        assertEquals(newName, desk.getName());
    }

    @Test
    public void updateDeskName_nonExistingDesk_throwsException() throws Exception {
        mockMvc.perform(post("/desk/update/name")
                        .param("deskId", NON_EXISTING_ID)
                        .param("name", getRandomName(random))
                        .param("userId", usersUtils.createUser(random)))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("Desk not found"));
    }

    @Test
    public void updateDeskName_nonExistingUser_throwException() throws Exception {
        mockMvc.perform(post("/desk/update/name")
                        .param("deskId", desksUtils.createPublicDesk(random))
                        .param("name", getRandomName(random))
                        .param("userId", NON_EXISTING_ID))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("User not found"));
    }

    @Test
    public void updateDeskName_nonDeskOwner_throwsException() throws Exception {
        String creatorId = usersUtils.createUser(random);
        String randomUserId = usersUtils.createUser(random);
        String deskId = desksUtils.createPublicDesk(creatorId, random);
        String newName = getRandomName(random);

        mockMvc.perform(post("/desk/update/name")
                        .param("deskId", deskId)
                        .param("name", newName)
                        .param("userId", randomUserId))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("Not enough permissions"));
    }

    @Test
    public void updateDeskName_incorrectDeskNameFormat_throwException() throws Exception {
        String creatorId = usersUtils.createUser(random);
        String deskId = desksUtils.createPublicDesk(creatorId, random);

        mockMvc.perform(post("/desk/update/name")
                        .param("deskId", deskId)
                        .param("name", DESK_NAME_INCORRECT_FORMAT)
                        .param("userId", creatorId))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("Desk name is in incorrect format"));
    }


    @Test
    public void updateDeskDescription_correctInput_updatesDesksDescription() throws Exception {
        String creatorId = usersUtils.createUser(random);
        String deskId = desksUtils.createPublicDesk(creatorId, random);
        String newDescription = getRandomDescription(random);

        mockMvc.perform(post("/desk/update/description")
                        .param("deskId", deskId)
                        .param("description", newDescription)
                        .param("userId", creatorId))
                .andExpect(status().isOk());

        assertTrue(desksRepository.existsById(Integer.parseInt(deskId)));
        Desk desk = desksRepository.findById(Integer.parseInt(deskId)).get();
        assertEquals(newDescription, desk.getDescription());
    }

    @Test
    public void updateDeskDescription_nonExistingDesk_throwsException() throws Exception {
        mockMvc.perform(post("/desk/update/description")
                        .param("deskId", NON_EXISTING_ID)
                        .param("description", getRandomDescription(random))
                        .param("userId", usersUtils.createUser(random)))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("Desk not found"));
    }

    @Test
    public void updateDeskDescription_nonExistingUser_throwException() throws Exception {
        mockMvc.perform(post("/desk/update/description")
                        .param("deskId", desksUtils.createPublicDesk(random))
                        .param("description", getRandomDescription(random))
                        .param("userId", NON_EXISTING_ID))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("User not found"));
    }

    @Test
    public void updateDeskDescription_nonDeskOwner_throwsException() throws Exception {
        String creatorId = usersUtils.createUser(random);
        String randomUserId = usersUtils.createUser(random);
        String deskId = desksUtils.createPublicDesk(creatorId, random);
        String newDescription = getRandomDescription(random);

        mockMvc.perform(post("/desk/update/description")
                        .param("deskId", deskId)
                        .param("description", newDescription)
                        .param("userId", randomUserId))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("Not enough permissions"));
    }

    @Test
    public void updateDeskDescription_incorrectDeskDescriptionFormat_throwException() throws Exception {
        String creatorId = usersUtils.createUser(random);
        String deskId = desksUtils.createPublicDesk(creatorId, random);

        mockMvc.perform(post("/desk/update/description")
                        .param("deskId", deskId)
                        .param("description", DESK_DESCRIPTION_INCORRECT_FORMAT)
                        .param("userId", creatorId))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("Desk description is in incorrect format"));
    }

}
