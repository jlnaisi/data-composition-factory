package data.composition.factory.keymap;

import data.composition.factory.function.FieldFunction;
import data.composition.factory.source.SingleSource;
import data.composition.factory.source.Source;

/**
 * @author zhangjinyu
 * @since 2023-08-09
 */
public class SingleSourceKeyMap<D, S> implements SourceKeyMap<D, S, S> {
    private final FieldFunction<D, ?> keyDataField;
    private final FieldFunction<S, ?> keySourceDataField;
    private final SingleSource<D, S> source;
    private FieldFunction<D, ?> dataValueField;
    private FieldFunction<S, ?> sourceValueDataField;

    public SingleSourceKeyMap(SingleSource<D, S> source, FieldFunction<D, ?> keyDataField, FieldFunction<S, ?> keySourceDataField) {
        this.source = source;
        this.keyDataField = keyDataField;
        this.keySourceDataField = keySourceDataField;
        source.addSourceKeyMap(this);
    }

    @Override
    public Source<D, S, S> value(FieldFunction<D, ?> dataValueField, FieldFunction<S, ?> sourceValueDataField) {
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
