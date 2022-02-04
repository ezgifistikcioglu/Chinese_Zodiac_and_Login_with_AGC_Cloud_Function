package com.huawei.cloudfunction.demo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.huawei.agconnect.function.AGCFunctionException;
import com.huawei.agconnect.function.AGConnectFunction;
import com.huawei.agconnect.function.FunctionResult;
import com.huawei.hmf.tasks.OnCompleteListener;
import com.huawei.hmf.tasks.Task;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private Button loginBtn;
    private EditText editTextTextPassword;
    private AutoCompleteTextView autoCompleteTextView;
    private TextView resultText;
    private Button previousBtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        loginBtn = findViewById(R.id.ok_btn);
        resultText = findViewById(R.id.rst_text2);
        editTextTextPassword = findViewById(R.id.editTextTextPassword);
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        previousBtn = findViewById(R.id.next_btn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trigger();
            }
        });
        previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
    private void trigger() {
        AGConnectFunction function = AGConnectFunction.getInstance();

        HashMap<String, String> mapNew = new HashMap();
        List<String> list = new ArrayList<>();
        list.isEmpty();
        mapNew.put("userName", autoCompleteTextView.getText().toString());
        mapNew.put("password", editTextTextPassword.getText().toString());
        function.wrap("authentication-$latest").call(mapNew)
                .addOnCompleteListener(new OnCompleteListener<FunctionResult>() {
                    @Override
                    public void onComplete(Task<FunctionResult> task) {
                        if (task.isSuccessful()) {
                            String value = task.getResult().getValue();
                            Log.i("TAG", "value " + value);
                            try {
                                JSONObject object = new JSONObject(value);
                                Log.i("TAG", "JSONObject " + object.toString());
                                String result = (String) object.get("response");
                                Log.i("TAG", "response " + result);

                                resultText.setText(result);

                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e("TAG", e.getMessage());
                            }

                        } else {
                            Exception e = task.getException();
                            if (e instanceof AGCFunctionException) {
                                AGCFunctionException functionException = (AGCFunctionException) e;
                                int errCode = functionException.getCode();
                                String message = functionException.getMessage();
                                Log.e("TAG", "errorCode: " + errCode + ", message: " + message);
                            }
                        }
                    }
                });
    }
}
