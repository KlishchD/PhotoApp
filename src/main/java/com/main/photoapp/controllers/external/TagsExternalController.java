package com.main.photoapp.controllers.external;


import com.main.photoapp.services.Tags.TagsPhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@ControllerAdvice
@Controller
public class TagsExternalController extends ExternalControllerBase {
    @Autowired
    private TagsPhotoService tagsPhotoService;

    @GetMapping("/tag")
    public String tags(@RequestParam int photoId, Model model) {
        model.addAttribute("tags", tagsPhotoService.getTagsIdsForPhoto(photoId));
        model.addAttribute("userId", getCurrentUser().getId());
        return "tags/tags";
    }
}
