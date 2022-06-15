package eu.unibuc.ro;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;


public class ExerciseActivity extends AppCompatActivity {

    public static final String IMAGE_KEY = "image_key";
    public static final String GIF_KEY = "gif_key";
    private static final int REQUEST_CODE_UPLOADED_IMAGE = 1;
    private static final String CHALLENGES_PREF_FILE = "challenges_pref_file";
    private static final String SQUATS_LINK = "squats_link";
    private static final String ABS_LINK = "abs_link";
    private static final String UPLOADED_LINK = "uploaded_link";
    private SharedPreferences sharedPreferences;
    private ImageView ivUploaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        initComponents();
    }


    private void initComponents() {

        ImageView ivSquats = findViewById(R.id.exercise_iv_squats_challenge);
        ImageView ivAbs = findViewById(R.id.exercise_iv_abs_challenge);
        ivUploaded = findViewById(R.id.exercise_iv_uploaded_image);
        Button btnUploadPicture = findViewById(R.id.exercise_btn_upload_picture);
        Button btnMaps = findViewById(R.id.exercise_btn_maps);

        sharedPreferences = getApplicationContext().getSharedPreferences(CHALLENGES_PREF_FILE, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(SQUATS_LINK, "https://i.pinimg.com/474x/38/87/a0/3887a0a52c041284f6e1a02475ddfc48.jpg");
        editor.putString(ABS_LINK, "https://i2.wp.com/bloggers.society19.com/wp-content/uploads/2015/11/d01d136765f197cd203770a0ef60ee1f.jpg?resize=425%2C600&ssl=1");
        editor.apply();

        Picasso.with(getApplicationContext()).load(sharedPreferences
                .getString(SQUATS_LINK, null)).resize(400, 450).into(ivSquats);

        Picasso.with(getApplicationContext()).load(sharedPreferences
                .getString(ABS_LINK, null)).resize(400, 450).into(ivAbs);

        if (sharedPreferences.contains(UPLOADED_LINK)) {
            byte[] b = Base64.decode(sharedPreferences.getString(UPLOADED_LINK, null).getBytes(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            ivUploaded.setImageBitmap(bitmap);
        } else {
            ivUploaded.setVisibility(View.GONE);
        }

        btnUploadPicture.setOnClickListener(v -> {

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                startActivityForResult(takePictureIntent, REQUEST_CODE_UPLOADED_IMAGE);
            } catch (ActivityNotFoundException ignored) {
            }
        });

        btnMaps.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
            startActivity(intent);
        });

        ivSquats.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ImageViewActivity.class);
            String squatsLink = sharedPreferences.getString(SQUATS_LINK, null);
            intent.putExtra(IMAGE_KEY, squatsLink);
            startActivity(intent);
        });

        ivAbs.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ImageViewActivity.class);
            intent.putExtra(IMAGE_KEY, sharedPreferences.getString(ABS_LINK, null));
            startActivity(intent);
        });

        ivUploaded.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ImageViewActivity.class);
            intent.putExtra(IMAGE_KEY, sharedPreferences.getString(UPLOADED_LINK, null));
            startActivity(intent);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_UPLOADED_IMAGE && resultCode == RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ivUploaded.setVisibility(View.VISIBLE);
            ivUploaded.setImageBitmap(imageBitmap);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] b = baos.toByteArray();

            String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(UPLOADED_LINK, encodedImage);
            editor.apply();
        }
    }
}
