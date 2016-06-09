package com.itpro.buildersbackyard.ui.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
/**
 * Created by root on 4/12/15.
 */
public class DateTimeDialogFragmentEnd extends DialogFragment {
    public final int DATE_PICKER = 1;
    public final int TIME_PICKER = 2;
    private Fragment mCurrentFragment;

    public DateTimeDialogFragmentEnd(PostJob fragment) {
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
