package data.composition.factory;


import cn.hutool.core.bean.BeanUtil;
import data.composition.factory.pipeline.FactoryPipelineImpl;
import data.composition.factory.pipeline.FactoryPipelineProcess;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author zhangjinyu
 * @since 2023-10-19
 */
public interface DataCompositionFactory {

    static <T> FactoryPipelineProcess<T> of(Collection<T> data) {
        return new FactoryPipelineImpl<>(Objects.isNull(data) ? Collections.emptyList() : data);
    }

    static <T, R> FactoryPipelineProcess<R> of(Collection<T> data, Class<R> clazz) {
        return new FactoryPipelineImpl<>(Objects.isNull(data) ? null : data.stream().map(t -> BeanUtil.toBean(t, clazz)).collect(Collectors.toList()));
    }

    static <T, R> FactoryPipelineProcess<R> of(Collection<T> data, Class<R> clazz, Function<T, R> converter) {
        return new FactoryPipelineImpl<>(Objects.isNull(data) ? null : data.stream().map(converter).collect(Collectors.toList()));
    }
}
