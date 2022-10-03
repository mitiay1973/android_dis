package com.example.myapplication;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;

public class MainActivity extends AppCompatActivity {
    Connection connection;
    String ConnectionResult="";
    ImageView image;
    String Img;
    public final int[] i = {1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configurationNextButton();
    }

    private void configurationNextButton()
    {
        Button addData = (Button) findViewById(R.id.addData);
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddData.class));
            }
        });
    }
    public void GetTextFromSql(View a)
    {
        TextView BaseId = findViewById(R.id.BaseId);
        TextView BaseName = findViewById(R.id.BaseName);
        TextView GeographyPosition = findViewById(R.id.GeografPosition);
        TextView NumberOfParse = findViewById(R.id.NumberOfParts);
        ImageView imageView = findViewById(R.id.image);
        try
        {
            ConectionHellper conectionHellper = new ConectionHellper();
            connection=conectionHellper.connectionClass();

            if(connection!=null)
            {
                Button NextList = findViewById((R.id.NextList));
                Button BackList = findViewById((R.id.BackList));
                Button DelData = findViewById(R.id.IzmData);
                String query0 = "select count(Base_Id) from Base ";
                Statement statement0 = connection.createStatement();
                ResultSet resultSet0 = statement0.executeQuery(query0);
                int c = 0;
                while (resultSet0.next())
                {
                    c=resultSet0.getInt(1);
                }
                BackList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(i[0]!=1)
                        {
                            i[0] = i[0] - 1;
                        }
                    }
                });
                int finalC = c;
                NextList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            if(i[0]!= finalC)
                            {
                                i[0] = i[0] + 1;
                            }
                    }
                });
                Button IzmData = (Button) findViewById(R.id.IzmData);
                IzmData.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, IzmData.class));
                    }});
                DelData.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            ConectionHellper conectionHellper = new ConectionHellper();
                            connection = conectionHellper.connectionClass();

                            if (connection != null) {

                                String query2 = "DELETE FROM Base WHERE Base_Id = "+i[0]+"";
                                Statement statement2 = connection.createStatement();
                                statement2.execute(query2);
                            } else {
                                ConnectionResult = "Check Connection";
                            }
                        } catch (SQLException throwable) {
                            throwable.printStackTrace();
                        }
                    }
                });
                String query = "Select * From Base where Base_Id="+i[0]+"";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    BaseId.setText(resultSet.getString(1));
                    BaseName.setText(resultSet.getString(2));
                    GeographyPosition.setText(resultSet.getString(3));
                    NumberOfParse.setText(resultSet.getString(4));
                    Img=(resultSet.getString(5));
                    image.setOnClickListener(v -> {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        pickImg.launch(intent);
                        imageView.setImageBitmap(getImgBitmap(Img));
                    });
                }
            }
            else
            {
                ConnectionResult = "Check Connection";
            }
        }
        catch (Exception ex)
        {

        }

    }

    private final ActivityResultLauncher<Intent> pickImg = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            if (result.getData() != null) {
                Uri uri = result.getData().getData();
                try {
                    InputStream is = getContentResolver().openInputStream(uri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    image.setImageBitmap(bitmap);
                    String encodedImage = encodeImage(bitmap);
                } catch (Exception e) {

                }
            }
        }
    });
    private String encodeImage(Bitmap bitmap) {
        int prevW = 150;
        int prevH = bitmap.getHeight() * prevW / bitmap.getWidth();
        Bitmap b = Bitmap.createScaledBitmap(bitmap, prevW, prevH, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Base64.getEncoder().encodeToString(bytes);
        }
        return "";
    }
    private Bitmap getImgBitmap(String encodedImg) {
        if (encodedImg != null) {
            byte[] bytes = new byte[0];
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                bytes = Base64.getDecoder().decode(encodedImg);
            }
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }
        return BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher_background);
    }
    }

