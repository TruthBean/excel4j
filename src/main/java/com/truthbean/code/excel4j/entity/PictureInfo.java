package com.truthbean.code.excel4j.entity;

import com.truthbean.code.excel4j.common.WorkbookPictureType;

/**
 * @author truthbean
 * @since 0.0.3
 */
public class PictureInfo {
    private int height;

    /**
     * if is media type
     */
    private WorkbookPictureType type;

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public WorkbookPictureType getType() {
        return type;
    }

    public void setType(WorkbookPictureType type) {
        this.type = type;
    }
}
