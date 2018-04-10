package com.mylocarson.medmanager.utils;

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
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by user on 05/04/2018.
 */

public class Utilities {
    private static final String TAG  = Utilities.class.getSimpleName();

    private Utilities(){}

    public static void validateEditText(EditText...editTexts){
        for (EditText editText: editTexts) {
            if (editText.getText().toString().isEmpty() && !(editText.getText().toString().length() > 0) ){
                editText.setError(Constants.EDIT_TEXT_REQUIRED);
            }
        }
    }

    public static boolean isEditTextValid(EditText editText){
        boolean result = false;
        result = !(editText.getText().toString().isEmpty() && !(editText.getText().toString().length() > 0));

        return result;
    }

    public static String  convertDate(@NonNull String mDate){
        String newDateString = "";
        String dDate;
        String today_string;
        Date mdate;
        Date theDate;
        Date today;
        SimpleDateFormat parser=new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        SimpleDateFormat simpleFormat = new SimpleDateFormat("E MMM d yyyy");
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
            }
            else if (diffMins > 0 && diffMins < 60 && diffHours < 1 && diffDays < 1){
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

    public static ArrayAdapter<String> arrayAdapter(Context context,String[] arrayOfStrings){
         ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                context,android.R.layout.simple_list_item_1,arrayOfStrings){
            @Override
            public boolean isEnabled(int position){
                return position != 0;
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
       return spinnerArrayAdapter;
    }

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

    public  static void showDatePicker(Context context, final EditText editText){
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month++;
                editText.setText(day +"/"+ month  +"/" +year);
            }
        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public static void showTimePicker(Context context, final EditText editText) {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minutes) {
                editText.setText("" + hour + ":" + minutes);
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    public static ArrayList<Long> getArrayListOfLongTimeMillis(String startDate, String endDate, String startTime) {
        ArrayList<Long> longArrayListOfTimeMillis = new ArrayList<>();

        Date fromDate,toDate;
        Calendar calendar = new GregorianCalendar();

        Calendar calendar2 = new GregorianCalendar();

        startDate = startDate.concat(" " + startTime);
        endDate = endDate.concat(" " + startTime);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:MM");
        try {
            fromDate = simpleDateFormat.parse(startDate);
            calendar.setTime(fromDate);
//			System.out.println(date2);
            toDate = simpleDateFormat.parse(endDate);
            calendar2.setTime(toDate);
            calendar.setTime(fromDate);


            longArrayListOfTimeMillis.add(calendar.getTimeInMillis());

            while (calendar.before(calendar2)) {

                calendar.add(Calendar.DATE, 1);
                longArrayListOfTimeMillis.add(calendar.getTimeInMillis());

            }
        }
        catch (Exception e) {
            // TODO: handle exception
        }
        return longArrayListOfTimeMillis;

    }

    public static long calculateRepeatTime(int frequency){
        int hourInDay = 24 / frequency;
        int minutesInDay = 24 % frequency;

        long hourInMilliSecs = hourInDay * 60 *60 * 1000;
        long minInMilliSecs = minutesInDay * 60 * 1000;

        return  hourInMilliSecs+minInMilliSecs;

    }

    public static boolean validateStartDate(String startDate, String endDate){
        boolean value = false;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date fromDate,toDate;

        try{
            fromDate = simpleDateFormat.parse(startDate);
            toDate = simpleDateFormat.parse(endDate);

            if (fromDate.before(toDate)){
                value = true;
            } else value = toDate.equals(fromDate);
        }catch (ParseException e){
            // TODO: 07/04/2018 handle this exception
        }
        return value;
    }

    public static ArrayList<Long> calculateAlarmtimeInMillis(Context context, String startDate, String startTime, int frequency) {
        ArrayList<Long> longArrayListOfTimeMillis = new ArrayList<>();
        Date offsetDate, fromDate;
        int offsetHour, offsetMins;
//         startDate ="10/4/2017";
//         frequency = 4;
//        startTime = "7:9";
        Calendar calendar = Calendar.getInstance();
        startDate = startDate.concat(" " + startTime);

        SimpleDateFormat parser = new SimpleDateFormat("dd/MM/yyyy HH:MM");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat hourformat = new SimpleDateFormat("HH");
        SimpleDateFormat minutesFormat = new SimpleDateFormat("MM");
        try {
            offsetDate = parser.parse(startDate);
            fromDate = simpleDateFormat.parse(simpleDateFormat.format(offsetDate));
            offsetHour = Integer.parseInt(hourformat.format(offsetDate));
            offsetMins = Integer.parseInt(minutesFormat.format(offsetDate));
            calendar.setTime(fromDate);
            int range = 24 / frequency;

            int newOffset;


            for (int i = 0; i <= frequency; i++) {
                newOffset = offsetHour + range++;
                Toast.makeText(context, "" + newOffset, Toast.LENGTH_SHORT).show();

                if (newOffset > 24) {
                    newOffset = newOffset % 24;
                    calendar.add(Calendar.DATE, 1);
                    calendar.add(Calendar.HOUR_OF_DAY, newOffset);
                    calendar.add(Calendar.MINUTE, offsetMins);
                    longArrayListOfTimeMillis.add(calendar.getTimeInMillis());
                    Toast.makeText(context, "Longtime inner " + calendar.getTimeInMillis(), Toast.LENGTH_SHORT).show();
                }
                calendar.add(Calendar.HOUR_OF_DAY, newOffset);
                calendar.add(Calendar.MINUTE, offsetMins);
                longArrayListOfTimeMillis.add(calendar.getTimeInMillis());
                Toast.makeText(context, "Longtime " + calendar.getTimeInMillis(), Toast.LENGTH_SHORT).show();
            }

        } catch (ParseException e) {
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "calculateAlarmtimeInMillis: ", e);
        }

        return longArrayListOfTimeMillis;

    }

    public static String convertToReadableDate(String date) {
        String value = "";
        SimpleDateFormat parser = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat format = new SimpleDateFormat("EEE MMM d yyyy");
        Date fromDate;

        try {
            fromDate = parser.parse(date);
            value = format.format(fromDate);
        } catch (ParseException e) {
        }

        return value;
    }

}
