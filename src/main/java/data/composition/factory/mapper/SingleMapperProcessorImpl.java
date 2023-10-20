package data.composition.factory.mapper;

import data.composition.factory.function.ConvertFunction;
import data.composition.factory.function.FieldFunction;
import data.composition.factory.key.MapKeyUnit;
import data.composition.factory.pipeline.FactoryPipeline;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author zhangjinyu
 * @since 2023-10-19
 */
public class SingleMapperProcessorImpl<T, R> implements SingleMapperProcessor<T, R>, SingleMapperProcess<T, R> {
    private final Map<FieldFunction<T, ?>, FieldFunction<R, ?>> fieldMapFunction;
    private final FactoryPipeline<T> factoryPipeline;
    private ConvertFunction<T, R> convertFunction;
    private boolean autoMap;

    public SingleMapperProcessorImpl(FactoryPipeline<T> factoryPipeline) {
        this.fieldMapFunction = new HashMap<>();
        this.factoryPipeline = factoryPipeline;
        this.autoMap = false;
    }

    @Override
    public boolean isAutoMap() {
        return autoMap;
    }

    @Override
    public Map<FieldFunction<T, ?>, FieldFunction<R, ?>> getFieldMapFunction() {
        return fieldMapFunction;
    }

    @Override
    public ConvertFunction<T, R> getConvertFunction() {
        return convertFunction;
    }

    @Override
    public FactoryPipeline<T> convert(ConvertFunction<T, R> convertFunction) {
        this.convertFunction = convertFunction;
        return this;
    }

    @Override
    public SingleMapperProcess<T, R> map(FieldFunction<T, ?> dataField, FieldFunction<R, ?> sourceField) {
        if (Objects.nonNull(dataField) && Objects.nonNull(sourceField)) {
            fieldMapFunction.put(dataField, sourceField);
        }
        return this;
    }

    @Override
    public FactoryPipeline<T> filter(Predicate<T> predicate) {
        return factoryPipeline.filter(predicate);
    }

    @Override
    public <S> MapKeyUnit<T, S> source(Collection<S> source) {
        return factoryPipeline.source(source);
    }

    @Override
    public <S> MapKeyUnit<T, S> source(Collection<S> source, Predicate<S> predicate) {
        return factoryPipeline.source(source, predicate);
    }

    @Override
    public <AR> FactoryPipeline<AR> as(Class<AR> clazz) {
        return factoryPipeline.as(clazz);
    }

    @Override
    public <AR> FactoryPipeline<AR> as(Class<AR> clazz, Function<T, AR> converter) {
        return factoryPipeline.as(clazz, converter);
    }

    @Override
    public Collection<T> composition() {
        return factoryPipeline.composition();
    }

    @Override
    public <CR> List<CR> compositionAs(Class<CR> clazz) {
        return factoryPipeline.compositionAs(clazz);
    }

    @Override
    public <CR> List<CR> compositionAs(Class<CR> clazz, Function<T, CR> converter) {
        return factoryPipeline.compositionAs(clazz, converter);
    }

    @Override
    public FactoryPipeline<T> autoMap() {
        this.autoMap = true;
        return factoryPipeline;
    }
}
