package my.project.trellocopy.service;

import lombok.RequiredArgsConstructor;
import my.project.trellocopy.entity.User;
import my.project.trellocopy.entity.WorkspaceWorker;
import my.project.trellocopy.entity.enums.ErrorType;
import my.project.trellocopy.entity.response.OnlineUserResponse;
import my.project.trellocopy.exception.RestException;
import my.project.trellocopy.repository.UserRepository;
import my.project.trellocopy.repository.WorkspaceWorkerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    final UserRepository userRepository;
    final PresenceService presenceService;
    final WorkspaceWorkerRepository workspaceWorkerRepository;

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> RestException.restThrow(ErrorType.USER_NOT_FOUND));
    }

    public Object getAllUsersByWorkspaceAndOnline(Long workspaceId) {
        Set<Long> onlineUserIds = presenceService.getOnlineUserIds();

        List<WorkspaceWorker> workspaceWorkers = workspaceWorkerRepository.findAllWorkersByWorkspace(workspaceId);

        List<Long> list = workspaceWorkers.stream()
                .map(worker -> worker.getWorker().getId())
                .filter(onlineUserIds::contains).toList();
        return userRepository.findAllById(list).stream().map(user-> new OnlineUserResponse(
               user.getId(),
               user.getUsername(),
               user.getAvatarUrl(),
               "online"
       )).toList();
    }

    public Object getMe(User currentUser) {
        return Map.of("id", currentUser.getId(),
                    "username",currentUser.getUsername(),
                "avatarUrl", currentUser.getAvatarUrl()!=null ? currentUser.getAvatarUrl():"",
                "email", currentUser.getEmail()!=null ? currentUser.getEmail() : "");
    }

    public Optional<User> getByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }
}
