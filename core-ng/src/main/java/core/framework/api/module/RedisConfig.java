package core.framework.api.module;

import core.framework.api.redis.Redis;
import core.framework.impl.module.ModuleContext;
import core.framework.impl.redis.RedisImpl;
import core.framework.impl.resource.RefreshPoolJob;
import core.framework.impl.scheduler.FixedRateTrigger;

import java.time.Duration;

/**
 * @author neo
 */
public final class RedisConfig {
    private final ModuleContext context;
    private final RedisImpl redis;

    public RedisConfig(ModuleContext context) {
        this.context = context;

        if (context.beanFactory.registered(Redis.class, null)) {
            redis = context.beanFactory.bean(Redis.class, null);
        } else if (context.isTest()) {
            redis = null;
            context.beanFactory.bind(Redis.class, null, context.mockFactory.create(Redis.class));
        } else {
            redis = new RedisImpl();
            context.shutdownHook.add(redis::close);
            context.scheduler().addTrigger(new FixedRateTrigger("refresh-redis-pool", new RefreshPoolJob(redis.pool), Duration.ofMinutes(5)));
            context.beanFactory.bind(Redis.class, null, redis);
        }
    }

    public void host(String host) {
        if (!context.isTest()) {
            redis.host(host);
        }
    }

    public void poolSize(int minSize, int maxSize) {
        if (!context.isTest()) {
            redis.pool.size(minSize, maxSize);
        }
    }

    public void slowQueryThreshold(Duration slowQueryThreshold) {
        if (!context.isTest()) {
            redis.slowQueryThreshold(slowQueryThreshold);
        }
    }

    public void timeout(Duration timeout) {
        if (!context.isTest()) {
            redis.timeout(timeout);
        }
    }
}
