package com.example.nicolascarraggi.trgrd;

import com.example.nicolascarraggi.trgrd.rulesys.Action;
import com.example.nicolascarraggi.trgrd.rulesys.Device;
import com.example.nicolascarraggi.trgrd.rulesys.Event;
import com.example.nicolascarraggi.trgrd.rulesys.Rule;
import com.example.nicolascarraggi.trgrd.rulesys.State;

import org.junit.Test;

import java.util.concurrent.Callable;

import static org.junit.Assert.assertEquals;

public class RuleUnitTest {
    @Test
    public void newRule_isCorrect() throws Exception {
        Rule r = new Rule("testRule");
        Device d = null; // not really used in this test case!
        Event e1 = new Event("testEvent1", d);
        //Event e2 = new Event("testEvent2", d);
        State s1 = new State("testState1", d, false);
        State s2 = new State("testState2", d, false);
        Action a = new Action("testAction", d, new Callable() {
            @Override
            public Object call() throws Exception {
                return null;
            }
        });

        // Add event and states!
        r.addEvent(e1);
        //r.addEvent(e2);
        r.addState(s1);
        r.addState(s2);
        r.addAction(a);

        // Activate rule!
        r.setActive(true);

        // Nothing triggered and states are false, so rule is not executed yet!
        assertEquals(0, r.getTimes());

        // Trigger the event!
        e1.trigger();
        //e2.trigger();

        // Event triggered, but states are still false, so rule is not executed yet!
        assertEquals(0, r.getTimes());

        // Set both states to true!
        s1.trigger();
        s2.trigger();

        // Both states are true, but since then event not triggered, so rule is not executed yet!
        assertEquals(0, r.getTimes());

        // Trigger the event!
        e1.trigger();

        // Both states are true AND event triggered, rule should have been executed once!
        assertEquals(1, r.getTimes());

    }
}