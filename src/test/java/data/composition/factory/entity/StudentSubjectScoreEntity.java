package data.composition.factory.entity;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 学生学科分数
 *
 * @author zhangjinyu
 * @since 2023-10-19
 */
@Data
@Builder
public class StudentSubjectScoreEntity {
    private Long id;
    private Long studentId;
    private Long schoolId;
    private Long subjectId;
    private BigDecimal score;
}
