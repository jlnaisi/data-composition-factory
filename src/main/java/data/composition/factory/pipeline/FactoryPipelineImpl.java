package data.composition.factory.pipeline;

import data.composition.factory.key.MapKeyUnit;
import data.composition.factory.process.SourceProcessImpl;
import data.composition.factory.process.SourceProcessor;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author zhangjinyu
 * @since 2023-10-19
 */
public class FactoryPipelineImpl<T> implements FactoryPipelineProcess<T> {
    private final Collection<T> data;
    private final List<SourceProcessor<T, ?>> sourceProcessors;
    private Predicate<T> predicate;

    public FactoryPipelineImpl(Collection<T> data) {
        this.data = data;
        this.sourceProcessors = new ArrayList<>();
    }

    @Override
    public FactoryPipeline<T> filter(Predicate<T> predicate) {
        this.predicate = predicate;
        return this;
    }

    @Override
    public Collection<T> getData() {
        if (Objects.isNull(predicate)) {
            return data;
        }
        return data.stream().filter(predicate).collect(Collectors.toList());
    }

    @Override
    public List<SourceProcessor<T, ?>> getSourceProcessors() {
        return sourceProcessors.stream().filter(SourceProcessor::enabled).collect(Collectors.toList());
    }

    @Override
    public <S> MapKeyUnit<T, S> source(Collection<S> source) {
        SourceProcessImpl<T, S> sourceProcess = new SourceProcessImpl<>(source, this);
        this.sourceProcessors.add(sourceProcess);
        return sourceProcess;
    }

    @Override
    public <S> MapKeyUnit<T, S> source(Collection<S> source, Predicate<S> predicate) {
        SourceProcessImpl<T, S> sourceProcess = new SourceProcessImpl<>(source.stream().filter(predicate).collect(Collectors.toList()), this);
        this.sourceProcessors.add(sourceProcess);
        return sourceProcess;
    }

    @Override
    public <R> FactoryPipeline<R> as(Class<R> clazz) {
        List<R> asResult = compositionAs(clazz);
        if (Objects.isNull(asResult)) {
            return new FactoryPipelineImpl<>(Collections.emptyList());
        }
        return new FactoryPipelineImpl<>(asResult);
    }

    @Override
    public <R> FactoryPipeline<R> as(Class<R> clazz, Function<T, R> converter) {
        List<R> rsResult = compositionAs(clazz, converter);
        if (Objects.isNull(rsResult)) {
            return new FactoryPipelineImpl<>(Collections.emptyList());
        }
        return new FactoryPipelineImpl<>(rsResult);
    }

    @Override
    public Collection<T> composition() {
        return new FactoryPipelineProcessor<>(this).composition();
    }

    @Override
    public <R> List<R> compositionAs(Class<R> clazz) {
        return new FactoryPipelineProcessor<>(this).compositionAs(clazz);
    }

    @Override
    public <R> List<R> compositionAs(Class<R> clazz, Function<T, R> converter) {
        return new FactoryPipelineProcessor<>(this).compositionAs(clazz, converter);
    }
}
