package me.ryansimon.informer;

/**
 * @author Ryan Simon
 *
 * Callback interface to be implemented by any Class interested in listening for clicks on the error
 * message action button.
 */
public interface OnNetworkErrorActionClickListener {
    void onRequestTimeoutActionClick();
    void onServerErrorActionClick();
    void onAuthErrorActionClick();
    void onGenericErrorActionClick();
}
