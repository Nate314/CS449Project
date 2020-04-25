package com.nathangawith.umkc.financeapp.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import java.util.function.Consumer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class MyDialog extends AppCompatDialogFragment {

    private String title;
    private String text;
    private String buttons[];
    private Consumer<String> buttonClickedConsumer;

    public MyDialog(String title) {
        this.title = title;
    }

    public MyDialog(String title, String text) {
        this.title = title;
        this.text = text;
        this.buttons = new String[] { "OK" };
    }

    public MyDialog(String title, String text, String[] buttons, Consumer<String> buttonClickedConsumer) {
        this.title = title;
        this.text = text;
        this.buttons = buttons;
        this.buttonClickedConsumer = buttonClickedConsumer;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(this.title).setMessage(this.text);
        for (int i = 0; i < this.buttons.length; i++) {
            MyDialog me = this;
            final int index = i;
            DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (me.buttonClickedConsumer != null) {
                        me.buttonClickedConsumer.accept(me.buttons[index]);
                    }
                }
            };
            String btnText = this.buttons[i];
            if (i == 0) {
                builder.setPositiveButton(btnText, listener);
            } else if (i < this.buttons.length - 1) {
                builder.setNeutralButton(btnText, listener);
            } else {
                builder.setNegativeButton(btnText, listener);
            }
        }
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
}
