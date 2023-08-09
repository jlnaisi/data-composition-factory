package data.composition.factory.keymap;


import data.composition.factory.function.FieldFunction;
import data.composition.factory.source.CollectionSource;
import data.composition.factory.source.Source;

import java.util.Collection;

/**
 * @author zhangjinyu
 * @since 2023-07-28
 */
public class CollectionSourceKeyMap<D, S> implements SourceKeyMap<D, S, Collection<S>> {
    private final FieldFunction<D, ?> keyDataField;
    private final FieldFunction<S, ?> keySourceDataField;
    private final CollectionSource<D, S> source;
    private FieldFunction<D, ?> dataValueField;
    private FieldFunction<S, ?> sourceValueDataField;

    public CollectionSourceKeyMap(CollectionSource<D, S> source, FieldFunction<D, ?> keyDataField, FieldFunction<S, ?> keySourceDataField) {
        this.source = source;
        this.keyDataField = keyDataField;
        this.keySourceDataField = keySourceDataField;
        source.addSourceKeyMap(this);
    }

    @Override
    public Source<D, S, Collection<S>> value(FieldFunction<D, ?> dataValueField, FieldFunction<S, ?> sourceValueDataField) {
        this.dataValueField = dataValueField;
        this.sourceValueDataField = sourceValueDataField;
        return source;
    }

    @Override
    public FieldFunction<D, ?> getDataKeyField() {
        return keyDataField;
    }

    @Override
    public FieldFunction<S, ?> getSourceDataKeyField() {
        return keySourceDataField;
    }

    @Override
    public FieldFunction<D, ?> getDataValueField() {
        return dataValueField;
    }

    @Override
    public FieldFunction<S, ?> getSourceDataValueField() {
        return sourceValueDataField;
    }

}
