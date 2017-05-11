package com.example.nicolascarraggi.trgrd;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CreateRuleFromTemplateActivity extends RuleSystemBindingActivity {

    private final int REQUEST_CODE_EVENT = 1;
    private final int REQUEST_CODE_STATE = 2;
    private final int REQUEST_CODE_ACTION = 3;

    private final int ASK_LOCATION_ARRIVING = 1;
    private final int ASK_LOCATION_LEAVING = 2;
    private final int ASK_LOCATION_CURRENTLY = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_rule_from_template);
    }
}
