package me.ryansimon.informer;

/**
 * @author Ryan Simon
 */
interface OnNetworkErrorDiscovered {
    void handleTimeoutError();
    void handleServerError();
    void handleUnauthorizedError();
    void handleNoNetworkError();
    void handleGenericError();
}
