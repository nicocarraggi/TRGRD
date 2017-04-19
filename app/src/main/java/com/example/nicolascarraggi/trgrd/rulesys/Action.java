package com.example.nicolascarraggi.trgrd.rulesys;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * Created by nicolascarraggi on 4/04/17.
 */

public class Action {

    private String name;
    private int iconResource;
    private Device device;
    private ActionType actionType;
    private Callable callable;
    private boolean usedInRule;
    private Set<Rule> rules;

    public Action(String name, int iconResource, Device device, ActionType actionType, Callable callable) {
        this.name = name;
        this.iconResource = iconResource;
        this.device = device;
        this.actionType = actionType;
        this.callable = callable;
        this.usedInRule = false;
        this.rules = new HashSet<>();
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

    public Device getDevice() {
        return device;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public Set<Rule> getRules() {
        return rules;
    }

    public boolean isUsedInRule() {
        return usedInRule;
    }

    public synchronized void addRule(Rule rule){
        rules.add(rule);
        this.usedInRule = true;
    }

    public synchronized void removeRule(Rule rule){
        rules.remove(rule);
        if(rules.isEmpty()) this.usedInRule = false;
    }

    public void execute(Rule rule) throws Exception {
        System.out.println("[Action] "+name+" by rule "+rule.getName());
        callable.call();
    }

}
