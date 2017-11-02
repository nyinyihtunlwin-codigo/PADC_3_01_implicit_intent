package projects.nyinyihtunlwin.implicit_intents;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ShareActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputLayout textInputLayout;
    private Button btnShareTxt, btnShareEmail, btnShareFile;
    private static final int REQUEST_CODE_IMAGE = 100;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, ShareActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        initializeUI();
        btnShareTxt.setOnClickListener(this);
        btnShareEmail.setOnClickListener(this);
        btnShareFile.setOnClickListener(this);
    }

    private void initializeUI() {
        textInputLayout = (TextInputLayout) findViewById(R.id.tip_text);
        btnShareTxt = (Button) findViewById(R.id.btn_share_text);
        btnShareEmail = (Button) findViewById(R.id.btn_share_email);
        btnShareFile = (Button) findViewById(R.id.btn_share_file);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_share_text:
                if (!textInputLayout.getEditText().getText().toString().isEmpty()) {
                    shareText(textInputLayout.getEditText().getText().toString());
                } else {
                    Toast.makeText(getApplicationContext(), "Enter text first!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_share_email:
                shareEmail();
                break;
            case R.id.btn_share_file:
                startImageChooser();
                break;
        }
    }

    private void shareText(String text) {
        ShareCompat.IntentBuilder intentBuilder = ShareCompat.IntentBuilder.from(this);
        Intent intent = intentBuilder
                .setType("text/plain")
                .setText(text)
                .createChooserIntent();

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "No client support this content", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareEmail() {
        ShareCompat.IntentBuilder intentBuilder = ShareCompat.IntentBuilder.from(this);
        Intent intent = intentBuilder
                .setType("text/html")
                .setHtmlText("<p>This is a paragraph.</p>")
                .setChooserTitle("Choose email client")
                .createChooserIntent();

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "No client support this content", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareFile(Uri imageUri) {
        Intent intent = ShareCompat.IntentBuilder.from(this)
                .setType("image/png")
                .setStream(imageUri)
                .setChooserTitle("Choose image client")
                .getIntent();

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "No client support this content", Toast.LENGTH_SHORT).show();
        }

    }

    private void startImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/png");
        startActivityForResult(intent, REQUEST_CODE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMAGE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    Uri imageUri = data.getData();
                    shareFile(imageUri);
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
