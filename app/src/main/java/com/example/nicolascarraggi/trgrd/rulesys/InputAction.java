package com.example.nicolascarraggi.trgrd.rulesys;

/**
 * Created by nicolascarraggi on 2/06/17.
 */

public class InputAction {

    private int id;
    private String name;
    private int iconResource;
    private InputActionEvent inputActionEvent;

    public InputAction(int id, String name, int iconResource) {
        this.id = id;
        this.name = name;
        this.iconResource = iconResource;
        this.inputActionEvent = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIconResource() {
        return iconResource;
    }

    public void setIconResource(int iconResource) {
        this.iconResource = iconResource;
    }

    public InputActionEvent getInputActionEvent() {
        return inputActionEvent;
    }

    public void setInputActionEvent(InputActionEvent inputActionEvent) {
        this.inputActionEvent = inputActionEvent;
    }

    @Override
    public String toString() {
        return "InputAction{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
