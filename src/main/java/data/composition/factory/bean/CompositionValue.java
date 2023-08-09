package data.composition.factory.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author zhangjinyu
 * @since 2023-07-31
 */
@Data
@AllArgsConstructor
public class CompositionValue<M> {
    private String dataValueFieldName;
    private String sourceValueFieldName;
    private Map<Object, M> valueGroupBy;
}
