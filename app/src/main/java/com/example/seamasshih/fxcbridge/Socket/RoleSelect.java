package com.example.seamasshih.fxcbridge.Socket;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.seamasshih.fxcbridge.R;

public class RoleSelect extends AppCompatActivity {

    Button btnServer, btnClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_select);
        findViews();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void findViews(){
        btnServer = findViewById(R.id.btnServer);
        btnClient = findViewById(R.id.btnClient);
    }

    public void btnListener(View view) {
        switch (view.getId()) {
            case R.id.btnServer:
                Intent intentServer = new Intent(this,Server.class);
                startActivity(intentServer);
                break;
            case R.id.btnClient:
                Intent intentClient = new Intent(this,Client.class);
                startActivity(intentClient);
                break;
        }
    }

}
