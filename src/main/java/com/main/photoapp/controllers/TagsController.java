package com.main.photoapp.controllers;


import com.main.photoapp.models.Tag;
import com.main.photoapp.repositories.TagsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class TagsController {

    @Autowired
    private TagsRepository repository;

    @GetMapping("/tag/get_by_id")
    @ResponseBody
    public Optional<Tag> getTagByID(@RequestParam int id) {
        return repository.findById(id);
    }

    @PostMapping("/tag/add")
    public void addTag(@RequestParam String text) {
        repository.save(new Tag(text));
    }

    @PostMapping("tag/remove")
    public void removeTag(@RequestParam int id) {
        if (!repository.existsById(id)) throw new IllegalArgumentException("There is no tag with id:" + id);
        repository.deleteById(id);
    }

    //TODO: update
}
