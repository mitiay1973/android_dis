package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class IzmData extends AppCompatActivity {
    Connection connection;
    String ConnectionResult = "";
    TextView BaseNameIzm;
    TextView GeografPositionIzm;
    TextView NumberOfPartsIzm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_izm_data);
        configureBackButton1();
    }
    private void configureBackButton1() {
        Button backs = (Button) findViewById(R.id.backs);
        backs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void IzmTextFromSql(View v) {
        BaseNameIzm = findViewById(R.id.BaseNameIzm);
        GeografPositionIzm = findViewById(R.id.GeografPositionIzm);
        NumberOfPartsIzm = findViewById(R.id.NumberOfPartsIzm);
        String Bases = BaseNameIzm.getText().toString();
        String Positions = GeografPositionIzm.getText().toString();
        String Numbers = NumberOfPartsIzm.getText().toString();
        try {
            ConectionHellper conectionHellper = new ConectionHellper();
            connection = conectionHellper.connectionClass();

            if (connection != null) {
                String query11 = "select Base_Id from Base where BaseName = '" + Bases + "', GeographicalPosition ='" + Positions + "',numberOfParts =" + Numbers + "";
                Statement statement11 = connection.createStatement();
                ResultSet resultSet11 = statement11.executeQuery(query11);
                int i = 0;
                while (resultSet11.next())
                {
                    i=resultSet11.getInt(1);
                }
                String query12 = "update Base set BaseName = '" + Bases + "', GeographicalPosition ='" + Positions + "',numberOfParts =" + Numbers + " where Base_Id = "+i+"";
                Statement statement12 = connection.createStatement();
                statement12.execute(query12);
            } else {
                ConnectionResult = "Check Connection";
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }
}