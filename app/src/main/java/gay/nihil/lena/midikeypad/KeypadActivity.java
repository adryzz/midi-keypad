package gay.nihil.lena.midikeypad;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.media.midi.MidiDevice;
import android.media.midi.MidiDeviceInfo;
import android.media.midi.MidiInputPort;
import android.media.midi.MidiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.widget.Toast;

import java.io.IOException;

import gay.nihil.lena.midikeypad.databinding.ActivityKeypadBinding;

public class KeypadActivity extends AppCompatActivity {
    private ActivityKeypadBinding binding;

    MidiInputPort port;

    MidiDevice device;

    int mode = 0;

    long lastEventTime = 0;
    boolean lastKey = false;
    boolean key0 = false;
    boolean key1 = false;

    @SuppressLint("ClickableViewAccessibility")
    View.OnTouchListener touchListener = (v, event) -> {
        if (port == null) {
            return false;
        }
        Log.i("a", "" + event.getEventTime());

        switch (mode) {
            case 0: {
                return mode0(v, event);
            }
            case 1: {
                return mode1(v, event);
            }
            case 2: {
                return mode2(v, event);
            }
        }

        return false;
    };

    boolean mode1(View v, MotionEvent event) {
        if (event.getEventTime() - lastEventTime < 200) {
            lastKey = !lastKey;
        }
        int code = 60;

        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            if (lastKey) {
                code = 61;
                key1 = true;
            } else {
                key0 = true;
            }
            sendNote(true, code);

            lastEventTime = event.getEventTime();
        } else if (event.getActionMasked() == MotionEvent.ACTION_UP) {
            if (key1) {
                code = 61;
                key1 = false;
            } else {
                key0 = false;
            }
            sendNote(false, code);
        } else {
            return false;
        }
        return true;
    }

    boolean mode0(View v, MotionEvent event) {
        int code = 60;
        if (event.getX(event.getActionIndex()) > (v.getWidth() / 2)) {
            code = 61;
        }

        int a = event.getActionMasked();
        if (a == MotionEvent.ACTION_DOWN || a == MotionEvent.ACTION_POINTER_DOWN) {
            sendNote(true, code);
        } else if (a == MotionEvent.ACTION_UP || a == MotionEvent.ACTION_POINTER_UP) {
            sendNote(false, code);
        } else {
            return false;
        }
        return true;
    }

    boolean mode2(View v, MotionEvent event) {

        float x = event.getX(event.getActionIndex());
        float width = v.getWidth();
        int code = 60;
        if (x > (width / 4) && x < (width / 2)) {
            code = 61;
        } else if (x > (width / 2) && x < ((width / 2) + (width / 4))) {
            code = 62;
        } else if (x > ((width / 2) + (width / 4))) {
            code = 63;
        }

        int a = event.getActionMasked();
        if (a == MotionEvent.ACTION_DOWN || a == MotionEvent.ACTION_POINTER_DOWN) {
            sendNote(true, code);
        } else if (a == MotionEvent.ACTION_UP || a == MotionEvent.ACTION_POINTER_UP) {
            sendNote(false, code);
        } else {
            return false;
        }
        return true;
    }

    void sendNote(boolean on, int code) {
        byte[] buffer = new byte[32];
        int numBytes = 0;
        int channel = 3; // MIDI channels 1-16 are encoded as 0-15.

        if (on) {
            buffer[numBytes++] = (byte)(0x90 + (channel - 1)); // note on
        } else {
            buffer[numBytes++] = (byte)(0x80 + (channel - 1)); // note off
        }

        buffer[numBytes++] = (byte)code; // pitch is middle C
        buffer[numBytes++] = (byte)127; // max velocity
        int offset = 0;
        // post is non-blocking
        try {
            port.send(buffer, offset, numBytes);
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Error while transmitting data", Toast.LENGTH_LONG).show();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityKeypadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            binding.fullscreenContent.getWindowInsetsController().hide(
                    WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
        }
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        binding.fullscreenContent.setOnTouchListener(touchListener);

        mode = getIntent().getIntExtra("mode", 0);

        if (getIntent().getBooleanExtra("orientation", false)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        Drawable drawable = AppCompatResources.getDrawable(this, R.drawable.mode1);
        switch (mode) {
            case 0:
                drawable = AppCompatResources.getDrawable(this, R.drawable.mode0);
                break;
            case 1:
                drawable = AppCompatResources.getDrawable(this, R.drawable.mode1);
                break;
            case 2:
                drawable = AppCompatResources.getDrawable(this, R.drawable.mode2);
                break;
        }
        binding.fullscreenContent.setImageDrawable(drawable);

        MidiManager m = (MidiManager)this.getSystemService(Context.MIDI_SERVICE);

        MidiDeviceInfo info = getIntent().getParcelableExtra("device");

        MidiDeviceInfo.PortInfo[] portInfos = info.getPorts();
        for (int j = 0; j < portInfos.length; j++) {
            String portName = portInfos[j].getName();
            if (portInfos[j].getType() == MidiDeviceInfo.PortInfo.TYPE_INPUT) {
                int finalJ = j;
                m.openDevice(info, dev -> {
                    if (dev == null) {
                        Toast.makeText(getApplicationContext(), "Could not open device " + portName, Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        port = dev.openInputPort(finalJ);
                        device = dev;
                    }}, new Handler(Looper.getMainLooper()));
                return;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (port != null) {
                port.close();
            }
            if (device != null) {
                device.close();
            }
        } catch (IOException e) {
            // we dont give a shit
        }
    }
}