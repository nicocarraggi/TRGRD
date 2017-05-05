package com.example.nicolascarraggi.trgrd.rulesys.devices;

import com.example.nicolascarraggi.trgrd.rulesys.NotificationAction;

import java.util.concurrent.Callable;

/**
 * Created by nicolascarraggi on 5/05/17.
 */

public interface NotificationDevice {

    NotificationAction getNotifyAction(String title, String text, NotificationAction.NotificationActionType type);

    void editNotifyAction(NotificationAction notificationAction, String title, String text);

    Callable getNotifyCallable(final String title, final String text, final NotificationAction.NotificationActionType type);

    void acNotify(String title, String text, NotificationAction.NotificationActionType type);

}
