package data.composition.factory.function;


/**
 * @author zhangjinyu
 * @since 2023-10-20
 */
@FunctionalInterface
public interface ConvertFunction<T, R> {
    void apply(T result, R source);
}
