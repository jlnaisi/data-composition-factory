package data.composition.factory.key;

import data.composition.factory.bean.MappingKey;

/**
 * @author zhangjinyu
 * @since 2023-10-20
 */
public interface KeyMapUnitProcessor<T, R> {

    MappingKey<T, R> getMappingKey();
}