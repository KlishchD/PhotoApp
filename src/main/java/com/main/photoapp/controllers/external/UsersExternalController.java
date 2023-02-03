package com.main.photoapp.controllers.external;


import com.main.photoapp.models.Desk.Desk;
import com.main.photoapp.models.Desk.OwnersMapping.DeskOwnerMapping;
import com.main.photoapp.models.User;
import com.main.photoapp.services.Desks.DesksOwnerService;
import com.main.photoapp.services.Desks.DesksPhotoService;
import com.main.photoapp.services.Desks.DesksService;
import com.main.photoapp.services.Photos.PhotoService;
import com.main.photoapp.services.UsersService;
import com.main.photoapp.utils.DesksTypesMapping;
import com.main.photoapp.utils.PermissionMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
@Controller
public class UsersExternalController extends ExternalControllerBase {

    private final DesksOwnerService desksOwnerService;

    private final DesksService desksService;

    private final DesksPhotoService desksPhotoService;

    private final PhotoService photoService;

    public UsersExternalController(DesksOwnerService desksOwnerService, DesksService desksService, DesksPhotoService desksPhotoService, UsersService usersService, PhotoService photoService) {
        super(usersService);
        this.desksOwnerService = desksOwnerService;
        this.desksService = desksService;
        this.desksPhotoService = desksPhotoService;
        this.photoService = photoService;
    }


    @GetMapping("/registration")
    public String registration(@RequestParam(required = false, defaultValue = "") String error, @RequestParam(required = false, defaultValue = "") String email, @RequestParam(required = false, defaultValue = "") String username) {
        return "users/registration";
    }

    @PostMapping("/perform_registration")
    public void registration(@RequestParam String username, @RequestParam String email, @RequestParam String password, HttpServletResponse response) throws IOException {
        try {
            usersService.createUser(username, email, password);
            response.sendRedirect("/login");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("/registration?error=" + e.getMessage() + "&email=" + email + "&username=" + username);
        }
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false, defaultValue = "false") String error, @RequestParam(required = false, defaultValue = "false") boolean logout) {
        return "users/login";
    }

    @GetMapping("/profile")
    public String getDesks(@RequestParam(defaultValue = "") String error, @RequestParam(defaultValue = "ALL") String permission, @RequestParam(defaultValue = "ALL") String type, Model model) throws Exception {
        User user = getCurrentUser();

        List<Desk> desks = desksService.getAllDesksForUser(user.getId(),
                PermissionMapping.getPermissionMapping().get(permission),
                DesksTypesMapping.getTypesMapping().get(type));

        Map<Integer, List<String>> owners = desksOwnerService.getOwnersNames(desks);

        model.addAttribute("photos", desksPhotoService.getFirstPhotoForDesks(desks));
        model.addAttribute("desks", desks);
        model.addAttribute("owners", owners);
        model.addAttribute("desksCount", desks.size());
        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("permission", permission);
        model.addAttribute("type", type);

        return "users/profile";
    }

    @GetMapping("/find_users")
    @ResponseBody
    public List<User> findUsers(@RequestParam(required = false) String username, Model model) {
        return usersService.findUsers(username);
    }
}
