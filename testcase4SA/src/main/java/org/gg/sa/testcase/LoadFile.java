package org.gg.sa.testcase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.*;

public interface LoadFile {
    void init(String jsonName);

    default String readJsonFile(InputStream inputStream) {
        String jsonStr = "";
        try {
            Reader reader = new InputStreamReader(inputStream, "utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
