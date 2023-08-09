package data.composition.factory;

import lombok.Data;

import java.util.List;

/**
 * @author zhangjinyu
 * @since 2023-07-28
 */
@Data
public class StudentScore {
    private int studentId;
    private String score;
    private String name;
    private List<Integer> index;
}
