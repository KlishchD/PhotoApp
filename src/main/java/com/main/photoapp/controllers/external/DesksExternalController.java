package com.main.photoapp.controllers.external;

import com.main.photoapp.exceptions.DeskNotFoundException;
import com.main.photoapp.exceptions.IncorrectUsernameFormat;
import com.main.photoapp.exceptions.NotEnoughPermissionsException;
import com.main.photoapp.exceptions.UserNotFoundException;
import com.main.photoapp.models.Desk.Desk;
import com.main.photoapp.models.Desk.OwnersMapping.DeskOwnerMapping;
import com.main.photoapp.services.Desks.DesksOwnerService;
import com.main.photoapp.services.Desks.DesksService;
import com.main.photoapp.services.UsersService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;


@ControllerAdvice
@Controller
public class DesksExternalController extends ExternalControllerBase {

    private final DesksOwnerService desksOwnerService;

    private final DesksService desksService;


    public DesksExternalController(DesksOwnerService desksOwnerService, DesksService desksService, UsersService usersService) {
        super(usersService);
        this.desksOwnerService = desksOwnerService;
        this.desksService = desksService;
    }

    @PostMapping("/perform_desk_creation")
    public void createDesk(@RequestParam String name, @RequestParam String description, @RequestParam Desk.DeskType type, HttpServletResponse response) throws IOException {
        try {
            desksService.addDesk(name, description, getCurrentUser().getId(), type);
            response.sendRedirect("/profile");
        } catch (Exception e) {
            response.sendRedirect("/create_desk?error=" + e.getMessage());
        }
    }

    @PostMapping("/perform_desk_deletion")
    public void removeDesk(@RequestParam int deskId, HttpServletResponse response) throws IOException {
        try {
            desksService.removeDesk(deskId, getCurrentUser().getId());
            response.sendRedirect("/profile");
        } catch (Exception e) {
            response.sendRedirect("/profile?error=" + e.getMessage());
        }
    }


    @GetMapping("/create_desk")
    public String createDesk(@RequestParam(required = false) String error) {
        return "desks/create_desk";
    }

    @GetMapping("/update_desk")
    public String updateDesk(@RequestParam int deskId, @RequestParam(defaultValue = "") String error, Model model) throws UserNotFoundException, IncorrectUsernameFormat, IOException, DeskNotFoundException, NotEnoughPermissionsException {
        int userId = getCurrentUser().getId();
        List<DeskOwnerMapping> owners = desksOwnerService.getOwners(deskId, getCurrentUser().getId());
        Map<Integer, String> usernames = usersService.getUsernamesMap(owners.stream().map(DeskOwnerMapping::getUserId).toList());

        model.addAttribute("userId", getCurrentUser().getId());
        model.addAttribute("username", getCurrentUser().getUsername());
        model.addAttribute("owners", owners);
        model.addAttribute("usernames", usernames);


        if (!desksOwnerService.getDeskOwnerPermission(deskId, userId).canModifyDeskInformation()) {
            model.addAttribute("error", "Not enough permissions");
        } else {
            try {
                Desk desk = desksService.getDeskInformation(deskId, userId);
                model.addAttribute("desk", desk);
            } catch (Exception e) {
                model.addAttribute("error", e.getMessage());
            }
        }
        return "desks/update_desk";
    }

    @PostMapping("/perform_desk_update")
    public void updateDesk(Desk desk, HttpServletResponse response) throws IOException {
        try {
            desksService.updateDesk(desk.getId(), desk, getCurrentUser().getId());
            response.sendRedirect("/profile");
        } catch (Exception e) {
            response.sendRedirect("/update_desk?deskId=" + desk.getId() + "&error=" + e.getMessage());
        }
    }


}
