package com.example.nicolascarraggi.trgrd;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nicolascarraggi.trgrd.rulesys.DeviceManager;
import com.example.nicolascarraggi.trgrd.rulesys.RuleSystemService;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TrgrdFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TrgrdFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrgrdFragment extends Fragment {

    protected OnFragmentInteractionListener mListener;
    protected boolean isServiceStarted = false;
    protected boolean isServiceBound = false;
    protected SearchView searchView;

    public TrgrdFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
            this.isServiceStarted = mListener.getIsServiceStarted();
            this.isServiceBound = mListener.getIsServiceBound();
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        boolean getIsServiceStarted();
        boolean getIsServiceBound();
        DeviceManager getDeviceManager();
        RuleSystemService getRuleSystemService();
        void finishWithResult(Intent intent);
    }

    public void notifyIsServiceStartedChanged(boolean isServiceStarted){
        this.isServiceStarted = isServiceStarted;
        if(!isServiceStarted) this.isServiceBound = false;
    }

    public void notifyIsServiceBoundChanged(boolean isServiceBound){
        this.isServiceBound = isServiceBound;
    }

}
