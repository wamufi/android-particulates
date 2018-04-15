package com.wamufi.particulates.utils;

import android.content.Context;

import com.wamufi.particulates.R;

public class GradeString {

    private Context mContext;

    public GradeString(Context context) {
        mContext = context;
    }

    public String getString(int grade) {
        switch (grade) {
        case 1:
            return mContext.getResources().getString(R.string.string_grade_one);
        case 2:
            return mContext.getResources().getString(R.string.string_grade_two);
        case 3:
            return mContext.getResources().getString(R.string.string_grade_three);
        case 4:
            return mContext.getResources().getString(R.string.string_grade_four);
        default:
            return mContext.getResources().getString(R.string.string_nothing);
    }
}
    public String getString(String grade) {
        switch (grade) {
            case "1":
                return mContext.getResources().getString(R.string.string_grade_one);
            case "2":
                return mContext.getResources().getString(R.string.string_grade_two);
            case "3":
                return mContext.getResources().getString(R.string.string_grade_three);
            case "4":
                return mContext.getResources().getString(R.string.string_grade_four);
            default:
                return mContext.getResources().getString(R.string.string_nothing);
        }
    }
}
