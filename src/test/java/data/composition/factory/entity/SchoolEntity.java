package data.composition.factory.entity;

import lombok.Builder;
import lombok.Data;

/**
 * @author zhangjinyu
 * @since 2023-10-20
 */
@Data
@Builder
public class SchoolEntity {
    private Long id;
    private String name;
}
