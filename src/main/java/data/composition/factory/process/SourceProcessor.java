package data.composition.factory.process;

import data.composition.factory.bean.MappingKey;
import data.composition.factory.mapper.GroupMapperProcessor;
import data.composition.factory.mapper.SingleMapperProcessor;

import java.util.Collection;
import java.util.List;

/**
 * 资源加工
 *
 * @author zhangjinyu
 * @since 2023-10-19
 */
public interface SourceProcessor<T, R> extends SourceProcess<T, R> {
    boolean enabled();

    SingleMapperProcessor<T, R> getSingleMapper();

    GroupMapperProcessor<T, R> getGroupMapper();

    Collection<R> getSource();

    MappingKey<T, R> getMappingKey();

    List<String> getSourceFieldNames();
}
