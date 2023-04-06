package place.lena.midikeypad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.midi.MidiDeviceInfo;
import android.media.midi.MidiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button startButton = findViewById(R.id.start_button);
        final Button showMeButton = findViewById(R.id.show_me_button);

        startButton.setOnClickListener(startOnClickListener);
        showMeButton.setOnClickListener(showMeOnClickListener);
    }

    View.OnClickListener startOnClickListener = v -> {
        if (!getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_MIDI)) {
            Toast.makeText(getApplicationContext(), "Unfortunately your device does not support MIDI.", Toast.LENGTH_LONG).show();
        }
        MidiManager m = (MidiManager)getApplicationContext().getSystemService(Context.MIDI_SERVICE);
        MidiDeviceInfo[] infos = m.getDevices();
        if (infos.length == 0) {
            Toast.makeText(getApplicationContext(), "No connected devices found.", Toast.LENGTH_LONG).show();
        }

        Intent intent = new Intent(getApplicationContext(), KeypadActivity.class);
        startActivity(intent);
    };

    View.OnClickListener showMeOnClickListener = v -> {
        Toast.makeText(getApplicationContext(), "Not implemented yet.", Toast.LENGTH_LONG).show();
    };
}