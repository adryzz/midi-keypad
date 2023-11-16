package gay.nihil.lena.midikeypad;

public class KeypadData {
    boolean handled;
    boolean on;
    int code;

    public KeypadData(boolean handled, boolean on, int code) {
        this.handled = handled;
        this.on = on;
        this.code = code;
    }
}
