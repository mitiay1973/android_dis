package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class AddData extends AppCompatActivity {
    Connection connection;
    String ConnectionResult = "";
    TextView BaseNameAdd;
    TextView GeografPositionAdd;
    TextView NumberOfPartsAdd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);
        configureBackButton();
    }

    private void configureBackButton() {
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void SetTextFromSql(View v) {
        BaseNameAdd = findViewById(R.id.BaseNameAdd);
        GeografPositionAdd = findViewById(R.id.GeografPositionAdd);
        NumberOfPartsAdd = findViewById(R.id.NumberOfPartsAdd);
        String Base = BaseNameAdd.getText().toString();
        String Position = GeografPositionAdd.getText().toString();
        String Number = NumberOfPartsAdd.getText().toString();
        try {
            ConectionHellper conectionHellper = new ConectionHellper();
            connection = conectionHellper.connectionClass();
            if (BaseNameAdd.getText().length()==0 || GeografPositionAdd.getText().length()==0 || NumberOfPartsAdd.getText().length()==0 )
            {
                Toast.makeText(this,"Не заполнены обязательные поля", Toast.LENGTH_LONG).show();
                return;
            }
            if (connection != null) {

                String query = "INSERT INTO Base (BaseName, GeographicalPosition, numberOfParts) values ('" + Base + "','" + Position + "'," + Number + ")";
                Statement statement = connection.createStatement();
                statement.execute(query);
                Toast.makeText(this,"Успешно добавлено", Toast.LENGTH_LONG).show();
            } else {
                ConnectionResult = "Check Connection";
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }
}