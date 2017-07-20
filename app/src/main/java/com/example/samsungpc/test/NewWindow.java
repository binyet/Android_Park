package com.example.samsungpc.test;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.*;
import java.net.*;
import java.util.Enumeration;

/**
 * Created by SamsungPC on 2017/7/19.
 */
public class NewWindow extends Activity {

    public Button button2;
    public TextView textView;
    public static String IpAddress = "123.207.165.51";
    public static int Port = 6666;

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    //完成主界面更新,拿到数据
                    String data = (String)msg.obj;
                    textView.setText(data);
                    break;
                default:
                    break;
            }
        }

    };


    public int CurrentColor = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_window);

        textView = (TextView) findViewById(R.id.textView);
        button2 = (Button) findViewById(R.id.button3);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    recvMsg();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("click");
                if (0 == CurrentColor) {
                    button2.setBackgroundColor(Color.parseColor("#FF0000"));
                    CurrentColor = 1;
                } else {
                    button2.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    CurrentColor = 0;
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String ip = getLocalIpAdress(getApplicationContext());
                        sendMsg("device");
                    }
                }).start();
            }
        });
    }



    private void sendMsg(String str) {
        // 创建socket对象，指定服务器端地址和端口号
        Socket socket = null;
        try {
            socket = new Socket(IpAddress, Port);
            // 获取 Client 端的输出流
            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())), true);
            // 填充信息
            out.println(str);


            // 关闭
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void recvMsg() throws IOException {

        while(true){
            InputStream is = null;
            try {
                Socket socket = new Socket(IpAddress, Port);

                is = socket.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String info = null;
                info = br.readLine();
                System.out.println("I am Client, Server Said : "+info);
                //Toast.makeText(this, "jjjj" , Toast.LENGTH_SHORT).show();
                //在线程里 修改UI
                //textView.setText(info);
                mHandler.sendEmptyMessage(0);
                Message message = new Message();
                message.obj = info;
                mHandler.sendMessage(message);
                br.close();
                is.close();

                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    public static String getLocalIpAdress(Context mContext) {

        //获取wifi服务
        WifiManager wifiManager = (WifiManager)mContext.getSystemService(Context.WIFI_SERVICE);
        //判断wifi是否开启
        if (!wifiManager.isWifiEnabled()) {
            try{
                for(Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();en.hasMoreElements();){
                    NetworkInterface intf = en.nextElement();
                    for(Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();enumIpAddr.hasMoreElements();){
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if(!inetAddress.isLoopbackAddress()){
                            return inetAddress.getHostAddress().toString();
                        }
                    }
                }
            }catch (Exception e){
                return null;
            }
        }
        else{
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            String ip = formatIpAddress(ipAddress);
            return ip;
        }
        return null;
    }
    private static String formatIpAddress(int ipAdress) {

        return (ipAdress & 0xFF ) + "." +
                ((ipAdress >> 8 ) & 0xFF) + "." +
                ((ipAdress >> 16 ) & 0xFF) + "." +
                ( ipAdress >> 24 & 0xFF) ;
    }
}