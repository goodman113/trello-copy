package my.project.trellocopy.controller;

import lombok.RequiredArgsConstructor;
import my.project.trellocopy.config.CurrentUser;
import my.project.trellocopy.entity.User;
import my.project.trellocopy.entity.WorkspaceInvitation;
import my.project.trellocopy.entity.enums.ErrorType;
import my.project.trellocopy.entity.response.InvitationDetailResponse;
import my.project.trellocopy.entity.response.UserDtoResponse;
import my.project.trellocopy.exception.RestException;
import my.project.trellocopy.repository.WorkspaceInvitationRepository;
import my.project.trellocopy.repository.WorkspaceRepository;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class InvitationPageController {

    public final WorkspaceInvitationRepository repository;
    public final WorkspaceRepository workspaceRepository;


    @GetMapping("/invitation")
    public String invitationPage(
            @RequestParam String token,
            Model model,
            @CurrentUser User user
    ) {
        Boolean userAuthenticated = user != null;
        WorkspaceInvitation workspaceInvitation = repository.getWorkspaceInvitationByToken(token)
                .orElse(null);
        InvitationDetailResponse response = null;
        String error = "token is invalid";
        String workspaceName = null, workspaceColor=null;
        if  (workspaceInvitation != null) {
            response = new InvitationDetailResponse(
                    workspaceInvitation.getWorkspace().getId(),
                    workspaceInvitation.getWorkspace().getName(),
                    workspaceInvitation.getWorkspace().getColor(),
                    new UserDtoResponse(
                            workspaceInvitation.getInviter().getId(),
                            workspaceInvitation.getInviter().getUsername(),
                            workspaceInvitation.getInviter().getAvatarUrl()
                    ),
                    workspaceInvitation.getInviteeEmail(),
                    workspaceInvitation.getRole(),
                    workspaceInvitation.getToken(),
                    workspaceInvitation.getStatus(),
                    workspaceInvitation.getExpireAt(),
                    workspaceRepository.getWorkspaceMembersCount(workspaceInvitation.getWorkspace().getId())
            );
            workspaceName = workspaceInvitation.getWorkspace().getName();
            workspaceColor = workspaceInvitation.getWorkspace().getColor();
            if (workspaceInvitation.getExpireAt().isBefore(LocalDateTime.now())){
                error = "token is expired";
            }else
                error = null;
        }

        model.addAttribute("workspaceName", workspaceName);
        model.addAttribute("workspaceColor", workspaceColor);
        model.addAttribute("invitation", response);
        model.addAttribute("error", error);
        model.addAttribute("token", token);
        model.addAttribute("isAuthenticated", userAuthenticated);
        return "invitation-accept";
    }
}
