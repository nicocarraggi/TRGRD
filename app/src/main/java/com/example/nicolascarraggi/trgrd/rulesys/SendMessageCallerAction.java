package com.example.nicolascarraggi.trgrd.rulesys;

import java.util.concurrent.Callable;

/**
 * Created by nicolascarraggi on 3/05/17.
 */

public class SendMessageCallerAction extends Action {

    private String message;

    // SKELETON constructor
    public SendMessageCallerAction(int id, String name, int iconResource, Device device, ActionType actionType) {
        super(id, name, iconResource, device, actionType, null);
        this.actionValueType = ActionValueType.SENDMESSAGECALLER;
        this.message = "";
    }

    public SendMessageCallerAction(int id, String name, int iconResource, Device device, ActionType actionType, String message, Callable callable) {
        this(id, name, iconResource, device, actionType);
        this.callable = callable;
        this.message = message;
        this.isSkeleton = false;
    }

    // copy constructor
    public SendMessageCallerAction(int id, SendMessageCallerAction sendMessageAction, String message, Callable callable){
        this(id, sendMessageAction.getName(),sendMessageAction.getIconResource(),sendMessageAction.getDevice(),sendMessageAction.getActionType(), message, callable);
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
        return "SendMessageCallerAction{" +
                "message='" + message + '\'' +
                '}';
    }
}
