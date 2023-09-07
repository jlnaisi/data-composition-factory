package data.composition.factory.bean;

import java.util.Objects;

/**
 * @author zhangjinyu
 * @since 2023-07-31
 */
public class CompositionKey {
    private final String dataKeyFieldName;
    private final String sourceKeyFieldName;

    public CompositionKey(String dataKeyFieldName, String sourceKeyFieldName) {
        this.dataKeyFieldName = dataKeyFieldName;
        this.sourceKeyFieldName = sourceKeyFieldName;
    }

    public String getDataKeyFieldName() {
        return dataKeyFieldName;
    }

    public String getSourceKeyFieldName() {
        return sourceKeyFieldName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompositionKey)) {
            return false;
        }

        CompositionKey that = (CompositionKey) o;

        if (!Objects.equals(dataKeyFieldName, that.dataKeyFieldName)) {
            return false;
        }
        return Objects.equals(sourceKeyFieldName, that.sourceKeyFieldName);
    }

    @Override
    public int hashCode() {
        int result = dataKeyFieldName != null ? dataKeyFieldName.hashCode() : 0;
        result = 31 * result + (sourceKeyFieldName != null ? sourceKeyFieldName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CompositionKey{" + "dataKeyFieldName='" + dataKeyFieldName + '\'' + ", sourceKeyFieldName='" + sourceKeyFieldName + '\'' + '}';
    }
}
