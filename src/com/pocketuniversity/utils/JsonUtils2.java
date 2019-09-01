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
 * JSONת�������� 
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
            // û��@Exposeע������Խ����ᱻ���л�  
            gson2 = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create();  
        }  
    }  
  
    private JsonUtils2() {  
    }  
  
    /** 
     * ������ת����json�ַ��� 
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
     * �������б�@Exposeע�������ת����json�ַ��� 
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
     * ��json�ַ����У�����keyֵ�ҵ�value 
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
     * ��json��ʽת����map���� 
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
     * ��jsonת����bean���� 
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
     * ��json��ʽת����List���� 
     *  
     * @param json 
     * @param type 
     *            �磺 Type type = new TypeToken<List<?>>() {}.getType(); 
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
     * ������ת����json��ʽ(���Զ������ڸ�ʽ) 
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
     * ������ת����json��ʽ�����������������»��ߵ����� 
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