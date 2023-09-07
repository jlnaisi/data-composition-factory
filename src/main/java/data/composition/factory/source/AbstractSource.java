package data.composition.factory.source;

import java.util.function.Function;

/**
 * @author zhangjinyu
 * @since 2023-09-07
 */
public abstract class AbstractSource<D, S, M, DF extends Function<D, ?>, VF extends Function<S, ?>> implements Source<D, S, M, DF, VF> {
    private boolean merge;
    private boolean distinct;

    @Override
    public Source<D, S, M, DF, VF> merge(boolean distinct) {
        this.merge = true;
        this.distinct = distinct;
        return this;
    }

    @Override
    public boolean isMerge() {
        return merge;
    }

    @Override
    public boolean isDistinct() {
        return distinct;
    }
}
