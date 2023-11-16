package gay.nihil.lena.midikeypad;

import android.view.MotionEvent;
import android.view.View;

public class OsuStaticKeypadHandler extends KeypadHandler {

    int down0;
    int down1;
    public OsuStaticKeypadHandler(View view) {
        super(view);
        this.down0 = -1;
        this.down1 = -1;
    }

    @Override
    public KeypadData HandleMotionEvent(MotionEvent event) {
        int code = 60;
        int index = event.getActionIndex();
        if (event.getX(index) > (view.getWidth() / 2)) {
            code = 61;
        }

        int a = event.getActionMasked();
        if (a == MotionEvent.ACTION_DOWN || a == MotionEvent.ACTION_POINTER_DOWN) {
            if (code == 60) {
                if (down0 < 0) {
                    down0 = index;
                } else {
                    return new KeypadData(false, false, 0);
                }
            } else {
                if (down1 < 0) {
                    down1 = index;
                } else {
                    return new KeypadData(false, false, 0);
                }
            }
            return new KeypadData(true, true, code);
        } else if (a == MotionEvent.ACTION_UP || a == MotionEvent.ACTION_POINTER_UP) {
            if (code == 60) {
                if (index == down0) {
                    down0 = -1;
                    return new KeypadData(true, false, code);
                } else {
                    return new KeypadData(false, false, 0);
                }
            } else {
                if (index == down1) {
                    down1 = -1;
                    return new KeypadData(true, false, code);
                } else {
                    return new KeypadData(false, false, 0);
                }
            }
        } else {
            return new KeypadData(false, false, 0);
        }
    }
}
