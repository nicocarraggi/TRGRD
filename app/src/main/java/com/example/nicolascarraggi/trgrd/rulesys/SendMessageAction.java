package com.example.nicolascarraggi.trgrd.rulesys;

import java.util.concurrent.Callable;

/**
 * Created by nicolascarraggi on 3/05/17.
 */

public class SendMessageAction extends Action {

    private String phonenumber;
    private String message;

    // SKELETON constructor
    public SendMessageAction(int id, String name, int iconResource, Device device, ActionType actionType) {
        super(id, name, iconResource, device, actionType, null);
        this.actionValueType = ActionValueType.SENDMESSAGE;
        this.phonenumber = "";
        this.message = "";
    }

    public SendMessageAction(int id, String name, int iconResource, Device device, ActionType actionType, String phonenumber, String message, Callable callable) {
        this(id, name, iconResource, device, actionType);
        this.callable = callable;
        this.phonenumber = phonenumber;
        this.message = message;
        this.isSkeleton = false;
    }

    // copy constructor
    public SendMessageAction(int id, SendMessageAction sendMessageAction, String phonenumber, String message, Callable callable){
        this(id, sendMessageAction.getName(),sendMessageAction.getIconResource(),sendMessageAction.getDevice(),sendMessageAction.getActionType(), phonenumber, message, callable);
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCallable(Callable callable) {
        this.callable = callable;
    }

    @Override
    public String toString() {
        return "SendMessageAction{" +
                "phonenumber='" + phonenumber + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
