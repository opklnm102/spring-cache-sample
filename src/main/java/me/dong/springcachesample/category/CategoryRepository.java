package me.dong.springcachesample.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ethan.kim on 2018. 5. 17..
 */
public interface CategoryRepository {

    Category findById(long id);

    List<Category> findAll();

    void save(Category category);
}


 */
public interface MissionRedisRepository {

    /**
     * 미션의 부가 정보를 저장한다
     *
     * @param missionId    미션 ID
     * @param AdditionalData 부가 정보
     */
    public abstract void setMissionAdditionalData(Long missionId, String AdditionalData);

    /**
     * 미션의 부가 정보를 가져온다
     *
     * @param missionId 미션 ID
     * @return
     */
    public abstract String getMissionAdditionalData(Long missionId);
}



@Repository
public class MissionRedisRepositoryImpl implements MissionRedisRepository {

    private static final String MISSION_ADDITIONAL_DATA_KEY_FORMAT = "mission:addition:data:%d";

    @Autowired
    private RedisTemplate redisTemplate;

    @CacheEvict(value = "mission:addition:data", key = "T(caching_redis.MissionRedisRepositoryImpl).createKey(#missionId)")
    @Override
    public void setMissionAdditionalData(Long missionId, String additionInfo) {
        String key = String.format(MISSION_ADDITIONAL_DATA_KEY_FORMAT, missionId);
        redisTemplate.opsForValue().set(key, additionInfo);
    }

    @Cacheable(value = "mission:addition:data", key = "T(caching_redis.MissionRedisRepositoryImpl).createKey(#missionId)")
    @Override
    public String getMissionAdditionalData(Long missionId) {
        simulateSlowService();
        String key = String.format(MISSION_ADDITIONAL_DATA_KEY_FORMAT, missionId);
        return (String) redisTemplate.opsForValue().get(key);
    }

    private void simulateSlowService() {
        try {
            long time = 300000L;
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    public static String createKey(Long missionId) {
        return String.format(MISSION_ADDITIONAL_DATA_KEY_FORMAT, missionId);
    }

}