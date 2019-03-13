package com.truthbean.code.excel4j.util;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * @since 0.0.3
 */
public class ServletUtils {

    /**
     * 通过HttpServletResponse返回给请求方
     * @param response HttpServletResponse
     * @param filename 文件名称
     * @param workbook workbook
     */
    public static void writeToServletResponse(HttpServletResponse response, String filename, Workbook workbook) {
        //servlet 响应生成excel文件
        try {
            String head = new String(filename.getBytes("GB2312"), StandardCharsets.ISO_8859_1);
            response.reset();
            response.setContentType("application/vnd.ms-excel");
            response.addHeader("Content-Disposition", "attachment; filename=" + head);
            OutputStream os = response.getOutputStream();
            workbook.write(os);
            workbook.close();
            os.close();
        } catch (IOException e) {
            LOGGER.error("", e);
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(ServletUtils.class);
}
