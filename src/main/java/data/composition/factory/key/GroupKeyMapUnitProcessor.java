package data.composition.factory.key;

import data.composition.factory.bean.MappingKey;

import java.util.function.Consumer;

/**
 * @author zhangjinyu
 * @since 2023-10-20
 */
public interface GroupKeyMapUnitProcessor<T,R> {
    void mappingKeyCreate(Consumer<MappingKey<T, R>> consumer);
}
