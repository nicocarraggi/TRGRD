package com.example.nicolascarraggi.trgrd.rulesys;

import java.util.concurrent.Callable;

/**
 * Created by nicolascarraggi on 3/05/17.
 */

public class NotificationAction extends Action {

    private String text;
    private NotificationActionType notificationActionType;

    // SKELETON constructor
    public NotificationAction(int id, String name, int iconResource, Device device, ActionType actionType, Callable callable) {
        super(id, name, iconResource, device, actionType, callable);
        this.actionValueType = ActionValueType.NOTIFICATION;
        this.text = "";
        this.notificationActionType = null;
    }

    public NotificationAction(int id, String name, int iconResource, Device device, ActionType actionType, Callable callable, String text) {
        super(id, name, iconResource, device, actionType, callable);
        this.text = text;
        this.isSkeleton = false;
    }

    // copy constructor
    public NotificationAction(int id, NotificationAction notificationAction, String text){
        this(id, notificationAction.getName(),notificationAction.getIconResource(),notificationAction.getDevice(),notificationAction.getActionType(),notificationAction.getCallable(), text);
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

    // Defines what on click action is bound to a notification
    // MAIN starts the application in the MainActivity
    public enum NotificationActionType {
        NONE,MAIN
    }

}
