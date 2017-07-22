package com.example.nicolascarraggi.trgrd.rulesys;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * Created by nicolascarraggi on 4/04/17.
 */

public class Action {

    protected int id;
    protected String name;
    protected int iconResource;
    protected ActionValueType actionValueType;
    protected Device device;
    protected ActionType actionType;
    protected Callable callable;
    protected boolean usedInRule;
    protected boolean isSkeleton;
    protected Set<Rule> rules;

    public Action(int id, String name, int iconResource, Device device, ActionType actionType, Callable callable) {
        this.id = id;
        this.name = name;
        this.iconResource = iconResource;
        this.actionValueType = ActionValueType.NONE;
        this.device = device;
        this.actionType = actionType;
        actionType.addAction(this);
        this.callable = callable;
        this.usedInRule = false;
        this.isSkeleton = true;
        this.rules = new HashSet<>();
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

    public Object getActionValueType() {
        return actionValueType;
    }

    public Callable getCallable() {
        return callable;
    }

    public void setCallable(Callable callable) {
        this.callable = callable;
    }

    public boolean isNotificationAction() {
        return actionValueType == ActionValueType.NOTIFICATION;
    }

    public boolean isSkeleton() {
        return isSkeleton;
    }

    public boolean isScoreValueAction() {
        return actionValueType == ActionValueType.SCOREVALUE;
    }

    public boolean isSendMessageAction() {
        return actionValueType == ActionValueType.SENDMESSAGE;
    }

    public boolean isSendMessageCallerAction() {
        return actionValueType == ActionValueType.SENDMESSAGECALLER;
    }

    public enum ActionValueType {
        NONE, VALUE, NOTIFICATION, SCOREVALUE, SENDMESSAGE, SENDMESSAGECALLER
    }
}
