/*
 *
 *  * Copyright (c) 2015, 360ITPRO and/or its affiliates. All rights reserved.
 *  *
 *  * Redistribution and use in source and binary forms, with or without
 *  * modification, are permitted provided that the following conditions
 *  * are met:
 *  *
 *  *   - Redistributions of source code must retain the above copyright
 *  *     notice, this list of conditions and the following disclaimer.
 *  *
 *  *   - Redistributions in binary form must reproduce the above copyright
 *  *     notice, this list of conditions and the following disclaimer in the
 *  *     documentation and/or other materials provided with the distribution.
 *  *
 *  */
package com.itpro.buildersbackyard.ui.fragment;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.os.Bundle;

/**
 * Created by ripan on 19/9/15.
 */
public class DateTimeDialogFragmentStart extends DialogFragment {
    /*
    * Date Time Picker Fragment for Activity
    * */
    public final int DATE_PICKER = 1;
    public final int TIME_PICKER = 2;
    private Fragment mCurrentFragment;

    public DateTimeDialogFragmentStart(PostJob fragment) {
        mCurrentFragment = fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = new Bundle();
        bundle = getArguments();
        int id = bundle.getInt("dialog_id");
        switch (id) {
            case DATE_PICKER:
                return new DatePickerDialog(getActivity(),
                        (DatePickerDialog.OnDateSetListener) mCurrentFragment, bundle.getInt("year"),
                        bundle.getInt("month"), bundle.getInt("day"));
            case TIME_PICKER:
                return new TimePickerDialog(getActivity(),
                        (TimePickerDialog.OnTimeSetListener) mCurrentFragment, bundle.getInt("hour"),
                        bundle.getInt("minute"), false);
        }
        return null;
    }
}