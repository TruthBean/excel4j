package com.truthbean.code.excel4j.handler;

import com.truthbean.code.excel4j.entity.CellEntityTest;
import org.junit.Test;

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
        File file = new File("D:\\develop\\data\\applogs\\203a1072-0fac-459d-8713-4e9ea854d993.xlsx");
        List<CellEntityTest> cellEntityTests = ImportHelper.readDataFromExcel(file, CellEntityTest.class);
        System.out.println(cellEntityTests);
        System.out.println(System.currentTimeMillis() - begin);
    }
}