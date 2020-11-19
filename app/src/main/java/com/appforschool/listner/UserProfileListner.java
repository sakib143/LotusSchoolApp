package com.appforschool.listner;


//This class is used to update User profile data into Home Pages after cloase User Profile screen
public class UserProfileListner {
    public interface onScreenCloseListner {
        void stateChanged();
    }

    private static UserProfileListner mInstance;
    private onScreenCloseListner mListener;
    private boolean mState;

    private UserProfileListner() {}

    public static UserProfileListner getInstance() {
        if(mInstance == null) {
            mInstance = new UserProfileListner();
        }
        return mInstance;
    }

    public void setListener(onScreenCloseListner listener) {
        mListener = listener;
    }

    public void changeState(boolean state) {
        if(mListener != null) {
            mState = state;
            notifyStateChange();
        }
    }

    public boolean getState() {
        return mState;
    }

    private void notifyStateChange() {
        mListener.stateChanged();
    }

}
