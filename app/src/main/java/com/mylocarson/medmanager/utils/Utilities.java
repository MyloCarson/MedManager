package com.mylocarson.medmanager.utils;

import android.app.DatePickerDialog;
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
        if (editText.getText().toString().isEmpty() && !(editText.getText().toString().length() > 0) ){
            result = false;
        }else{
            result = true;
        }

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
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
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

    public static ArrayList<Long> getArrayListOfLongTimeMillis(String startDate, String endDate){
        ArrayList<Long> longArrayListOfTimeMillis = new ArrayList<>();

        Date fromDate,toDate;
        Calendar calendar = new GregorianCalendar();

        Calendar calendar2 = new GregorianCalendar();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
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
            }else if (toDate.equals(fromDate)){
                value = true;
            }
            else{
                value = false;
            }
        }catch (ParseException e){
            // TODO: 07/04/2018 handle this exception
        }
        return value;
    }


}