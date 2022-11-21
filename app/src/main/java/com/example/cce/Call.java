package com.example.cce;

import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import android.os.Bundle;

public class Call extends AppCompatActivity {
    Button b1, b2, b3;
    EditText e1;
    ListView listView;
    SQLiteOpenHelper s1;
    SQLiteDatabase sqLitedb;
    DataBaseHandler myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        e1 = findViewById(R.id.contact);
        b1 = findViewById(R.id.add);
        b2 = findViewById(R.id.delete);
        b3 = findViewById(R.id.view);



        myDB = new DataBaseHandler(this);

        listView = findViewById(R.id.list);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sr = e1.getText().toString();
                addData(sr);

                if (!sr.isEmpty()){
                    Toast.makeText(Call.this, "Data Added", Toast.LENGTH_SHORT).show();
                    e1.setText("");
                }else if (sr.isEmpty()){
                    Toast.makeText(Call.this, "No Data Entered", Toast.LENGTH_SHORT).show();
                    e1.setText("");

                }

            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqLitedb = myDB.getWritableDatabase();
                String x = e1.getText().toString();
                DeleteData(x);

                if (!x.isEmpty()){
                    Toast.makeText(Call.this, "Data Deleted", Toast.LENGTH_SHORT).show();
                    e1.setText("");
                }else if (x.isEmpty()){
                    Toast.makeText(Call.this, "No Data to delete", Toast.LENGTH_SHORT).show();
                    e1.setText("");

                }
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });

    }



    private void loadData() {
        ArrayList<String> theList = new ArrayList<>();
        Cursor data = myDB.getListContents();
        if (data.getCount() == 0) {
            Toast.makeText(Call.this, "There is no content", Toast.LENGTH_SHORT).show();
        } else {
            while (data.moveToNext()) {
                theList.add(data.getString(1));
                ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, theList);
                listView.setAdapter(listAdapter);
            }
        }
    }

    private boolean DeleteData(String x) {
        return sqLitedb.delete(DataBaseHandler.TABLE_NAME, DataBaseHandler.COL2 + "=?", new String[]{x}) > 0;
    }

    private void addData(String newEntry) {
        boolean insertData = myDB.addData(newEntry);
        if (insertData == true) {
            Toast.makeText(Call.this, "Data Added..", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(Call.this, "Unsuccessful", Toast.LENGTH_SHORT).show();
        }
    }

}
