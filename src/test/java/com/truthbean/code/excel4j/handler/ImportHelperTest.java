package com.truthbean.code.excel4j.handler;

import com.truthbean.code.excel4j.entity.CellEntityTest;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

/**
 * @author TruthBean
 * @since 2018-01-16 15:34
 */
public class ImportHelperTest {

    @Test
    public void readDataFromExcel() throws Exception {
        long begin = System.currentTimeMillis();
        File file = new File("D:\\1b2a72e5-4af7-47fb-b236-46bbc43104f3.xls");
        List<CellEntityTest> cellEntityTests = ImportHelper.readDataFromExcel(file, CellEntityTest.class);
        System.out.println(cellEntityTests);
        System.out.println(System.currentTimeMillis() - begin);
    }
}