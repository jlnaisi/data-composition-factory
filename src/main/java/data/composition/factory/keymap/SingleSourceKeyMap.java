package data.composition.factory.keymap;

import data.composition.factory.bean.ValueFieldMap;
import data.composition.factory.function.FieldFunction;
import data.composition.factory.source.SingleSource;
import data.composition.factory.source.Source;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangjinyu
 * @since 2023-08-09
 */
public class SingleSourceKeyMap<D, S> implements SourceKeyMap<D, S, S, FieldFunction<D, ?>, FieldFunction<S, ?>> {
    private final FieldFunction<D, ?> keyDataField;
    private final FieldFunction<S, ?> keySourceDataField;
    private final SingleSource<D, S> source;
    private final List<ValueFieldMap<D, S, FieldFunction<D, ?>, FieldFunction<S, ?>>> valueFieldMaps;

    public SingleSourceKeyMap(SingleSource<D, S> source, FieldFunction<D, ?> keyDataField, FieldFunction<S, ?> keySourceDataField) {
        this.source = source;
        this.keyDataField = keyDataField;
        this.keySourceDataField = keySourceDataField;
        this.valueFieldMaps = new ArrayList<>();
        source.getSourceKeyMapList().add(this);
    }

    @Override
    public SourceKeyMap<D, S, S, FieldFunction<D, ?>, FieldFunction<S, ?>> value(FieldFunction<D, ?> dataValueField, FieldFunction<S, ?> sourceValueDataField) {
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
    public Source<D, S, S, FieldFunction<D, ?>, FieldFunction<S, ?>> build() {
        return source;
    }


}
