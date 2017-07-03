package com.example.nicolascarraggi.trgrd.rulesys;

import java.util.concurrent.Callable;

/**
 * Created by nicolascarraggi on 3/05/17.
 *
 * A ScoreValueAction is an action of which (only) the value can be changed by the user.
 *
 */

public class ScoreValueAction extends Action {

    private int value;
    private ScoreValueActionType scoreValueActionType;
    private ScoreSide scoreSide;

    // SKELETON constructor
    public ScoreValueAction(int id, String name, int iconResource, Device device, ActionType actionType, ScoreSide scoreSide, ScoreValueActionType scoreValueActionType) {
        super(id, name, iconResource, device, actionType, null);
        this.actionValueType = ActionValueType.SCOREVALUE;
        this.scoreValueActionType = scoreValueActionType;
        this.scoreSide = scoreSide;
        this.value = 0;
    }

    public ScoreValueAction(int id, String name, int iconResource, Device device, ActionType actionType, ScoreSide scoreSide, ScoreValueActionType scoreValueActionType, int value, Callable callable) {
        this(id, name, iconResource, device, actionType, scoreSide,scoreValueActionType);
        this.callable = callable;
        this.scoreSide = scoreSide;
        this.scoreValueActionType = scoreValueActionType;
        this.value = value;
        this.isSkeleton = false;
    }

    // copy constructor
    public ScoreValueAction(int id, ScoreValueAction scoreValueAction, int value, Callable callable){
        this(id, scoreValueAction.getName(),scoreValueAction.getIconResource(),scoreValueAction.getDevice(),scoreValueAction.getActionType(), scoreValueAction.getScoreSide(), scoreValueAction.getScoreValueActionType(), value, callable);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public ScoreValueActionType getScoreValueActionType() {
        return scoreValueActionType;
    }

    public void setScoreValueActionType(ScoreValueActionType scoreValueActionType) {
        this.scoreValueActionType = scoreValueActionType;
    }

    public ScoreSide getScoreSide() {
        return scoreSide;
    }

    public void setScoreSide(ScoreSide scoreSide) {
        this.scoreSide = scoreSide;
    }

    public void setCallable(Callable callable) {
        this.callable = callable;
    }

    public enum ScoreValueActionType {
        ADD,SUBTRACT,SET
    }

    public enum ScoreSide {
        LEFT,RIGHT
    }

    @Override
    public String toString() {
        return "ScoreValueAction{" +
                "value=" + value +
                ", scoreValueActionType=" + scoreValueActionType +
                ", scoreSide=" + scoreSide +
                '}';
    }
}
