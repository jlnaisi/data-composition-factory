package data.composition.factory.dto;

import lombok.Data;

import java.util.List;

/**
 * @author zhangjinyu
 * @since 2023-10-19
 */
@Data
public class StudentDto {
    private Long id;
    private Long schoolId;
    private String name;
    private String schoolName;
    private String no;
    private List<SubjectScoreDto> subjectScores;
}
