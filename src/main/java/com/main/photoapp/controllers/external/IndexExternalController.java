package com.main.photoapp.controllers.external;

import com.main.photoapp.exceptions.DeskNotFoundException;
import com.main.photoapp.exceptions.NotEnoughPermissionsException;
import com.main.photoapp.exceptions.UserNotFoundException;
import com.main.photoapp.models.Photo;
import com.main.photoapp.services.Desks.DesksOwnerService;
import com.main.photoapp.services.Desks.DesksPhotoService;
import com.main.photoapp.services.PhotoService;
import com.main.photoapp.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@ControllerAdvice
public class IndexExternalController extends ExternalControllerBase {
    private final UsersService usersService;

    private final PhotoService photoService;

    @Autowired
    public IndexExternalController(UsersService usersService, UsersService usersService1, PhotoService photoService) {
        super(usersService);
        this.usersService = usersService1;
        this.photoService = photoService;
    }

    @GetMapping("/")
    public String viewPhotos(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "2") int pageSize, Model model) throws DeskNotFoundException, UserNotFoundException, NotEnoughPermissionsException {
        Page<Photo> photos = photoService.getPhotos(page, pageSize);
        model.addAttribute("photos", photos.getContent());
        model.addAttribute("users", usersService.getUsernamesMap(photos.map(Photo::getOwnerId).getContent()));
        model.addAttribute("pages", photoService.getPageNumber(pageSize));
        model.addAttribute("userId", getCurrentUser().getId());
        return "index";
    }
}
