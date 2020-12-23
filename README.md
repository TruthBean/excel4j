# excel4j

NOTE:
This project merged into https://github.com/truthbean/debbie, will not update any more

### desc

This project use gradle build, you can learn from 
[https://docs.gradle.org/4.4.1/userguide/userguide.html](https://docs.gradle.org/4.4.1/userguide/userguide.html).

Apache poi is used too, and jxl may used if someone asked, but now is not yet

And kotlin lang should be used too, i like it, you can learn from
[http://kotlinlang.org/docs/reference/](http://kotlinlang.org/docs/reference/).

And ...

### example

#### maven
```xml
<dependency>
  <groupId>com.truthbean.code</groupId>
  <artifactId>excel4j</artifactId>
  <version>0.0.2-RELEASE</version>
</dependency>
```

#### gradle
```groovy
compile 'com.truthbean.code:excel4j:0.0.2-RELEASE'
```

Using annotation

```java
@Sheet(name = "测试SHEET", bigTitle = "hello excel4j")
public class CellEntityTest {

    @Column(order = 2, name = "ID", columnValue = @ColumnValue(
            type = CellValueType.DOUBLE
    ), width = 2864)
    private int id;

    @Column(name = "ShortNum", columnValue = @ColumnValue(type = CellValueType.DOUBLE))
    private long shortNum;

    @Column(order = 3, name = "用户名", width = 9000)
    private String userName;

    @Column(order = 5, name = "时间", columnValue = @ColumnValue(type = CellValueType.DATE))
    private Date time;

    @Column(order = 4, name = "金额", columnValue = @ColumnValue(type = CellValueType.DOUBLE))
    private BigDecimal decimal;

    private Exception exception;
    
    ...
}
```

#### java bean export into excel
 
 eg.
 
 ```java
    ExcelModel excelModel = ExportHelper.handleData(list, CellEntityTest.class);
    ExportHelper.writeToFile(excelModel, UUID.randomUUID().toString() + ".xlsx", "D:\\develop\\data\\applogs\\");
```
 more example, please see test class ExportHelperTest.java
 
 ---
 
 #### excel to java bean
 
 eg.
 ```java
    File file = new File("D:\\develop\\data\\applogs\\203a1072-0fac-459d-8713-4e9ea854d993.xlsx");
    List<CellEntityTest> cellEntityTests = ImportHelper.readDataFromExcel(file, CellEntityTest.class);
```
 please see test class ImportHelperTest.java
 
 ---
 
 ### custom
 
 you can custom transform handler, implement CellEntityValueHandler or extend other handler class.
 And then you just do this:
 ```java
    @Column(order = 2, name = "ID", columnValue = @ColumnValue(
            type = CellValueType.DOUBLE, transformHandler = CustomDoubleTransformHandler.class
    ), width = 2864)
    private int id;
```
this transformHandler is your CustomDoubleTransformHandler

### attache
this project is just started, I am glad to see you join。

### release
##### 0.0.1-RELEASE
first version, init project

##### 0.0.2-RELEASE
1. add cell entity model's super class support
eg: 
```java
@Sheet(name = "测试SHEET", bigTitle = "hello excel4j")
public class CellEntityTest extends CommonEntityTest {

    @Column(name = "ShortNum", columnValue = @ColumnValue(type = CellValueType.NUMBER))
    private long shortNum;

    @Column(order = 3, name = "用户名", width = 9000)
    private String userName;

    @Column(order = 5, name = "时间", columnValue = @ColumnValue(type = CellValueType.DATE))
    private Date time;

    @Column(order = 4, name = "金额", columnValue = @ColumnValue(type = CellValueType.NUMBER,
            transformHandler = BigDecimalTransformHandler.class))
    private BigDecimal decimal;
    
    ...
}

public class CommonEntityTest {

    @Column(order = 2, name = "ID", columnValue = @ColumnValue(
            type = CellValueType.DOUBLE, transformHandler = DoubleTransformHandler.class
    ), width = 2864)
    private int id;
    ...
}
```

2. get cell entity model filed value by invoke getter method

3. get map data from excel, get model (class) data from excel is not work, next version will handle it 
