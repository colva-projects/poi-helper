package org.colva.common.helper.poi.converter;

import java.util.HashMap;
import java.util.Map;

/**
 * 转换器池
 * 
 * @description
 * @author piaoruiqing
 * @date: 2019/02/07 12:28
 *
 * @since JDK 1.8
 */
public abstract class ConverterPool {

    private static final Map<String, Converter<?>> POOL = new HashMap<>(16);
    static {
        POOL.put(GenericConverter.class.getName(), GenericConverter.INSTANCE);
    }

    /**
     * 获取转换器 <br/>
     * 若没有则初始化
     * 
     * @author piaoruiqing
     * @date: 2019/02/07 15:47
     * 
     * @param clazz
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static Converter<?> getOrInit(Class<? extends Converter<?>> clazz) throws InstantiationException, IllegalAccessException {

        String key = clazz.getName();
        Converter<?> converter = POOL.get(clazz.getName());
        if (null != converter) {
            return converter;
        }
        synchronized (clazz) {
            if (POOL.containsKey(key)) {
                return POOL.get(key);
            }
            converter = clazz.newInstance();
            POOL.put(key, converter);
        }
        return converter;
    }

    /**
     * 获取转换器
     * 
     * @author piaoruiqing
     * @date: 2019/02/07 12:31
     * 
     * @param key
     * @return
     */
    public static Converter<?> get(String key) {
        return POOL.getOrDefault(key, GenericConverter.INSTANCE);
    }
}
