package com.truthbean.excel4j.handler;

import com.truthbean.excel4j.annotation.ExcelEntityHandler;
import com.truthbean.excel4j.entity.CellEntity;
import com.truthbean.excel4j.entity.CellEntityTest;
import com.truthbean.excel4j.entity.CellEntityValueClass;
import com.truthbean.excel4j.entity.ExcelInfo;
import com.truthbean.excel4j.util.ZipUtils;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author TruthBean
 * @since 2018-01-15 11:36
 */
public class ExportHelperTest {

    @Test
    public void testThread() throws InterruptedException {
        long begin = System.currentTimeMillis();

        List<List<CellEntityTest>> lists = new ArrayList<>();
        for (int a = 0; a <= 100; a++) {
            List<CellEntityTest> list = new ArrayList<>();
            CellEntityTest test;
            for (int i = 0; i <= 10000; i++) {
                test = new CellEntityTest();
                test.setId(i);
                test.setShortNum(10000L - i);
                test.setTime(new Date(System.currentTimeMillis()));
                test.setUserName("user" + UUID.randomUUID().toString());
                test.setDecimal(new BigDecimal(new Random().nextDouble()));
                list.add(test);
            }
            System.out.println(System.currentTimeMillis() - begin);
            lists.add(list);
        }

        System.out.println(System.currentTimeMillis() - begin);

        ExecutorService executorService = Executors.newCachedThreadPool();

        List<String> filePaths = new ArrayList<>();
        String distFileDir = "D:\\develop\\data\\applogs\\";
        for (List<CellEntityTest> list : lists) {
            executorService.execute(() -> {
                String file = ExportHelper.toExcelFile(list, CellEntityTest.class,
                        UUID.randomUUID().toString() + ".xls", distFileDir);

                String path = distFileDir + file;
                System.out.println(path);
                filePaths.add(path);

                System.out.println(System.currentTimeMillis() - begin);
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(60, TimeUnit.SECONDS);

        ZipUtils.zip(distFileDir + UUID.randomUUID().toString() + ".zip", filePaths);
        System.out.println(System.currentTimeMillis() - begin);

    }

    @Test
    public void testBigData() {
        long begin = System.currentTimeMillis();
        List<CellEntityTest> list = new ArrayList<>();
        CellEntityTest test;
        for (int i = 0; i <= 100_0000; i++) {
            test = new CellEntityTest();
            test.setId(i);
            test.setShortNum((short) i);
            test.setTime(new Date(System.currentTimeMillis()));
            test.setUserName("user" + UUID.randomUUID().toString());
            test.setDecimal(new BigDecimal(new Random().nextDouble()));
            test.setException(new RuntimeException());
            list.add(test);
        }
        System.out.println(System.currentTimeMillis() - begin);

        ExcelInfo excelInfo = ExportHelper.handleData(list, CellEntityTest.class);
        System.out.println(System.currentTimeMillis() - begin);
        ExportHelper.writeToFile(excelInfo, UUID.randomUUID().toString() + ".xlsx", "D:\\develop\\data\\applogs\\");
        System.out.println(System.currentTimeMillis() - begin);
    }

    @Test
    public void handle() {
        List<CellEntityTest> list = new ArrayList<>();
        ExcelInfo excelInfo = ExportHelper.handleData(list, CellEntityTest.class);
        ExportHelper.writeToFile(excelInfo, UUID.randomUUID().toString() + ".xls", "D:\\develop\\data\\applogs\\");
    }

    @Test
    public void handleExcelInfo() {
        List<CellEntityTest> list = new ArrayList<>();
        CellEntityTest test = new CellEntityTest();
        test.setTime(new Date());
        test.setUserName("user");
        test.setDecimal(new BigDecimal("12345678890.01"));
        list.add(test);

        ExcelEntityHandler<CellEntityTest> entityHandler = new ExcelEntityHandler<>(CellEntityTest.class);
        ExcelInfo excelInfo = entityHandler.handleExcelTitle();

        //content
        List<List<CellEntityTest>> content = new ArrayList<>();
        content.add(list);

        entityHandler.handleExcelBigContent(excelInfo, content);

        ExportHelper.writeToFile(excelInfo, UUID.randomUUID().toString() + ".xls", "D:\\develop\\data\\applogs\\");
    }

    @Test
    public void writeToFile() {
        ExcelInfo excelInfo = new ExcelInfo();

        //sheet name
        excelInfo.setSheetName("sheet1");
        //大标题
        CellEntity bigTitle = new CellEntity();
        bigTitle.setValue("测试大标题");
        bigTitle.setValueClass(CellEntityValueClass.TEXT);
        excelInfo.setBigTitle(bigTitle);
        //表头
        List<CellEntity> titles = new ArrayList<>();
        CellEntity title1 = new CellEntity();
        title1.setValueClass(CellEntityValueClass.TEXT);
        title1.setValue("用户名");
        titles.add(title1);

        CellEntity title2 = new CellEntity();
        title2.setValueClass(CellEntityValueClass.TEXT);
        title2.setValue("时间");
        titles.add(title2);

        CellEntity title3 = new CellEntity();
        title3.setValueClass(CellEntityValueClass.TEXT);
        title3.setValue("金额");
        titles.add(title3);

        excelInfo.setTitles(titles);

        //内容
        List<List<CellEntity>> content = new ArrayList<>();
        List<CellEntity> row1 = new ArrayList<>();
        CellEntity cell1 = new CellEntity();
        cell1.setValueClass(CellEntityValueClass.TEXT);
        cell1.setValue("6666666");
        row1.add(cell1);

        CellEntity cell3 = new CellEntity();
        cell3.setValueClass(CellEntityValueClass.DOUBLE);
        cell3.setValue(new BigDecimal("555555555555555555555555.55"));
        row1.add(cell3);

        CellEntity cell2 = new CellEntity();
        cell2.setValueClass(CellEntityValueClass.TEXT);
        cell2.setValue("时间");
        row1.add(title2);
        content.add(row1);
        excelInfo.setContent(content);

        excelInfo.setContent(content);
        List<List<List<CellEntity>>> contentList = new ArrayList<>();
        contentList.add(content);
        excelInfo.setBigDataContent(contentList);

        ExportHelper.writeToFile(excelInfo, "test.xls", "D:\\develop\\data\\applogs\\");
    }
}