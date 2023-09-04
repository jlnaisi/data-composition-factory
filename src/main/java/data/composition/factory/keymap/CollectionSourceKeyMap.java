package data.composition.factory.keymap;


import data.composition.factory.bean.ValueFieldMap;
import data.composition.factory.function.FieldFunction;
import data.composition.factory.source.CollectionSource;
import data.composition.factory.source.Source;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author zhangjinyu
 * @since 2023-07-28
 */
public class CollectionSourceKeyMap<D, S> implements SourceKeyMap<D, S, Collection<S>, FieldFunction<D, ?>, FieldFunction<S, ?>> {
    private final FieldFunction<D, ?> keyDataField;
    private final FieldFunction<S, ?> keySourceDataField;
    private final CollectionSource<D, S> source;
    private final List<ValueFieldMap<D, S, FieldFunction<D, ?>, FieldFunction<S, ?>>> valueFieldMaps;

    public CollectionSourceKeyMap(CollectionSource<D, S> source, FieldFunction<D, ?> keyDataField, FieldFunction<S, ?> keySourceDataField) {
        this.source = source;
        this.keyDataField = keyDataField;
        this.keySourceDataField = keySourceDataField;
        this.valueFieldMaps = new ArrayList<>();
        source.getSourceKeyMapList().add(this);
    }

    @Override
    public SourceKeyMap<D, S, Collection<S>, FieldFunction<D, ?>, FieldFunction<S, ?>> value(FieldFunction<D, ?> dataValueField, FieldFunction<S, ?> sourceValueDataField) {
        this.valueFieldMaps.add(new ValueFieldMap<>(dataValueField, sourceValueDataField));
        return this;
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
    public List<ValueFieldMap<D, S, FieldFunction<D, ?>, FieldFunction<S, ?>>> getValueFieldMaps() {
        return valueFieldMaps;
    }

    @Override
    public Source<D, S, Collection<S>, FieldFunction<D, ?>, FieldFunction<S, ?>> build() {
        return source;
    }
}
