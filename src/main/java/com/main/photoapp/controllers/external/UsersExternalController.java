package com.main.photoapp.controllers.external;


import com.main.photoapp.models.Desk.Desk;
import com.main.photoapp.models.Desk.OwnersMapping.DeskOwnerMapping;
import com.main.photoapp.models.User;
import com.main.photoapp.services.Desks.DesksOwnerService;
import com.main.photoapp.services.Desks.DesksPhotoService;
import com.main.photoapp.services.Desks.DesksService;
import com.main.photoapp.services.PhotoService;
import com.main.photoapp.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private DesksOwnerService desksOwnerService;

    @Autowired
    private DesksService desksService;

    @Autowired
    private DesksPhotoService desksPhotoService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private PhotoService photoService;

    private static final Map<String, List<DeskOwnerMapping.Permission>> permissionMapping = new HashMap<>();
    private static final Map<String, List<Desk.DeskType>> typesMapping = new HashMap<>();

    static {
        permissionMapping.put("CREATOR", List.of(DeskOwnerMapping.Permission.CREATOR_PERMISSION));
        permissionMapping.put("FULL", List.of(DeskOwnerMapping.Permission.FULL_PERMISSION));
        permissionMapping.put("VIEW", List.of(DeskOwnerMapping.Permission.VIEW_ONLY_PERMISSION));
        permissionMapping.put("NONE", List.of(DeskOwnerMapping.Permission.NO_PERMISSIONS));
        permissionMapping.put("ALL", List.of(DeskOwnerMapping.Permission.values()));

        typesMapping.put("PUBLIC", List.of(Desk.DeskType.PUBLIC));
        typesMapping.put("PRIVATE", List.of(Desk.DeskType.PRIVATE));
        typesMapping.put("ALL", List.of(Desk.DeskType.values()));
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

        List<Desk> desks = desksService.getAllDesksForUser(user.getId(), permissionMapping.get(permission), typesMapping.get(type));
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
