package data.composition.factory.bean;

import java.util.function.Function;

/**
 * @author zhangjinyu
 * @since 2023-08-11
 */
public class ValueFieldMap<D, S, DF extends Function<D, ?>, VF extends Function<S, ?>> {
    private final DF dataValueField;
    private final VF sourceDataValueField;

    public ValueFieldMap(DF dataValueField, VF sourceDataValueField) {
        this.dataValueField = dataValueField;
        this.sourceDataValueField = sourceDataValueField;
    }

    public DF getDataValueField() {
        return dataValueField;
    }

    public VF getSourceDataValueField() {
        return sourceDataValueField;
    }
}
