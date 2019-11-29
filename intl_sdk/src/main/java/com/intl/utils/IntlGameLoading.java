package com.intl.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * @Author: yujingliang
 * @Date: 2019/11/29
 */
public class IntlGameLoading {
    private Context context;
    private Dialog dialog;
    private void setDialog() {
        try {
            this.dialog = new Dialog(this.context, this.context.getResources().getIdentifier("yc_game_style_loading", "style", this.context.getPackageName()));
            this.dialog.requestWindowFeature(1);
            View loadRootView;
            final ImageView imgLoading = (loadRootView = LayoutInflater.from(this.context).inflate(this.context.getResources().getIdentifier("yc_game_view_loading", "layout", this.context.getPackageName()), (ViewGroup)null)).findViewById(this.context.getResources().getIdentifier("yc_img_loading", "id", this.context.getPackageName()));
            this.dialog.setContentView(loadRootView);
            this.dialog.setCancelable(true);
            this.dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                public final void onShow(DialogInterface dialogInterface) {
                    ((AnimationDrawable)imgLoading.getDrawable()).start();
                }
            });
            return;
        } catch (Exception e) {
            IntlGameExceptionUtil.handle(e);
        }
    }

    private void setDialog(Context context) {
        this.context = context;
        IntlGameLoading loading = this;
        setDialog();
    }
    public final void destory() {
        try {
            if(this.dialog != null && this.dialog.isShowing()) {
                this.dialog.dismiss();
            }

            this.dialog = null;
            this.context = null;
        } catch (Exception e) {
            IntlGameExceptionUtil.handle(e);
        }
    }

    public final void show(Context context) {
        if(this.dialog == null) {
            this.setDialog(context);
        }

        if(this.context != context) {
            if(this.dialog.isShowing()) {
                this.dialog.dismiss();
            }

            this.setDialog(context);
        }

        if(this.dialog != null && !this.dialog.isShowing()) {
            try {
                this.dialog.show();
                return;
            } catch (Exception e) {
                IntlGameExceptionUtil.handle(e);
            }
        }

    }

    public final void hide() {
        if(this.dialog != null && this.dialog.isShowing()) {
            this.dialog.dismiss();
        }

    }

    public static IntlGameLoading getInstance() {
        return LoadingHelper.INSTANCE;
    }

    private static class LoadingHelper {
        private static IntlGameLoading INSTANCE = new IntlGameLoading();

        private LoadingHelper() {
        }
    }
}
