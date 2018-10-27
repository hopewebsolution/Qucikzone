package com.quikzon.add.utility;
import android.os.StrictMode;
import android.util.Log;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
public class SendSms {
    public static String send_sms(String mobile, String message) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //Your user name
        String username = "telsys";
        //Your authentication key
        String authkey = "9e1169912dXX";
        //Sender ID,While using route4 sender id should be 6 characters long.
        String senderId = "WEBITJ";
        //define route
        String accusage = "1";

        //Prepare Url
        URLConnection myURLConnection = null;
        URL myURL = null;
        BufferedReader reader = null;

        //encoding message
        String encoded_message = URLEncoder.encode(message);

        //Send SMS API
        String mainUrl = "http://sms.itwebsolution.in/sendsms.jsp?";

        //Prepare parameter string
        StringBuilder sbPostData = new StringBuilder(mainUrl);
        sbPostData.append("user=" + username);
        sbPostData.append("&password=" + authkey);
        sbPostData.append("&mobiles=" + mobile);
        sbPostData.append("&sms=" + encoded_message);
        sbPostData.append("&accusage=" + accusage);
        sbPostData.append("&senderid=" + senderId);
        String response = null;
        //final string
        mainUrl = sbPostData.toString();
        Log.d("Url",mainUrl);
        try {
            //prepare connection
            myURL = new URL(mainUrl);
            myURLConnection = myURL.openConnection();
            myURLConnection.connect();
            reader = new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));
            //reading response
            while ((response = reader.readLine()) != null)
                //print response
                System.out.println(response);
            //finally close connection
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static void main(String[] args) {
        String response = send_sms("9999999999", "this is sample message");
        System.out.println(response);
    }
}