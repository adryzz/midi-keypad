package gay.nihil.lena.midikeypad;

import android.view.MotionEvent;
import android.view.View;

public class OsuDynamicKeypadHandler extends KeypadHandler {
    long lastEventTime;
    boolean lastKey;
    boolean key0;
    boolean key1;

    public OsuDynamicKeypadHandler(View view) {
        super(view);
        this.key0 = false;
        this.key1 = false;
        this.lastKey = false;
        this.lastEventTime = 0;
    }

    @Override
    public KeypadData HandleMotionEvent(MotionEvent event) {
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


            lastEventTime = event.getEventTime();
            return new KeypadData(true, true, code);

        } else if (event.getActionMasked() == MotionEvent.ACTION_UP) {
            if (key1) {
                code = 61;
                key1 = false;
            } else {
                key0 = false;
            }
            return new KeypadData(true, false, code);
        } else {
            return new KeypadData(false, false, 0);
        }
    }
}
