package data.composition.factory.mapper;

import data.composition.factory.function.ConvertFunction;
import data.composition.factory.key.MapKeyUnit;
import data.composition.factory.pipeline.FactoryPipeline;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author zhangjinyu
 * @since 2023-10-19
 */
public class GroupSingleMapperProcessorImpl<T, R> implements GroupMapperProcessor<T, R>, GroupMapperProcess<T, R> {
    private final FactoryPipeline<T> factoryPipeline;
    private ConvertFunction<T, Collection<R>> fieldConvertFunction;

    public GroupSingleMapperProcessorImpl(FactoryPipeline<T> factoryPipeline) {
        this.factoryPipeline = factoryPipeline;
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
    public ConvertFunction<T, Collection<R>> getConvertFunction() {
        return fieldConvertFunction;
    }

    @Override
    public FactoryPipeline<T> convert(ConvertFunction<T, Collection<R>> convertFunction) {
        this.fieldConvertFunction = convertFunction;
        return this;
    }

}
