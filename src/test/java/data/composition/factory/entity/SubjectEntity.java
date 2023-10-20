package data.composition.factory.entity;

import lombok.Builder;
import lombok.Data;

/**
 * 学科
 *
 * @author zhangjinyu
 * @since 2023-10-19
 */
@Data
@Builder
public class SubjectEntity {
    private Long id;
    private String name;
}
