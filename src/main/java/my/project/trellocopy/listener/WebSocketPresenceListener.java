package my.project.trellocopy.listener;

import lombok.RequiredArgsConstructor;
import my.project.trellocopy.entity.User;
import my.project.trellocopy.entity.enums.ErrorType;
import my.project.trellocopy.exception.RestException;
import my.project.trellocopy.repository.UserRepository;
import my.project.trellocopy.service.PresenceService;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.net.Authenticator;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class WebSocketPresenceListener {

    final PresenceService presenceService;

    final UserRepository userRepository;

    @EventListener
    public void handleConnect(SessionConnectedEvent event){
        Authentication user = (Authentication) event.getUser();
        if (user==null){
            return;
        }
        UserDetails userDetails = (UserDetails) user.getPrincipal();

        User user1 = userRepository.findUserByEmail(userDetails.getUsername()).orElseThrow(() -> RestException.restThrow(ErrorType.USER_NOT_FOUND));
        presenceService.connect(user1.getId());
    }
    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event){
        Authentication user = (Authentication) event.getUser();
        if (user==null){
            return;
        }
        UserDetails userDetails = (UserDetails) user.getPrincipal();

        User user1 = userRepository.findUserByEmail(userDetails.getUsername()).orElseThrow(() -> RestException.restThrow(ErrorType.USER_NOT_FOUND));
        presenceService.disconnect(user1.getId());
        user1.setLastSeen(LocalDateTime.now());
        userRepository.save(user1);
    }
}
