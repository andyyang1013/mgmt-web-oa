package com.yxy.oa.repository.impl;

import com.yxy.oa.repository.IRedisRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @param <K>
 * @param <V>
 * @Desc redis工具类-接口实现  【业务调用此类，采用注解引入】
 * 基于RedisTemplate之上再次封装，以实现redis接口，方便使用
 */

@Component("redisRepository")
public class RedisRepository<K, V> implements IRedisRepository<K, V> {

    private Logger logger = LoggerFactory.getLogger(RedisRepository.class);

    private final RedisTemplate<K, V> redisTemplate;

    @Autowired
    public RedisRepository(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private BoundValueOperations<K, V> getBoundValueOps(K key) {
        return redisTemplate.boundValueOps(key);
    }

    private BoundZSetOperations<K, V> getBoundZSetOps(K key) {
        return redisTemplate.boundZSetOps(key);
    }

    private BoundSetOperations<K, V> getBoundSetOps(K key) {
        return redisTemplate.boundSetOps(key);
    }

    private BoundListOperations<K, V> getBoundListOps(K key) {
        return redisTemplate.boundListOps(key);
    }

    private <HK, HV> BoundHashOperations<K, HK, HV> getBoundHashOps(K key) {
        return redisTemplate.boundHashOps(key);
    }

    // Key

    @Override
    public void del(final K key) {
        redisTemplate.delete(key);
    }

    @Override
    public void del(final Collection<K> keys) {
        redisTemplate.delete(keys);
    }

    @Override
    public Boolean exists(final K key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public Boolean expire(final K key, final long timeout, final TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    @Override
    public void expireAt(final K key, Date date) {
        redisTemplate.expireAt(key, date);
    }

    @Override
    public Set<K> keys(final K pattern) {
        return redisTemplate.keys(pattern);
    }

    @Override
    public String type(final K key) {
        return redisTemplate.type(key).code();
    }

    @Override
    public V get(final K key) {
        BoundValueOperations<K, V> ops = this.getBoundValueOps(key);
        return ops.get();
    }

    @Override
    public V getSet(final K key, final V value) {
        BoundValueOperations<K, V> ops = this.getBoundValueOps(key);
        return ops.getAndSet(value);
    }

    @Override
    public Long incr(final K key, final long delta) {
        BoundValueOperations<K, V> ops = this.getBoundValueOps(key);
        return ops.increment(delta);
    }

    @Override
    public void set(final K key, final V value) {
        BoundValueOperations<K, V> ops = this.getBoundValueOps(key);
        ops.set(value);
    }

    @Override
    public void set(final K key, final V value, final long timeout, final TimeUnit unit) {
        BoundValueOperations<K, V> ops = this.getBoundValueOps(key);
        ops.set(value, timeout, unit);
    }

    // Hash
    @Override
    public void hDel(final K key, final Object... hKeys) {
        BoundHashOperations<K, K, V> ops = this.getBoundHashOps(key);
        ops.delete(hKeys);
    }

    @Override
    public Boolean hExists(final K key, final K hKeys) {
        BoundHashOperations<K, K, V> ops = this.getBoundHashOps(key);
        return ops.hasKey(hKeys);
    }

    @Override
    public Map<K, V> hGet(final K key) {
        BoundHashOperations<K, K, V> ops = this.getBoundHashOps(key);
        return ops.entries();
    }

    @Override
    public V hGet(final K key, final K hKey) {
        BoundHashOperations<K, K, V> ops = this.getBoundHashOps(key);
        return ops.get(hKey);
    }

    @Override
    public Set<K> hKeys(final K key) {
        BoundHashOperations<K, K, V> ops = this.getBoundHashOps(key);
        return ops.keys();
    }

    @Override
    public Long hLen(final K key) {
        BoundHashOperations<K, K, V> ops = this.getBoundHashOps(key);
        return ops.size();
    }

    @Override
    public void hSet(final K key, final K hk, final V hv) {
        BoundHashOperations<K, K, V> ops = this.getBoundHashOps(key);
        ops.put(hk, hv);
    }

    @Override
    public void hSet(final K key, final Map<K, V> map) {
        BoundHashOperations<K, K, V> ops = this.getBoundHashOps(key);
        ops.putAll(map);
    }

    @Override
    public List<V> hVals(final K key) {
        BoundHashOperations<K, K, V> ops = this.getBoundHashOps(key);
        return ops.values();
    }

    // List
    @Override
    public V lIndex(final K key, final long index) {
        BoundListOperations<K, V> ops = this.getBoundListOps(key);
        return ops.index(index);
    }

    @Override
    public void lInsert(final K key, final long index, V value) {
        BoundListOperations<K, V> ops = this.getBoundListOps(key);
        ops.set(index, value);
    }

    @Override
    public Long lLen(final K key) {
        BoundListOperations<K, V> ops = this.getBoundListOps(key);
        return ops.size();
    }

    @Override
    public V lPop(final K key) {
        BoundListOperations<K, V> ops = this.getBoundListOps(key);
        return ops.leftPop();
    }

    @Override
    public V lPop(final K key, long timeout, TimeUnit unit) {
        BoundListOperations<K, V> ops = this.getBoundListOps(key);
        return ops.leftPop(timeout, unit);
    }

    @Override
    public Long lPush(final K key, final V value) {
        BoundListOperations<K, V> ops = this.getBoundListOps(key);
        return ops.leftPush(value);
    }

    @Override
    public List<V> lRange(final K key, final long start, final long end) {
        BoundListOperations<K, V> ops = this.getBoundListOps(key);
        return ops.range(start, end);
    }

    @Override
    public Long lRem(final K key, final long index, final V value) {
        BoundListOperations<K, V> ops = this.getBoundListOps(key);
        return ops.remove(index, value);
    }

    @Override
    public void lSet(final K key, final long index, final V value) {
        BoundListOperations<K, V> ops = this.getBoundListOps(key);
        ops.set(index, value);
    }

    @Override
    public void ltrim(final K key, final long start, final long end) {
        BoundListOperations<K, V> ops = this.getBoundListOps(key);
        ops.trim(start, end);
    }

    @Override
    public Long rPush(final K key, final V value) {
        BoundListOperations<K, V> ops = this.getBoundListOps(key);
        return ops.rightPush(value);
    }

    @Override
    public V rPop(final K key) {
        BoundListOperations<K, V> ops = this.getBoundListOps(key);
        return ops.rightPop();
    }

    // Set
    @Override
    public Long sAdd(final K key, final V value) {
        BoundSetOperations<K, V> ops = this.getBoundSetOps(key);
        return ops.add(value);
    }

    @Override
    public Set<V> sDiff(final K key) {
        BoundSetOperations<K, V> ops = this.getBoundSetOps(key);
        return ops.diff(key);
    }

    @Override
    public Set<V> sMembers(final K key) {
        BoundSetOperations<K, V> ops = this.getBoundSetOps(key);
        return ops.members();
    }

    @Override
    public Boolean sIsMember(final K key, final V value) {
        BoundSetOperations<K, V> ops = this.getBoundSetOps(key);
        return ops.isMember(value);
    }

    @Override
    public V sPop(final K key) {
        BoundSetOperations<K, V> ops = this.getBoundSetOps(key);
        return ops.pop();
    }

    @Override
    public Long sRem(final K key, final V value) {
        BoundSetOperations<K, V> ops = this.getBoundSetOps(key);
        return ops.remove(value);
    }

    @Override
    public Long sCard(K key) {
        BoundSetOperations<K, V> ops = this.getBoundSetOps(key);
        return ops.size();
    }

    // SortedSet
    @Override
    public void zAdd(final K key, final V value, final double score) {
        BoundZSetOperations<K, V> ops = this.getBoundZSetOps(key);
        ops.add(value, score);
    }

    @Override
    public Set<V> zRange(final K key, final long start, final long end) {
        BoundZSetOperations<K, V> ops = this.getBoundZSetOps(key);
        return ops.range(start, end);
    }

    @Override
    public Long zRem(final K key, final Object... values) {
        BoundZSetOperations<K, V> ops = this.getBoundZSetOps(key);
        return ops.remove(values);
    }

    @Override
    public Long zCard(K key) {
        BoundZSetOperations<K, V> ops = this.getBoundZSetOps(key);
        return ops.zCard();
    }
}
