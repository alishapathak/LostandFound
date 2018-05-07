package com.sourcey.materiallogindemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sourcey.materiallogindemo.DatabaseHelper.Database;
import com.sourcey.materiallogindemo.domain.LostAndFound;

public class found_data extends AppCompatActivity {
    LostAndFound lostAndFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_data);

        Intent intent = getIntent();
        String x1 = intent.getStringExtra("category");
        String x2 = intent.getStringExtra("item_name");

        Database db = new Database(getApplicationContext());
        lostAndFound = db.searchData(x2, x1);

        TextView category = findViewById(R.id.category3);
        EditText item_Name = findViewById(R.id.item_name3);
        EditText item_location = findViewById(R.id.item_location3);
        EditText lost_date = findViewById(R.id.lost_date3);
        EditText description = findViewById(R.id.item_description3);
        ImageView imageView = findViewById(R.id.image_view3);

        if (lostAndFound != null) {
            category.setText(lostAndFound.getCategory());
            item_Name.setText(lostAndFound.getItem_name());
            item_location.setText(lostAndFound.getLocation());
            lost_date.setText(lostAndFound.getLost_date());
            description.setText(lostAndFound.getDescription());
            imageView.setImageBitmap(lostAndFound.getImage());
        } else {
            Toast.makeText(found_data.this, "No items Found!", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(found_data.this, MainActivity.class);
            i.putExtra("val", 1);
            startActivity(i);
        }
        Button b1 = findViewById(R.id.submit3);
        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText user_name = findViewById(R.id.user_name3);
                String user = user_name.getText().toString();

                EditText user_email = findViewById(R.id.user_email3);
                String email = user_email.getText().toString();

                EditText user_phone = findViewById(R.id.user_phone3);
                String phone = user_phone.getText().toString();

                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{lostAndFound.getUser_email()});
                i.putExtra(Intent.EXTRA_SUBJECT, "Your Lost Items Found!!");

                String body = "Hello " + lostAndFound.getUser_name() + ",\n\n The item, " + lostAndFound.getItem_name() + " you found at " +
                        lostAndFound.getLocation() + " on " + lostAndFound.getLost_date() +
                        " belongs to me" +
                        "!!\n\n Please contact me to return my item :- \n\n" +
                        "Name : " + user +
                        "\nPhone : " + email +
                        "\nEmail : " + phone +
                        "\n\n Regards,\n\n LostAndFound App!!!";
                System.out.println(body);
                i.putExtra(Intent.EXTRA_TEXT, body);
                try {
                    startActivity(Intent.createChooser(i, "Send Via Email..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(found_data.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
