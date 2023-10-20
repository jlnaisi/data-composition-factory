package data.composition.factory.entity;

import lombok.Builder;
import lombok.Data;

/**
 * 学生
 *
 * @author zhangjinyu
 * @since 2023-10-19
 */
@Data
@Builder
public class StudentEntity {
    private Long id;
    private Long schoolId;
    private String name;
    private String schoolName;
}
