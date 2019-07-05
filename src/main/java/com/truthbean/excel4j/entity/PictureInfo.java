package com.truthbean.excel4j.entity;

import com.truthbean.excel4j.common.WorkbookPictureType;

/**
 * @author truthbean
 * @since 0.0.3
 */
public class PictureInfo {
    private int height;

    private int width;

    private byte[] content;

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

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
