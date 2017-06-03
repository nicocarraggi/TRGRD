package com.example.nicolascarraggi.trgrd.rulesys;

/**
 * Created by nicolascarraggi on 2/06/17.
 */

public class InputAction {

    private int id;
    private String description;
    private String name;
    private int iconResource;
    private InputActionEvent inputActionEvent;

    public InputAction(int id, String description, String name, int iconResource) {
        this.id = id;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
                ", description='" + description + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
