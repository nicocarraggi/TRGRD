package com.example.nicolascarraggi.trgrd.rulesys;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * Created by nicolascarraggi on 4/04/17.
 */

public class Action {

    private String name;
    private Device device;
    private Callable callable;
    private boolean usedInRule;
    private Set<Rule> rules;

    public Action(String name, Device device, Callable callable) {
        this.name = name;
        this.device = device;
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

    public Device getDevice() {
        return device;
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
