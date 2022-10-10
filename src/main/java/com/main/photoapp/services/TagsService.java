package com.main.photoapp.services;

import com.main.photoapp.models.Tag;
import com.main.photoapp.repositories.TagsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@Component
public class TagsService {
    @Autowired
    private TagsRepository repository;

    public Tag getTagByID(int id) {
        return repository.findById(id).get();
    }

    public void addTag(String text) {
        repository.save(new Tag(text));
    }

    public void removeTag(int id) {
        repository.deleteById(id);

    }

    public void updateTag(int id, String text) {
        Tag tag = repository.findById(id).orElseThrow();
        tag.setText(text);
        repository.save(tag);
    }
}
