package me.ryansimon.informer;

import android.content.Context;

import com.fishermenlabs.errorutility.R;

/**
 * @author Ryan Simon
 */
public abstract class NetworkErrorHandler {

    protected final CharSequence mRequestTimeoutText;
    protected final CharSequence mRequestTimeoutActionText;
    protected final CharSequence mServerErrorText;
    protected final CharSequence mServerErrorActionText;
    protected final CharSequence mAuthErrorText;
    protected final CharSequence mAuthErrorActionText;
    protected final CharSequence mNoInternetText;
    protected final CharSequence mNoInternetActionText;
    protected final CharSequence mGenericErrorText;
    protected final CharSequence mGenericErrorActionText;
    protected final Context mContext;

    protected NetworkErrorHandler(final Builder builder, final Context context) {
        mRequestTimeoutText = builder.mRequestTimeoutText;
        mRequestTimeoutActionText = builder.mRequestTimeoutActionText;
        mServerErrorText = builder.mServerErrorText;
        mServerErrorActionText = builder.mServerErrorActionText;
        mAuthErrorText = builder.mAuthErrorText;
        mAuthErrorActionText = builder.mAuthErrorActionText;
        mNoInternetText = builder.mNoInternetText;
        mNoInternetActionText = builder.mNoInternetActionText;
        mGenericErrorText = builder.mGenericErrorText;
        mGenericErrorActionText = builder.mGenericErrorActionText;
        mContext = context;
    }

    protected static void handleError(int httpStatusCode, OnNetworkErrorDiscovered onNetworkErrorDiscovered) {
        if(httpStatusCode == HttpStatusCode.REQUEST_TIMEOUT) {
            onNetworkErrorDiscovered.handleTimeoutError();
        }
        else if(HttpStatusCode.SERVER_ERROR_LIST.contains(httpStatusCode)) {
            onNetworkErrorDiscovered.handleServerError();
        }
        else if(httpStatusCode == HttpStatusCode.UNAUTHORIZED) {
            onNetworkErrorDiscovered.handleUnauthorizedError();
        }
        else if(httpStatusCode == HttpStatusCode.NO_INTERNET) {
            onNetworkErrorDiscovered.handleNoNetworkError();
        }
        else {
            onNetworkErrorDiscovered.handleGenericError();
        }
    }

    /***** ABSTRACT METHODS *****/

    public abstract void show();

    public abstract void dismiss();

    /***** BUILDER *****/

    protected abstract static class Builder<T extends Builder<T>> {

        // Required
        protected final int mHttpStatusCode;
        protected final Context mContext;
        protected final OnNetworkErrorActionClickListener mOnNetworkErrorActionClickListener;

        // Optional
        protected CharSequence mRequestTimeoutText;
        protected CharSequence mRequestTimeoutActionText;
        protected CharSequence mServerErrorText;
        protected CharSequence mServerErrorActionText;
        protected CharSequence mAuthErrorText;
        protected CharSequence mAuthErrorActionText;
        protected CharSequence mNoInternetText;
        protected CharSequence mNoInternetActionText;
        protected CharSequence mGenericErrorText;
        protected CharSequence mGenericErrorActionText;

        public Builder(final int httpStatusCode,
                       final Context context,
                       final OnNetworkErrorActionClickListener onNetworkErrorActionClickListener) {
            mHttpStatusCode = httpStatusCode;
            mContext = context;
            mOnNetworkErrorActionClickListener = onNetworkErrorActionClickListener;

            // Set default error messages
            mRequestTimeoutText = mContext.getText(R.string.network_error_timeout);
            mRequestTimeoutActionText = mContext.getText(R.string.network_error_snackbar_retry);
            mServerErrorText = mContext.getText(R.string.network_error_generic_server_issue);
            mServerErrorActionText = mContext.getText(R.string.network_error_snackbar_retry);
            mAuthErrorText = mContext.getText(R.string.network_error_need_to_sign_out);
            mAuthErrorActionText = mContext.getString(R.string.network_error_log_out);
            mNoInternetText = mContext.getText(R.string.network_error_no_internet);
            mNoInternetActionText = mContext.getText(R.string.network_error_no_internet_snackbar_settings);
            mGenericErrorText = mContext.getText(R.string.network_error_generic);
            mGenericErrorActionText = mContext.getText(R.string.network_error_snackbar_retry);
        }

        public abstract NetworkErrorHandler build();

        protected abstract T getThis();

        public T requestTimeoutText(CharSequence requestTimeoutText) {
            mRequestTimeoutText = requestTimeoutText;
            return getThis();
        }

        public T requestTimeoutActionText(CharSequence requestTimeoutActionText) {
            mRequestTimeoutActionText = requestTimeoutActionText;
            return getThis();
        }

        public T serverErrorText(CharSequence serverErrorText) {
            mServerErrorText = serverErrorText;
            return getThis();
        }

        public T serverErrorActionText(CharSequence serverErrorActionText) {
            mServerErrorActionText = serverErrorActionText;
            return getThis();
        }

        public T authErrorText(CharSequence authErrorText) {
            mAuthErrorText = authErrorText;
            return getThis();
        }

        public T authErrorActionText(CharSequence authErrorActionText) {
            mAuthErrorActionText = authErrorActionText;
            return getThis();
        }

        public T noInternetText(CharSequence noInternetText) {
            mNoInternetText = noInternetText;
            return getThis();
        }

        public T noInternetActionText(CharSequence noInternetActionText) {
            mNoInternetActionText = noInternetActionText;
            return getThis();
        }

        public T genericErrorText(CharSequence genericErrorText) {
            mGenericErrorText = genericErrorText;
            return getThis();
        }

        public T genericErrorActionText(CharSequence genericErrorActionText) {
            mGenericErrorActionText = genericErrorActionText;
            return getThis();
        }
    }
}
