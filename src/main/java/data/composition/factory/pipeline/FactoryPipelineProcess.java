package data.composition.factory.pipeline;

import data.composition.factory.process.SourceProcessor;

import java.util.Collection;
import java.util.List;

/**
 * @author zhangjinyu
 * @since 2023-10-20
 */
public interface FactoryPipelineProcess<T> extends FactoryPipeline<T> {

    Collection<T> getData();

    List<SourceProcessor<T, ?>> getSourceProcessors();
}
