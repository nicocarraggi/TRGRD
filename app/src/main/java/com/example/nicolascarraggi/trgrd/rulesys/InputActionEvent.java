package com.example.nicolascarraggi.trgrd.rulesys;

/**
 * Created by nicolascarraggi on 26/04/17.
 *
 * An event that occurs when an input action happens on a device.
 *
 */

public class InputActionEvent extends Event {

    private InputAction inputAction;

    // SKELETON constructor
    public InputActionEvent(int id, String name, int iconResource, Device device, EventType eventType) {
        super(id, name, iconResource, device, eventType);
        this.eventValueType = EventValueType.INPUTACTION;
        this.inputAction = null;
    }

    public InputActionEvent(int id, String name, int iconResource, Device device, EventType eventType, InputAction inputAction) {
        this(id, name, iconResource, device, eventType);
        this.inputAction = inputAction;
        this.inputAction.setInputActionEvent(this);
        this.isSkeleton=false;
    }

    // copy constructor
    public InputActionEvent(int id, InputActionEvent inputActionEvent, InputAction inputAction){
        this(id, inputActionEvent.getName(),inputActionEvent.getIconResource(),inputActionEvent.getDevice(),inputActionEvent.getEventType(),inputAction);
    }

    public InputAction getInputAction() {
        return inputAction;
    }

    public void setInputAction(InputAction inputAction) {
        this.inputAction = inputAction;
    }
}
