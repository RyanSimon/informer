package com.example.informer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import me.ryansimon.informer.HttpStatusCode;
import me.ryansimon.informer.NetworkErrorMessageInline;
import me.ryansimon.informer.NetworkErrorMessageSnackbar;
import me.ryansimon.informer.OnNetworkErrorActionClickListener;

public class MainActivity extends AppCompatActivity implements OnNetworkErrorActionClickListener {

    private NetworkErrorMessageSnackbar mNetworkErrorMessageSnackbar;
    private NetworkErrorMessageInline mNetworkErrorMessageInline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewGroup view = (ViewGroup) findViewById(R.id.main_coordinator_layout);

        Button showSnackbarErrorBtn = (Button) findViewById(R.id.show_snackbar_error_btn);
        showSnackbarErrorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNetworkErrorMessageSnackbar.show();
                mNetworkErrorMessageInline.dismiss();
            }
        });

        Button showInlineErrorBtn = (Button) findViewById(R.id.show_inline_error_btn);
        showInlineErrorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNetworkErrorMessageInline.show();
                mNetworkErrorMessageSnackbar.dismiss();
            }
        });

        mNetworkErrorMessageSnackbar = new NetworkErrorMessageSnackbar.Builder(HttpStatusCode.BAD_REQUEST, showSnackbarErrorBtn, this).build();
        mNetworkErrorMessageInline = new NetworkErrorMessageInline.Builder(HttpStatusCode.REQUEST_TIMEOUT, view, this).build();
    }

    @Override
    public void onRequestTimeoutActionClick() {
        Toast.makeText(MainActivity.this, R.string.request_timeout_click, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onServerErrorActionClick() {
        Toast.makeText(MainActivity.this, R.string.server_error_click, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAuthErrorActionClick() {
        Toast.makeText(MainActivity.this, R.string.auth_error_click, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGenericErrorActionClick() {
        Toast.makeText(MainActivity.this, R.string.generic_error_click, Toast.LENGTH_SHORT).show();
    }
}
