package me.ryansimon.informer;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fishermenlabs.errorutility.R;

/**
 * @author Ryan Simon
 *
 * Deals with network errors using ViewGroup {@link #mRootView} passed in, and a relevant View is
 * added to the {@link #mRootView} and is shown for users to respond.
 *
 * You can customize this by using your own {@link #mInlineErrorContainer} and associated {@link #mErrorMessageView}
 * and {@link #mActionButtonView}.
 *
 * By default, the {@link #mRootView} is not dismissed when {@link #dismiss()} is called.
 *
 * NOTE: This class must be used with its {@link Builder}
 */
public final class NetworkErrorMessageInline extends NetworkErrorHandler {

    private TextView mErrorMessageView;
    private TextView mActionButtonView;
    private View mInlineErrorContainer;
    private ViewGroup mRootView;
    private boolean mDismissRootView = false;

    private NetworkErrorMessageInline(final Builder builder, final Context context) {
        super(builder, context);

        // set vars from builder
        mRootView = builder.mRootView;
        mErrorMessageView = builder.mErrorMessageView;
        mActionButtonView = builder.mActionButtonView;
        mInlineErrorContainer = builder.mInlineErrorContainer;
        mDismissRootView = builder.mDismissRootView;

        // if there isn't a custom mInlineErrorContainer, then provide a standard one
        if(mInlineErrorContainer == null) {
            mInlineErrorContainer = ViewGroup.inflate(mRootView.getContext(), R.layout.inline_error, mRootView).findViewById(R.id.error_container);
            mActionButtonView = (TextView) mInlineErrorContainer.findViewById(R.id.action_btn);
            mErrorMessageView = (TextView) mInlineErrorContainer.findViewById(R.id.error_msg);

            // set text colors
            mErrorMessageView.setTextColor(builder.mErrorMessageViewTextColor);
            mActionButtonView.setTextColor(builder.mActionButtonViewTextColor);
        }
        else {
            if(mActionButtonView == null || mErrorMessageView == null) {
                throw new IllegalStateException("You must include a actionButtonView and errorMessageView is you're using a custom inlineErrorContainer");
            }
        }

        handleError(builder.mHttpStatusCode, new OnNetworkErrorDiscovered() {
            @Override
            public void handleTimeoutError() {
                NetworkErrorMessageInline.configureTimeoutView(
                        mErrorMessageView,
                        mActionButtonView,
                        NetworkErrorMessageInline.this,
                        builder.mOnNetworkErrorActionClickListener
                );
            }

            @Override
            public void handleServerError() {
                NetworkErrorMessageInline.configureServerErrorView(
                        mErrorMessageView,
                        mActionButtonView,
                        NetworkErrorMessageInline.this,
                        builder.mOnNetworkErrorActionClickListener
                );
            }

            @Override
            public void handleUnauthorizedError() {
                NetworkErrorMessageInline.configureUnauthorizedView(
                        mErrorMessageView,
                        mActionButtonView,
                        NetworkErrorMessageInline.this,
                        builder.mOnNetworkErrorActionClickListener
                );
            }

            @Override
            public void handleNoNetworkError() {
                NetworkErrorMessageInline.configureNoNetworkView(
                        mInlineErrorContainer.getContext(),
                        mErrorMessageView,
                        mActionButtonView,
                        NetworkErrorMessageInline.this
                );
            }

            @Override
            public void handleGenericError() {
                NetworkErrorMessageInline.configureGenericView(
                        mErrorMessageView,
                        mActionButtonView,
                        NetworkErrorMessageInline.this,
                        builder.mOnNetworkErrorActionClickListener
                );
            }
        });
    }

    @Override
    public void show() {
        if(mDismissRootView && mRootView != null && mInlineErrorContainer != null) {
            mRootView.setVisibility(View.VISIBLE);
            mInlineErrorContainer.setVisibility(View.VISIBLE);
        }
        else if(mInlineErrorContainer != null) {
            mInlineErrorContainer.setVisibility(View.VISIBLE);
        }
        else {
            // do nothing
        }
    }

    @Override
    public void dismiss() {
        if(mDismissRootView && mRootView != null) {
            mRootView.setVisibility(View.GONE);
        }
        else if(mInlineErrorContainer != null) {
            mInlineErrorContainer.setVisibility(View.GONE);
        }
        else {
            // do nothing
        }
    }

    /***** HELPER METHODS *****/

