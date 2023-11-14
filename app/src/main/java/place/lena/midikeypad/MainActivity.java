package place.lena.midikeypad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.midi.MidiDeviceInfo;
import android.media.midi.MidiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button startButton;
    Button showMeButton;
    Spinner mode_spinner;
    Spinner device_spinner;
    CheckBox landscapeCheckBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = findViewById(R.id.start_button);
        showMeButton = findViewById(R.id.show_me_button);
        mode_spinner = findViewById(R.id.mode_spinner);
        device_spinner = findViewById(R.id.device_spinner);
        landscapeCheckBox = findViewById(R.id.landscape_checkbox);

        startButton.setOnClickListener(startOnClickListener);
        showMeButton.setOnClickListener(showMeOnClickListener);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.modes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mode_spinner.setAdapter(adapter);
        mode_spinner.setSelection(0);
        if (!getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_MIDI)) {
            TextView text = findViewById(R.id.connect_device_text);
            text.setText(R.string.midi_unsupported_text);
            LinearLayout layout = findViewById(R.id.main_layout);
            layout.setVisibility(View.GONE);
        }
    }

    View.OnClickListener startOnClickListener = v -> {
        MidiManager m = (MidiManager)getApplicationContext().getSystemService(Context.MIDI_SERVICE);
        MidiDeviceInfo[] infos = m.getDevices();
        if (infos.length == 0) {
            Toast.makeText(getApplicationContext(), "No connected devices found.", Toast.LENGTH_LONG).show();
        }

        Intent intent = new Intent(getApplicationContext(), KeypadActivity.class);
        intent.putExtra("mode", mode_spinner.getSelectedItemPosition());
        intent.putExtra("orientation", landscapeCheckBox.isChecked());
        startActivity(intent);
    };

    View.OnClickListener showMeOnClickListener = v -> {
        Toast.makeText(getApplicationContext(), "Not implemented yet.", Toast.LENGTH_LONG).show();
    };
}