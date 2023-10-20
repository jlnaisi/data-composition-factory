package data.composition.factory.pipeline;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import data.composition.factory.mapper.MapperSupport;
import data.composition.factory.process.SourceProcessor;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author zhangjinyu
 * @since 2023-10-20
 */
public class FactoryPipelineProcessor<T> {
    private final FactoryPipelineProcess<T> factoryPipelineProcess;

    public FactoryPipelineProcessor(FactoryPipelineProcess<T> factoryPipelineProcess) {
        this.factoryPipelineProcess = factoryPipelineProcess;
    }

    public Collection<T> composition() {
        Collection<T> data = factoryPipelineProcess.getData();
        List<SourceProcessor<T, ?>> sourceProcessors = factoryPipelineProcess.getSourceProcessors();
        if (!data.isEmpty() && !sourceProcessors.isEmpty()) {
            List<MapperSupport<T>> mapperSupports = sourceProcessors.stream().map(MapperSupport::create).collect(Collectors.toList());
            List<String> dataFieldNames = Arrays.stream(ReflectUtil.getFields(CollUtil.getFirst(data).getClass())).map(Field::getName).collect(Collectors.toList());
            for (T datum : data) {
                if (Objects.isNull(datum)) {
                    break;
                }
                for (MapperSupport<T> mapperSupport : mapperSupports) {
                    mapperSupport.composition(datum, dataFieldNames);
                }
            }
        }
        return data;
    }

    public <R> List<R> compositionAs(Class<R> clazz) {
        Collection<T> composition = composition();
        return composition.stream().map(t -> BeanUtil.toBean(t, clazz)).collect(Collectors.toList());
    }

    public <R> List<R> compositionAs(Class<R> clazz, Function<T, R> converter) {
        Collection<T> composition = composition();
        return composition.stream().map(converter).collect(Collectors.toList());
    }


}
