package com.example.nicolascarraggi.trgrd.rulesys;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by nicolascarraggi on 4/04/17.
 */

public class Rule {

    private String name;
    private boolean active;
    private Set<Event> events;
    private Set<State> states;
    private Set<Action> actions;
    private int times;

    public Rule(String name) {
        this.name = name;
        this.active = false;
        this.events = new HashSet<>();
        this.states = new HashSet<>();
        this.actions = new HashSet<>();
        this.times = 0;
    }

    private void executeRule(){
        if(!actions.isEmpty()) { // TODO error if no actions?
            for(Action a: actions){
                try {
                    a.execute(this);
                } catch (Exception e) {
                    // Action Callable Throws Error!
                    e.printStackTrace();
                }
            }
            times += 1;
        }
    }

    public void checkRule(){
        if(active){
            if(!states.isEmpty()){
                // If event and states, check if all states are true !
                boolean isRuleTrue = true;
                for(State s: states){
                    // If 1 state is false, stop the loop!
                    if(!s.isState()){
                        isRuleTrue = false;
                        break;
                    }
                }
                if (isRuleTrue) executeRule();
            } else {
                // If event and no states, rule is executed !
                executeRule();
            }
        }
    }

    public void eventTriggered(Event e){
        System.out.println("[Rule] Event "+e.getName()+" triggered!");
        checkRule();
    }

    public void stateChanged(State s){
        System.out.println("[Rule] State "+s.getName()+" triggered with state "+s.isState()+"!");
        // If there isn't an event, check rule !
        if(events.isEmpty()) checkRule();
    }

    public void addEvent(Event e){
        events.add(e);
        e.addRule(this);
    }

    public void removeEvent(Event e){
        events.remove(e);
        e.removeRule(this);
    }

    public void addState(State s){
        states.add(s);
        s.addRule(this);
        checkRule(); // TODO too much automation? Should be done manually?
    }

    public void removeState(State s){
        states.remove(s);
        s.removeRule(this);
        checkRule(); // TODO too much automation? Should be done manually?
    }

    public void addAction(Action a){
        actions.add(a);
    }

    public void removeAction(Action a){
        actions.remove(a);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }
}
