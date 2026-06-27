package my.project.trellocopy.controller;

import lombok.RequiredArgsConstructor;
import my.project.trellocopy.config.CurrentUser;
import my.project.trellocopy.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class DashboardController {

    @GetMapping
    public String landing() {
        return "landing";
    }

    @GetMapping("/dashboard")
    public String dashboard(@CurrentUser User user, Model model) {
        if (user != null) {
            model.addAttribute("userId", user.getId());
            model.addAttribute("userName", user.getUsername());
            model.addAttribute("userAvatar", user.getAvatarUrl() != null ? user.getAvatarUrl() : "");
        }
        return "dashboard";
    }

}
