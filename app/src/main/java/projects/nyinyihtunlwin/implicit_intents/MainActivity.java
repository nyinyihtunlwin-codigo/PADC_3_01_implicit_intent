package projects.nyinyihtunlwin.implicit_intents;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnAlarm, btnTimer, btnCalendar, btnPhone, btnCamera, btnContact, btnEmail, btnMap, btnShare;
    static final int REQUEST_SELECT_CONTACT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViews();
        btnAlarm.setOnClickListener(this);
        btnTimer.setOnClickListener(this);
        btnCamera.setOnClickListener(this);
        btnCalendar.setOnClickListener(this);
        btnPhone.setOnClickListener(this);
        btnContact.setOnClickListener(this);
        btnEmail.setOnClickListener(this);
        btnMap.setOnClickListener(this);
        btnShare.setOnClickListener(this);
    }

    private void initializeViews() {
        btnAlarm = (Button) findViewById(R.id.btn_alarm);
        btnTimer = (Button) findViewById(R.id.btn_timer);
        btnCalendar = (Button) findViewById(R.id.btn_calendar);
        btnPhone = (Button) findViewById(R.id.btn_phone);
        btnCamera = (Button) findViewById(R.id.btn_camera);
        btnContact = (Button) findViewById(R.id.btn_contact);
        btnEmail = (Button) findViewById(R.id.btn_email);
        btnMap = (Button) findViewById(R.id.btn_maps);
        btnShare = (Button) findViewById(R.id.btn_share);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_alarm:
                createAlarm("Hello Nyi Nyi", 12, 11);
                break;
            case R.id.btn_timer:
                startTimer("Timer is starting!", 10);
                break;
            case R.id.btn_calendar:
                addEvent("Party", "Yangon", 1, 2);
                break;
            case R.id.btn_phone:
                dialPhoneNumber("09796841952");
                break;
            case R.id.btn_camera:
                Intent intent = CameraActivity.newIntent(getApplicationContext());
                startActivity(intent);
                break;
            case R.id.btn_contact:
                selectContact();
                break;
            case R.id.btn_email:
                String[] address = {"nnhl.mgk@gmail.com"};
                Log.e("email : ", "yes");
                composeEmail(address, "Hello Buddy", null);
                break;
            case R.id.btn_maps:
                showMap(Uri.parse("geo:0,0?q=34.99,-106.61(Treasure)"));
                break;
            case R.id.btn_share:
                Intent inten = ShareActivity.newIntent(getApplicationContext());
                startActivity(inten);
                break;
        }
    }

    public void dialPhoneNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void showMap(Uri geoLocation) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void addEvent(String title, String location, long begin, long end) {
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.Events.TITLE, title)
                .putExtra(CalendarContract.Events.EVENT_LOCATION, location)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, begin)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, end);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void composeEmail(String[] addresses, String subject, Uri attachment) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_STREAM, attachment);
        Intent chooser = Intent.createChooser(intent, "Choose App");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(chooser);
        }
    }

    public void selectContact() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_SELECT_CONTACT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    public void createAlarm(String message, int hour, int minutes) {
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                .putExtra(AlarmClock.EXTRA_MESSAGE, message)
                .putExtra(AlarmClock.EXTRA_HOUR, hour)
                .putExtra(AlarmClock.EXTRA_MINUTES, minutes);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void startTimer(String message, int seconds) {
        Intent intent = new Intent(AlarmClock.ACTION_SET_TIMER)
                .putExtra(AlarmClock.EXTRA_MESSAGE, message)
                .putExtra(AlarmClock.EXTRA_LENGTH, seconds)
                .putExtra(AlarmClock.EXTRA_SKIP_UI, true);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
