package my.project.trellocopy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PresenceService {
    private final StringRedisTemplate redisTemplate;

    private static final String PREFIX = "presence:user:";


    public void connect(Long userId){
        String key = PREFIX + userId;
        redisTemplate.opsForValue().increment(key);
        redisTemplate.expire(key, Duration.ofHours(1)
        );

        redisTemplate.opsForSet()
                .add("online_users",
                        String.valueOf(userId));
    }

    public void disconnect(Long userId){
        String key = PREFIX + userId;
        String value = redisTemplate.opsForValue().get(key);
        if (value == null){
            return;
        }
        int count = Integer.parseInt(value);
        if (count <=1){
            redisTemplate.delete(key);
            redisTemplate.opsForSet().remove("online_users", String.valueOf(userId));
        }else {
            redisTemplate.opsForValue().decrement(key);
        }
    }

    public boolean isOnline(Long userId){
        return Boolean.TRUE.equals(redisTemplate.hasKey(PREFIX + userId));
    }

    public Set<Long> getOnlineUserIds(){

        return redisTemplate.opsForSet().members("online_users").stream()
                .map(Long::valueOf)
                .collect(java.util.stream.Collectors.toSet());

    }
}
