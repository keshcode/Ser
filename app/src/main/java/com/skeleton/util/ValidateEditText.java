package com.skeleton.util;

import android.content.Context;
import android.util.Patterns;
import android.widget.EditText;

import com.skeleton.R;
import com.skeleton.util.dialog.CustomAlertDialog;

/**
 * Developer: Saurabh Verma
 * Dated: 03-03-2017.
 */

public final class ValidateEditText {
    private static final String REGEX_MORE_SPACE = "[ ]{2,}";

    private static final int PASSWORD_LENGTH = 6;
    private static final int MIN_NAME_LENGTH = 2;

    /**
     * Empty Constructor
     * not called
     */
    public ValidateEditText() {
    }

    /**
     * @param et instance of edit text
     * @return boolean
     */
    public static boolean genericEmpty(final EditText et) {
        return et.getText().toString().trim().isEmpty();
    }

    /**
     * Method to validate email id
     *
     * @param et instance of edit text
     * @return boolean
     */
    public static boolean checkEmail(final EditText et) {
        String email = et.getText().toString().trim();
        if (genericEmpty(et)) {
            return setErrorAndRequestFoucs(et, getContext(et).getString(R.string.error_email_field_empty));
        }
        if (!email.matches(Patterns.EMAIL_ADDRESS.toString())) {
            return setErrorAndRequestFoucs(et, getContext(et).getString(R.string.error_invalid_email));
        }
        return true;
    }

    /**
     * Method to validate password field
     *
     * @param et        instance of edit text
     * @param isConfirm (true for confirm password & false for password field)
     * @return boolean
     */
    public static boolean checkPassword(final EditText et, final boolean isConfirm) {
        String password = et.getText().toString().trim();
        if (genericEmpty(et)) {
            String msg;
            if (isConfirm) {
                msg = getContext(et).getString(R.string.error_confirm_password_field_empty);
            } else {
                msg = getContext(et).getString(R.string.error_password_field_empty);
            }
            return setErrorAndRequestFoucs(et, msg);
        }

        if (password.length() < PASSWORD_LENGTH) {
            return setErrorAndRequestFoucs(et, getContext(et).getString(R.string.error_password_must_be_greater_than_5_character));
        }
        return true;
    }

    /**
     * Method to validate phoneNumber Field
     *
     * @param et instance of edit text
     * @return boolean
     */
    public static boolean checkPhoneNumber(final EditText et) {
        String phoneNumber = et.getText().toString().trim();
        if (genericEmpty(et)) {
            return setErrorAndRequestFoucs(et, getContext(et).getString(R.string.error_phone_number_field_empty));
        }

        if (phoneNumber.length() < PASSWORD_LENGTH || Long.valueOf(phoneNumber) == PASSWORD_LENGTH) {
            return setErrorAndRequestFoucs(et, getContext(et).getString(R.string.error_invalid_phone_number));
        }

        return true;
    }

    /**
     * Method to validate name field
     *
     * @param et          et instance of edit text
     * @param isFirstName (true for first name & false for last name)
     * @return boolean
     */
    public static boolean checkName(final EditText et, final boolean isFirstName) {
        String name = et.getText().toString().trim().replaceAll(REGEX_MORE_SPACE, " ");
        et.setText(StringUtil.toCamelCase(name));
        if (genericEmpty(et)) {
            String msg;
            if (isFirstName) {
                msg = getContext(et).getString(R.string.error_first_name_field_empty);
            } else {
                msg = getContext(et).getString(R.string.error_last_name_field_empty);
            }
            return setErrorAndRequestFoucs(et, msg);
        }

        //It takes alphabets and spaces and dots...
        if (!name.matches("^[\\p{L} .'-]+$")) {
            String msg;
            if (isFirstName) {
                msg = getContext(et).getString(R.string.error_first_name_special_number_character);
            } else {
                msg = getContext(et).getString(R.string.error_last_name_special_number_character);
            }
            return setErrorAndRequestFoucs(et, msg);
        }


        if (name.length() < MIN_NAME_LENGTH) {
            String msg;
            if (isFirstName) {
                msg = getContext(et).getString(R.string.error_first_name_two_character_long);
            } else {
                msg = getContext(et).getString(R.string.error_last_name_two_character_long);
            }
            return setErrorAndRequestFoucs(et, msg);
        }
        return true;
    }

    /**
     * Method to validate password & confirm password field
     *
     * @param et  et instance of edit text
     * @param etc etc instance of edit text
     * @return boolean
     */
    public static boolean comparePassword(final EditText et, final EditText etc) {
        if (!(et.getText().toString().trim().equals(etc.getText().toString().trim()))) {
            setErrorAndRequestFoucs(et, getContext(et).getString(R.string.error_password_mismatch));
            return setErrorAndRequestFoucs(et, getContext(etc).getString(R.string.error_password_mismatch));
        }
        return true;
    }

    /**
     * @param et           instance of edit text
     * @param errorMessage error msg
     * @return boolean
     */
    private static boolean setErrorAndRequestFoucs(final EditText et, final String errorMessage) {
        new CustomAlertDialog.Builder(getContext(et))
                .setMessage(errorMessage)
                .setPositiveButton(R.string.text_ok, new CustomAlertDialog.CustomDialogInterface.OnClickListener() {
                    @Override
                    public void onClick() {
                    }
                })
                .show();

        //et.setError(errorMessage);
        et.setSelection(et.getText().toString().length());
        et.setHovered(true);
        et.requestFocus();
        return false;
    }

    /**
     * @param et instance of edit text
     * @return context
     */
    private static Context getContext(final EditText et) {
        return et.getContext();
    }

    /**
     * Method to validate field is empty or not
     *
     * @param et           instance of edit text
     * @param errorMessage error message
     * @return boolean
     */
    public boolean genericEmpty(final EditText et, final String errorMessage) {
        if (et.getText().toString().trim().isEmpty()) {
            return setErrorAndRequestFoucs(et, errorMessage);
        }
        return true;
    }
}
