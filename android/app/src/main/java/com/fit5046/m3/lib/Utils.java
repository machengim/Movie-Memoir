package com.fit5046.m3.lib;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// static helper methods are collected in this class.
public class Utils {

    // Get current date and format as 'yyyy-MM-dd'.
    public static String getCurrentDate() {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return sdf.format(date);
    }

    // Get current time and format as 'yyyy-MM-dd HH:mm'.
    public static String getCurrentTime() {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date();
        return sdf.format(date);
    }

    // Trim string to required length.
    public static String trimDate(String date, int length) {
        if (date.length() <= length) {
            return date;
        }
        return date.substring(0, length);
    }

    // Use to hide keyboard after user input.
    // Should be modified to avoid passing an activity.
    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        assert inputManager != null;
        inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    // Add a "0" if the given number is smaller than 10.
    public static String addZeroBeforeStr(int number) {
        String s = Integer.toString(number);
        if (number < 10) s = "0" + s;
        return s;
    }

    // Hash user password using SHA-256.
    public static String hash(String text) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(text.getBytes(StandardCharsets.UTF_8));
        byte[] digest = md.digest();
        return  String.format("%064x", new BigInteger(1, digest));
    }

    // Use regex to check email validity.
    public static boolean checkEmailFormat(String email) {
        Pattern REGEX =  Pattern.compile(
                "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z0-9.-]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = REGEX.matcher(email);
        return matcher.find();
    }
}
