package com.main.photoapp.controllers;


import com.main.photoapp.models.Tag;
import com.main.photoapp.repositories.TagsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class TagsController {

    @Autowired
    private TagsRepository repository;

    @GetMapping("/tag/get")
    @ResponseBody
    public Tag getTagByID(@RequestParam int id) {
        return repository.findById(id).get();
    }

    @PostMapping("/tag/add")
    public void addTag(@RequestParam String text) {
        repository.save(new Tag(text));
    }

    @PostMapping("tag/remove")
    public void removeTag(@RequestParam int id) {
        repository.deleteById(id);

    }

    @PutMapping("tag/update")
    public void updateTag(@RequestParam int id, @RequestParam String text) {
        Tag tag = repository.findById(id).get();
        tag.setTag(text);
        repository.save(tag);
    }
}
