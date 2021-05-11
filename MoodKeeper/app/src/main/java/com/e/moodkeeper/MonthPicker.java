package com.e.moodkeeper;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;


public class MonthPicker extends DatePickerDialog {
    public MonthPicker(Context context, int theme, OnDateSetListener callBack,
                       int year, int monthOfYear, int dayOfMonth) {
        super(context, DatePickerDialog.THEME_HOLO_LIGHT, callBack, year, monthOfYear, dayOfMonth);
        setTitle(year + "年" + (monthOfYear + 1) + "月");
        try {
            ((ViewGroup) ((ViewGroup) (getDatePicker().getChildAt(0))).getChildAt(0)).getChildAt(2)
                    .setVisibility(View.GONE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public MonthPicker(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
        super(context, callBack, year, monthOfYear, dayOfMonth);
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int month, int day) {
        super.onDateChanged(view, year, month, day);
        setTitle(year + "年" + (month + 1) + "月");
    }
}
