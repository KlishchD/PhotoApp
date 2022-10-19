package com.main.photoapp.controllers;


import com.main.photoapp.exceptions.TagAlreadyExistsException;
import com.main.photoapp.exceptions.TagNotFoundException;
import com.main.photoapp.models.Tag;
import com.main.photoapp.repositories.TagsRepository;
import com.main.photoapp.services.TagsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class TagsController {

    @Autowired
    private TagsService service;

    @GetMapping("/tag/get")
    @ResponseBody
    public Tag getTagByID(@RequestParam int id) throws TagNotFoundException {
        return service.getTagByID(id);
    }

    @PostMapping("/tag/add")
    @ResponseBody
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
}