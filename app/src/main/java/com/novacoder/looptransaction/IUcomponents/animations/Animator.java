package com.novacoder.looptransaction.IUcomponents.animations;

import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

public class Animator {

    static public void visible(View witget, boolean flag) {
        visible(witget, flag, 50, 1.0f);
    }
    static public void visible(View witget, boolean flag, int time) {
        visible(witget, flag, time, 1.0f);
    }

    static public void visible(View witget, boolean flag, float maxAlpha) {
        visible(witget, flag, 50, maxAlpha);
    }

    static public void visible(View witget, boolean flag, int time, float maxAlpha) {

        int new_status = (flag) ? View.VISIBLE : View.GONE;
        float valor = (flag) ? maxAlpha : 0.0f;
        int duracion = time;

        if (flag) {
            witget.setVisibility(new_status);
            witget.animate().alpha(valor).setDuration(duracion)
                    .setInterpolator(new AccelerateDecelerateInterpolator());
        } else {
            witget.animate().alpha(valor).setDuration(duracion)
                    .setInterpolator(new AccelerateDecelerateInterpolator()).withEndAction(() -> {
                        witget.setVisibility(new_status);
                    });
        }
    }

}
