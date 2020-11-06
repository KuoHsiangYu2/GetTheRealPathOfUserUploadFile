// 參考資料
// https://cythilya.github.io/2020/07/24/encode-decode/
// https://blog.csdn.net/KokJuis/article/details/84140514
// https://www.ewdna.com/2008/11/urlencoder.html
// https://blog.xuite.net/programer/1/7702032
package com.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

@MultipartConfig(location = "", fileSizeThreshold = 25 * 1024 * 1024, maxFileSize = 25 * 1024
        * 1024, maxRequestSize = 25 * 1024 * 1024)
@WebServlet("/GetFileRealPath")
public class GetFileRealPath extends HttpServlet {
    private static final long serialVersionUID = 1L;
    public static final String ALLOWED_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_.!~*'()";

    public GetFileRealPath() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        System.out.println("doGet");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        System.out.println("doPost");
        HttpSession httpSession = request.getSession();
        Collection<Part> parts = request.getParts();

        String filename = "";

        if (parts != null) {
            Iterator<Part> iterator = parts.iterator();
            Part part = null;
            while (iterator.hasNext()) {
                part = iterator.next();

                if (part.getContentType() != null) {
                    filename = getFileName(part);
                    System.out.println("filename = " + filename);
                }
            }
            // filename = URLEncoder.encode(filename, "UTF-8");
            filename = encodeURIComponent(filename);

            httpSession.setAttribute("filename", filename);
            response.sendRedirect("index.jsp");
            return;
        }
    }

    private static String getFileName(Part part) {
        // 取得使用者上傳檔案名稱
        System.out.println("part.getHeader(\"content-disposition\") -> " + part.getHeader("content-disposition"));
        String[] content = part.getHeader("content-disposition").split(";");
        int length = content.length;
        String result = "";

        for (int i = 0; i < length; i++) {
            result = String.format("content[%d] -> [%s]", i, content[i]);
            System.out.println(result);
        }

        for (int i = 0; i < length; i++) {
            if (content[i].trim().startsWith("filename")) {
                return content[i].substring(content[i].indexOf('=') + 1).trim();
            }
        }

        return "";
    }

    private static String encodeURIComponent(String inputString) {
        if (null == inputString || "".equals(inputString.trim())) {
            return inputString;
        }

        int stringLength = inputString.length();
        StringBuilder stringBuilder = new StringBuilder(stringLength * 3);
        try {
            for (int i = 0; i < stringLength; i++) {
                String element = inputString.substring(i, i + 1);
                if (ALLOWED_CHARS.indexOf(element) == -1) {
                    byte[] byteArray = element.getBytes("UTF-8");
                    stringBuilder.append(getHex(byteArray));
                    continue;
                }
                stringBuilder.append(element);
            }
            return stringBuilder.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return inputString;
    }

    private static String getHex(byte buffer[]) {
        StringBuilder stringBuilder = new StringBuilder(buffer.length * 3);
        for (int i = 0; i < buffer.length; i++) {
            int n = (int) buffer[i] & 0xff;
            stringBuilder.append("%");
            if (n < 0x10) {
                stringBuilder.append("0");
            }
            stringBuilder.append(Long.toString(n, 16).toUpperCase());
        }
        return stringBuilder.toString();
    }
}