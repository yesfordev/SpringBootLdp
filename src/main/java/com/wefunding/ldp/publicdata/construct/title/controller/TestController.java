package com.wefunding.ldp.publicdata.construct.title.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wefunding.ldp.publicdata.construct.title.dto.Item;
import com.wefunding.ldp.publicdata.construct.title.dto.RegisterTitleRes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

@RestController
@RequestMapping("/title/test")
public class TestController {

    private String singunguCd = "11680";
    private String bjdongCd = "10300";
    private String ji = "0000";
    private String numOfRows = "100";
    private String platGbCd = "0";

    @Value("${serviceKey}")
    private String serviceKey;

    /*
    xml 출력 test
     */
    @GetMapping("/xml")
    public String getConstructInfoXml() {

        StringBuffer result = new StringBuffer();
        try {
            String urlstr = "http://apis.data.go.kr/1611000/BldRgstService/getBrTitleInfo?" +
                    "sigunguCd=" + singunguCd +
                    "&bjdongCd=" + bjdongCd +
                    "&ji=" + ji +
                    "&_type=json" +
                    "&ServiceKey=" + serviceKey;

            URL url = new URL(urlstr);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));

            String returnLine;
            result.append("<xmp>");
            while ((returnLine = br.readLine()) != null) {
                result.append(returnLine + "\n");
            }
            urlConnection.disconnect();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result + "</xmp>";
    }

    /*
    json 출력 test
     */
    @GetMapping("/json")
    public List<Item> getConstructInfoJson() {

        RegisterTitleRes registerTitleRes = new RegisterTitleRes();

        try {
            String urlstr = "http://apis.data.go.kr/1611000/BldRgstService/getBrTitleInfo?" +
                    "sigunguCd=" + singunguCd +
                    "&bjdongCd=" + bjdongCd +
                    "&numOfRows=" + numOfRows +
                    "&platGbCd=" + platGbCd +
                    "&_type=json" +
                    "&ServiceKey=" + serviceKey;

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

            System.out.println(result);

            Gson gson = new Gson();

            registerTitleRes = gson.fromJson(result, new TypeToken<RegisterTitleRes>(){}.getType());

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        return jsonObject;
        return registerTitleRes.getResponse().getBody().getItems().getItem();
    }
}
