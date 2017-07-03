package com.example.nicolascarraggi.trgrd.rulesys;

import java.util.concurrent.Callable;

/**
 * Created by nicolascarraggi on 3/05/17.
 */

public class NotificationAction extends Action {

    private String title, text;
    private NotificationActionType notificationActionType;

    // SKELETON constructor
    public NotificationAction(int id, String name, int iconResource, Device device, ActionType actionType) {
        super(id, name, iconResource, device, actionType, null);
        this.actionValueType = ActionValueType.NOTIFICATION;
        this.notificationActionType = NotificationActionType.NONE;
        this.title = "";
        this.text = "";
    }

    public NotificationAction(int id, String name, int iconResource, Device device, ActionType actionType, String title, String text, Callable callable) {
        this(id, name, iconResource, device, actionType);
        this.callable = callable;
        this.actionValueType = ActionValueType.NOTIFICATION;
        this.notificationActionType = NotificationActionType.NONE;
        this.title = title;
        this.text = text;
        this.isSkeleton = false;
    }

    // copy constructor
    public NotificationAction(int id, NotificationAction notificationAction, String title, String text, Callable callable){
        this(id, notificationAction.getName(),notificationAction.getIconResource(),notificationAction.getDevice(),notificationAction.getActionType(),title, text,callable);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public NotificationActionType getNotificationActionType() {
        return notificationActionType;
    }

    public void setNotificationActionType(NotificationActionType notificationActionType) {
        this.notificationActionType = notificationActionType;
    }

    public void setCallable(Callable callable) {
        this.callable = callable;
    }

    // Defines what on click action is bound to a notification
    // MAIN starts the application in the MainActivity
    public enum NotificationActionType {
        NONE,MAIN
    }

    @Override
    public String toString() {
        return "NotificationAction{}";
    }
}
