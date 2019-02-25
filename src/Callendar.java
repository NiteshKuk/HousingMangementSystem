package com.example.nitesh.housingmanagmentapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.stacktips.view.CalendarListener;
import com.stacktips.view.CustomCalendarView;
import com.stacktips.view.DayDecorator;
import com.stacktips.view.DayView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Callendar extends AppCompatActivity {
    CustomCalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callendar);


        calendarView = (CustomCalendarView) findViewById(R.id.calendar_view);
        Calendar currentCalendar = Calendar.getInstance(Locale.getDefault());
        calendarView.setFirstDayOfWeek(Calendar.MONDAY);
        calendarView.setShowOverflowDate(false);
        calendarView.refreshCalendar(currentCalendar);


        calendarView.setCalendarListener(new CalendarListener() {
            @Override
            public void onDateSelected(Date date) {
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");

                if (!isPastDay(date))
                    Toast.makeText(Callendar.this, df.format(date), Toast.LENGTH_SHORT).show();
                Intent n = new Intent(Callendar.this, BillsActivity.class);
                n.putExtra("Date", df.format(date).toString());
                startActivity(n);

            }

            @Override
            public void onMonthChanged(Date date) {
                SimpleDateFormat df = new SimpleDateFormat("MM-yyyy");
                if (!isPastDay(date))
                    Toast.makeText(Callendar.this, df.format(date), Toast.LENGTH_SHORT).show();
            }
        });
        List decorators = new ArrayList<>();
        decorators.add(new DaysDecorator());
        calendarView.setDecorators(decorators);
        calendarView.refreshCalendar(currentCalendar);
    }

    private class DaysDecorator implements DayDecorator {
        @Override
        public void decorate(DayView dayView) {
            if (!isPastDay(dayView.getDate())) {
                dayView.setBackgroundColor(getResources().getColor(R.color.grey));

            }
            if (isPastDay(dayView.getDate())) {
                dayView.setVisibility(View.INVISIBLE);
            }
        }
    }

    private boolean isPastDay(Date date) {
        Calendar c = Calendar.getInstance();

        // set the calendar to start of today
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        // and get that as a Date
        Date today = c.getTime();

        // test your condition, if Date specified is before today
        if (date.before(today)) {
            return true;
        }
        return false;
    }
}