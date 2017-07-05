package com.example.nicolascarraggi.trgrd.rulesys;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by nicolascarraggi on 5/07/17.
 */

public class RuleEngine {

    public RuleEngine() {
    }

    public void checkRules(Set<Rule> rules){
        Set<Rule> toExecute = new HashSet<>();
        for (Rule r: rules){
            if (r.checkRule()) toExecute.add(r);
        }
        executeRules(toExecute);
    }

    private void executeRules(Set<Rule> rules){
        if (rules.isEmpty()) return;
        for (Rule r: rules){
            r.executeRule();
        }
    }
}
