package place.lena.midikeypad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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

    View.OnClickListener startOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_MIDI)) {
                Toast.makeText(getApplicationContext(), "Unfortunately your device does not support MIDI.", Toast.LENGTH_LONG).show();
            }
            MidiManager m = (MidiManager)getApplicationContext().getSystemService(Context.MIDI_SERVICE);
            MidiDeviceInfo[] infos = m.getDevices();
            if (infos.length == 0) {
                Toast.makeText(getApplicationContext(), "No connected devices found.", Toast.LENGTH_LONG).show();
            }
        }
    };

    View.OnClickListener showMeOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
}