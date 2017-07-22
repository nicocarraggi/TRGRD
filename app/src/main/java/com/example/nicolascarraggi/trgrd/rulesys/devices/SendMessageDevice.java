package com.example.nicolascarraggi.trgrd.rulesys.devices;

import com.example.nicolascarraggi.trgrd.rulesys.NotificationAction;
import com.example.nicolascarraggi.trgrd.rulesys.SendMessageAction;

import java.util.concurrent.Callable;

/**
 * Created by nicolascarraggi on 5/05/17.
 */

public interface SendMessageDevice {

    SendMessageAction getSendMessageAction(String phonenumber, String message);

    void editSendMessageAction(SendMessageAction sendMessageAction, String phonenumber, String message);

    Callable getSendMessageCallable(final String phonenumber, final String message);

    void acSendMessage(String phonenumber, String message);

}
