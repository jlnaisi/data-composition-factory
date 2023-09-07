package data.composition.factory;

import java.util.List;

/**
 * @author zhangjinyu
 * @since 2023-07-28
 */
public class Student {
    private int id;
    private List<Integer> index;
    private String name;
    private String score;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Integer> getIndex() {
        return index;
    }

    public void setIndex(List<Integer> index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Student{" + "id=" + id + ", index=" + index + ", name='" + name + '\'' + ", score='" + score + '\'' + '}';
    }
}
