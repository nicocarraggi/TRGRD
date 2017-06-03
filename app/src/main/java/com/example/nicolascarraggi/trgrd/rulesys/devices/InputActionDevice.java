package com.example.nicolascarraggi.trgrd.rulesys.devices;

import com.example.nicolascarraggi.trgrd.rulesys.InputAction;
import com.example.nicolascarraggi.trgrd.rulesys.InputActionEvent;

import java.util.ArrayList;

/**
 * Created by nicolascarraggi on 3/06/17.
 */

public interface InputActionDevice {
    ArrayList<InputAction> getInputActions();
    InputActionEvent getInputActionEvent(InputAction inputAction);
}
