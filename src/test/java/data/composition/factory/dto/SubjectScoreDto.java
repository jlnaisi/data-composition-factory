package data.composition.factory.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zhangjinyu
 * @since 2023-10-20
 */
@Data
@Builder
public class SubjectScoreDto {
    private Long subjectId;
    private String subjectName;
    private BigDecimal score;
}
