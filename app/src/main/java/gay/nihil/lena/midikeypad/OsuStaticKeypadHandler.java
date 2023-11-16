package gay.nihil.lena.midikeypad;

import android.view.MotionEvent;
import android.view.View;

public class OsuStaticKeypadHandler extends KeypadHandler {

    public OsuStaticKeypadHandler(View view) {
        super(view);
    }

    @Override
    public KeypadData HandleMotionEvent(MotionEvent event) {
        int code = 60;
        if (event.getX(event.getActionIndex()) > (view.getWidth() / 2)) {
            code = 61;
        }

        int a = event.getActionMasked();
        if (a == MotionEvent.ACTION_DOWN || a == MotionEvent.ACTION_POINTER_DOWN) {
            return new KeypadData(true, true, code);
        } else if (a == MotionEvent.ACTION_UP || a == MotionEvent.ACTION_POINTER_UP) {
            return new KeypadData(true, false, code);
        } else {
            return new KeypadData(false, false, 0);
        }
    }
}
