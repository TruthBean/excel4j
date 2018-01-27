package com.truthbean.code.excel4j.handler;

import com.truthbean.code.excel4j.annotation.ExcelEntityHandler;
import com.truthbean.code.excel4j.entity.*;
import com.truthbean.code.excel4j.util.ZipUtils;

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

    //@Test
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
                        UUID.randomUUID().toString() + " 大标题", UUID.randomUUID().toString() + ".xls",
                        distFileDir);

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

    //@Test
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

        ExcelModel excelModel = ExportHelper.handleData(list, CellEntityTest.class);
        System.out.println(System.currentTimeMillis() - begin);
        ExportHelper.writeToFile(excelModel, UUID.randomUUID().toString() + ".xlsx", "D:\\develop\\data\\applogs\\");
        System.out.println(System.currentTimeMillis() - begin);
    }

    //@Test
    public void handle() {
        List<CellEntityTest> list = new ArrayList<>();
        ExcelModel excelModel = ExportHelper.handleData(list, CellEntityTest.class);
        ExportHelper.writeToFile(excelModel, UUID.randomUUID().toString() + ".xls", "D:\\develop\\data\\applogs\\");
    }

    //@Test
    public void handleExcelInfo() {
        List<CellEntityTest> list = new ArrayList<>();
        CellEntityTest test = new CellEntityTest();
        test.setTime(new Date());
        test.setUserName("user");
        test.setDecimal(new BigDecimal("-12345678890.01"));
        list.add(test);

        ExcelEntityHandler<CellEntityTest> entityHandler = new ExcelEntityHandler<>(CellEntityTest.class);
        ExcelModel excelModel = entityHandler.handleExcelTitle();

        //content
        List<List<CellEntityTest>> content = new ArrayList<>();
        content.add(list);

        entityHandler.handleExcelBigContent(excelModel, content);

        ExportHelper.writeToFile(excelModel, UUID.randomUUID().toString() + ".xlsx", "D:\\develop\\data\\applogs\\");
    }

    //@Test
    public void writeToFile() {
        ExcelModel excelModel = new ExcelModel();

        //sheet name
        excelModel.setSheetName("sheet1");
        //大标题
        CellModel bigTitle = new CellModel();
        bigTitle.setValue("测试大标题");
        bigTitle.setValueModel(CellValueModel.CellValueModelBuilder.buildTextCellValueModel());
        bigTitle.setStyleModel(new CellStyleModel());
        excelModel.setBigTitle(bigTitle);
        //表头
        List<CellModel> titles = new ArrayList<>();
        CellModel title1 = new CellModel();
        title1.setValueModel(CellValueModel.CellValueModelBuilder.buildTextCellValueModel());
        title1.setValue("用户名");
        title1.setOrder(1);
        CellStyleModel title1Model = new CellStyleModel();
        title1Model.setColumnWidth(10000);
        title1.setStyleModel(title1Model);
        titles.add(title1);

        CellModel title2 = new CellModel();
        title2.setValueModel(CellValueModel.CellValueModelBuilder.buildTextCellValueModel());
        title2.setValue("时间");
        title2.setOrder(2);
        CellStyleModel title2Mode = new CellStyleModel();
        title2Mode.setColumnWidth(10000);
        title2.setStyleModel(title2Mode);
        titles.add(title2);

        CellModel title3 = new CellModel();
        title3.setValueModel(CellValueModel.CellValueModelBuilder.buildTextCellValueModel());
        title3.setValue("金额");
        title3.setOrder(3);
        CellStyleModel title3Model = new CellStyleModel();
        title3Model.setColumnWidth(8000);
        title3.setStyleModel(title3Model);
        titles.add(title3);

        excelModel.setTitles(titles);

        //内容
        List<List<CellModel>> content = new ArrayList<>();
        List<CellModel> row1 = new ArrayList<>();
        CellModel cell1 = new CellModel();
        cell1.setValueModel(CellValueModel.CellValueModelBuilder.buildTextCellValueModel());
        cell1.setValue("6666666");
        cell1.setOrder(1);
        row1.add(cell1);

        CellModel cell3 = new CellModel();
        cell3.setValueModel(CellValueModel.CellValueModelBuilder.buildDoubleCellValueModel());
        cell3.setValue(new BigDecimal("555555555555555555555555.55"));
        cell3.setOrder(3);
        row1.add(cell3);

        CellModel cell2 = new CellModel();
        cell2.setValueModel(CellValueModel.CellValueModelBuilder.buildTextCellValueModel());
        cell2.setValue("时间");
        row1.add(title2);
        cell2.setOrder(2);
        content.add(row1);
        excelModel.setContent(content);

        excelModel.setContent(content);
        List<List<List<CellModel>>> contentList = new ArrayList<>();
        contentList.add(content);
        excelModel.setBigDataContent(contentList);

        ExportHelper.writeToFile(excelModel, "test.xls", "D:\\develop\\data\\applogs\\");
    }
}