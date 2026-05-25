package my.project.trellocopy.controller;

import ch.qos.logback.core.model.Model;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class DashboardController {

    @GetMapping
    public String dashboard() {
        return "landing";
    }


    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        return "dashboard";
    }




}
