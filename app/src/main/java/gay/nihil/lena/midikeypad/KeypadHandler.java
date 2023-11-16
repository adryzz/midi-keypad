package gay.nihil.lena.midikeypad;

import android.view.MotionEvent;
import android.view.View;

public abstract class KeypadHandler {
    View view;

    public KeypadHandler(View view) {
        this.view = view;
    }

    public abstract KeypadData HandleMotionEvent(MotionEvent event);
}

