package com.wamufi.particulates.utils;

import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.util.DisplayMetrics;

import com.wamufi.particulates.R;

public class ColorResources extends Resources {

    private int mSetGradeColor;

    /**
     * @param assets
     * @param metrics
     * @param config
     * @deprecated
     */
    public ColorResources(AssetManager assets, DisplayMetrics metrics, Configuration config) {
        super(assets, metrics, config);
    }

    public ColorResources(Resources original) {
        super(original.getAssets(), original.getDisplayMetrics(), original.getConfiguration());
    }

    @Override
    public int getColor(int id) throws NotFoundException {
        return getColor(id, null);
    }

    @Override
    public int getColor(int id, Theme theme) throws NotFoundException {
        switch (getResourceEntryName(id)) {
            case "gradeColor":
                // You can change the return value to an instance field that loads from SharedPreferences.
//                return Color.RED; // used as an example. Change as needed.
                return mSetGradeColor;
            default:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    return super.getColor(id, theme);
                }
                return super.getColor(id);
        }
    }

    public void setColor(int grade) {
        switch (grade) {
            case 1:
                mSetGradeColor = getColor(R.color.gradeOne);
                break;
            case 2:
                mSetGradeColor = getColor(R.color.gradeTwo);
                break;
            case 3:
                mSetGradeColor = getColor(R.color.gradeThree);
                break;
            case 4:
                mSetGradeColor = getColor(R.color.gradeFour);
                break;
            default:
                mSetGradeColor = Color.BLACK;
                break;
        }
    }

    public void setColor(String grade) {
        switch (grade) {
            case "1":
                mSetGradeColor = getColor(R.color.gradeOne);
                break;
            case "2":
                mSetGradeColor = getColor(R.color.gradeTwo);
                break;
            case "3":
                mSetGradeColor = getColor(R.color.gradeThree);
                break;
            case "4":
                mSetGradeColor = getColor(R.color.gradeFour);
                break;
            default:
                mSetGradeColor = Color.WHITE;
                break;
        }
    }

    public int[] setGradientColor(String grade) {
        int[] colors = new int[2];
        switch (grade) {
            case "1":
                colors = new int[]{getColor(R.color.grade_one_first), getColor(R.color.grade_one_second)};
                break;
            case "2":
                colors = new int[]{getColor(R.color.grade_two_first), getColor(R.color.grade_two_second)};
                break;
            case "3":
                colors = new int[]{getColor(R.color.grade_three_first), getColor(R.color.grade_three_second)};
                break;
            case "4":
                colors = new int[]{getColor(R.color.grade_four_first), getColor(R.color.grade_four_second)};
                break;
            default:
                colors = new int[]{getColor(R.color.primary_light), getColor(R.color.primary_dark)};
                break;
        }
        return colors;
    }
}
