package data.composition.factory;

import data.composition.factory.source.CollectionSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author zhangjinyu
 * @since 2023-07-25
 */
public class CollectionDataTest {
    public static void main(String[] args) {
        collectSourceTest();
//        singleSourceTest();
    }

    private static void singleSourceTest() {
        List<Student> students = new ArrayList<>();
        StudentScore studentScore1 = new StudentScore();
        studentScore1.setStudentId(1);
        studentScore1.setName("张三");
        studentScore1.setScore("100");
        studentScore1.setIndex(Arrays.asList(1, 2, 3));
        for (int i = 1; i <= 10; i++) {
            Student student = new Student();
            student.setId(i);
            students.add(student);
        }
        List<StudentScore> studentScores = new ArrayList<>();
        for (int i = 2; i <= 10; i++) {
            StudentScore studentScore = new StudentScore();
            studentScore.setStudentId(i);
            studentScore.setName(new String("张三" + i));
            studentScore.setIndex(i == 2 ? null : Arrays.asList(i, i + 1));
            studentScore.setScore(String.valueOf(i + 10));
            studentScores.add(studentScore);
        }
        DataCompositionFactory.data(students)
                .source(CollectionSource.data(studentScores, Student.class)
                        .filter(new Predicate<StudentScore>() {
                            @Override
                            public boolean test(StudentScore studentScore) {
                                return studentScore.getStudentId() == 2;
                            }
                        })
                        .key(Student::getId, StudentScore::getStudentId)
                        .value(Student::getName, StudentScore::getName)
                        .value(Student::getScore, StudentScore::getScore)
                        .value(Student::getIndex, StudentScore::getIndex)
                        .build()
                )
                .composition();
        System.out.println(students);
    }

    private static void collectSourceTest() {
        List<Student> students = new ArrayList<>();
        List<StudentScore> studentScores = new ArrayList<>();
        StudentScore studentScore1 = new StudentScore();
        studentScore1.setStudentId(1);
        studentScore1.setName("张三" + 1);
        studentScore1.setIndex(Arrays.asList(3, 4, 5, 1, 2));
        studentScore1.setScore(String.valueOf(11));
        studentScores.add(studentScore1);
        for (int i = 1; i <= 1; i++) {
            Student student = new Student();
            student.setId(i);
            students.add(student);
            StudentScore studentScore = new StudentScore();
            studentScore.setStudentId(i);
            studentScore.setName(new String("张三" + i));
            studentScore.setIndex(Arrays.asList(i, i + 1));
            studentScore.setScore(String.valueOf(i + 10));
            studentScores.add(studentScore);
        }
//        collectSourceTest(students, studentScores);
//        collectSourceTest(students, studentScores);
//        collectSourceTest(students, studentScores);
//        collectSourceTest(students, studentScores);
//        collectSourceTest(students, studentScores);
//        collectSourceTest(students, studentScores);
//        collectSourceTest(students, studentScores);
//        collectSourceTest(students, studentScores);
//        collectSourceTest(students, studentScores);
        collectSourceTest(students, studentScores);
        System.out.println(students);
    }

    private static void collectSourceTest(List<Student> students, List<StudentScore> studentScores) {
        long l = System.currentTimeMillis();
        DataCompositionFactory.data(students)
                .source(CollectionSource.data(studentScores, Student.class)
                        .merge(true)
                .key(Student::getId, StudentScore::getStudentId)
                .value(Student::getName, StudentScore::getName)
                .value(Student::getScore, StudentScore::getScore)
                .value(Student::getIndex, StudentScore::getIndex)
                .build()).composition();
        System.out.println("总体耗时:" + (System.currentTimeMillis() - l));
    }
}
