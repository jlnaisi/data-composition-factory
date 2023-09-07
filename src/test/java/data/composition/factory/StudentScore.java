package data.composition.factory;

import java.util.List;
import java.util.Objects;

/**
 * @author zhangjinyu
 * @since 2023-07-28
 */
public class StudentScore {
    private int studentId;
    private String score;
    private String name;
    private List<Integer> index;

    @Override
    public String toString() {
        return "StudentScore{" + "studentId=" + studentId + ", score='" + score + '\'' + ", name='" + name + '\'' + ", index=" + index + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudentScore)) return false;

        StudentScore that = (StudentScore) o;

        if (studentId != that.studentId) return false;
        if (!Objects.equals(score, that.score)) return false;
        if (!Objects.equals(name, that.name)) return false;
        return Objects.equals(index, that.index);
    }

    @Override
    public int hashCode() {
        int result = studentId;
        result = 31 * result + (score != null ? score.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (index != null ? index.hashCode() : 0);
        return result;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getIndex() {
        return index;
    }

    public void setIndex(List<Integer> index) {
        this.index = index;
    }
}
