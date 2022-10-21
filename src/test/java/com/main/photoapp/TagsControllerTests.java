package com.main.photoapp;

import com.main.photoapp.Utils.TagsUtils;
import com.main.photoapp.models.Tag;
import com.main.photoapp.repositories.TagsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Random;

import static com.main.photoapp.Utils.RandomTextGenerator.getRandomTagText;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class TagsControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TagsRepository repository;

    @Autowired
    private TagsUtils tagsUtils;

    private Random random;

    @BeforeEach
    public void setUp() {
        random = new Random(0);
        repository.deleteAll();
    }

    @Test
    public void updateTag_nonExistingId_throwException() throws Exception {
        mockMvc.perform(post("/tag/update").param("id", "-100").param("text", "abba"))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("Tag is not found"));
    }

    @Test
    public void updateTag_correctInput_updatesTag() throws Exception {
        String text = getRandomTagText(random);
        String id = tagsUtils.createTag(text);
        mockMvc.perform(post("/tag/update").param("id", id).param("text", text)).andExpect(status().isOk());
        assertEquals(text, repository.findById(Integer.valueOf(id)).orElseThrow().getText());
    }

    @Test
    public void removeTag_correctInput_removesTag() throws Exception {
        String id = tagsUtils.createTag(random);
        mockMvc.perform(post("/tag/remove").param("id", id)).andExpect(status().isOk());
        repository.existsById(Integer.valueOf(id));
    }

    @Test
    public void removeTag_nonExistingId_throwsException() throws Exception {
        mockMvc.perform(post("/tag/remove").param("id", "-100"))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("Tag is not found"));
    }

    @Test
    public void addTag_correctInput_addsTag() throws Exception {
        String text = getRandomTagText(random);
        MvcResult result = mockMvc.perform(post("/tag/add").param("text", text))
                .andExpect(status().isOk())
                .andReturn();
        int id = Integer.parseInt(result.getResponse().getContentAsString());
        assertTrue(repository.existsById(id));

        Tag tag = repository.findById(id).get();
        assertEquals(text, tag.getText());
    }

    @Test
    public void addTag_existingText_throwsException() throws Exception {
        String text = getRandomTagText(random);
        tagsUtils.createTag(text);
        mockMvc.perform(post("/tag/add").param("text", text))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason(equalTo("Tag with specified text already exists")));
    }

    @Test
    public void getTag_nonExistingId_throwsException() throws Exception {
        mockMvc.perform(get("/tag/get").param("id", "-100"))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("Tag is not found"));
    }

    @Test
    public void getTag_correctInput_returnsTag() throws Exception {
        String text = getRandomTagText(random);
        String id = tagsUtils.createTag(text);
        mockMvc.perform(get("/tag/get").param("id", id))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"id\":" + id + ",\"text\":\"" + text + "\"}"));
    }


}
