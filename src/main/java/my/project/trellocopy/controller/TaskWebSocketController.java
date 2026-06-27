package my.project.trellocopy.controller;

import lombok.RequiredArgsConstructor;
import my.project.trellocopy.entity.payload.PresencePayload;
import my.project.trellocopy.service.TaskPresenceService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class TaskWebSocketController {
    final TaskPresenceService  taskPresenceService;

    @MessageMapping("/task/{taskId}/presence")
    @SendTo("/topic/task/{taskId}/presence")
    public PresencePayload handlePresence(PresencePayload payload, @DestinationVariable Long taskId){
        if ("join".equals(payload.action())){
            taskPresenceService.join(taskId, payload.userId(), payload.name(), payload.avatarUrl());
        } else if ("leave".equals(payload.action())){
            taskPresenceService.leave(taskId, payload.userId());
        }
        return payload;
    }
}
