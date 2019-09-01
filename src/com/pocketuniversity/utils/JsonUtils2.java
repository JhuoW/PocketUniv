package com.pocketuniversity.utils;

import java.lang.reflect.Type;  
import java.text.SimpleDateFormat;  
import java.util.Date;  
import java.util.HashMap;  
import java.util.List;  
import java.util.Map;  
  


import com.google.gson.ExclusionStrategy;  
import com.google.gson.FieldAttributes;  
import com.google.gson.Gson;  
import com.google.gson.GsonBuilder;  
import com.google.gson.JsonElement;  
import com.google.gson.JsonPrimitive;  
import com.google.gson.JsonSerializationContext;  
import com.google.gson.JsonSerializer;  
import com.google.gson.reflect.TypeToken;  
  
/** 
 * JSON转换工具类 
 *  
 */  
public final class JsonUtils2 {  
  
    private static Gson gson = null;  
    private static Gson gson2 = null;  
    static {  
        if (gson == null) {  
            // gson = new Gson();  
            gson = new GsonBuilder().serializeNulls().create();  
        }  
        if (gson2 == null) {  
            // 没有@Expose注解的属性将不会被序列化  
            gson2 = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create();  
        }  
    }  
  
    private JsonUtils2() {  
    }  
  
    /** 
     * 将对象转换成json字符串 
     *  
     * @param obj 
     * @return 
     */  
    public static String toJson(Object obj) {  
        if (obj == null) {  
            return "{}";  
        }  
        return gson.toJson(obj);  
    }  
  
    /** 
     * 将对象中被@Expose注解的属性转换成json字符串 
     *  
     * @param obj 
     * @return 
     */  
    public static String toJsonWithExpose(Object obj) {  
        if (obj == null) {  
            return "{}";  
        }  
        return gson2.toJson(obj);  
    }  
  
    /** 
     * 在json字符串中，根据key值找到value 
     *  
     * @param json 
     * @param key 
     * @return 
     */  
    public static Object getValue(String json, String key) {  
        Object rulsObj = null;  
        Map<?, ?> rulsMap = jsonToMap(json);  
        if (rulsMap != null && rulsMap.size() > 0) {  
            rulsObj = rulsMap.get(key);  
        }  
        return rulsObj;  
    }  
  
    /** 
     * 将json格式转换成map对象 
     *  
     * @param json 
     * @return 
     */  
    public static Map<String, Object> jsonToMap(String json) {  
        Map<String, Object> objMap = null;  
        if (gson != null) {  
            Type type = new TypeToken<Map<String, Object>>() {  
            }.getType();  
            objMap = gson.fromJson(json, type);  
        }  
        if (objMap == null) {  
            objMap = new HashMap<String, Object>();  
        }  
        return objMap;  
    }  
  
    /** 
     * 将json转换成bean对象 
     *  
     * @param <T> 
     * @param json 
     * @param clazz 
     * @return 
     */  
    public static <T> T jsonToBean(String json, Class<T> clazz) {  
        T obj = null;  
        if (gson != null) {  
            obj = gson.fromJson(json, clazz);  
        }  
        return obj;  
    }  
  
    /** 
     * 将json格式转换成List对象 
     *  
     * @param json 
     * @param type 
     *            如： Type type = new TypeToken<List<?>>() {}.getType(); 
     * @return 
     */  
    public static <T> List<T> jsonToList(String json, Type type) {  
        List<T> list = null;  
        if (gson != null) {  
            list = gson.fromJson(json, type);  
        }  
        return list;  
    }  
  
    /** 
     * 将对象转换成json格式(并自定义日期格式) 
     *  
     * @param obj 
     * @param dateformat 
     * @return 
     */  
    public static String toJson(Object obj, final String dateformat) {  
        if (obj == null || org.apache.commons.lang3.StringUtils.isBlank(dateformat)) {  
            return toJson(obj);  
        }  
        Gson gson = new GsonBuilder().registerTypeHierarchyAdapter(Date.class, new JsonSerializer<Date>() {  
            @Override  
            public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {  
                SimpleDateFormat format = new SimpleDateFormat(dateformat);  
                return new JsonPrimitive(format.format(src));  
            }  
        }).setDateFormat(dateformat).serializeNulls().create();  
        return gson.toJson(obj);  
    }  
  
    /** 
     * 将对象转换成json格式，忽略属性名含有下划线的属性 
     *  
     * @param obj 
     * @return 
     */  
    public static String toJsonSkipFieldContains_(Object obj) {  
        if (obj == null) {  
            return toJson(obj);  
        }  
        Gson gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {  
            @Override  
            public boolean shouldSkipField(FieldAttributes field) {  
                return field.getName().indexOf('_') > -1;  
            }  
  
            @Override  
            public boolean shouldSkipClass(Class<?> clazz) {  
                return false;  
            }  
        }).serializeNulls().create();  
        return gson.toJson(obj);  
    }  
  
    public static final String DATE = "yyyy-MM-dd";  
    public static final String DATEMIN = "yyyy-MM-dd HH:mm";  
    public static final String DATETIME = "yyyy-MM-dd HH:mm:ss";  
}  