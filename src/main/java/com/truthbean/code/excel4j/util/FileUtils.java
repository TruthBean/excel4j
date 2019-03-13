package com.truthbean.code.excel4j.util;

import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {
    private FileUtils() {
    }

    public static byte[] rawValueToByteArray(Object value) {
        byte[] data = null;
        if (value instanceof File) {
            data = readDataFromFile((File) value);
        } else if (value instanceof String) {
            data = readDataFromFile((String) value);
        } else if (value instanceof InputStream) {
            data = inputStreamToByteArray((InputStream) value);
        } else if (value instanceof byte[]){
            data = (byte[]) value;
        }
        return data;
    }

    public static byte[] inputStreamToByteArray(InputStream value) {
        byte[] data = null;
        try {
            data = IOUtils.toByteArray(value);
        } catch (IOException e) {
            LOGGER.error("read file error");
        }
        return data;
    }

    public static byte[] readDataFromFile(String fileName) {
        byte[] data = null;
        try {
            InputStream is = new FileInputStream(fileName);
            data = IOUtils.toByteArray(is);
        } catch (IOException e) {
            LOGGER.error("read file error");
        }
        return data;
    }

    public static byte[] readDataFromFile(File file) {
        byte[] data = null;
        try {
            InputStream is = new FileInputStream(file);
            data = IOUtils.toByteArray(is);
        } catch (IOException e) {
            LOGGER.error("read file error");
        }
        return data;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);
}
