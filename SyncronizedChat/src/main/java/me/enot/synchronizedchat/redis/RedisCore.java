package me.enot.synchronizedchat.redis;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.enot.synchronizedchat.SynchronizedChat;
import me.enot.synchronizedchat.configurations.Settings;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class RedisCore {

//    private static final Logger LOG = LoggerFactory.getLogger(RedisCore.class);

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private JedisPoolConfig config;
    private JedisPool jedisPool;
    protected RedisCore(String host, Integer port, Integer timeout, String password, Integer maxRedisConnections) {
        this.config = new JedisPoolConfig();
        this.config.setMaxTotal(maxRedisConnections);
        if (password == null)
            this.jedisPool = new JedisPool(this.config, host, port, 0);
        else
            this.jedisPool = new JedisPool(this.config, host, port, timeout, password);
    }

    public void jedisQuery(Consumer<Jedis> consumer) {
        try (Jedis jedis = jedisPool.getResource()) {
            consumer.accept(jedis);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T> T get(Function<Jedis, T> function) {
        try (Jedis jedis = jedisPool.getResource()) {
            return function.apply(jedis);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Boolean is(Predicate<Jedis> predicate) {
        try (Jedis jedis = jedisPool.getResource()) {
            return predicate.test(jedis);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String ping() {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.ping();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ERROR";
    }

    public boolean subscribe() {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.subscribe(SynchronizedChat.getJedisPublSubs(), Settings.getInstance().getChannelName());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void message(String channel, String message) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.publish(channel, message);
        }
    }


    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public Gson getGson() {
        return gson;
    }
}
