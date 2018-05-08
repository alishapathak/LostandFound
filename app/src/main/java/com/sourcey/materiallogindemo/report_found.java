package com.sourcey.materiallogindemo;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.sourcey.materiallogindemo.DatabaseHelper.Database;
import com.sourcey.materiallogindemo.domain.LostAndFound;
import com.sourcey.materiallogindemo.utils.BitmapSaver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class report_found extends AppCompatActivity {
    String date;
    LostAndFound lostAndFound = new LostAndFound();
    private ImageView imageView;
    private int GALLERY = 1, CAMERA = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_found);
        showDate();

        final Spinner category = findViewById(R.id.spinner1);
        final EditText item_Name = findViewById(R.id.item_name1);
        final EditText item_location = findViewById(R.id.item_location1);
        final EditText lost_date = findViewById(R.id.lost_date1);
        imageView = findViewById(R.id.image_view1);

        Button b = findViewById(R.id.gallery_image1);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });

        final EditText description = findViewById(R.id.item_description1);
        final EditText user_name = findViewById(R.id.user_name1);
        final EditText user_email = findViewById(R.id.user_email1);
        final EditText user_phone = findViewById(R.id.user_phone1);


        Button b1 = findViewById(R.id.submit1);
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
//                if(lost_date.getText().toString() <=) {
//                    lost_date.setError("Lost Date cannot Be Empty");
//                    return;
//                }
                if (TextUtils.isEmpty(description.getText().toString())) {
                    description.setError("Description cannot Be Empty");
                    return;
                }
                if (TextUtils.isEmpty(user_name.getText().toString())) {
                    user_name.setError("User Name cannot Be Empty");
                    return;
                }
                if (TextUtils.isEmpty(user_phone.getText().toString())) {
                    user_phone.setError("User Phone Number cannot Be Empty");
                    return;
                }
                if (TextUtils.isEmpty(user_email.getText().toString())) {
                    user_email.setError("User Email cannot Be Empty");
                    return;
                }
                if (imageView.getDrawable() == null) {
                    imageView.setTag("red");
                    return;
                }

                lostAndFound.setCategory(category.getSelectedItem().toString());
                lostAndFound.setItem_name(item_Name.getText().toString());
                lostAndFound.setLocation(item_location.getText().toString());
                lostAndFound.setLost_date(lost_date.getText().toString());
                lostAndFound.setDescription(description.getText().toString());
                lostAndFound.setUser_name(user_name.getText().toString());
                lostAndFound.setUser_email(user_email.getText().toString());
                lostAndFound.setUser_phone(user_phone.getText().toString());

                Database db = new Database(getApplicationContext());
                boolean x = db.insertFoundData(lostAndFound);
                if (x) {
                    Toast.makeText(report_found.this, "Found Data Saved to Database!", Toast.LENGTH_SHORT).show();

                    Database db1 = new Database(getApplicationContext());
                    LostAndFound l = db1.checkData(lostAndFound.getItem_name(), lostAndFound.getCategory());
                    if (l != null) {
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("message/rfc822");
                        i.putExtra(Intent.EXTRA_EMAIL, new String[]{l.getUser_email()});
                        i.putExtra(Intent.EXTRA_SUBJECT, "Your Lost Items Found!!");

                        String body = "Hello " + l.getUser_name() + ",\n\n The item, " + l.getItem_name() + " you lost at " +
                                l.getLost_date() + " is found near " + lostAndFound.getLocation() + ".\n\n Please contact the following person :- \n\n" +
                                "Name : " + lostAndFound.getUser_name() +
                                "\nPhone : " + lostAndFound.getUser_phone() +
                                "\nEmail : " + lostAndFound.getUser_email() +
                                "\n\n Regards,\n\n LostAndFound App!!!";
                        System.out.println(body);
                        i.putExtra(Intent.EXTRA_TEXT, body);
                        i.setType("image/*");
                        File file = BitmapSaver.saveImageToExternalStorage(report_found.this, lostAndFound.getImage());
                        i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                        try {
                            startActivity(Intent.createChooser(i, "Send Via Email..."));
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(report_found.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Intent intent = new Intent(report_found.this, MainActivity.class);
                        intent.putExtra("val", 1);
                        startActivity(intent);
                    }

                } else {
                    Toast.makeText(report_found.this, "Error Saving to Database!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(report_found.this, MainActivity.class);
                    intent.putExtra("val", 1);
                    startActivity(intent);
                }
            }
        });


    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from Gallery",
                "Capture photo from Camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    imageView.setImageBitmap(bitmap);
                    lostAndFound.setImage(bitmap);
                    Toast.makeText(report_found.this, "Image Obtained from Gallery!!", Toast.LENGTH_SHORT).show();

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(report_found.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(thumbnail);
            lostAndFound.setImage(thumbnail);
            Toast.makeText(report_found.this, "Image Obtained from Camera!!", Toast.LENGTH_SHORT).show();
        }
    }


    private void showDate() {
        final Calendar myCalendar = Calendar.getInstance();

        final EditText edittext = findViewById(R.id.lost_date1);
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
                new DatePickerDialog(report_found.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabel(EditText editText, Calendar myCalendar) {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editText.setText(sdf.format(myCalendar.getTime()));
        date = (sdf.format(myCalendar.getTime()));
    }


}
