package data.composition.factory;

import lombok.Data;

import java.util.List;

/**
 * @author zhangjinyu
 * @since 2023-07-28
 */
@Data
public class Student {
    private int id;
    private List<Integer> index;
    private String name;
    private String score;
}
