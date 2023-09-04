package data.composition.factory.data;

import data.composition.factory.source.Source;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author zhangjinyu
 * @since 2023-08-09
 */
public class SingleData<D> implements Data<D, D> {
    private final List<Source<D, ?, ?, ?, ?>> sourceList;
    private final D data;

    public SingleData(D data) {
        this.data = data;
        this.sourceList = new ArrayList<>();
    }

    public SingleData(List<Source<D, ?, ?, ?, ?>> sourceList, D data) {
        this.sourceList = sourceList;
        this.data = data;
    }

    @Override
    public Data<D, D> filter(Predicate<? super D> predicate) {
        return null;
    }

    @Override
    public <S, M, DF extends Function<D, ?>, VF extends Function<S, ?>> Data<D, D> source(Source<D, S, M, DF, VF> source) {
        return null;
    }

    @Override
    public void composition() {

    }
}
