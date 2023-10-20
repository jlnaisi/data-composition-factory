# data-composition-factory

## 中文介绍

### 项目名称：数据组装工厂

### 前言

> 在进行微服务开发的过程中，有如下场景：`A微服务需要从B、C、D等微服务中获取数据，然后在A服务中进行数据拼装返回给调用方`</br>
>
> 在通常的情况下，我们在A服务中使用for循环去拼装B、C、D的数据，现在，我们可以使用本项目进行拼装

### 实现

| 数据类型/数据源类型 | 集合  | 单个对象 | 
|:----------:|:---:|:----:|
|     集合     |  √  |  √   |
|    单个对象    |  √  | √  | 

### 版本说明

> 1.x 版本有严重逻辑问题,生产中不可以使用
>
> 2.x 版本逻辑重构,问题较少


### 安装

- 最新Maven依赖
    ```maven
      <dependency>
        <groupId>cn.jlnaisi</groupId>
        <artifactId>data-composition-factory</artifactId>
        <version>2.0.0</version>
      </dependency>
    ```
   从1.1.1版本开始,无任何三方依赖

### 示例

```java
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
```