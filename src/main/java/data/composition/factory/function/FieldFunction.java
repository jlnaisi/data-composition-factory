package data.composition.factory.function;

import cn.hutool.core.lang.func.Func1;

/**
 * @author zhangjinyu
 * @since 2023-10-19
 */
@FunctionalInterface
public interface FieldFunction<T, R> extends Func1<T, R> {
}
