package com.btmessenger.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.btmessenger.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * The ErrorAlertDialog class handles the error message dialog in the library
 */
public class ErrorAlertDialog extends DialogFragment {

    /**
     * Variables
     **/
    private final String message;
    private final AlertDialogEventListener eventListener;

    /**
     * Constructor
     **/
    public ErrorAlertDialog(String message, AlertDialogEventListener eventListener) {
        this.message = message;
        this.eventListener = eventListener;
    }

    /**
     * Methods
     **/
    public static ErrorAlertDialog newInstance(String message, AlertDialogEventListener alertDialogEventListener) {
        return new ErrorAlertDialog(message, alertDialogEventListener);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.alert_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Object Initialization
        final TextView tvErrorMessage = view.findViewById(R.id.tv_error_message);
        Button btClose = view.findViewById(R.id.bt_close_button);

        // Populate Data
        tvErrorMessage.setText(message);

        // Responds
        btClose.setOnClickListener(view1 -> {
            dismiss();
            eventListener.onCloseClicked();
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Interface
     **/
    public interface AlertDialogEventListener {
        void onCloseClicked();
    }
}
