package data.composition.factory;

import cn.hutool.core.util.RandomUtil;
import com.google.gson.Gson;
import data.composition.factory.source.CollectionSource;
import data.composition.factory.source.SingleSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author zhangjinyu
 * @since 2023-07-25
 */
public class SingleDataTest {
    public static void main(String[] args) {
//        collectSourceTest();
        singleSourceTest();
    }

    private static void singleSourceTest() {
        Student student = new Student();
        student.setId(1);
        StudentScore studentScore1 = new StudentScore();
        studentScore1.setStudentId(1);
        studentScore1.setName("张三");
        studentScore1.setScore("100");
        studentScore1.setIndex(Arrays.asList(1, 2, 3));

        DataCompositionFactory.data(student)
                .source(SingleSource.data(studentScore1, Student.class)
                        .filter(new Predicate<StudentScore>() {
                            @Override
                            public boolean test(StudentScore studentScore) {
                                return studentScore.getStudentId() == 1;
                            }
                        })
                        .key(Student::getId, StudentScore::getStudentId)
                        .value(Student::getIndex, StudentScore::getIndex)
                        .value(Student::getName, StudentScore::getName)
                        .value(Student::getScore, StudentScore::getScore)
                        .build())
                .composition();
        System.out.println(new Gson().toJson(student));
    }

    private static void collectSourceTest() {
        Student student = new Student();
        student.setId(100000);
        List<StudentScore> studentScores = new ArrayList<>();
        for (int i = 1; i <= 100000; i++) {
            StudentScore studentScore = new StudentScore();
            studentScore.setStudentId(i);
            studentScore.setName(new String(new char[]{RandomUtil.randomChinese(), RandomUtil.randomChinese(), RandomUtil.randomChinese()}));
            studentScore.setIndex(i == 2 ? null : Arrays.asList(i, i + 1));
            studentScore.setScore(RandomUtil.randomNumbers(3));
            studentScores.add(studentScore);
        }
        collectSourceTest(student, studentScores);
        collectSourceTest(student, studentScores);
        collectSourceTest(student, studentScores);
        collectSourceTest(student, studentScores);
        collectSourceTest(student, studentScores);
        collectSourceTest(student, studentScores);
        collectSourceTest(student, studentScores);
        collectSourceTest(student, studentScores);
        collectSourceTest(student, studentScores);
        collectSourceTest(student, studentScores);
        System.out.println(student);
    }

    private static void collectSourceTest(Student student, List<StudentScore> studentScores) {
        long l = System.currentTimeMillis();
        DataCompositionFactory.data(student)
                .source(CollectionSource.data(studentScores, Student.class)
                        .key(Student::getId, StudentScore::getStudentId)
                        .value(Student::getName, StudentScore::getName)
                        .value(Student::getScore, StudentScore::getScore)
                        .value(Student::getIndex, StudentScore::getIndex)
                        .build()).composition();
        System.out.println("总体耗时:" + (System.currentTimeMillis() - l));
    }
}
