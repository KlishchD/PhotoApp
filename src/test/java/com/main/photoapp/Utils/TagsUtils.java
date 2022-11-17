package com.main.photoapp.Utils;

import com.main.photoapp.services.Tags.TagsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

import static com.main.photoapp.Utils.RandomTextGenerator.getRandomTagText;


@Component
public class TagsUtils {
    @Autowired
    private TagsService service;

    public String createTag(String text) throws Exception {
        int id = service.addTag(text);
        return String.valueOf(id);
    }

    public String createTag(Random random) throws Exception {
        String text = getRandomTagText(random);
        int id = service.addTag(text);
        return String.valueOf(id);
    }
}
