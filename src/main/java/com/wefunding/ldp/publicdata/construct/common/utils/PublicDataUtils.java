package com.wefunding.ldp.publicdata.construct.common.utils;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by yes on 2020/09/29
 */

public class PublicDataUtils {

    public String connectUrl(String urlstr) throws IOException {
        URL url = new URL(urlstr);

        String result = "";

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");

        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));

        String returnLine;
        while ((returnLine = br.readLine()) != null) {
            result = result.concat(returnLine);
        }
        urlConnection.disconnect();

        return result;
    }

    /**
     * [참고] JSONObject return type 추가
     * @param
     * @return
     * @throws IOException
     */
    public JSONObject connectUrlReturnObject(String urlstr) throws IOException {
        URL url = new URL(urlstr);

        JSONObject resObject;

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");

        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));

        resObject = (JSONObject)JSONValue.parse(br);

        urlConnection.disconnect();

        return resObject;
    }

    public int getTotalCount(String result) {
        int totalCount;
        int countStart = result.indexOf("totalCount\":");

        totalCount = Integer.parseInt(result.substring(countStart + "totalCount\":".length(), result.length() - 3));
        return totalCount;
    }

    public String getItemString(String result) {
        int start = result.indexOf("item\":");
        int end = result.lastIndexOf("},\"numOfRows");

        String itemString = result.substring(start + "item\":".length(), end);
        return itemString;
    }
}
