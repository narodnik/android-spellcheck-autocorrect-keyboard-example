package com.example.simple;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.SurfaceView;
import android.view.MotionEvent;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputContentInfo;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.BaseInputConnection;
import android.content.Context;
import android.view.Window;
import android.text.InputType;
import android.text.Editable;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;

class CustomInputConnection extends BaseInputConnection {

    public CustomInputConnection(View view, boolean fullEditor) {
        super(view, fullEditor);
    }

    /*
    @Override
    public boolean sendKeyEvent(KeyEvent event) {
        Log.i("darkfi", "got key event");
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            char keyChar = (char)event.getUnicodeChar();
            Log.i("darkfi", "key event: " + keyChar);
        }
        return super.sendKeyEvent(event);
    }
    */

    @Override
    public boolean commitText(CharSequence text, int newCursorPosition) {
        Log.i("darkfi", String.format("commitText(%s, %d)", text.toString(), newCursorPosition));
        return true;
    }

    @Override
    public boolean setSelection(int start, int end) {
        Log.i("darkfi", "setSelection");
        return true;
    }

    @Override
    public boolean endBatchEdit() {
        String curr = getTextBeforeCursor(100, 0).toString();
        Log.i("darkfi", "endBatchEdit: " + curr);
        return super.endBatchEdit();
    }
}

class CustomView
    extends
        View
    implements
        View.OnKeyListener,
        View.OnTouchListener {

    private InputMethodManager imm;

    public CustomView(Context context, InputMethodManager imm){
        super(context);

        this.imm = imm;

        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
        setOnTouchListener(this);
        setOnKeyListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.d("darkfi", "onTouch() v1");
        imm.showSoftInput(this, 0);
        return true;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        Log.d("darkfi", "onKey() v1 " + keyCode);
        return true;
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        outAttrs.inputType = InputType.TYPE_CLASS_TEXT;
        outAttrs.imeOptions = EditorInfo.IME_ACTION_DONE;
        // fullEditor is false, but we might set this to true for enabling
        // text selection, and copy/paste. Lets see.
        return new CustomInputConnection(this, true);
    }
}

public class MainActivity extends Activity {
    private CustomView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("darkfi", "onCreate()");

        super.onCreate(savedInstanceState);

        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.showSoftInput(view, 0);
        //imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 1);

        view = new CustomView(this, imm);
        setContentView(view);
    }
}
