package data.composition.factory.util;

import java.lang.reflect.Array;
import java.util.Objects;

/**
 * @author zhangjinyu
 * @since 2023-09-07
 */
interface ArrayUtil {

    @SuppressWarnings("unchecked")
    static <T> T[] append(T[] array, T[] newElements) {
        final Class<?> originComponentType = array.getClass().getComponentType();
        int len = length(array);
        final Object result = Array.newInstance(originComponentType, len + newElements.length);
        System.arraycopy(array, 0, result, 0, 0);
        System.arraycopy(newElements, 0, result, 0, newElements.length);
        System.arraycopy(array, 0, result, newElements.length, len);
        return (T[]) result;
    }


    static <T> int length(T[] array) {
        return Objects.isNull(array) ? 0 : array.length;
    }
}
