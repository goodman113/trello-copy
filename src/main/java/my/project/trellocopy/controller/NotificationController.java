package my.project.trellocopy.controller;

import lombok.RequiredArgsConstructor;
import my.project.trellocopy.config.CurrentUser;
import my.project.trellocopy.entity.User;
import my.project.trellocopy.entity.enums.NotificationType;
import my.project.trellocopy.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

     private final NotificationService notificationService;


     @GetMapping()
     public ResponseEntity<?> getNotifications(@CurrentUser User user,
                                               @RequestParam(required = false) String filter,
                                               @RequestParam(required = false, defaultValue = "0") Integer page,
                                               @RequestParam(required = false, defaultValue = "20") Integer size) {
         return ResponseEntity.ok(notificationService.getNotificationsByFilter(user,filter,page,size));
     }


    @GetMapping("/count")
    public ResponseEntity<?> getCount(@CurrentUser User user){
        return ResponseEntity.ok(notificationService.getCountForUser(user));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<?> putRead(@CurrentUser User user,
                                     @PathVariable Long id){
         return ResponseEntity.ok(notificationService.markReadNotificationByUser(user,id));
    }

    @PutMapping("/read-all")
    public ResponseEntity<?> readAll(@CurrentUser User user){
         return ResponseEntity.ok(notificationService.readAllNotifications(user));
    }



}
