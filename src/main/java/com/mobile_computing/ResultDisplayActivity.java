package com.mobile_computing;

import static androidx.core.content.PackageManagerCompat.LOG_TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.InputStream;
import java.net.URL;

public class ResultDisplayActivity extends AppCompatActivity {



    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_result_display);



        TextView titleTextView = findViewById(R.id.titleTextView);
        TextView textTextView = findViewById(R.id.textTextView);
        TextView dateTextView = findViewById(R.id.dateTextView);
        //TextView idTextView = findViewById(R.id.idTextView);
        ImageView bookImageView = findViewById(R.id.bookImageView);

        //bookImageView = findViewById(R.id.bookImageView);




        Intent intent = getIntent();
        if (intent != null) {
            String title = intent.getStringExtra("title");
            String date = intent.getStringExtra("date");
            String text = intent.getStringExtra("text");

            String imageURL = intent.getStringExtra("imageURL");
            System.out.println(imageURL);
            Glide.with(getApplicationContext()).load(imageURL).placeholder(R.drawable.baseline_image_24).into(bookImageView);
            titleTextView.setText(title);
            dateTextView.setText(date);
            textTextView.setText(text);
            //bookImageView.setImageDrawable(Drawable.createFromPath("@drawable/book_search_icon.xml"));
            //bookImageView.setImageResource(R.drawable.bookImageView);
            //Glide.with(this).load(imageURL).into(bookImageView);
            /*
            Drawable d = loadingImageFromURL(imageURL);
            assert imageURL != null;
            Log.d(LOG_TAG,"Image in " + imageURL);
            if (d != null) {
                bookImageView.setImageDrawable(d);
            } else {
                //NO IMAGE FOUND
                System.out.println("No image found");
            }

             */

        }

        //ImageButton star = findViewById();
        ImageButton grayStar = findViewById(R.id.grayStarBtn);
        ImageButton goldStar = findViewById(R.id.goldStarBtn);

// Initially show gray star and hide gold star
        goldStar.setVisibility(View.GONE); // Hide gold star initially
        grayStar.setVisibility(View.VISIBLE); // Show gray star initially

        grayStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // On clicking gray star, hide it and show gold star
                grayStar.setVisibility(View.GONE);
                goldStar.setVisibility(View.VISIBLE);
            }
        });

        goldStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // On clicking gold star, hide it and show gray star
                goldStar.setVisibility(View.GONE);
                grayStar.setVisibility(View.VISIBLE);
            }
        });

        Button returnButton = findViewById(R.id.button);

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (goldStar.getVisibility() == View.VISIBLE){
                    //add to favorites
                    System.out.println("Favorite book");
                }
                finish();
            }
        });

    }
    @SuppressLint("RestrictedApi")
    public static Drawable loadingImageFromURL(String url) {


        // Only for Background process (can take time depending on the Internet speed)

        /*
        try {
            Log.d(LOG_TAG, "Trying...");
            InputStream is = (InputStream) new URL(url).getContent();
            return Drawable.createFromStream(is, "@tools:sample/avatars");
        }
        catch(Exception e){
            Log.d(LOG_TAG, "EXCEPTION FOUND");
            return null;
        }
        //return null;

         */
        return null;
    }
}