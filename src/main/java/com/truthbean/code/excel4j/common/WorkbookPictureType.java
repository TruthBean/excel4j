package com.truthbean.code.excel4j.common;

import org.apache.poi.ss.usermodel.Workbook;

/**
 * @author truthbean
 * @since 0.0.3
 */
public enum WorkbookPictureType {

    /**
     * Extended windows meta file
     *
     * @see Workbook#PICTURE_TYPE_EMF
     */
    EMF(Workbook.PICTURE_TYPE_EMF),

    /**
     * Windows Meta File
     *
     * @see Workbook#PICTURE_TYPE_WMF
     */
    WMF(Workbook.PICTURE_TYPE_WMF),

    /**
     * Mac PICT format
     *
     * @see Workbook#PICTURE_TYPE_PICT
     */
    PICT(Workbook.PICTURE_TYPE_PICT),

    /**
     * JPEG format
     *
     * @see Workbook#PICTURE_TYPE_JPEG
     */
    JPEG(Workbook.PICTURE_TYPE_JPEG),

    /**
     * PNG format
     *
     * @see Workbook#PICTURE_TYPE_PNG
     */
    PNG(Workbook.PICTURE_TYPE_PNG),

    /**
     * Device independent bitmap
     *
     * @see Workbook#PICTURE_TYPE_DIB
     */
    DIB(Workbook.PICTURE_TYPE_DIB);

    private int type;

    WorkbookPictureType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
    }
