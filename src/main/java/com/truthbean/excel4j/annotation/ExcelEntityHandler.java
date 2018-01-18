package com.truthbean.excel4j.annotation;

import com.truthbean.excel4j.entity.CellEntity;
import com.truthbean.excel4j.entity.CellEntityValueClass;
import com.truthbean.excel4j.entity.ExcelInfo;
import com.truthbean.excel4j.handler.transform.CellEntityValueHandler;
import com.truthbean.excel4j.handler.transform.date.DefaultTimeTransformHandler;
import com.truthbean.excel4j.handler.transform.text.DefaultTextTransformHandler;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * @author TruthBean
 * @since 2018-01-15 14:24
 */
public class ExcelEntityHandler<T> {

    /**
     * cellModel class
     */
    private final Class<T> cellModelClass;

    /**
     * constructor, handle cellModel class
     * @param cellModelClass cellModel class
     */
    public ExcelEntityHandler(Class<T> cellModelClass) {
        //excel info instance
        this.cellModelClass = cellModelClass;
    }

    public ExcelInfo handleExcelTitle() {
        try {
            ExcelInfo excelInfo = new ExcelInfo();
            //sheet name
            Sheet sheet = cellModelClass.getAnnotation(Sheet.class);
            if (sheet == null) {
                throw new IllegalArgumentException(cellModelClass.getName() + " without @Sheet annotation");
            }
            String sheetName = sheet.name();
            excelInfo.setSheetName(sheetName);

            //big title
            String bigTitleValue = sheet.bigTitle();
            if (!"".equals(bigTitleValue)) {
                CellEntity bigTitle = new CellEntity();
                bigTitle.setValue(sheet.bigTitle());
                bigTitle.setValueClass(CellEntityValueClass.TEXT);

                excelInfo.setBigTitle(bigTitle);
            }

            //title
            List<CellEntity> titles = new ArrayList<>();

            Field[] fields = cellModelClass.getDeclaredFields();

            CellEntity title;
            Cell cell;
            for (Field field : fields) {
                cell = field.getAnnotation(Cell.class);
                if (cell == null) {
                    continue;
                }

                if (!Modifier.isPublic(field.getModifiers())) {
                    field.setAccessible(true);
                }

                title = new CellEntity();
                title.setValue(cell.column());
                title.setValueClass(CellEntityValueClass.TEXT);
                title.setOrder(cell.order());
                title.setColumnWidth(cell.columnWidth());

                titles.add(title);
            }

            //sort by order
            titles.sort(Comparator.comparingInt(CellEntity::getOrder));
            excelInfo.setTitles(titles);

            return excelInfo;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void handleExcelRow(T cellModel, List<List<CellEntity>> content) {
        try {
            //row
            List<CellEntity> row = new ArrayList<>();

            Field[] fields = cellModelClass.getDeclaredFields();

            Object fieldValue;

            CellEntity cellEntity;
            Cell cell;

            int counter = 0;

            for (Field field : fields) {
                cell = field.getAnnotation(Cell.class);
                if (cell == null) {
                    continue;
                }

                if (!Modifier.isPublic(field.getModifiers())) {
                    field.setAccessible(true);
                }
                fieldValue = field.get(cellModel);

                cellEntity = new CellEntity();
                if (fieldValue == null) {
                    cellEntity.setValue(null);
                    cellEntity.setValueClass(CellEntityValueClass.TEXT);
                    cellEntity.setValueHandler(new DefaultTextTransformHandler());
                    cellEntity.setOrder(cell.order());
                    counter++;
                } else {
                    cellEntity.setValue(fieldValue);
                    cellEntity.setValueClass(cell.valueClass());
                    cellEntity.setOrder(cell.order());

                    Class<? extends CellEntityValueHandler> valueHandlerClass = cell.transformHandler();
                    if (cell.valueClass().equals(CellEntityValueClass.DATE) && valueHandlerClass.equals(DefaultTextTransformHandler.class)) {
                        cellEntity.setValueHandler(new DefaultTimeTransformHandler());
                    } else {
                        cellEntity.setValueHandler(valueHandlerClass.getDeclaredConstructor().newInstance());
                    }
                }

                row.add(cellEntity);
            }

            //sort by order
            row.sort(Comparator.comparingInt(CellEntity::getOrder));

            //此行应该为空
            if (counter != fields.length) {
                content.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void handleExcelBigContent(ExcelInfo excelInfo, List<List<T>> lists) {
        List<List<List<CellEntity>>> contentList = new ArrayList<>();

        List<List<CellEntity>> content;

        for (List<T> tList : lists) {
            content = new ArrayList<>();
            for (T model : tList) {
                handleExcelRow(model, content);
            }
            contentList.add(content);
        }

        if (contentList.size() == 1) {
            excelInfo.setContent(contentList.get(0));
        }

        excelInfo.setBigDataContent(contentList);
    }

    public void handleExcelContent(ExcelInfo excelInfo, Collection<T> tList) {
        List<List<CellEntity>> content = new ArrayList<>();
        for (T model : tList) {
            handleExcelRow(model, content);
        }
        excelInfo.setContent(content);
    }

}
