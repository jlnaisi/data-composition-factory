package data.composition.factory.util;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author ZhangJinyu
 * @since 2020-10-06
 */
public class ReflectUtil {
    /**
     * 字段缓存
     */
    private static final SimpleCache<Class<?>, Field[]> FIELDS_CACHE = new SimpleCache<>();
    private static final SimpleCache<Class<?>, Boolean> FIELDS_PEEK_CACHE = new SimpleCache<>();
    private static final String GET = "get";
    private static final String IS = "is";


    public static Stream<Field> getStreamCacheFields(Class<?> beanClass, boolean oncePeek, Consumer<Field> action) {
        Field[] fields = FIELDS_CACHE.get(beanClass);
        if (!Objects.isNull(fields)) {
            return Arrays.stream(fields);
        }
        Field[] fieldsDirectly = getFieldsDirectly(beanClass, true);
        FIELDS_CACHE.put(beanClass, fieldsDirectly);
        Stream<Field> stream = Arrays.stream(fieldsDirectly);
        if (oncePeek && Objects.isNull(FIELDS_PEEK_CACHE.get(beanClass))) {
            if (!Objects.isNull(action)) {
                stream = stream.peek(action);
            }
            FIELDS_PEEK_CACHE.put(beanClass, Boolean.TRUE);
        }
        return stream;
    }

    public static Field[] getCacheFields(Class<?> beanClass) throws SecurityException {
        Field[] fields = FIELDS_CACHE.get(beanClass);
        if (!Objects.isNull(fields)) {
            return fields;
        }
        Field[] fieldsDirectly = getFieldsDirectly(beanClass, true);
        FIELDS_CACHE.put(beanClass, fieldsDirectly);
        return fieldsDirectly;
    }

    /**
     * 直接获取字段
     *
     * @param beanClass            类
     * @param withSuperClassFields 是否包含父类
     * @return 字段数据
     * @throws SecurityException 异常
     */
    public static Field[] getFieldsDirectly(Class<?> beanClass, boolean withSuperClassFields) throws SecurityException {
        if (Objects.isNull(beanClass)) {
            throw new IllegalArgumentException("this argument is required; it must not be null");
        }
        Field[] allFields = null;
        for (Class<?> searchType = beanClass; searchType != null; searchType = withSuperClassFields ? searchType.getSuperclass() : null) {
            Field[] declaredFields = searchType.getDeclaredFields();
            if (null == allFields) {
                allFields = declaredFields;
            } else {
                allFields = ArrayUtil.append(allFields, declaredFields);
            }
        }
        return allFields;
    }

    public static <T> String getFieldName(Function<T, ?> beanFunction) {
        Method writeReplaceMethod;
        try {
            writeReplaceMethod = beanFunction.getClass().getDeclaredMethod("writeReplace");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        writeReplaceMethod.setAccessible(true);
        SerializedLambda serializedLambda;
        try {
            serializedLambda = (SerializedLambda) writeReplaceMethod.invoke(beanFunction);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return resolveFieldName(serializedLambda.getImplMethodName());
    }

    private static String resolveFieldName(String getMethodName) {
        if (getMethodName.startsWith(GET)) {
            getMethodName = getMethodName.substring(GET.length());
        } else if (getMethodName.startsWith(IS)) {
            getMethodName = getMethodName.substring(IS.length());
        }
        return firstToLowerCase(getMethodName);
    }

    private static String firstToLowerCase(String param) {
        if (StrUtil.isBlank(param)) {
            return "";
        }
        return param.substring(0, 1).toLowerCase() + param.substring(1);
    }

    public static Object getFieldValue(Field field, Object data) {
        try {
            return field.get(data);
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    public static Object getFieldValueSafe(Field field, Object data, Object nullDefaultValue) {
        try {
            return field.get(data);
        } catch (IllegalAccessException e) {
            return nullDefaultValue;
        }
    }

    public static void setFieldValue(Field field, Object data, Object value) {
        if (Objects.isNull(data)) {
            return;
        }
        try {
            if (field.getType().isAssignableFrom(value.getClass())) {
                field.set(data, value);
            } else {
                Class<?> type = field.getType();
                if (Collection.class.isAssignableFrom(type)) {
                    if (value instanceof Collection<?>) {
                        field.set(data, value);
                    } else {
                        field.set(data, Collections.singletonList(value));
                    }
                } else {
                    if (value instanceof Collection<?>) {
                        Collection<?> values = (Collection<?>) value;
                        field.set(data, CollectUtil.getLast(values));
                    } else {
                        field.set(data, value);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Object unfold(Field sourceValuefield, Object obj, boolean merge, boolean distinct) {
        if (Objects.isNull(obj)) {
            return null;
        }
        if (Collection.class.isAssignableFrom(sourceValuefield.getType())) {
            if (Collection.class.isAssignableFrom(obj.getClass())) {
                List<?> list = (List<?>) obj;
                if (merge || distinct) {
                    Stream<Object> objectStream = list.stream().flatMap(o -> {
                    Object fieldValue = ReflectUtil.getFieldValue(sourceValuefield, o);
                        return fieldValue == null ? Stream.empty() : ((List<?>) fieldValue).stream();
                    });
                    if (distinct) {
                        objectStream = objectStream.distinct();
                    }
                    return objectStream.collect(Collectors.toList());
                }
                return getFieldValue(sourceValuefield, CollectUtil.getLast(list));
            } else {
                return getFieldValue(sourceValuefield, obj);
            }
        } else {
            if (obj instanceof Collection<?>) {
                Collection<?> objList = (Collection<?>) obj;
                if (merge) {
                    Stream<?> stream = objList.stream();
                    return stream.map((Function<Object, Object>) o -> getFieldValue(sourceValuefield, o)).collect(Collectors.toList());
                } else {
                    return getFieldValue(sourceValuefield, CollectUtil.getLast(objList));
                }
            }
            return getFieldValue(sourceValuefield, obj);
        }
    }
}