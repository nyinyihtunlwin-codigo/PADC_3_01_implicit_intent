package projects.nyinyihtunlwin.implicit_intents;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.IOException;

public class CameraActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnTakePhoto, btnPick;
    private ImageView imvPhoto;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CODE_IMAGE = 100;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, CameraActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        initializeUI();
        btnTakePhoto.setOnClickListener(this);
        btnPick.setOnClickListener(this);
    }

    private void initializeUI() {
        btnTakePhoto = (Button) findViewById(R.id.btn_take_photo);
        btnPick = (Button) findViewById(R.id.btn_pick);
        imvPhoto = (ImageView) findViewById(R.id.imv_photo);
    }

    private void startImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/png");
        startActivityForResult(intent, REQUEST_CODE_IMAGE);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imvPhoto.setImageBitmap(imageBitmap);
        } else if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri imageUri = data.getData();
                uriToBitmap(imageUri);
            }
        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
        }

    }

    private void uriToBitmap(Uri selectedFileUri) {
        try {
            ParcelFileDescriptor parcelFileDescriptor =
                    getContentResolver().openFileDescriptor(selectedFileUri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            imvPhoto.setImageBitmap(image);

            parcelFileDescriptor.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_take_photo:
                dispatchTakePictureIntent();
                break;
            case R.id.btn_pick:
                startImageChooser();
                break;
        }
    }
}
