package data.composition.factory.key;

import data.composition.factory.function.FieldFunction;
import data.composition.factory.mapper.GroupMapperProcess;
import data.composition.factory.mapper.SingleMapperProcess;

/**
 * @author zhangjinyu
 * @since 2023-10-20
 */
public interface MapKeyUnit<T, R> {

    /**
     * 一对一
     *
     * @param dataField   数据字段
     * @param sourceField 数据源字段
     * @return 单映射器进程
     */
    SingleMapperProcess<T, R> key(FieldFunction<T, ?> dataField, FieldFunction<R, ?> sourceField);

    /**
     * 一对多
     *
     * @param dataField   数据字段
     * @param sourceField 数据源字段
     * @return 组映射器进程
     */
    GroupMapperProcess<T, R> groupKey(FieldFunction<T, ?> dataField, FieldFunction<R, ?> sourceField);

    /**
     * 多个字段 一对一
     *
     * @return 多个字段映射单元流程
     */
    KeyMapUnitProcess<T, R> multipleKey();

    /**
     * 多个字段 一对多
     *
     * @return 多个字段映射单元流程
     */
    GroupKeyMapUnitProcess<T, R> multipleGroupKey();

}
