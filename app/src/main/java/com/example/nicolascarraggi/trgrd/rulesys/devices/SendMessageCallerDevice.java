package com.example.nicolascarraggi.trgrd.rulesys.devices;

import com.example.nicolascarraggi.trgrd.rulesys.SendMessageCallerAction;

import java.util.concurrent.Callable;

/**
 * Created by nicolascarraggi on 5/05/17.
 */

public interface SendMessageCallerDevice {

    SendMessageCallerAction getSendMessageCallerAction(String message);

    void editSendMessageCallerAction(SendMessageCallerAction sendMessageCallerAction, String message);

    Callable getSendMessageCallerCallable(final String message);

    void acSendMessageCaller(String message);

}
