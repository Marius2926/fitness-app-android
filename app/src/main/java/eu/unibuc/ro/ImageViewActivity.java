package eu.unibuc.ro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import static eu.unibuc.ro.ExerciseActivity.IMAGE_KEY;

public class ImageViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        Intent intent = getIntent();

        ImageView imageView = findViewById(R.id.image_view_iv);
        if (intent.hasExtra(IMAGE_KEY)) {
            String uri = intent.getStringExtra(IMAGE_KEY);
            if (uri != null) {
                Picasso.with(getApplicationContext()).load(uri).into(imageView);
            }
        }
    }
}
