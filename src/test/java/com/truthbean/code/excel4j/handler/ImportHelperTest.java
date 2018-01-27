package com.truthbean.code.excel4j.handler;

import com.truthbean.code.excel4j.entity.CellEntityTest;

import java.io.File;
import java.util.List;

/**
 * @author TruthBean
 * @since 2018-01-16 15:34
 */
public class ImportHelperTest {

    //@Test
    public void readDataFromExcel() throws Exception {
        long begin = System.currentTimeMillis();
        File file = new File("D:\\develop\\data\\applogs\\7923cd7b-351a-4c33-bd6d-ed0b12a69202.xlsx");
        List<CellEntityTest> cellEntityTests = ImportHelper.readDataFromExcel(file, CellEntityTest.class);
        System.out.println(cellEntityTests);
        System.out.println(System.currentTimeMillis() - begin);
    }
}