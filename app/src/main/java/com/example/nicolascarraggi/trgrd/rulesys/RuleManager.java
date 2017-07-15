package com.example.nicolascarraggi.trgrd.rulesys;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by nicolascarraggi on 15/07/17.
 */

public class RuleManager {

    private RuleSystemService mRuleSystemService;

    private RuleEngine mRuleEngine;

    private HashMap<Integer,Rule> mRules;
    private HashMap<Integer,Rule> mExampleRules;
    private HashMap<Integer,RuleTemplate> mRuleTemplates;
    private HashMap<Integer,RuleTemplate> mRuleTemplateInstances;

    public RuleManager(RuleSystemService ruleSystemService) {
        this.mRuleSystemService = ruleSystemService;
        this.mRuleEngine = new RuleEngine();
        this.mRules = new HashMap<>();
        this.mExampleRules = new HashMap<>();
        this.mRuleTemplates = new HashMap<>();
        this.mRuleTemplateInstances = new HashMap<>();
    }

    public RuleEngine getRuleEngine() {
        return mRuleEngine;
    }

    public Set<Rule> getRules(){
        return new HashSet(mRules.values());
    }

    public Rule getRule(int id){
        return mRules.get(id);
    }

    public void addRule(Rule rule){
        this.mRules.put(rule.getId(),rule);
    }

    public Set<Rule> getExampleRules(){
        return new HashSet(mExampleRules.values());
    }

    public ExampleRule getExampleRule(int id){
        return (ExampleRule) mExampleRules.get(id);
    }

    public void addExampleRule(ExampleRule rule){
        this.mExampleRules.put(rule.getId(),rule);
    }

    public Set<RuleTemplate> getRuleTemplates(){
        return new HashSet(mRuleTemplates.values());
    }

    public RuleTemplate getRuleTemplate(int id){
        return mRuleTemplates.get(id);
    }

    public void addRuleTemplate(RuleTemplate ruleTemplate){
        this.mRuleTemplates.put(ruleTemplate.getId(),ruleTemplate);
    }

    public Set<RuleTemplate> getRuleTemplateInstances(){
        return new HashSet(mRuleTemplateInstances.values());
    }

    public RuleTemplate getRuleTemplateInstance(int id){
        return mRuleTemplateInstances.get(id);
    }

    public void addRuleTemplateInstance(RuleTemplate ruleTemplate){
        this.mRuleTemplateInstances.put(ruleTemplate.getId(),ruleTemplate);
    }

}
