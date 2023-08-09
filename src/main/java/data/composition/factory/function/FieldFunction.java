package data.composition.factory.function;

import java.io.Serializable;
import java.util.function.Function;

/**
 * @author ZhangJinyu
 * @since 2022-09-21
 */
@FunctionalInterface
public interface FieldFunction<A, R> extends Function<A, R>, Serializable {
}
