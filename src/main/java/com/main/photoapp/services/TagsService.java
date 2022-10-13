package com.main.photoapp.services;

import com.main.photoapp.exceptions.TagAlreadyExistsException;
import com.main.photoapp.exceptions.TagNotFoundException;
import com.main.photoapp.models.Tag;
import com.main.photoapp.repositories.TagsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.Optional;

@Component
public class TagsService {
    @Autowired
    private TagsRepository repository;

    public Tag getTagByID(int id) throws TagNotFoundException {
        Optional<Tag> tag = repository.findById(id);
        if (tag.isEmpty()) throw new TagNotFoundException(id);
        return tag.get();
    }

    public int addTag(String text) throws TagAlreadyExistsException {
        if (repository.existsByText(text)) throw new TagAlreadyExistsException(text);
        return repository.save(new Tag(text)).getId();
    }

    public void removeTag(int id) throws TagNotFoundException {
        if (!repository.existsById(id)) throw new TagNotFoundException(id);
        repository.deleteById(id);

    }

    public void updateTag(int id, String text) throws TagNotFoundException {
        if (!repository.existsById(id)) throw new TagNotFoundException(id);
        repository.save(new Tag(id, text));
    }
}
