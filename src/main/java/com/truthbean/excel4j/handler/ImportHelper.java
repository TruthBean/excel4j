package com.truthbean.excel4j.handler;

import com.truthbean.excel4j.annotation.Column;
import com.truthbean.excel4j.handler.transform.CellEntityValueHandler;
import com.truthbean.excel4j.handler.transform.TransformFactory;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author TruthBean
 * @since 2018-01-14 20:27
 */
public final class ImportHelper {
    private ImportHelper() {
    }

    /**
     * read data from excel file
     * @param file file
     * @param modelClass model class
     * @param <T> t
     * @return list of list string
     * @throws Exception
     */
    public static <T> List<T> readDataFromExcel(File file, Class<T> modelClass) throws Exception {
        List<List<String>> contents = new ArrayList<>();
        // The package open is instantaneous, as it should be.
        try (OPCPackage opcPackage = OPCPackage.open(file.getPath(), PackageAccess.READ)) {
            XlsxExcel2Csv xlsxExcel2Csv = new XlsxExcel2Csv(opcPackage, contents);
            xlsxExcel2Csv.process();
        }

        return handleData(contents.subList(2, contents.size()), modelClass);
    }

    private static <T> List<T> handleData(List<List<String>> content, Class<T> modelClass) throws Exception {
        List<T> result = new ArrayList<>();

        Field[] fields = modelClass.getDeclaredFields();
        Map<Integer, Field> cellMap = new HashMap<>();
        for (Field field : fields) {
            Column column = field.getAnnotation(Column.class);
            if (column != null) {
                cellMap.put(column.order(), field);
            }
        }

        T model;
        for (List<String> row : content) {
            model = modelClass.getDeclaredConstructor().newInstance();
            for (int i = 0; i < row.size(); i++) {
                Field field = cellMap.get(i + 1);
                if (!Modifier.isPublic(field.getModifiers())) {
                    field.setAccessible(true);
                }

                CellEntityValueHandler<Object, Object> handler = TransformFactory.factory(field.getType());

                String value = row.get(i);
                field.set(model, handler.transform(value));

            }

            result.add(model);
        }

        return result;
    }


}
