package my.project.trellocopy.service;

import jakarta.mail.internet.MimeMessage;
import jakarta.validation.constraints.AssertFalse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import my.project.trellocopy.entity.Workspace;
import my.project.trellocopy.entity.WorkspaceInvitation;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
public class MailService {
    final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;


    @Async
    @SneakyThrows
    public  void sendMail(WorkspaceInvitation invitation) {
        Context ctx  = new Context();
        ctx.setVariable("workspaceName", invitation.getWorkspace().getName());
        ctx.setVariable("inviterName", invitation.getInviter().getFullName());
        ctx.setVariable("inviterEmail", invitation.getInviter().getEmail());
        ctx.setVariable("role",  invitation.getRole());
        ctx.setVariable("inviteLink", "https://localhost:8080/invitation?token=" + invitation.getToken());
        ctx.setVariable("expiresInDays",5);

        String html = templateEngine.process("email-invitation", ctx);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        helper.setTo(invitation.getInviteeEmail());
        helper.setSubject("Invitation to join "+ invitation.getWorkspace().getName());
        helper.setText(html, true);

        mailSender.send(mimeMessage);
    }
}
