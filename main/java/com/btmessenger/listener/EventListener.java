package com.btmessenger.listener;

import com.btmessenger.model.ErrorDataModel;

/**
 * The EventListener interface offers the library event methods to the user
 */
public interface EventListener {

    void onConnected();

    void onNotified(int notificationType);

    void onReceived(String message);

    void onErrorOccurred(ErrorDataModel errorData);

}
