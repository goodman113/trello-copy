package my.project.trellocopy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskPresenceService {
    private final String PREFIX  = "task:presence:";
    final StringRedisTemplate redisTemplate;

    public void join(Long taskId, Long userId,String name, String avatarUrl){
        String key  = PREFIX + taskId;
        String value = userId+":"+name+ ":"+(avatarUrl != null ? avatarUrl : "");
        redisTemplate.opsForHash().put(key, String.valueOf(userId), value);
        redisTemplate.expire(key, java.time.Duration.ofMinutes(5));

    }

    public void leave(Long taskId, Long userId){
        String key = PREFIX + taskId;
        redisTemplate.opsForHash().delete(key, String.valueOf(userId));
    }

    public List<Map<String, Object>> getViewers(Long taskId) {
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(PREFIX + taskId);
        return entries.values().stream().map(v -> {
            String[] parts = v.toString().split(":", 3);
            Map<String, Object> m = new HashMap<>();
            m.put("id", Long.parseLong(parts[0]));
            m.put("name", parts[1]);
            m.put("avatarUrl", parts.length > 2 ? parts[2] : "");
            return m;
        }).collect(Collectors.toList());
    }

}
