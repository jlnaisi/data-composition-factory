package data.composition.factory.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * @author zhangjinyu
 * @since 2023-09-07
 */
public interface CollectUtil {
    static Object getLast(Collection<?> objList) {
        Object last = null;
        if (Objects.nonNull(objList)) {
            if (objList instanceof ArrayList) {
                ArrayList<?> arrayList = (ArrayList<?>) objList;
                last = arrayList.get(arrayList.size() - 1);
            }
        }
        return last;
    }
}
