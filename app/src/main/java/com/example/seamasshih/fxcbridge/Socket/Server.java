package com.example.seamasshih.fxcbridge.Socket;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.seamasshih.fxcbridge.MainGameServer;
import com.example.seamasshih.fxcbridge.MainGameServerV2;
import com.example.seamasshih.fxcbridge.R;

import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class Server extends AppCompatActivity {

    private EditText edtServerPort;
    private TextView txtIP;

    public static int CLIENT_LIMITATION = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        findViews();
        ini();
    }

    private void findViews(){
        edtServerPort = findViewById(R.id.edtServerPort);
        txtIP = findViewById(R.id.txtIP);
    }

    private void ini(){
        txtIP.setText(getHostIP());
    }

    public String getHostIP(){
        String hostIP = null;
        try {
            Enumeration nis = NetworkInterface.getNetworkInterfaces();
            InetAddress ia = null;
            while (nis.hasMoreElements()){
                NetworkInterface ni = (NetworkInterface)nis.nextElement();
                Enumeration<InetAddress> ias = ni.getInetAddresses();
                while (ias.hasMoreElements()){
                    ia = ias.nextElement();
                    if (ia instanceof Inet6Address){
                        continue;
                    }
                    String ip = ia.getHostAddress();
                    if (!"127.0.0.1".equals(ip)){
                        hostIP = ia.getHostAddress();
                        break;
                    }
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return hostIP;
    }

    private int getServerPort(String port){
        if (port.equals("")){
            port = "1234";
        }
        return Integer.parseInt(port);
    }

    public void btnServerListener(View view) {
        Intent intent = new Intent(this, MainGameServer.class);
        intent.putExtra("port",getServerPort(edtServerPort.getText().toString()));
        startActivity(intent);
        finish();
    }
}
