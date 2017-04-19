package com.example.nicolascarraggi.trgrd.rulesys;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by nicolascarraggi on 4/04/17.
 */

public class Rule {

    private int id;
    private String name;
    private boolean active;
    private Set<Event> events;
    private Set<State> states;
    private Set<Action> actions;
    private int times;

    public Rule(int id, String name) {
        this.id = id;
        this.name = name;
        this.active = false;
        this.events = new HashSet<>();
        this.states = new HashSet<>();
        this.actions = new HashSet<>();
        this.times = 0;
    }

    private void executeRule(){
        System.out.println("[Rule "+this.name+"] executeRule()");
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
        System.out.println("[Rule "+this.name+"] checkRule()");
        if(active){
            if(!states.isEmpty()){
                System.out.println("[Rule "+this.name+"] checkRule() checking states");
                // If event and states, check if all states are true !
                boolean isRuleTrue = true;
                for(State s: states){
                    System.out.println("[Rule "+this.name+"] checkRule() state: "+s.getName()+" isState= "+s.isState());
                    // If 1 state is false, stop the loop!
                    if(!s.isState()){
                        isRuleTrue = false;
                        break;
                    }
                }
                if (isRuleTrue) executeRule();
            } else {
                System.out.println("[Rule "+this.name+"] checkRule() no states");
                // If event and no states, rule is executed !
                executeRule();
            }
        }
    }

    public void eventTriggered(Event e){
        System.out.println("[Rule "+this.name+"] Event "+e.getName()+" triggered!");
        checkRule();
    }

    public void stateChanged(State s){
        System.out.println("[Rule "+this.name+"] State "+s.getName()+" triggered with state "+s.isState()+"!");
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

    public Set<Event> getEvents() {
        return events;
    }

    public Set<State> getStates() {
        return states;
    }

    public Set<Action> getActions() {
        return actions;
    }
}
