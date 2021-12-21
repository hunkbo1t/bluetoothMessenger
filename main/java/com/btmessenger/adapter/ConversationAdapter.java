package com.btmessenger.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.btmessenger.R;
import com.btmessenger.commands.IncomingType;
import com.btmessenger.commands.Notification;
import com.btmessenger.model.ConversationModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

/**
 * The ConversationAdapter class helps to create user conversation chat list
 */
public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ViewHolder> {

    /**
     * Variables
     **/
    @SuppressLint("SimpleDateFormat")
    private final DateFormat DATE_TIME_FORMAT = new SimpleDateFormat("KK:mm aaa");
    private final Context context;
    private final List<ConversationModel> conversationList;
    private final int screenWidth;

    /**
     * Constructor
     **/
    public ConversationAdapter(Activity activity, List<ConversationModel> conversationList) {
        this.context = activity.getBaseContext();
        this.conversationList = conversationList;

        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
    }

    /**
     * Methods
     **/
    @Override
    public int getItemViewType(int position) {
        ConversationModel conversationModel = conversationList.get(position);
        int messageType = conversationModel.getMessageType();

        if (messageType == 0)
            return 0;
        else {
            boolean isOutGoingMessage = conversationModel.isOutGoingMessage();
            if (isOutGoingMessage)
                return 1;
            else
                return 2;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View itemView;
        switch (viewType) {

            case 0:
                itemView = layoutInflater.inflate(R.layout.recyclerview_conversation_notification_item, parent, false);
                break;
            case 1:
                itemView = layoutInflater.inflate(R.layout.recyclerview_conversation_outgoing_item, parent, false);
                break;
            default:
                itemView = layoutInflater.inflate(R.layout.recyclerview_conversation_incoming_item, parent, false);
        }
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ConversationModel conversationData = conversationList.get(position);

        String message = conversationData.getMessage();
        Date time = conversationData.getDate();
        holder.tvDateTime.setText(DATE_TIME_FORMAT.format(time));

        int messageType = conversationData.getMessageType();
        boolean isOutGoingMessage = conversationData.isOutGoingMessage();

        if (messageType != IncomingType.NOTIFICATION) {

            holder.tvMessage.setMaxWidth(getConversationMaxWidth());

        }
        holder.tvMessage.setText(message);

    }

    @Override
    public int getItemCount() {
        return conversationList.size();
    }

    public void addNotification(int notificationType) {

        String message = "";
        if (notificationType == Notification.NOTIFICATION_ROOM_ESTABLISH)
            message = context.getString(R.string.notification_room_establish);
        else if (notificationType == Notification.NOTIFICATION_SELF_ENTER)
            message = context.getString(R.string.notification_self_enter);
        else if (notificationType == Notification.NOTIFICATION_ENTER)
            message = "Guest " + context.getString(R.string.notification_enter);
        else if (notificationType == Notification.NOTIFICATION_LEAVE)
            message = "Guest " + context.getString(R.string.notification_leave);

        ConversationModel conversationData = new ConversationModel();
        conversationData.setMessageType(0);
        conversationData.setOutGoingMessage(false);
        conversationData.setMessage(message);
        conversationData.setDate(generateCurrentDateTime());
        conversationList.add(conversationData);

        int lastInsertedIndex = conversationList.size() - 1;
        notifyItemInserted(lastInsertedIndex);

    }

    public void addIncomingMessage(String message) {

        ConversationModel conversationData = new ConversationModel();
        conversationData.setMessageType(1);
        conversationData.setOutGoingMessage(false);
        conversationData.setMessage(message);
        conversationData.setDate(generateCurrentDateTime());
        conversationList.add(conversationData);

        int lastInsertedIndex = conversationList.size() - 1;
        notifyItemInserted(lastInsertedIndex);

    }

    public void addOutgoingMessage(String message) {

        ConversationModel conversationData = new ConversationModel();
        conversationData.setMessageType(1);
        conversationData.setOutGoingMessage(true);
        conversationData.setMessage(message);
        conversationData.setDate(generateCurrentDateTime());
        conversationList.add(conversationData);

        int lastInsertedIndex = conversationList.size() - 1;
        notifyItemInserted(lastInsertedIndex);

    }

    private Date generateCurrentDateTime() {
        return new Date();
    }

    private int getConversationMaxWidth() {
        return (int) (screenWidth / 1.5);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ConstraintLayout clContainer;
        public RelativeLayout rlMessageBox;
        public TextView tvMessage, tvDateTime;

        public ViewHolder(View itemView) {
            super(itemView);
            clContainer = itemView.findViewById(R.id.cl_container);
            rlMessageBox = itemView.findViewById(R.id.rl_message_container);
            tvMessage = itemView.findViewById(R.id.tv_message);
            tvDateTime = itemView.findViewById(R.id.tv_date_time);
        }

    }
}
