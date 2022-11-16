package com.main.photoapp.controllers.internal.Tags;


import com.main.photoapp.exceptions.TagAlreadyExistsException;
import com.main.photoapp.exceptions.TagNotFoundException;
import com.main.photoapp.models.Tag.Tag;
import com.main.photoapp.services.Tags.TagsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TagsController {

    @Autowired
    private TagsService service;

    @GetMapping("tag/get")
    public Tag getTagByID(@RequestParam int id) throws TagNotFoundException {
        return service.getTagById(id);
    }

    @PostMapping("tag/add")
    public int addTag(@RequestParam String text) throws TagAlreadyExistsException {
        return service.addTag(text);
    }

    @PostMapping("tag/remove")
    public void removeTag(@RequestParam int id) throws TagNotFoundException {
        service.removeTag(id);
    }

    @PostMapping("tag/update")
    public void updateTag(@RequestParam int id, @RequestParam String text) throws TagNotFoundException {
        service.updateTag(id, text);
    }

    @GetMapping("tag/exists")
    public boolean existsTagWithText(@RequestParam String text) {
        return service.existsTagWithText(text);
    }

    @GetMapping("tag/get_by_name")
    public List<Tag> getTagByName(@RequestParam String text) {
        return service.getTagByName(text);
    }
}