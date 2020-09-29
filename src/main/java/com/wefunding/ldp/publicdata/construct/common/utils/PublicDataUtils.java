package com.wefunding.ldp.publicdata.construct.common.utils;

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
