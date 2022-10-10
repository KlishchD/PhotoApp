package com.main.photoapp;

import com.main.photoapp.models.Tag;
import com.main.photoapp.repositories.TagsRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class TagsControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private static TagsRepository repository;

    @BeforeEach
    public static void setup() {
        repository.save(new Tag("added0"));
        repository.save(new Tag("added1"));
        repository.save(new Tag("added2"));
    }

    @Test
    public void addTag() throws Exception {
        mockMvc.perform(post("/tag/add").param("text", "added")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void getTag() throws Exception {
        mockMvc.perform(get("/tag/get").param("id", "0")).andDo(print()).andExpect(status().isOk());
    }

}
