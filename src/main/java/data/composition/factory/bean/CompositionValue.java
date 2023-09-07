package data.composition.factory.bean;

import java.util.Map;
import java.util.Objects;

/**
 * @author zhangjinyu
 * @since 2023-07-31
 */
public class CompositionValue<M> {
    private final String dataValueFieldName;
    private final String sourceValueFieldName;
    private final Map<Object, M> valueGroupBy;

    public CompositionValue(String dataValueFieldName, String sourceValueFieldName, Map<Object, M> valueGroupBy) {
        this.dataValueFieldName = dataValueFieldName;
        this.sourceValueFieldName = sourceValueFieldName;
        this.valueGroupBy = valueGroupBy;
    }

    public String getDataValueFieldName() {
        return dataValueFieldName;
    }

    public String getSourceValueFieldName() {
        return sourceValueFieldName;
    }

    public Map<Object, M> getValueGroupBy() {
        return valueGroupBy;
    }


    @Override
    public int hashCode() {
        int result = dataValueFieldName != null ? dataValueFieldName.hashCode() : 0;
        result = 31 * result + (sourceValueFieldName != null ? sourceValueFieldName.hashCode() : 0);
        result = 31 * result + (valueGroupBy != null ? valueGroupBy.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompositionValue)) {
            return false;
        }

        CompositionValue<?> that = (CompositionValue<?>) o;

        if (!Objects.equals(dataValueFieldName, that.dataValueFieldName)) {
            return false;
        }
        if (!Objects.equals(sourceValueFieldName, that.sourceValueFieldName)) {
            return false;
        }
        return Objects.equals(valueGroupBy, that.valueGroupBy);
    }

    @Override
    public String toString() {
        return "CompositionValue{" + "dataValueFieldName='" + dataValueFieldName + '\'' + ", sourceValueFieldName='" + sourceValueFieldName + '\'' + ", valueGroupBy=" + valueGroupBy + '}';
    }
}
