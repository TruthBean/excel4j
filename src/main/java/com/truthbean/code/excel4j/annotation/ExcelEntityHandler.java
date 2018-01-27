package com.truthbean.code.excel4j.annotation;

import com.truthbean.code.excel4j.entity.*;
import com.truthbean.code.excel4j.handler.transform.CellEntityValueHandler;
import com.truthbean.code.excel4j.handler.transform.date.DefaultTimeTransformHandler;
import com.truthbean.code.excel4j.handler.transform.text.DefaultTextTransformHandler;
import com.truthbean.code.excel4j.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * @author TruthBean
 * @since 0.0.1
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

    /**
     * handle excel title
     * @return ExcelModel
     */
    public ExcelModel handleExcelTitle() {
        try {
            ExcelModel excelModel = new ExcelModel();
            //sheet name
            Sheet sheet = cellModelClass.getAnnotation(Sheet.class);
            if (sheet == null) {
                throw new IllegalArgumentException(cellModelClass.getName() + " without @Sheet annotation");
            }
            String sheetName = sheet.name();
            excelModel.setSheetName(sheetName);

            //big title
            boolean noBigTitle = sheet.noBigTitle();
            excelModel.setNoBigTitle(noBigTitle);
            if (!noBigTitle) {
                CellModel bigTitle = new CellModel();
                bigTitle.setValue(sheet.bigTitle());

                CellValueModel model = new CellValueModel();
                model.setValueType(CellValueType.TEXT);
                bigTitle.setValueModel(model);

                CellStyleModel styleModel = new CellStyleModel();
                bigTitle.setStyleModel(styleModel);

                excelModel.setBigTitle(bigTitle);
            }

            //title
            List<CellModel> titles = new ArrayList<>();

            List<Field> fields = ReflectionUtils.getDeclaredFields(cellModelClass);

            CellModel title;
            Column column;
            for (Field field : fields) {
                column = field.getAnnotation(Column.class);
                if (column == null) {
                    continue;
                }

                title = new CellModel();
                title.setValue(column.name());

                CellValueModel model = new CellValueModel();
                model.setValueType(CellValueType.TEXT);
                title.setValueModel(model);

                title.setOrder(column.order());
                CellStyleModel styleModel = new CellStyleModel();
                styleModel.setColumnWidth(column.width());
                title.setStyleModel(styleModel);

                titles.add(title);
            }

            //sort by order
            titles.sort(Comparator.comparingInt(CellModel::getOrder));
            excelModel.setTitles(titles);

            return excelModel;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * handle each excel row
     * @param cellModel cell model
     * @param content content
     */
    public void handleExcelRow(T cellModel, List<List<CellModel>> content) {
        try {
            //row
            List<CellModel> row = new ArrayList<>();

            List<Field> fields = ReflectionUtils.getDeclaredFields(cellModelClass);

            Object fieldValue;

            CellModel cellEntity;
            Column column;

            int counter = 0;

            for (Field field : fields) {
                column = field.getAnnotation(Column.class);
                if (column == null) {
                    continue;
                }

                //invoke get method rather than get the field value
                //so you can custom the data in this filed in get method
                //NOTE: if the method cannot access, get the field value
                fieldValue = ReflectionUtils.getPropertyValue(cellModel, field);

                cellEntity = new CellModel();
                if (fieldValue == null) {
                    cellEntity.setValue(null);

                    CellValueModel model = new CellValueModel();
                    model.setValueType(CellValueType.TEXT);
                    model.setValueHandler(new DefaultTextTransformHandler());
                    cellEntity.setValueModel(model);

                    cellEntity.setOrder(column.order());
                    counter++;
                } else {
                    cellEntity.setValue(fieldValue);

                    //type
                    CellValueType type = column.columnValue().type();

                    CellValueModel model = new CellValueModel();
                    model.setValueType(type);
                    model.setValueHandler(new DefaultTextTransformHandler());

                    cellEntity.setOrder(column.order());

                    Class<? extends CellEntityValueHandler> valueHandlerClass = column.columnValue().transformHandler();
                    if (type.equals(CellValueType.DATE) &&
                            valueHandlerClass.equals(DefaultTextTransformHandler.class)) {
                        model.setValueHandler(new DefaultTimeTransformHandler());
                    } else {
                        model.setValueHandler(valueHandlerClass.getDeclaredConstructor().newInstance());
                    }

                    cellEntity.setValueModel(model);
                }

                row.add(cellEntity);
            }

            //sort by order
            row.sort(Comparator.comparingInt(CellModel::getOrder));

            //此行应该为空
            if (counter != fields.size()) {
                content.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void handleExcelBigContent(ExcelModel excelModel, List<List<T>> lists) {
        List<List<List<CellModel>>> contentList = new ArrayList<>();

        List<List<CellModel>> content;

        for (List<T> tList : lists) {
            content = new ArrayList<>();
            for (T model : tList) {
                handleExcelRow(model, content);
            }
            contentList.add(content);
        }

        if (contentList.size() == 1) {
            excelModel.setContent(contentList.get(0));
        }

        excelModel.setBigDataContent(contentList);
    }

    public void handleExcelContent(ExcelModel excelModel, Collection<T> tList) {
        List<List<CellModel>> content = new ArrayList<>();
        for (T model : tList) {
            handleExcelRow(model, content);
        }
        excelModel.setContent(content);
    }

}
