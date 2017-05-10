package com.skeleton.util.dialog;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;


/**
 * Calling for Date Picker
 * <p>
 * DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
 * <p>
 * public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
 * <p>
 * Do whatever you want to do with selected date
 * <p>
 * }
 * }).show(getSupportFragmentManager(), "datePicker");
 */
public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {


    private TimePickerDialog.OnTimeSetListener listener;

    /**
     * @param listener instance of TimePickerDialog.OnTimeSetListener
     * @return object of TimePickerFragment
     */
    public static TimePickerFragment newInstance(final TimePickerDialog.OnTimeSetListener listener) {
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.listener = listener;
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    /**
     * @param view      view of Time picker
     * @param hourOfDay hour to set on time picker
     * @param minute    minute to set on time picker
     */
    public void onTimeSet(final TimePicker view, final int hourOfDay, final int minute) {
        // Do something with the time chosen by the user
        listener.onTimeSet(view, hourOfDay, minute);
    }

}
