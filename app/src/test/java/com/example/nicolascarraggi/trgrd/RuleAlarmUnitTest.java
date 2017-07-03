package com.example.nicolascarraggi.trgrd;

import com.example.nicolascarraggi.trgrd.rulesys.Rule;
import com.example.nicolascarraggi.trgrd.rulesys.devices.AndroidPhone;
import com.example.nicolascarraggi.trgrd.rulesys.devices.Pebble;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RuleAlarmUnitTest {
    @Test
    public void newRule_isCorrect() throws Exception {
        Rule ruleAlarmStop = new Rule(0, "testRuleAlarmStop");
        //Rule ruleAlarmSnooze = new Rule("testRuleAlarmSnooze");
        AndroidPhone androidPhone = new AndroidPhone(null, null, null, null, null, null, null, null, null, null, null, null);
        Pebble pebble = new Pebble(null, null, null, null, null, null, null, null, null,null);

        // When phone's alarm is going off and user presses button down on pebble, stop alarm!
        ruleAlarmStop.addState(androidPhone.getAlarmGoing());
        ruleAlarmStop.addEvent(pebble.getBtnDown());
        ruleAlarmStop.addAction(androidPhone.getSimAcAlarmDismiss());

        ruleAlarmStop.setActive(true);

        assertEquals(0, ruleAlarmStop.getTimes());

        androidPhone.evAlarmStart();

        assertEquals(0, ruleAlarmStop.getTimes());

        pebble.evBtnDown();

        assertEquals(1, ruleAlarmStop.getTimes());

        // Now alarm is off, so rule shouldn't be triggered!

        pebble.evBtnDown();

        assertEquals(1, ruleAlarmStop.getTimes());

        androidPhone.evAlarmStart();

        assertEquals(1, ruleAlarmStop.getTimes());

        pebble.evBtnDown();

        assertEquals(2, ruleAlarmStop.getTimes());

        // Both states are true AND event triggered, rule should have been executed once!
        //assertEquals(1, r.getTimes());

    }
}