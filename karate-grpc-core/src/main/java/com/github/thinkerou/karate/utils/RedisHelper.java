package com.github.thinkerou.karate.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.github.fppt.jedismock.RedisServer;
import com.github.thinkerou.karate.constants.RedisParams;

import redis.clients.jedis.Jedis;

/**
 * RedisHelper
 *
 * @author thinkerou
 */
public final class RedisHelper {

    private static final int REDIS_TIMEOUT = 3000;

    private static volatile Jedis jedis;

    private static void init() {
        if (jedis != null) {
            return;
        }
        synchronized (RedisHelper.class) {
            if (jedis != null) {
                return;
            }
            RedisServer redisServer = JedisMock.getRedisServer();
            jedis = new Jedis(redisServer.getHost(), redisServer.getBindPort(), REDIS_TIMEOUT);
        }
    }

    public RedisHelper() {
        init();
    }

    public Boolean putDescriptorSets(Path descriptorPath) {
        byte[] data;
        try {
            data = Files.readAllBytes(descriptorPath);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        Long status = jedis.hset(RedisParams.KEY.getText(), RedisParams.FIELD.getText(), data);
        if (status != 1) {
            return false;
        }

        return true;
    }

    public byte[] getDescriptorSets() {
        return jedis.hget(RedisParams.KEY.getText(), RedisParams.FIELD.getText());
    }

    public Long deleteDescriptorSets() {
        return jedis.hdel(RedisParams.KEY.getText(), RedisParams.FIELD.getText());
    }
}
