package com.main.photoapp.services.Tags;

import com.main.photoapp.exceptions.TagAlreadyExistsException;
import com.main.photoapp.exceptions.TagNotFoundException;
import com.main.photoapp.models.Tag.Tag;
import com.main.photoapp.repositories.TagsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TagsService {
    @Autowired
    private TagsRepository repository;

    public Tag getTagById(int id) {
        return  repository.findById(id).orElse(null);
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

    public List<Tag> getTagByName(String text) {
        return repository.findByTextLike(text);
    }

    public boolean existsTagWithText(String text) {
        return repository.existsByText(text);
    }

    protected boolean tagNotExists(int tagId) {
        return !repository.existsById(tagId);
    }
}
