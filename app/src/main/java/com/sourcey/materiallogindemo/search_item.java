package com.sourcey.materiallogindemo;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.sourcey.materiallogindemo.DatabaseHelper.Database;
import com.sourcey.materiallogindemo.domain.LostAndFound;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class search_item extends AppCompatActivity {
    String date;
    LostAndFound lostAndFound = new LostAndFound();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_item);
        showDate();

        final Spinner category = findViewById(R.id.category2);
        final EditText item_Name = findViewById(R.id.item_name2);
        final EditText item_location = findViewById(R.id.item_location2);
        final EditText lost_date = findViewById(R.id.losts);

        Button b1 = findViewById(R.id.search);
        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (TextUtils.isEmpty(item_Name.getText().toString())) {
                    item_Name.setError("Item Name cannot Be Empty");
                    return;
                }
                if (TextUtils.isEmpty(item_location.getText().toString())) {
                    item_location.setError("Item Location cannot Be Empty");
                    return;
                }
                if (TextUtils.isEmpty(lost_date.getText().toString())) {
                    lost_date.setError("Lost Date cannot Be Empty");
                    return;
                }

                lostAndFound.setCategory(category.getSelectedItem().toString());
                lostAndFound.setItem_name(item_Name.getText().toString());
                lostAndFound.setLocation(item_location.getText().toString());
                lostAndFound.setLost_date(lost_date.getText().toString());

                System.out.println("Category = " + lostAndFound.getCategory());
                System.out.println("Name = " + lostAndFound.getItem_name());

                Database db = new Database(getApplicationContext());
                LostAndFound l = db.searchData(lostAndFound.getItem_name(), lostAndFound.getCategory());
                if (l == null) {
                    System.out.println("Null Data!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                } else
                    System.out.println("Data found!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                if (l != null) {
                    Toast.makeText(search_item.this, "Item found on Database!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(search_item.this, found_data.class);
                    intent.putExtra("category", l.getCategory());
                    intent.putExtra("item_name", l.getItem_name());
//                    intent.putExtra("location", l.getLocation());
//                    intent.putExtra("lost_date", l.getLost_date());
//                    intent.putExtra("description", l.getDescription());
//
//                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                    l.getImage().compress(Bitmap.CompressFormat.PNG, 100, stream);
//                    byte[] byteArray = stream.toByteArray();
//
//                    intent.putExtra("image", byteArray);
//
//                    intent.putExtra("user_name", l.getUser_name());
//                    intent.putExtra("user_email", l.getUser_email());
                    startActivity(intent);
                } else {
                    Toast.makeText(search_item.this, "Item Not Found!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(search_item.this, MainActivity.class);
                    intent.putExtra("val", 1);
                    startActivity(intent);
                }
            }
        });

    }


    private void showDate() {
        final Calendar myCalendar = Calendar.getInstance();

        final EditText edittext = findViewById(R.id.losts);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                view.setMaxDate(myCalendar.getTimeInMillis());
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(edittext, myCalendar);
            }

        };

        edittext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(search_item.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabel(EditText editText, Calendar myCalendar) {

        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editText.setText(sdf.format(myCalendar.getTime()));
        date = (sdf.format(myCalendar.getTime()));
    }


}
