package com.main.photoapp.Utils;

import com.main.photoapp.services.TagsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Component
public class TagsUtils {
    @Autowired
    private TagsService service;

    private String getIdString(MvcResult result) throws UnsupportedEncodingException {
        return result.getResponse().getContentAsString();
    }

    public String createTag(String text) throws Exception {
        return String.valueOf(service.addTag(text));
    }

    public String createTag() throws Exception {
        return String.valueOf(service.addTag(getCurrentTime()));
    }

    public String getCurrentTime() {
        return LocalDateTime.now().toString();
    }
}