    private static void configureGenericView(@NonNull final TextView errorMessage,
                                             @NonNull final TextView actionButton,
                                             @NonNull final NetworkErrorHandler networkErrorHandler,
                                             @NonNull final OnNetworkErrorActionClickListener networkErrorListener) {
        errorMessage.setText(networkErrorHandler.mGenericErrorText);
        actionButton.setText(networkErrorHandler.mGenericErrorActionText);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                networkErrorListener.onGenericErrorActionClick();
                networkErrorHandler.dismiss();
            }
        });
    }

    private static void configureTimeoutView(@NonNull final TextView errorMessage,
                                             @NonNull final TextView actionButton,
                                             @NonNull final NetworkErrorHandler networkErrorHandler,
                                             @NonNull final OnNetworkErrorActionClickListener networkErrorListener) {
        errorMessage.setText(networkErrorHandler.mRequestTimeoutText);
        actionButton.setText(networkErrorHandler.mRequestTimeoutActionText);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                networkErrorListener.onRequestTimeoutActionClick();
                networkErrorHandler.dismiss();
            }
        });
    }

    private static void configureNoNetworkView(@NonNull final Context context,
                                               @NonNull final TextView errorMessage,
                                               @NonNull final TextView actionButton,
                                               @NonNull final NetworkErrorHandler networkErrorHandler) {
        errorMessage.setText(networkErrorHandler.mNoInternetText);
        actionButton.setText(networkErrorHandler.mNoInternetActionText);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                networkErrorHandler.dismiss();
            }
        });
    }

    private static void configureServerErrorView(@NonNull final TextView errorMessage,
                                                 @NonNull final TextView actionButton,
                                                 @NonNull final NetworkErrorHandler networkErrorHandler,
                                                 @NonNull final OnNetworkErrorActionClickListener networkErrorListener) {
        errorMessage.setText(networkErrorHandler.mServerErrorText);
        actionButton.setText(networkErrorHandler.mServerErrorActionText);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                networkErrorListener.onServerErrorActionClick();
                networkErrorHandler.dismiss();
            }
        });
    }

    private static void configureUnauthorizedView(@NonNull final TextView errorMessage,
                                                  @NonNull final TextView actionButton,
                                                  @NonNull final NetworkErrorHandler networkErrorHandler,
                                                  @NonNull final OnNetworkErrorActionClickListener networkErrorListener) {
        errorMessage.setText(networkErrorHandler.mAuthErrorText);
        actionButton.setText(networkErrorHandler.mAuthErrorActionText);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                networkErrorListener.onAuthErrorActionClick();
                networkErrorHandler.dismiss();
            }
        });
    }

    /***** BUILDER *****/

    public static class Builder extends NetworkErrorHandler.Builder<Builder> {

        // Required
        private ViewGroup mRootView;

        // Optional
        private TextView mActionButtonView = null;
        private @ColorInt int mActionButtonViewTextColor;
        private TextView mErrorMessageView = null;
        private @ColorInt int mErrorMessageViewTextColor;
        private View mInlineErrorContainer = null;
        private boolean mDismissRootView = false;

        public Builder(int httpStatusCode,
                       ViewGroup rootView,
                       OnNetworkErrorActionClickListener onNetworkErrorActionClickListener) {
            super(httpStatusCode, rootView.getContext(), onNetworkErrorActionClickListener);
            mRootView = rootView;
            mActionButtonViewTextColor = ContextCompat.getColor(mContext, R.color.colorAccent);
            mErrorMessageViewTextColor = ContextCompat.getColor(mContext, android.R.color.primary_text_light);
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        @Override
        public NetworkErrorMessageInline build() {
            return new NetworkErrorMessageInline(this, mRootView.getContext());
        }

        public Builder errorMessageView(TextView errorMessage) {
            mErrorMessageView = errorMessage;
            return this;
        }

        public Builder errorMessageTextColor(@ColorInt int errorMessageViewColor) {
            mErrorMessageViewTextColor = errorMessageViewColor;
            return this;
        }

        public Builder actionButton(TextView actionButton) {
            mActionButtonView = actionButton;
            return this;
        }

        public Builder actionButtonTextColor(@ColorInt int actionButtonTextColor) {
            mActionButtonViewTextColor = actionButtonTextColor;
            return this;
        }

        public Builder customErrorViews(View inlineErrorContainer,
                                            TextView actionButtonView,
                                            TextView errorMessageView) {
            mInlineErrorContainer = inlineErrorContainer;
            mActionButtonView = actionButtonView;
            mErrorMessageView = errorMessageView;
            return this;
        }

        public Builder rootView(ViewGroup rootView) {
            mRootView = rootView;
            return this;
        }

        public Builder dismissRootView(boolean dismissRootView) {
            mDismissRootView = dismissRootView;
            return this;
        }

    }
}
