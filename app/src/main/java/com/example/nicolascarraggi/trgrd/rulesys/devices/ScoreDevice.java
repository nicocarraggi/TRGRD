package com.example.nicolascarraggi.trgrd.rulesys.devices;

import com.example.nicolascarraggi.trgrd.rulesys.NotificationAction;
import com.example.nicolascarraggi.trgrd.rulesys.ScoreValueAction;

import java.util.concurrent.Callable;

/**
 * Created by nicolascarraggi on 5/05/17.
 */

public interface ScoreDevice {

    ScoreValueAction getScoreValueAction(ScoreValueAction scoreValueActionSkeleton, int value);

    void editScoreValueAction(ScoreValueAction scoreValueAction, int value);

    Callable getScoreValueActionCallable(final int value, final ScoreValueAction.ScoreValueActionType type, final ScoreValueAction.ScoreSide side);

    void acScore(int value, ScoreValueAction.ScoreValueActionType scoreValueActionType, ScoreValueAction.ScoreSide side);

}
