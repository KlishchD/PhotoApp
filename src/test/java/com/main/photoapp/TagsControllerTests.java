package com.main.photoapp;

import com.main.photoapp.Utils.TagsUtils;
import com.main.photoapp.repositories.TagsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

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

    @Test
    public void updateTag_nonExistingId_throwException() throws Exception {
        mockMvc.perform(post("/tag/update").param("id", "-1000").param("text", "abba"))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("Tag is not found"));
    }

    @Test
    public void updateTag_correctInput_updatesTag() throws Exception {
        String time = tagsUtils.getCurrentTime();
        String id = tagsUtils.createTag(time);
        mockMvc.perform(post("/tag/update").param("id", id).param("text", time + time)).andExpect(status().isOk());
        assertEquals(time + time, repository.findById(Integer.valueOf(id)).orElseThrow().getText());
    }

    @Test
    public void removeTag_correctInput_removesTag() throws Exception {
        String id = tagsUtils.createTag();
        mockMvc.perform(post("/tag/remove").param("id", id)).andExpect(status().isOk());
        repository.existsById(Integer.valueOf(id));
    }

    @Test
    public void removeTag_nonExistingId_throwsException() throws Exception {
        mockMvc.perform(post("/tag/remove").param("id", "-1000"))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("Tag is not found"));
    }

    @Test
    public void addTag_correctInput_addsTag() throws Exception {
        MvcResult result = mockMvc.perform(post("/tag/add").param("text", tagsUtils.getCurrentTime()))
                .andExpect(status().isOk())
                .andReturn();
        String id = result.getResponse().getContentAsString();
        assertTrue(repository.existsById(Integer.valueOf(id)));
    }

    @Test
    public void addTag_existingText_throwsException() throws Exception {
        String time = tagsUtils.getCurrentTime();
        tagsUtils.createTag(time);
        mockMvc.perform(post("/tag/add").param("text", time))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason(equalTo("Tag with specified text already exists")));
    }

    @Test
    public void getTag_nonExistingId_throwsException() throws Exception {
        mockMvc.perform(get("/tag/get").param("id", "-1000"))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("Tag is not found"));
    }

    @Test
    public void getTag_correctInput_returnsTag() throws Exception {
        String time = tagsUtils.getCurrentTime();
        String id = tagsUtils.createTag(time);
        mockMvc.perform(get("/tag/get").param("id", id))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"id\":" + id + ",\"text\":\"" + time + "\"}"));
    }


}
