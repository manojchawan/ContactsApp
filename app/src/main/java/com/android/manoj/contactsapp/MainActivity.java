package com.android.manoj.contactsapp;

import android.Manifest;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    public static final int MY_PERMISSIONS_REQUEST_WRITE_FIELS = 102;
    AlertDialog dialog;
    private String contactID;
    Bitmap photo;
    ContactAdapter adapter;
    ListView listView;
    private TextView emptyView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        listView = (ListView) findViewById(R.id.mylist);
        adapter = new ContactAdapter(this, new ArrayList<User>());
        listView.setAdapter(adapter);

        emptyView = (TextView) findViewById(R.id.empty_view);
        listView.setEmptyView(emptyView);

        checkAppPermissions();
        getContacts();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User currUser = adapter.getItem(position);
                String n = currUser.getName();
                String p1 = currUser.getPhone1();
                String p2 = currUser.getPhone2();
                String e = currUser.getEmail();

               /* Log.d(TAG, "onItemClick: "+n +", "+p1+", p2 "+p2+", email"+e);
                Intent intent = new Intent(MainActivity.this, UserDetail.class);
                intent.putExtra("name", n);
                intent.putExtra("phone1", p1);
                intent.putExtra("phone2", p2);
                intent.putExtra("email", e);
                startActivity(intent);*/

            }
        });
    }

    private void getContacts() {
        ArrayList<User> userList = new ArrayList<User>();

        ContentResolver resolver = getContentResolver();
        try {
            Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

            Log.d(TAG, "getContacts: count " + cursor.getCount());

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {



                    String phone1 = null, phone2 = null, email = null;


                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    Log.d(TAG, "getContacts: name " + name);

                    if (cursor.getInt(cursor.getColumnIndex(
                            ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {

                        Cursor pCur = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                new String[]{id}, null);

                        while (pCur.moveToNext()) {
                            phone2 = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                            if (TextUtils.isEmpty(phone1)) {
                                phone1 = phone2;
                                phone2 = null;
                            }
                        }
                        pCur.close();
                    }

                    Cursor cur1 = resolver.query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (cur1.moveToNext()) {
                        email = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        Log.d(TAG, "email" + email);
                    }
                    cur1.close();

                    Log.d(TAG, "getContacts: " + name + ", p1 " + phone1 + ", p2 " + phone2 + ", " + email);
                    User user = new User(name, phone1, phone2, email);
                    userList.add(user);

                }
                adapter.clear();
            }
            cursor.close();

        } catch (Exception e) {
            Log.e(TAG, "getContacts: " + e);
        }
        adapter.addAll(userList);
        // listView.setAdapter(adapter);

    }

    private void checkAppPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
                go_next();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{
                                Manifest.permission.READ_CONTACTS
                        },
                        MY_PERMISSIONS_REQUEST_WRITE_FIELS);
            }
        } else {
            go_next();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_FIELS) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                go_next();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("App needs Permission to Read Contacts")
                        .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                openPermissionScreen();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                                dialog.dismiss();
                            }
                        });
                dialog = builder.show();
            }

        }
    }

    public void openPermissionScreen() {
        // startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", MainActivity.this.getPackageName(), null));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void go_next() {
        Log.d(TAG, "go_next: permissions granted ");
        getContacts();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
