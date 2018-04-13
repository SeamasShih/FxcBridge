package com.example.seamasshih.fxcbridge.Socket;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.example.seamasshih.fxcbridge.MainGameClient;
import com.example.seamasshih.fxcbridge.MainGameClientV2;
import com.example.seamasshih.fxcbridge.R;

public class Client extends AppCompatActivity {

    private EditText edtIP, edtPort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        findViews();
    }

    private void findViews() {
        edtIP = findViewById(R.id.edtIP);
        edtPort = findViewById(R.id.edtPort);
    }

    private int getPort(String port){
        if (port.equals("")){
            port = "1234";
        }
        return Integer.parseInt(port);
    }

    public void btnClientListener(View view) {
        Intent intent = new Intent(this, MainGameClient.class);
        Bundle bundle = new Bundle();
        bundle.putString("ip",edtIP.getText().toString());
        bundle.putInt("port", getPort(edtPort.getText().toString()));
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }


}
