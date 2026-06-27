package my.project.trellocopy.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import my.project.trellocopy.entity.User;
import my.project.trellocopy.entity.Workspace;
import my.project.trellocopy.entity.WorkspaceInvitation;
import my.project.trellocopy.entity.WorkspaceWorker;
import my.project.trellocopy.entity.enums.ErrorType;
import my.project.trellocopy.entity.enums.InvitationStatus;
import my.project.trellocopy.entity.enums.WorkspaceWorkerRole;
import my.project.trellocopy.entity.projection.DashboardSearchProjection;
import my.project.trellocopy.entity.projection.UserSearchProjection;
import my.project.trellocopy.entity.request.MemberRoleChangeRequest;
import my.project.trellocopy.entity.request.SendInvitationRequest;
import my.project.trellocopy.entity.request.WorkspaceCreateRequest;
import my.project.trellocopy.entity.response.*;
import my.project.trellocopy.exception.RestException;
import my.project.trellocopy.repository.*;
import org.apache.coyote.BadRequestException;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.Arrays.stream;

@Service
@RequiredArgsConstructor
public class WorkspaceService {
    final WorkspaceRepository workspaceRepository;
    final BoardRepository boardRepository;
    final WorkspaceInvitationRepository workspaceInvitationRepository;
    final MailService mailService;
    private final WorkspaceWorkerRepository workspaceWorkerRepository;
    final UserRepository userRepository;
    final WorkspaceWorkerService workspaceWorkerService;

    public List<WorkspaceResponse> getWorkspaces(User user) {
        List<WorkspaceResponse> workspacesDatas = workspaceRepository.getWorkspacesDatas(user).stream()
                .map(workspaceProjection -> new WorkspaceResponse(
                workspaceProjection.getId(),
                workspaceProjection.getName(),
                workspaceProjection.getDescription(),
                workspaceProjection.getBoardCount(),
                workspaceProjection.getMemberCount(),
                workspaceProjection.getColor()
        )).toList();

        return workspacesDatas;
    }

    public WorkspaceResponse createWorkspace(WorkspaceCreateRequest workspaceRequest, User currentUser) {
        Workspace workspace = new Workspace();
        workspace.setName(workspaceRequest.getName());
        workspace.setDescription(workspaceRequest.getDescription());
        workspace.setColor(colorGenerator());
        workspace.setLogoUrl(getAvatarUrl(workspace.getName()));
        workspace.setOwner(currentUser);
        Workspace save = workspaceRepository.save(workspace);

        workspaceWorkerService.createWorkspaceWorker(workspace, currentUser, WorkspaceWorkerRole.OWNER);

        return new WorkspaceResponse(save.getId(),
                save.getName(),
                save.getDescription(),
                0L,
                1L,
                save.getColor());
    }


    public String getAvatarUrl(String name) {
//        if(user.getAvatarUrl() != null) {
//            return user.getAvatarUrl();
//        }

        return "https://api.dicebear.com/9.x/fun-emoji/svg?seed="
                + name;
    }

    public String colorGenerator() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[4];
        random.nextBytes(bytes);

