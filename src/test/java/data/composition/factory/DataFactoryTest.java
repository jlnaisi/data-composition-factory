package data.composition.factory;

import cn.hutool.core.util.RandomUtil;
import data.composition.factory.source.CollectionSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author zhangjinyu
 * @since 2023-07-25
 */
public class DataFactoryTest {
    public static void main(String[] args) {
        allCollectTest();
    }

    private static void allCollectTest() {
        List<Student> students = new ArrayList<>();
        List<StudentScore> studentScores = new ArrayList<>();
        for (int i = 1; i <= 10000; i++) {
            Student student = new Student();
            student.setId(i);
            students.add(student);
            StudentScore studentScore = new StudentScore();
            studentScore.setStudentId(i);
            studentScore.setName(new String(new char[]{RandomUtil.randomChinese(), RandomUtil.randomChinese(), RandomUtil.randomChinese()}));
            studentScore.setIndex(i == 2 ? null : Arrays.asList(i, i + 1));
            studentScore.setScore(RandomUtil.randomNumbers(3));
            studentScores.add(studentScore);
        }
        allCollectTest(students, studentScores);
        allCollectTest(students, studentScores);
        allCollectTest(students, studentScores);
        allCollectTest(students, studentScores);
        allCollectTest(students, studentScores);
        allCollectTest(students, studentScores);
        allCollectTest(students, studentScores);
        allCollectTest(students, studentScores);
        allCollectTest(students, studentScores);
        allCollectTest(students, studentScores);
    }

    private static void allCollectTest(List<Student> students, List<StudentScore> studentScores) {
        long l = System.currentTimeMillis();
        DataCompositionFactory.data(students).from(CollectionSource.create(studentScores, Student.class).key(Student::getId, StudentScore::getStudentId).value(Student::getName, StudentScore::getName).key(Student::getId, StudentScore::getStudentId).value(Student::getScore, StudentScore::getScore).key(Student::getId, StudentScore::getStudentId).value(Student::getIndex, StudentScore::getIndex)).composition();
        System.out.println("总体耗时:" + (System.currentTimeMillis() - l));
    }
}
