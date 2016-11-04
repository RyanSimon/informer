package me.ryansimon.informer;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.fishermenlabs.errorutility.R;

/**
 * @author Ryan Simon
 *
 * Deals with network errors using a {@link Snackbar}, and a relevant action is shown for users
 * to respond.
 *
 * NOTE: This class must be used with its {@link Builder}
 */
public final class NetworkErrorMessageSnackbar extends NetworkErrorHandler {

    private Snackbar mSnackbar;

    private NetworkErrorMessageSnackbar(final Builder builder, final Context context) {
        super(builder, context);

        if(builder.mSnackbar == null && builder.mAnchorView == null) {
            throw new IllegalStateException("There must be a Snackbar or an anchor View");
        }
        else {
            if (builder.mSnackbar == null) {
                mSnackbar = Snackbar.make(builder.mAnchorView, "", builder.mSnackBarDuration);
            } else mSnackbar = builder.mSnackbar;

            mSnackbar.setActionTextColor(builder.mActionTextColor);

            handleError(builder.mHttpStatusCode, new OnNetworkErrorDiscovered() {
                @Override
                public void handleTimeoutError() {
                    NetworkErrorMessageSnackbar.configureTimeoutView(
                            mSnackbar,
                            builder.mOnNetworkErrorActionClickListener,
                            NetworkErrorMessageSnackbar.this
                    );
                }

                @Override
                public void handleServerError() {
                    NetworkErrorMessageSnackbar.configureServerErrorView(
                            mSnackbar,
                            builder.mOnNetworkErrorActionClickListener,
                            NetworkErrorMessageSnackbar.this
                    );
                }

                @Override
                public void handleUnauthorizedError() {
                    NetworkErrorMessageSnackbar.configureUnauthorizedView(
                            mSnackbar,
                            builder.mOnNetworkErrorActionClickListener,
                            NetworkErrorMessageSnackbar.this
                    );
                }

                @Override
                public void handleNoNetworkError() {
                    NetworkErrorMessageSnackbar.configureNoNetworkView(
                            mSnackbar,
                            context,
                            NetworkErrorMessageSnackbar.this
                    );
                }

                @Override
                public void handleGenericError() {
                    NetworkErrorMessageSnackbar.configureGenericView(
                            mSnackbar,
                            NetworkErrorMessageSnackbar.this,
                            builder.mOnNetworkErrorActionClickListener
                    );
                }
            });
        }
    }

    /***** ACCESSOR METHODS *****/

    @Override
    public void show() {
        if(mSnackbar != null) {
            mSnackbar.show();
        }
    }

    @Override
    public void dismiss() {
        if(mSnackbar != null) {
            mSnackbar.dismiss();
        }
    }

    /***** HELPER METHODS *****/

    private static void configureGenericView(@NonNull final Snackbar snackbar,
                                             @NonNull final NetworkErrorMessageSnackbar networkErrorMessageSnackbar,
                                             @NonNull final OnNetworkErrorActionClickListener networkErrorListener) {
        snackbar.setText(networkErrorMessageSnackbar.mGenericErrorText);
        snackbar.setAction(
                networkErrorMessageSnackbar.mGenericErrorActionText,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        networkErrorListener.onGenericErrorActionClick();
                    }
                }
        );
    }


    private static void configureTimeoutView(@NonNull Snackbar snackbar,
                                             @NonNull final OnNetworkErrorActionClickListener networkErrorListener,
                                             NetworkErrorMessageSnackbar networkErrorMessageSnackbar) {
        snackbar.setText(networkErrorMessageSnackbar.mRequestTimeoutText);
        snackbar.setAction(
                networkErrorMessageSnackbar.mRequestTimeoutActionText,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        networkErrorListener.onRequestTimeoutActionClick();
                    }
                }
        );
    }

    private static void configureNoNetworkView(@NonNull Snackbar snackbar,
                                               final Context context,
                                               NetworkErrorMessageSnackbar networkErrorMessageSnackbar) {
        snackbar.setText(networkErrorMessageSnackbar.mNoInternetText);
        snackbar.setAction(
                networkErrorMessageSnackbar.mNoInternetActionText,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                }
        );
    }

    private static void configureServerErrorView(@NonNull Snackbar snackbar,
                                                 @NonNull final OnNetworkErrorActionClickListener networkErrorListener,
                                                 NetworkErrorMessageSnackbar networkErrorMessageSnackbar) {
        snackbar.setText(networkErrorMessageSnackbar.mServerErrorText);
        snackbar.setAction(
                networkErrorMessageSnackbar.mServerErrorActionText,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        networkErrorListener.onServerErrorActionClick();
                    }
                }
        );
    }

    private static void configureUnauthorizedView(@NonNull Snackbar snackbar,
                                                  @NonNull final OnNetworkErrorActionClickListener networkErrorListener,
                                                  NetworkErrorMessageSnackbar networkErrorMessageSnackbar) {
        snackbar.setText(networkErrorMessageSnackbar.mAuthErrorText);
        snackbar.setAction(networkErrorMessageSnackbar.mAuthErrorActionText,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        networkErrorListener.onAuthErrorActionClick();
                    }
                });
    }

    /***** BUILDER *****/

    public static class Builder extends NetworkErrorHandler.Builder<Builder> {

        // Optional
        private Snackbar mSnackbar = null;
        private @ColorInt int mActionTextColor;
        private @Snackbar.Duration int mSnackBarDuration = Snackbar.LENGTH_INDEFINITE;
        private View mAnchorView = null;

        public Builder(int httpStatusCode,
                       View anchorView,
                       OnNetworkErrorActionClickListener onNetworkErrorActionClickListener) {
            super(httpStatusCode, anchorView.getContext(), onNetworkErrorActionClickListener);
            mAnchorView = anchorView;
            mActionTextColor = ContextCompat.getColor(mContext, R.color.colorPrimary);
        }

        public Builder(int httpStatusCode,
                       Snackbar snackbar,
                       OnNetworkErrorActionClickListener onNetworkErrorActionClickListener) {
            super(httpStatusCode, snackbar.getView().getContext(), onNetworkErrorActionClickListener);
            mSnackbar = snackbar;
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        @Override
        public NetworkErrorMessageSnackbar build() {
            final Context context = (mAnchorView != null) ? mAnchorView.getContext() : mSnackbar.getView().getContext();
            return new NetworkErrorMessageSnackbar(this, context);
        }

        public Builder snackbar(Snackbar snackbar) {
            mSnackbar = snackbar;
            return this;
        }

        public Builder snackbarDuration(@Snackbar.Duration int snackBarDuration) {
            mSnackBarDuration = snackBarDuration;
            return this;
        }

        public Builder anchorView(@NonNull View anchorView) {
            mAnchorView = anchorView;
            return this;
        }

        public Builder actionTextColor(@ColorInt int actionTextColor) {
            mActionTextColor = actionTextColor;
            return this;
        }
    }
}
