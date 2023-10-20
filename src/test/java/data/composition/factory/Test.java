package data.composition.factory;

import data.composition.factory.dto.StudentDto;
import data.composition.factory.dto.SubjectScoreDto;
import data.composition.factory.entity.SchoolEntity;
import data.composition.factory.entity.StudentEntity;
import data.composition.factory.entity.StudentSubjectScoreEntity;
import data.composition.factory.entity.SubjectEntity;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhangjinyu
 * @since 2023-10-19
 */
public class Test {
    public static void main(String[] args) {
        List<StudentEntity> studentEntity = getStudentEntity();
        List<StudentSubjectScoreEntity> studentSubjectScore = getStudentSubjectScore();
        List<SubjectEntity> subjectEntity = getSubjectEntity();
        Map<Long, SubjectEntity> subjectEntityMap = subjectEntity.stream().collect(Collectors.toMap(SubjectEntity::getId, v -> v));

        List<SchoolEntity> schoolEntity = getSchoolEntity();
        Collection<StudentDto> composition = DataCompositionFactory.of(studentEntity, StudentDto.class)
                .source(studentSubjectScore)
                .groupKey(StudentDto::getId, StudentSubjectScoreEntity::getStudentId)
                .convert((result, source) -> result.setSubjectScores(source.stream().map(v -> SubjectScoreDto.builder().subjectId(v.getSubjectId()).subjectName(subjectEntityMap.get(v.getSubjectId()).getName()).score(v.getScore()).build()).collect(Collectors.toList())))
                .source(schoolEntity)
                .key(StudentDto::getSchoolId, SchoolEntity::getId)
                .map(StudentDto::getSchoolName, SchoolEntity::getName)
                .composition();
        System.out.println(composition);
    }

    public static List<StudentEntity> getStudentEntity() {
        List<StudentEntity> studentEntities = new ArrayList<>();
        studentEntities.add(StudentEntity.builder().id(1L).name("张三").schoolId(1L).build());
        studentEntities.add(StudentEntity.builder().id(2L).name("李四").schoolId(1L).build());
        studentEntities.add(StudentEntity.builder().id(3L).name("王五").schoolId(2L).build());
        return studentEntities;
    }

    public static List<StudentSubjectScoreEntity> getStudentSubjectScore() {
        List<StudentSubjectScoreEntity> list = new ArrayList<>();
        list.add(StudentSubjectScoreEntity.builder().id(1L).studentId(1L).subjectId(1L).score(BigDecimal.valueOf(100)).build());
        list.add(StudentSubjectScoreEntity.builder().id(2L).studentId(1L).subjectId(2L).score(BigDecimal.valueOf(95)).build());
        list.add(StudentSubjectScoreEntity.builder().id(3L).studentId(2L).subjectId(1L).score(BigDecimal.valueOf(99)).build());
        list.add(StudentSubjectScoreEntity.builder().id(4L).studentId(2L).subjectId(2L).score(BigDecimal.valueOf(96)).build());
        list.add(StudentSubjectScoreEntity.builder().id(5L).studentId(3L).subjectId(1L).score(BigDecimal.valueOf(86)).build());
        list.add(StudentSubjectScoreEntity.builder().id(6L).studentId(3L).subjectId(2L).score(BigDecimal.valueOf(100)).build());
        return list;
    }

    public static List<SubjectEntity> getSubjectEntity() {
        List<SubjectEntity> subjectEntities = new ArrayList<>();
        subjectEntities.add(SubjectEntity.builder().id(1L).name("语文").build());
        subjectEntities.add(SubjectEntity.builder().id(2L).name("数学").build());
        return subjectEntities;
    }

    public static List<SchoolEntity> getSchoolEntity() {
        return Arrays.asList(SchoolEntity.builder().id(1L).name("清华").build(), SchoolEntity.builder().id(2L).name("北大").build());
    }
}
