package data.composition.factory.bean;

import cn.hutool.core.lang.func.LambdaUtil;
import cn.hutool.core.util.ReflectUtil;
import data.composition.factory.function.FieldFunction;

import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * @author zhangjinyu
 * @since 2023-10-20
 */
public class MappingKey<T, R> {
    private final Set<String> dataFieldNames;
    private final Set<String> sourceFieldNames;

    private MappingKey(List<FieldFunction<T, ?>> dataFieldFunction, List<FieldFunction<R, ?>> sourceFieldFunction) {
        this.dataFieldNames = dataFieldFunction.stream().map(LambdaUtil::getFieldName).collect(Collectors.toSet());
        this.sourceFieldNames = sourceFieldFunction.stream().map(LambdaUtil::getFieldName).collect(Collectors.toSet());
    }

    public static <T, R> MappingKey<T, R> create(List<FieldFunction<T, ?>> dataFieldFunction, List<FieldFunction<R, ?>> sourceFieldFunction) {
        return new MappingKey<>(dataFieldFunction, sourceFieldFunction);
    }

    public String getDataFieldValue(Object data) {
        StringJoiner stringJoiner = new StringJoiner("-");
        dataFieldNames.forEach(fieldName -> stringJoiner.add(String.valueOf(ReflectUtil.getFieldValue(data, fieldName))));
        return stringJoiner.toString();
    }

    public String getSourceFieldValue(Object source) {
        StringJoiner stringJoiner = new StringJoiner("-");
        sourceFieldNames.forEach(fieldName -> stringJoiner.add(String.valueOf(ReflectUtil.getFieldValue(source, fieldName))));
        return stringJoiner.toString();
    }
}
