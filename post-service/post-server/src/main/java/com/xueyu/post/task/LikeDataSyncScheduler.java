package com.xueyu.post.task;

import com.xueyu.common.core.constant.RedisKeyConstant;
import com.xueyu.post.mapper.PostGeneralMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Component
@Slf4j
public class LikeDataSyncScheduler {

    @Resource
    PostGeneralMapper postGeneralMapper;

    private final HashOperations<String, Integer, Long> hashOps;

    public LikeDataSyncScheduler(HashOperations<String, Integer, Long> hashOps) {
        this.hashOps = hashOps;
    }

    // 每小时执行一次
    @Scheduled(cron = "0 0 * * * ?")
    public void refreshPostLikes() {
        String cacheKey = RedisKeyConstant.CACHE_POST_LIKES_KEY;
        Map<Integer, Long> entries = hashOps.entries(cacheKey);

        for (Map.Entry<Integer, Long> entry : entries.entrySet()) {
            Integer postId = entry.getKey();
            Long likesIncrement = entry.getValue();

            // 更新数据库中的点赞数
            if (likesIncrement > 0) {
                postGeneralMapper.updateLikeNumByPostId(postId, likesIncrement.intValue());
            }

            // 清除已处理过的缓存数据
            hashOps.delete(cacheKey, postId);
        }
    }
}