package com.truthbean.excel4j.handler;

import com.truthbean.excel4j.entity.CellEntityTest;
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
        File file = new File("D:\\develop\\data\\applogs\\ef7eb8ce-27f1-4580-b119-87e8d457d790.xlsx");
        List<CellEntityTest> cellEntityTests = ImportHelper.readDataFromExcel(file, CellEntityTest.class);
        System.out.println(cellEntityTests);
    }
}