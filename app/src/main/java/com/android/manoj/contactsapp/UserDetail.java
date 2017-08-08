package com.android.manoj.contactsapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class UserDetail extends AppCompatActivity {

    TextView name, phone1, phone2, email;
    View v1, v2;
    String n, p1, p2, e;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        Intent i = getIntent();
        Bundle b = i.getExtras();

        if (b != null) {
            n = b.getString("name");
            p1 = b.getString("phone1");
            p2 = b.getString("phone2");
            e = b.getString("email");
        }

        name = (TextView) findViewById(R.id.name);
        phone1 = (TextView) findViewById(R.id.phone1);
        phone2 = (TextView) findViewById(R.id.phone2);
        email = (TextView) findViewById(R.id.email);
        v1 = findViewById(R.id.v1);
        v2 = findViewById(R.id.v2);

        name.setText(n);

        Log.d("Details", "onCreate: "+p1+p2+e);
        if (TextUtils.isEmpty(p1)) {
            phone1.setVisibility(View.GONE);
        } else {
            phone1.setText(p1);
        }

        if (TextUtils.isEmpty(p2)) {
            phone2.setVisibility(View.GONE);
            v1.setVisibility(View.GONE);
        } else {
            phone2.setText(p2);
        }

        if (TextUtils.isEmpty(e)) {
            email.setVisibility(View.GONE);
        } else {
            email.setText(e);
        }
    }
}
