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
|    Map     | 实现中 | 实现中  | 

### 历史
- 2023-09-04 
  > 版本: 1.1.0<br>
  > 1.实现单个对象数据组装<br>
  > 2.增加数据和数据源需要组装的数据的过滤

### 安装

- Maven依赖
    ```maven
      <dependency>
        <groupId>cn.jlnaisi</groupId>
        <artifactId>data-composition-factory</artifactId>
        <version>1.1.0</version>
      </dependency>
    ```
  考虑到非强制依赖性，因此hutool需要用户自行引入：
  ```maven
  <dependency>
    <groupId>cn.hutool</groupId>
    <artifactId>hutool-core</artifactId>
    <version>请参考本项目pom里的具体依赖</version>
  </dependency>
  ```
  > 说明 本项目pom内的hutool依赖scope级别为provided

### 示例

```java
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
        DataCompositionFactory.data(students)
                .source(CollectionSource.data(studentScores, Student.class)
                        .key(Student::getId, StudentScore::getStudentId)
                        .value(Student::getName, StudentScore::getName)
                        .value(Student::getScore, StudentScore::getScore)
                        .value(Student::getIndex, StudentScore::getIndex)
                        .build()
                ).composition();
        System.out.println("总体耗时:" + (System.currentTimeMillis() - l));
    }
}
```

### 性能

> 经过测试:数据`1万条`，数据源`1万条`，给数据三个字段赋值，测试结果

总体耗时:111<br>
总体耗时:57<br>
总体耗时:62<br>
总体耗时:46<br>
总体耗时:39<br>
总体耗时:35<br>
总体耗时:34<br>
总体耗时:35<br>
总体耗时:34<br>
总体耗时:18<br>

第一次运行耗时111毫秒，以后会趋于平稳，原因是使用了hutool的反射工具类，hutool的反射工具类有缓存功能，所以第一次耗时较高