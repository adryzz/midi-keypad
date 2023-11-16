package gay.nihil.lena.midikeypad;

import android.view.MotionEvent;
import android.view.View;

public class ManiaStaticKeypadHandler extends KeypadHandler {
    int down0;
    int down1;
    int down2;
    int down3;
    public ManiaStaticKeypadHandler(View view) {
        super(view);
        this.down0 = -1;
        this.down1 = -1;
        this.down2 = -1;
        this.down3 = -1;
    }

    @Override
    public KeypadData HandleMotionEvent(MotionEvent event) {
        int index = event.getActionIndex();
        float x = event.getX(index);
        float width = view.getWidth();
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
            switch (code) {
                case 60:
                    if (down0 < 0) {
                        down0 = index;
                    } else {
                        return new KeypadData(false, false, 0);
                    }
                    break;
                case 61:
                    if (down1 < 0) {
                        down1 = index;
                    } else {
                        return new KeypadData(false, false, 0);
                    }
                    break;
                case 62:
                    if (down2 < 0) {
                        down2 = index;
                    } else {
                        return new KeypadData(false, false, 0);
                    }
                    break;
                case 63:
                    if (down3 < 0) {
                        down3 = index;
                    } else {
                        return new KeypadData(false, false, 0);
                    }
                    break;
                default:
                    return new KeypadData(false, false, 0);
            }
            return new KeypadData(true, true, code);
        } else if (a == MotionEvent.ACTION_UP || a == MotionEvent.ACTION_POINTER_UP) {
            switch (code) {
                case 60:
                    if (index == down0) {
                        down0 = -1;
                        return new KeypadData(true, false, code);
                    } else {
                        return new KeypadData(false, false, 0);
                    }
                case 61:
                    if (index == down1) {
                        down1 = -1;
                        return new KeypadData(true, false, code);
                    } else {
                        return new KeypadData(false, false, 0);
                    }
                case 62:
                    if (index == down2) {
                        down2 = -1;
                        return new KeypadData(true, false, code);
                    } else {
                        return new KeypadData(false, false, 0);
                    }
                case 63:
                    if (index == down3) {
                        down3 = -1;
                        return new KeypadData(true, false, code);
                    } else {
                        return new KeypadData(false, false, 0);
                    }
                default:
                    return new KeypadData(false, false, 0);
            }
        } else {
            return new KeypadData(false, false, 0);
        }
    }
}
