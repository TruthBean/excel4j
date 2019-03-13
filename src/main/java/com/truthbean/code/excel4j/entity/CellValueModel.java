package com.truthbean.code.excel4j.entity;

import com.truthbean.code.excel4j.common.WorkbookPictureType;
import com.truthbean.code.excel4j.handler.transform.CellEntityValueHandler;
import com.truthbean.code.excel4j.handler.transform.number.DoubleTransformHandler;
import com.truthbean.code.excel4j.handler.transform.text.DefaultTextTransformHandler;

/**
 * @author TruthBean
 * @since 0.0.1
 */
public class CellValueModel {
    /**
     * value class
     */
    private CellValueType valueType;

    /**
     * value transform
     */
    private CellEntityValueHandler valueHandler;

    private PictureInfo pictureInfo;

    public CellValueType getValueType() {
        return valueType;
    }

    public void setValueType(CellValueType valueType) {
        this.valueType = valueType;
    }

    public CellEntityValueHandler getValueHandler() {
        return valueHandler;
    }

    public void setValueHandler(CellEntityValueHandler valueHandler) {
        this.valueHandler = valueHandler;
    }

    public PictureInfo getPictureInfo() {
        return pictureInfo;
    }

    public void setPictureInfo(PictureInfo pictureInfo) {
        this.pictureInfo = pictureInfo;
    }

    public static class CellValueModelBuilder {
        /**
         * build text cell value
         * @return CellValueModel
         */
        public static CellValueModel buildTextCellValueModel() {
            CellValueModel model = new CellValueModel();
            model.setValueType(CellValueType.TEXT);
            model.setValueHandler(new DefaultTextTransformHandler());
            return model;
        }

        /**
         * build double cell value
         * @return CellValueModel
         */
        public static CellValueModel buildDoubleCellValueModel() {
            CellValueModel model = new CellValueModel();
            model.setValueType(CellValueType.NUMBER);
            model.setValueHandler(new DoubleTransformHandler());
            return model;
        }
    }
}
