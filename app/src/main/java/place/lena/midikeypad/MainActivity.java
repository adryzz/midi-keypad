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
    }

    @Override
    protected void onResume() {
        super.onResume();

        // if MIDI is supported, fill up the spinner, otherwise hide everything and change the text
        if (getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_MIDI)) {

            MidiManager m = (MidiManager)getApplicationContext().getSystemService(Context.MIDI_SERVICE);
            MidiDeviceInfo[] infos = m.getDevices();

            if (infos.length == 0) {
                device_spinner.setEnabled(false);
                String[] data = new String[]{getString(R.string.midi_no_devices)};

                ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, data);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                device_spinner.setAdapter(adapter1);
            } else {
                device_spinner.setEnabled(true);
                MidiDeviceSpinnerInfo[] spinfos = new MidiDeviceSpinnerInfo[infos.length];
                int def = 0;
                for (int i = 0; i < infos.length; i++) {
                    MidiDeviceInfo info = infos[i];
                    String manufacturer = info.getProperties().getString("manufacturer");

                    if (manufacturer.equals("Android")) {
                        def = i;
                    }
                    spinfos[i] = new MidiDeviceSpinnerInfo(info);
                }

                ArrayAdapter<MidiDeviceSpinnerInfo> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinfos);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                device_spinner.setAdapter(adapter1);
                device_spinner.setSelection(def);
            }

        } else {
            TextView text = findViewById(R.id.connect_device_text);
            text.setText(R.string.midi_unsupported_text);
            LinearLayout layout = findViewById(R.id.main_layout);
            layout.setVisibility(View.GONE);
        }
    }

    View.OnClickListener startOnClickListener = v -> {
        if (!device_spinner.isEnabled()) {
            Toast.makeText(getApplicationContext(), getText(R.string.no_devices_cant_start_text), Toast.LENGTH_LONG).show();
            return;
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

class MidiDeviceSpinnerInfo {
    MidiDeviceInfo inner;
    String name;
    public MidiDeviceSpinnerInfo(MidiDeviceInfo info) {
        this.inner = info;
        this.name = info.getProperties().getString("name");
    }

    @Override
    public String toString() {
        return name;
    }
}