package com.dml.project.rbs.Redis;
import java.util.concurrent.TimeUnit;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ch.qos.logback.classic.Logger;
@Repository
public class RedisUtility {
    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(RedisUtility.class);
    private transient final ValueOperations<String, String> valOps;
    private transient final RedisTemplate<String, String> redisTemplate;
    @Autowired
    public RedisUtility(final RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valOps = redisTemplate.opsForValue();
    }

    public void delData(String key)
    {
        redisTemplate.delete(key);
    }
    public String getData(final String key) {
        String str = this.valOps.get(key);
        LOGGER.info("Getting data from redis for key:{}", key);
        return str;
    }
    public void setData(final String key, final Object data) {
        setData(key, data, 5);
    }
    public void setData(final String key, final Object data, final Integer expiryInMinutes) {
        try {
            if (data == null)
                return;
            final ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            String stringData;
            if (data instanceof String) {
                stringData = (String) data;
            } else {
                stringData = objectMapper.writeValueAsString(data);
            }
            LOGGER.info("Setting data to redis for key:{}", key);
            this.valOps.set(key, stringData, expiryInMinutes, TimeUnit.MINUTES);
        }
        catch (JsonProcessingException e) {
            LOGGER.warn("Error saving data to redis key : {}", key, e);
        }
    }
}