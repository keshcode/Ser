package com.skeleton.util.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;


/**
 * Calling for Time picker
 * <p>
 * TimePickerFragment.newInstance(new TimePickerDialog.OnTimeSetListener() {
 *
 *
 * public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
 * <p>
 * Do whatever you want to do with selected time
 * <p>
 * }
 * }).show(getSupportFragmentManager(), "timePicker");
 */

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    private DatePickerDialog.OnDateSetListener listener;

    /**
     *
     * @param listener instance of DatePickerDialog.OnDateSetListener
     * @return object of DatePickerFragment
     */
    public static DatePickerFragment newInstance(final DatePickerDialog.OnDateSetListener listener) {
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.listener = listener;
        return fragment;
    }


    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
        dialog.getDatePicker().setMinDate(c.getTime().getTime());
        return dialog;

        // Create a new instance of DatePickerDialog and return it
        //return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    /**
     *
     * @param view view of DatePicker
     * @param year year that set on DatePicker
     * @param month month that set on DatePicker
     * @param day day that set on DatePicker
     */
    public void onDateSet(final DatePicker view, final int year, final int month, final int day) {
        // Do something with the date chosen by the user
        listener.onDateSet(view, year, month, day);
    }
}
