package com.mylocarson.medmanager.utils;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by user on 05/04/2018.
 */

@SuppressWarnings("ALL")
public class Utilities {
    private static final String TAG  = Utilities.class.getSimpleName();

    private Utilities(){}

    /**
     * this method checks if  editText has a value, then sets its error if otherwise
     *
     * @param editTexts
     **/
    public static void validateEditText(EditText...editTexts){
        for (EditText editText : editTexts) {
            if (editText.getText().toString().isEmpty() &&
                    !(editText.getText().toString().length() > 0) ){
                editText.setError(Constants.EDIT_TEXT_REQUIRED);
            }
        }
    }

    /** this method checks if the editText has content in it
     * and returns true if so or false if otherwise
     * @param editText **/
    public static boolean isEditTextValid(EditText editText){
        boolean result;
        result = !(editText.getText().toString().isEmpty() &&
                !(editText.getText().toString().length() > 0));

        return result;
    }

    /** this method converts a Date into a readable string.
     * It basically calculates how long ago a medication was added to MedManager.
     * @param mDate **/
    public static String  convertDate(@NonNull String mDate){
        String newDateString = "";
        String dDate;
        String today_string;
        Date mdate;
        Date theDate;
        Date today;
        @SuppressLint("SimpleDateFormat") SimpleDateFormat parser
                = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");

        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat
                = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleFormat
                = new SimpleDateFormat("E MMM d yyyy");
        try {
            mdate = parser.parse(mDate);
            dDate = simpleDateFormat.format(mdate);
            theDate = simpleDateFormat.parse(dDate);
            today_string = simpleDateFormat.format(new Date());
            today = simpleDateFormat.parse(today_string);

            long diff = today.getTime() - theDate.getTime();
            long diffSeconds = diff/1000 % 60;
            long diffMins = diff/ (60 *1000) % 60;
            long diffHours =  diff / (60*60 *1000) %24;
            long diffDays = diff / (24 *60*60*1000);

            if (diffMins <= 0 && diffHours <=0 && diffDays <=0){
                newDateString = ""+diffSeconds+" secs ago";
            } else if (diffMins > 0 && diffMins < 60 && diffHours < 1 && diffDays < 1){
                newDateString = ""+diffMins+" mins ago";
            }else if (diffHours <= 23 && diffDays <= 0){
                newDateString =""+diffHours+" hours ago";
            }else if (diffDays == 1){
                newDateString = ""+diffDays +"day ago";
            }else if (diffDays >= 2 && diffDays <=7 ){
                newDateString =""+diffDays+"days ago";
            }else if (diffDays > 7 || diffDays <=13){
                newDateString ="Last week";
            }else if(diffDays >= 14 || diffDays <=20){
                newDateString = "2weeks ago";
            }else if (diffDays >=21 || diffDays <=28){
                newDateString = "3weeks ago";
            }else if (diffDays > 28){
                newDateString = simpleFormat.format(mDate);
            }

        } catch (Exception e) {
            // TODO: handle exception
            Log.d(TAG, "convertDate: ",e);
        }


        return  newDateString;

    }

    /** this is a custom ArrayAdapter for a Spinner.
     * It makes sure the first item is not selectable
     *  @param context
     * @param arrayOfStrings
     * **/
    public static ArrayAdapter<String> arrayAdapter(Context context, String[] arrayOfStrings){
        return new ArrayAdapter<String>(
                context, android.R.layout.simple_list_item_1, arrayOfStrings) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
    }

    /** This method hides or shows views
     * @param value
     * @param views **/
    public static void toggleVisibility(int value, View...views){
        switch (value){
            case 0:
                for (View view : views){
                    view.setVisibility(View.GONE);
                }
                break;
            case 1 :
                for (View view : views){
                    view.setVisibility(View.VISIBLE);
                }
        }

    }

    /** this method shows DatePicker
     * @param context
     * @param editText **/
    public  static void showDatePicker(Context context, final EditText editText){
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month++;
                        editText.setText(day +"/"+ month  +"/" +year);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    /** this method shows TimePicker
     * @param context
     * @param editText **/
    public static void showTimePicker(Context context, final EditText editText) {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minutes) {
                        editText.setText("" + hour + ":" + minutes);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }


    /**this method calculates the time in milliseconds for the interval of a medication
     * @param frequency  **/
    public static long calculateRepeatTime(int frequency){
        int hourInDay = 24 / frequency;
        int minutesInDay = 24 % frequency;

        long hourInMilliSecs = hourInDay * 60 *60 * 1000;
        long minInMilliSecs = minutesInDay * 60 * 1000;

        return  hourInMilliSecs+minInMilliSecs;

    }

    /** this method checks if the startDate for a medication is not greater than the endDate
     * @param startDate
     * @param endDate **/
    public static boolean validateStartDate(String startDate, String endDate){
        boolean value = false;
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat
                = new SimpleDateFormat("dd/MM/yyyy");
        Date fromDate,toDate;

        try {
            fromDate = simpleDateFormat.parse(startDate);
            toDate = simpleDateFormat.parse(endDate);

            value = fromDate.before(toDate) || toDate.equals(fromDate);
            ;
        }catch (ParseException e){
            // TODO: 07/04/2018 handle this exception
        }
        return value;
    }


    /**this method converts a date to a very easy readable string
     * @param date  **/
    public static String convertToReadableDate(String date) {
        String value = "";
        @SuppressLint("SimpleDateFormat") SimpleDateFormat parser
                = new SimpleDateFormat("dd/MM/yyyy");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format
                = new SimpleDateFormat("EEE MMM d yyyy");
        Date fromDate;

        try {
            fromDate = parser.parse(date);
            value = format.format(fromDate);
        } catch (ParseException ignored) {

        }

        return value;
    }

    /** this method calculates a time in milliseconds for startDate and startTime
     * @param startDate
     * @param startTime **/
    public static long calculateTimeInMillis(String startDate, String startTime) {
        long longTimeInMillis = 0L;
        @SuppressLint("SimpleDateFormat") SimpleDateFormat parser
                = new SimpleDateFormat("dd/MM/yyyy");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat
                = new SimpleDateFormat("dd/MM/yyyy");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat hourformat
                = new SimpleDateFormat("HH:mm");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat minutesFormat
                = new SimpleDateFormat("mm");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat yearFormat
                = new SimpleDateFormat("yyyy");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dayFormat
                = new SimpleDateFormat("dd");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat monthFormat
                = new SimpleDateFormat("MM");

        Calendar calendar = GregorianCalendar.getInstance();
        Date fromDate, selectedDate, selectedTime;
        startDate = startDate.concat(" " + startTime);
        try {
            fromDate = parser.parse(startDate);
            selectedTime = hourformat.parse(startTime);
            String time = hourformat.format(selectedTime);
            int year = Utilities.getIntegerValue(yearFormat.format(fromDate));
            int month = Utilities.getIntegerValue(monthFormat.format(fromDate));
            int day = Utilities.getIntegerValue(dayFormat.format(fromDate));
            String times[] = time.split(":");
            calendar.setTime(fromDate);
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(times[0]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(times[1]));

            longTimeInMillis = calendar.getTimeInMillis();


        } catch (ParseException e) {
            // TODO: 11/04/2018 fix this exception
        }

        return longTimeInMillis;
    }

    /** this method converts String to Integer
     * @param value **/
    private static int getIntegerValue(String value) {
        return Integer.parseInt(value);
    }
}
