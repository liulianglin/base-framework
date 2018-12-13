package com.ldzhn.baseframework.persistence.cache.redis;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Author : llliu
 * @Despriction : redis缓存操作服务（使用StringRedisTemplate版本，另外一个版本为使用Jedis实现，详细参见MJedisUtil）
 *
 *
 *      针对所有的hash 都是以h开头的方法
 *      针对所有的Set 都是以s开头的方法
 *      不含通用方法
 *      针对所有的List 都是以l开头的方法

 * @Date : create on 2018/8/8 23:17
 */
@Component
public class MRedisCache {

    private static final Logger logger = LogManager.getLogger(MRedisCache.class);
    @Autowired
    private StringRedisTemplate redisTemplate;

    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
//        RedisSerializer stringSerializer = new StringRedisSerializer();
//        redisTemplate.setKeySerializer(stringSerializer);
//        redisTemplate.setValueSerializer(stringSerializer);
//        redisTemplate.setHashKeySerializer(stringSerializer);
//        redisTemplate.setHashValueSerializer(stringSerializer);
        this.redisTemplate = redisTemplate;
    }
    //=============================common============================
    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    //============================String=============================
    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, String value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key, String value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta
     * @return
     */
    public long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta
     * @return
     */
    public long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    //================================Map=================================
    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public Object hget(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<Object, Object> hmget(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public boolean hmset(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public boolean hmset(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒)  注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public void hdel(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return
     */
    public double hincr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     * @return
     */
    public double hdecr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

    /**
     * 获取指定map的数据长度
     * @param queueName 指定的map队列名称
     * @return
     */
    public Long hlen(String queueName) {
        return redisTemplate.opsForHash().size(queueName);
    }

    /**
     * 获取队列中所有数据
     * @param queueName
     * @return
     */
    public Map<Object, Object> hGetAll(String queueName) {
        return redisTemplate.opsForHash().entries(queueName);
    }



    //============================set=============================
    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return
     */
    public Set<String> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public boolean sHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSet(String key, String... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSetAndTime(String key, long time, String... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0) expire(key, time);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return
     */
    public long sGetSetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public long setRemove(String key, String... values) {
        try {
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 移动集合元素到另外一个集合
     * @param srcKey
     * @param value
     * @param destKey
     */
    public boolean sSetMove(String srcKey, String value, String destKey){
        try {
            boolean count = redisTemplate.opsForSet().move(srcKey, value, destKey);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //===============================list=================================

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束  0 到 -1代表所有值
     * @return
     */
    public List<String> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return
     */
    public long lGetListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean lSet(String key, String value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public boolean lSet(String key, String value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0) expire(key, time);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean lSet(String key, List<String> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public boolean lSet(String key, List<String> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0) expire(key, time);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return
     */
    public boolean lUpdateIndex(String key, long index, String value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public long lRemove(String key, long count, Object value) {
        try {
            Long remove = redisTemplate.opsForList().remove(key, count, value);
            return remove;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 移除并获取列表的第一个元素
     * @return
     */
    public String lLeftPopGet(String key){
        try {
            String ret = redisTemplate.opsForList().leftPop(key);
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 移除并获取列表的最后一个元素
     * @param key
     * @return
     */
    public String lRightPopGet(String key){
        try {
            String ret = redisTemplate.opsForList().rightPop(key);
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @description 插入list头部
     * @author xlwang
     * @param key 队列名
     * @param value 插入值
     * @return
     * @throws IllegalArgumentException
     */
    public Long lLeftPush(String key, String value)  throws IllegalArgumentException{
        long result = 0;
        try {
            if(StringUtils.isNoneBlank(key)){
                result = redisTemplate.opsForList().leftPush(key, value);
            }else{
                throw new IllegalArgumentException("key is null");
            }
        } catch (Exception e) {
            logger.error(key,e);
        }
        return result;
    }

    /**
     * @description 插入list尾部
     * @author xlwang
     * @param key 队列名
     * @param value 插入值
     * @return
     * @throws IllegalArgumentException
     */
    public Long lRightPush(String key, String value)  throws IllegalArgumentException{
        long result = 0;
        try {
            if(StringUtils.isNoneBlank(key)){
                result = redisTemplate.opsForList().rightPush(key, value);
            }else{
                throw new IllegalArgumentException("key is null");
            }
        } catch (Exception e) {
            logger.error(key,e);
        }
        return result;
    }

    /**
     * @author xlwang
     * @description list 中取数据
     * @param key list 名称
     * @param start 开始位置,0是开始位置
     * @param end 结束为止，-1是结束为止
     * @return
     * @throws IllegalArgumentException
     */
    public List<String> lRange(String key, long start, long end) throws IllegalArgumentException{
        List<String> result = Lists.newArrayList();
        try {
            if(StringUtils.isNoneBlank(key)){
                result =  redisTemplate.opsForList().range(key, start, end);
            }else{
                throw new IllegalArgumentException("key is null");
            }
        } catch (Exception e) {
            logger.error(key,e);
        }

        return result;
    }

    /**
     *  @description
     * @param key list名称
     * @return
     */
    public String lLeftPop(String key) {
        String result = "";
        try {
            result = redisTemplate.opsForList().leftPop(key);
        } catch (Exception e) {
            logger.error(key,e);
        }
        return result;
    }

    /**
     * 发布消息,失败重试，第一次设为1，最多尝试3次
     * @param channel
     * @param data
     * @return
     */
    public boolean publish(String channel, String data,int retry){
        boolean result = false;
        try {
            redisTemplate.convertAndSend(channel, data);
            result = true;
        } catch (Exception e) {
            logger.error(channel,e);
        }finally{
            if(!result&&retry<=3){
                result = publish(channel,data, ++retry);
            }
        }
        return result;
    }

    /**
     * 删除key
     * @param key
     * @return
     */
    public boolean del(String key){
        boolean result = false;
        try {
            result = redisTemplate.delete(key);
        } catch (Exception e) {
            logger.error(key,e);
        }
        return result;
    }

    /**
     * 删除key，失败重试，第一次设为1，最多尝试3次
     * @param key
     * @param retry 尝试重试次数，最大值3
     * @return
     */
    public boolean del(String key, int retry){
        boolean result = false;
        try {
            result = redisTemplate.delete(key);
            if(!result&&retry<=3){
                result = del(key, ++retry);
            }
        } catch (Exception e) {
            logger.error(key,e);
        }
        return result;
    }

    /**
     * 存储HashMap 数据，失败重试，第一次设为1，最多尝试3次
     * @param redisKey hashmap key
     * @param dataKey data key
     * @param dataValue data value
     * @param retry hmSet number
     * @return
     */
    public boolean hmPut(String redisKey, String dataKey, String dataValue, int retry){
        boolean result = false;
        try {
            redisTemplate.opsForHash().put(redisKey,dataKey,dataValue);
            result = true;
        } catch (Exception e) {
            logger.error(redisKey,e);
        }
        if(!result && retry<=3){
            result = hmPut(redisKey, dataKey, dataValue, ++retry);
        }
        return result;
    }







}