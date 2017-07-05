package com.example.nicolascarraggi.trgrd.rulesys;

/**
 * Created by nicolascarraggi on 26/04/17.
 *
 * An event that occurs when an input action happens on a device.
 *
 */

public class InputActionEvent extends Event {

    private InputAction inputAction;
    private String type;

    // SKELETON constructor
    public InputActionEvent(int id, String name, String type, int iconResource, Device device, EventType eventType, RuleEngine ruleEngine) {
        super(id, name, iconResource, device, eventType, ruleEngine);
        this.eventValueType = EventValueType.INPUTACTION;
        this.type = type;
        this.inputAction = null;
    }

    public InputActionEvent(int id, String name, String type, int iconResource, Device device, EventType eventType, InputAction inputAction, RuleEngine ruleEngine) {
        this(id, name, type, iconResource, device, eventType, ruleEngine);
        this.inputAction = inputAction;
        this.inputAction.setInputActionEvent(this);
        this.isSkeleton=false;
    }

    // copy constructor
    public InputActionEvent(int id, InputActionEvent inputActionEvent, InputAction inputAction){
        this(id, inputActionEvent.getName(), inputActionEvent.getInputActionType(), inputActionEvent.getIconResource(),inputActionEvent.getDevice(),inputActionEvent.getEventType(),inputAction, inputActionEvent.getRuleEngine());
    }

    public InputAction getInputAction() {
        return inputAction;
    }

    public void setInputAction(InputAction inputAction) {
        this.inputAction = inputAction;
    }

    public String getInputActionType() {
        return type;
    }

    public void setInputActionType(String type) {
        this.type = type;
    }
}
