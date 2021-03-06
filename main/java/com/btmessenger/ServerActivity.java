package com.btmessenger;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.btmessenger.adapter.ConversationAdapter;
import com.btmessenger.dialog.ErrorAlertDialog;
import com.btmessenger.listener.EventListener;
import com.btmessenger.model.ConversationModel;
import com.btmessenger.model.ErrorDataModel;
import com.btmessenger.server.ServerConnectivity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * The ServerActivity is a starting point of server side activity
 */
public class ServerActivity extends AppCompatActivity implements ErrorAlertDialog.AlertDialogEventListener {

    /**
     * Variables
     **/
    private Context context;
    private EditText etMessageField;
    private ImageButton ibMessageSend;
    private RecyclerView recyclerView;
    private ProgressBar pbLoadingDialog;
    private ErrorAlertDialog errorAlertDialog;
    private ServerConnectivity serverConnectivity;
    private ConversationAdapter conversationAdapter;
    private MediaPlayer messageSound, notifySound;

    /**
     * Activity Life Cycle
     **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        context = getApplicationContext();

        //Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.action_bar_name));

        // Object Initialization
        recyclerView = findViewById(R.id.rv_conversation);
        etMessageField = findViewById(R.id.et_message_field);
        ibMessageSend = findViewById(R.id.ib_send);
        pbLoadingDialog = findViewById(R.id.pb_loading_sign);
        notifySound = MediaPlayer.create(context, R.raw.login_sound);
        messageSound = MediaPlayer.create(context, R.raw.incoming_sound);

        // Layout Setup
        List<ConversationModel> conversationList = new ArrayList<>();
        conversationAdapter = new ConversationAdapter(this, conversationList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(conversationAdapter);

        // Responds
        ibMessageSend.setOnClickListener(view -> {

            String message = etMessageField.getText().toString();
            if (!TextUtils.isEmpty(message)) {
                resetMessageField();
                serverConnectivity.sendMessage(message);

                conversationAdapter.addOutgoingMessage(message);
                recyclerView.scrollToPosition(conversationAdapter.getItemCount() - 1);
            }

        });

        etMessageField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String message = charSequence.toString();
                if (message.isEmpty()) {
                    DrawableCompat.setTint(ibMessageSend.getDrawable(), ContextCompat.getColor(context, R.color.black));
                    ibMessageSend.setBackgroundResource(R.drawable.shape_inactive_send_button_circle);
                } else {
                    DrawableCompat.setTint(ibMessageSend.getDrawable(), ContextCompat.getColor(context, R.color.purple_200));
                    ibMessageSend.setBackgroundResource(R.drawable.shape_active_send_button_circle);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // Connect to server
        EventListener eventListener = new EventListener() {

            @Override
            public void onConnected() {
                runOnUiThread(() -> pbLoadingDialog.setVisibility(View.GONE));
            }

            @Override
            public void onNotified(final int notificationType) {
                runOnUiThread(() -> {
                    notifySound.start();
                    conversationAdapter.addNotification(notificationType);
                    recyclerView.scrollToPosition(conversationAdapter.getItemCount() - 1);
                });
            }

            @Override
            public void onReceived(final String message) {
                runOnUiThread(() -> {
                    messageSound.start();
                    conversationAdapter.addIncomingMessage(message);
                    recyclerView.scrollToPosition(conversationAdapter.getItemCount() - 1);
                });
            }

            @Override
            public void onErrorOccurred(ErrorDataModel errorData) {
                String errorMessage = errorData.getErrorMessage();
                errorAlertDialog = ErrorAlertDialog.newInstance(errorMessage, ServerActivity.this);
                errorAlertDialog.show(getSupportFragmentManager(), "ERROR_ALERT");
            }
        };
        serverConnectivity = new ServerConnectivity(this, eventListener);

    }

    /**
     * Methods
     **/
    private void resetMessageField() {
        etMessageField.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Set color tint on icon
        Drawable exitDrawable = menu.findItem(R.id.action_exit).getIcon();
        exitDrawable = DrawableCompat.wrap(exitDrawable);
        DrawableCompat.setTint(exitDrawable, Color.WHITE);
        menu.findItem(R.id.action_exit).setIcon(exitDrawable);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_exit) {
            serverConnectivity.onDestroy();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCloseClicked() {
        serverConnectivity.onDestroy();
        finish();
    }
}