        return String.format(
                "#%02X%02X%02X%02X",
                bytes[0] & 0xFF,
                bytes[1] & 0xFF,
                bytes[2] & 0xFF,
                bytes[3] & 0xFF);
    }

    public Object getBoardsByWorkspaceId(User currentUser, Long id) {
        Workspace workspace = workspaceRepository.
                findByIdAndOwnerOrIsWorker(id,currentUser)
                .orElseThrow(() -> RestException
                        .restThrow(ErrorType.WORKSPACE_NOT_FOUND_OR_IS_NOT_RELATED));
        List<BoardResponse> list = boardRepository.getBoardsByWorkspace(workspace)
                .stream().map(board -> new BoardResponse(
                        board.getId(),
                        board.getTitle(),
                        board.getDescription(),
                        board.getColor(),
                        board.getTaskCount(),
                        board.getCreatedAt()
                )).toList();
        return list;
    }

    public Workspace getWorkspaceById(Long aLong) {
        return workspaceRepository.findById(aLong)
                .orElseThrow(() -> RestException
                        .restThrow(ErrorType.WORKSPACE_NOT_FOUND_OR_IS_NOT_RELATED));
    }


    public Object search(String query, Long workspaceId) {
        query = prepareSearchQuery(query);

       List<DashboardSearchProjection> dashboardSearchProjection =  workspaceRepository.search(query, workspaceId);
       List<BoardDtoResponse> boards = new ArrayList<>();
       List<TaskDtoResponse> tasks = new ArrayList<>();
       List<WorkspaceWorkerDtoResponse> members = new ArrayList<>();
        for (DashboardSearchProjection projection : dashboardSearchProjection) {
            if (projection.getBoardId()!=null && projection.getBoardColor() !=null && projection.getBoardTitle()!=null) {
                boards.add(new BoardDtoResponse(
                        projection.getBoardId(),
                        projection.getBoardTitle(),
                        projection.getBoardColor()
                ));
            }
            if (projection.getTaskId() != null && projection.getTaskTitle() != null && projection.getTaskBoardId()!=null) {
                tasks.add(new TaskDtoResponse(
                        projection.getTaskId(),
                        projection.getTaskTitle(),
                        projection.getTaskBoardId()
                ));
            }
            if (projection.getMemberId() != null && projection.getMemberName() != null && projection.getAvatarUrl()!=null){
                members.add(new WorkspaceWorkerDtoResponse(
                        projection.getMemberId(),
                        projection.getMemberName(),
                        projection.getAvatarUrl()
                ));
            }
        }
        return new DashboardSearchResponse(
                boards,
                tasks,
                members
        );
    }

    private String prepareSearchQuery(String query) {
        if (query == null || query.isBlank()) {
            return null;
        }
        return "%" + query.trim().replaceAll("\\s+", "%") + "%";
    }

    public Object getWorkspaceMembers(User currentUser, Long workspaceId) {
        Workspace workspace = workspaceRepository.
                findByIdAndOwnerOrIsWorker(workspaceId,currentUser)
                .orElseThrow(() -> RestException
                        .restThrow(ErrorType.WORKSPACE_NOT_FOUND_OR_IS_NOT_RELATED));
        return workspaceRepository.getWorkspaceMembers(workspace)
                .stream().map(member -> new WorkSpaceMembersResponse(
                        member.getId(),
                        member.getWorker().getId(),
                        member.getWorker().getUsername(),
                        member.getWorker().getAvatarUrl(),
                        member.getWorkerRole(),
                        member.getWorker().getEmail(),
                        member.getWorkerRole().equals(WorkspaceWorkerRole.OWNER) || workspace.getOwner().getId().equals(member.getWorker().getId()),
                        member.getWorker().getFullName()
                )).toList();

    }

    public Object searchWorkspaceUsers(User currentUser, Long workspaceId, String query) {
        Workspace workspace = workspaceRepository.
                findById(workspaceId)
                .orElseThrow(() -> RestException
                        .restThrow(ErrorType.WORKSPACE_NOT_FOUND_OR_IS_NOT_RELATED));
        query = prepareSearchQuery(query);

        List<UserSearchProjection> members = workspaceInvitationRepository.getWorkersInvitedAndMember(workspaceId,query, currentUser.getId());
        return members.stream().map(member -> new UserSearchResponse(
                member.getId(),
                member.getEmail(),
                member.getUsername(),
                member.getFullName(),
                member.getAvatarUrl(),
                member.getAlreadyMember(),
                member.getAlreadyInvited()
        ));
    }

    public Object sendInvitation(SendInvitationRequest request, Long workspaceId,User user) {
        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(() -> RestException.restThrow(ErrorType.WORKSPACE_NOT_FOUND));
        WorkspaceInvitation invitation = new WorkspaceInvitation();
        invitation.setWorkspace(workspace);
        invitation.setInviter(user);
        invitation.setRole(WorkspaceWorkerRole.valueOf(request.role().toUpperCase()));
        invitation.setStatus(InvitationStatus.PENDING);
        invitation.setToken(UUID.randomUUID().toString());
        invitation.setExpireAt(LocalDateTime.now().plusDays(5));
        invitation.setInviteeEmail(request.email());
        WorkspaceInvitation save = workspaceInvitationRepository.save(invitation);
        mailService.sendMail(save);

        return toUserInvitationResponse(save);
    }

    private static @NonNull UserInvitationResponse toUserInvitationResponse(WorkspaceInvitation save) {
        return new UserInvitationResponse(
                save.getId(),
                save.getWorkspace().getId(),
                save.getInviter().getId(),
                save.getInviter().getFullName(),
                save.getInviter().getAvatarUrl(),
                save.getInviteeEmail(),
                save.getRole(),
                save.getStatus(),
                save.getCreatedAt()
        );
    }

    public Object getAllInvitations(User currentUser, Long workspaceId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> RestException.restThrow(ErrorType.WORKSPACE_NOT_FOUND));
        List<WorkspaceInvitation> invitations = workspaceInvitationRepository.
                findWorkspaceInvitationsByWorkspaceAndStatus(workspace, InvitationStatus.PENDING, LocalDateTime.now());
        return invitations.stream().map(WorkspaceService::toUserInvitationResponse);
    }

    @Transactional
    public Object cancelInvitation(Long invitationId, Long workspaceId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> RestException.restThrow(ErrorType.WORKSPACE_NOT_FOUND));
        WorkspaceInvitation workspaceInvitation = workspaceInvitationRepository.findById(invitationId).orElseThrow(() -> RestException.restThrow(ErrorType.INVITATION_NOT_FOUND));
        workspaceInvitation.setStatus(InvitationStatus.CANCELLED);
        workspaceInvitation.setUpdatedAt(LocalDateTime.now());
        WorkspaceInvitation save = workspaceInvitationRepository.save(workspaceInvitation);
        return toUserInvitationResponse(save);
    }

    public Object getInvitation(String token) {
        WorkspaceInvitation workspaceInvitation = workspaceInvitationRepository
                .getWorkspaceInvitationByToken(token).orElseThrow(() -> RestException.restThrow(ErrorType.INVITATION_NOT_FOUND));
        return toUserInvitationResponse(workspaceInvitation);

    }

    @Transactional
    public Object acceptInvitation(String token, User user) throws BadRequestException {
        if (user == null) {
            return RestException.restThrow(ErrorType.USER_IS_NOT_AUTHENTICATED);
        }
        WorkspaceInvitation workspaceInvitation = workspaceInvitationRepository
                .getWorkspaceInvitationByToken(token).orElseThrow(() -> RestException.restThrow(ErrorType.INVITATION_NOT_FOUND));
        if (workspaceInvitation.getStatus() != InvitationStatus.PENDING) {
            String error;
            error = switch (workspaceInvitation.getStatus()) {
                case CANCELLED -> "already been cancelled by the inviter";
                case PENDING -> null;
                case ACCEPTED -> "invitation already been accepted";
                case DECLINED -> "invitation already been declined";
            };
            if (workspaceInvitation.getExpireAt().isBefore(LocalDateTime.now())) {
                error = "invitation was expired";
            }
            throw new BadRequestException(error);
        }
        else {

            workspaceInvitation.setStatus(InvitationStatus.ACCEPTED);
            workspaceInvitation.setUpdatedAt(LocalDateTime.now());
            workspaceInvitationRepository.save(workspaceInvitation);
            WorkspaceWorker workspaceWorker = new WorkspaceWorker();
            workspaceWorker.setWorkspace(workspaceInvitation.getWorkspace());
            workspaceWorker.setWorker(user);
            workspaceWorker.setWorkerRole(workspaceInvitation.getRole());
            workspaceWorkerRepository.save(workspaceWorker);
            return Map.of("workspaceId",  workspaceInvitation.getWorkspace().getId(),
                            "message", "You've joined "+ workspaceInvitation.getWorkspace().getName());
        }
    }

    @Transactional
    public Object declineInvitation(String token, User user) throws BadRequestException {
        if (user == null) {
            return RestException.restThrow(ErrorType.USER_IS_NOT_AUTHENTICATED);
        }
        WorkspaceInvitation workspaceInvitation = workspaceInvitationRepository
                .getWorkspaceInvitationByToken(token).orElseThrow(() -> RestException.restThrow(ErrorType.INVITATION_NOT_FOUND));
        if (workspaceInvitation.getStatus() != InvitationStatus.PENDING) {
            String error;
            error = switch (workspaceInvitation.getStatus()) {
                case CANCELLED -> "already been cancelled by the inviter";
                case PENDING -> null;
                case ACCEPTED -> "invitation already been accepted";
                case DECLINED -> "invitation already been declined";
            };
            if (workspaceInvitation.getExpireAt().isBefore(LocalDateTime.now())) {
                error = "invitation was expired";
            }
            throw new BadRequestException(error);
        }
        else {
            workspaceInvitation.setStatus(InvitationStatus.DECLINED);
            workspaceInvitation.setUpdatedAt(LocalDateTime.now());
            workspaceInvitationRepository.save(workspaceInvitation);
            return Map.of("message", "You've declined invitation from: "+ workspaceInvitation.getWorkspace().getName());
        }
    }

    @Transactional
    public Object updateMemberRole(Long memberId, Long workspaceId,
                                   MemberRoleChangeRequest request,
                                   User user) {

        WorkspaceWorker worker = validateWorker(memberId, workspaceId, user);


        worker.setWorkerRole(WorkspaceWorkerRole.valueOf(request.role()));

        return Map.of("message", "Role updated");

    }

    @Transactional
    public Object deleteWorker(Long memberId, Long workspaceId, User user) {

        WorkspaceWorker worker = validateWorker(memberId, workspaceId, user);

        worker.setDeleted(true);

        workspaceWorkerRepository.save(worker);

        return Map.of("message", "Member has been deleted");
    }





    public WorkspaceWorker validateWorker(Long memberId, Long workspaceId, User user) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> RestException.restThrow(ErrorType.WORKSPACE_NOT_FOUND));

        if (!workspace.getOwner().getId().equals(user.getId())){
            throw RestException.restThrow(ErrorType.USER_IS_NOT_OWNER_OF_WORKSPACE);
        }
        WorkspaceWorker worker = workspaceWorkerRepository
                .findWorkspaceWorkerByIdAndWorkspace(memberId, workspace)
                .orElseThrow(() -> RestException.restThrow(ErrorType.WORKER_NOT_FOUND));

        return worker;
    }

}
