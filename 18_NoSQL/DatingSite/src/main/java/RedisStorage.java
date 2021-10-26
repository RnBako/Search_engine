import org.redisson.Redisson;
import org.redisson.api.RKeys;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisConnectionException;
import org.redisson.config.Config;

import java.util.Date;

public class RedisStorage {
    private RedissonClient redissonClient;
    private RKeys rKeys;
    private RScoredSortedSet<String> userQuery;
    private final static String KEY = "USERS_QUERY";

    private long getTime() {
        return new Date().getTime();
    }

    public void init() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        try {
            redissonClient = Redisson.create(config);
        } catch (RedisConnectionException ex) {
            ex.printStackTrace();
        }
        rKeys = redissonClient.getKeys();
        userQuery = redissonClient.getScoredSortedSet(KEY);
        rKeys.delete(KEY);
    }

    public void shutdown() {
        redissonClient.shutdown();
    }

    public void addInQuery(int userId) {
        userQuery.add(getTime(), String.valueOf(userId));
    }

    public void showUser() {
        String user = userQuery.first();
        System.out.println("Показали - Пользователя " + user);
        userQuery.add(getTime(), user);
    }
}
