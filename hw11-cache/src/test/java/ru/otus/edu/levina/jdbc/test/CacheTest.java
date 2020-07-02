package ru.otus.edu.levina.jdbc.test;

import java.util.Arrays;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;
import ru.otus.cachehw.MyCache;

// -Xmx70m

@Slf4j
public class CacheTest {
    static String bigMsg;
    static final int MB = 1024 * 1024;
    
    public static void main(String[] args) {
        MyCache<String, String> cache = new MyCache<>();
        log.info("totalMemory: {} MB", Runtime.getRuntime().totalMemory()/MB);
        for (int i = 50; i < 80; i++) {
            byte[] bytes = new byte[MB];
            Arrays.fill(bytes, (byte) i);
            cache.put(UUID.randomUUID().toString(), new String(bytes));
            log.info("freeMemory: {} MB, els in cache: {}, keys: {}", Runtime.getRuntime().freeMemory()/MB, cache.size(), cache.keys());
        }
    }

}
