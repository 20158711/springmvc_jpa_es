package org.wingstudio.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class TokenCache {

    public static final String TOKEN_PREFIX="token_";

    private static LoadingCache<String,String> loadingCache=CacheBuilder.newBuilder()
            .initialCapacity(1000)
            .maximumSize(10000)
            .expireAfterAccess(12,TimeUnit.HOURS)
            .build(new CacheLoader<String, String>() {
                @Override
                public String load(String s) throws Exception {
                    return "null";
                }
            });

    public static void setKey(String key,String value){
        loadingCache.put(key,value);
    }

    public static String getKey(String key){
        String value=null;
        try{
            value=loadingCache.get(key);
            if ("null".equals(value))
                return null;
            return value;
        }catch (Exception e){
            log.error("loadCache :{}",e.getMessage());
        }
        return null;
    }
}
