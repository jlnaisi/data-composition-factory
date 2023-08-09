package data.composition.factory.keymap;

import data.composition.factory.function.FieldFunction;
import data.composition.factory.source.Source;

import java.util.function.Function;

/**
 * @author zhangjinyu
 * @since 2023-07-25
 */
public interface SourceKeyMap<D, S, M> {
    /**
     * 数据赋值
     *
     * @param dataValueField       数据值字段,被数据源{@code sourceValueDataField}字段值赋值
     * @param sourceValueDataField 数据源值字段,赋值给{@code dataValueField}字段
     * @return 数据源对象 {@link Source}
     */
    Source<D, S, M> value(FieldFunction<D, ?> dataValueField, FieldFunction<S, ?> sourceValueDataField);

    Function<D, ?> getDataKeyField();

    Function<S, ?> getSourceDataKeyField();

    Function<D, ?> getDataValueField();

    Function<S, ?> getSourceDataValueField();

}
