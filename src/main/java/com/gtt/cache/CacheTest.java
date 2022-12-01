package com.gtt.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.junit.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class CacheTest {

    private LoadingCache<String, BlockingQueue> graphs = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build(
                    new CacheLoader<String, BlockingQueue>() {
                        public BlockingQueue load(String key)  {
                            return new ArrayBlockingQueue(5);
                        }
                    });

    @Test
    public void testCache() {
        BlockingQueue ss = graphs.getUnchecked("kk");
        System.out.println(ss);
        graphs.invalidate("kk");

        BlockingQueue allocateAgain = graphs.getUnchecked("kk");
        System.out.println(allocateAgain);
    }
}
