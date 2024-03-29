package com.example.nicolascarraggi.trgrd;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

/**
 * Created by nicolascarraggi on 7/06/17.
 */

public class IntroActivity extends AppIntro {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Note here that we DO NOT use setContentView();

        // Add your slide fragments here.
        // AppIntro will automatically generate the dots indicator and buttons.
        // addSlide(firstFragment);
        // addSlide(secondFragment);
        // addSlide(thirdFragment);
        // addSlide(fourthFragment);

        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest.
        addSlide(AppIntroFragment.newInstance("Welcome to Triggered", "Triggered allows the user to automate specific tasks that combine devices and services.",R.drawable.trgrd_small, getResources().getColor(R.color.colorPrimary)));
        addSlide(AppIntroFragment.newInstance("Rules", "To connect events (and states) to actions we use rules. A rule consists of triggers (events and states) and actions.",R.drawable.rulescut, getResources().getColor(R.color.colorAccent)));
        addSlide(AppIntroFragment.newInstance("Event\n( If ... )", "A service or device can trigger an event. This event can trigger a rule.",R.drawable.event, getResources().getColor(R.color.colorEventDark)));
        addSlide(AppIntroFragment.newInstance("State\n ( While ... )", "A state can be true or false. While an event is true only the exact moment it happens, a state is true over a period of time.",R.drawable.state, getResources().getColor(R.color.colorStateDark)));
        addSlide(AppIntroFragment.newInstance("Action\n ( Then ... )", "When a rule is triggered, its actions will be executed.",R.drawable.action, getResources().getColor(R.color.colorActionDark)));
        addSlide(AppIntroFragment.newInstance("For example", "This rule allows the user to turn on the TV at home, by making a double tap gesture with the MYO gesture armband.",R.drawable.tvrule, getResources().getColor(R.color.colorRed)));

        // OPTIONAL METHODS
        // Override bar/separator color.
        //setBarColor(Color.parseColor("#3F51B5"));
        //setSeparatorColor(Color.parseColor("#2196F3"));

        // Hide Skip/Done button.
        showSkipButton(true);
        //setProgressButtonEnabled(true);

        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permission in Manifest.
        setVibrate(false);
        setVibrateIntensity(30);

        //setFadeAnimation();

        showStatusBar(false);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }

}
