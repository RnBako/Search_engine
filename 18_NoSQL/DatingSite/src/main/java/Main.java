import java.util.Random;

public class Main {

    private static int USER_COUNT = 20;

    public static void main(String[] args) throws InterruptedException {
        RedisStorage redisStorage = new RedisStorage();
        redisStorage.init();

        generateUserQueue(redisStorage);
        for (int second = 1; second <= 60; second++){
            if (second % 10 == 0) {
                int generousUser = new Random().nextInt(USER_COUNT);
                redisStorage.upInQuery(generousUser);
            }
            redisStorage.showUser();
            Thread.sleep(1000);
        }
        redisStorage.shutdown();
    }

    public static void generateUserQueue(RedisStorage redisStorage) {
        for (int i = 1; i <= USER_COUNT; i++) {
            redisStorage.addInQuery(i);
        }
    }
}
