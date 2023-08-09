package data.composition.factory.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author zhangjinyu
 * @since 2023-07-31
 */
@Data
@AllArgsConstructor
public class CompositionKey {
    private String dataKeyFieldName;
    private String sourceKeyFieldName;
}
