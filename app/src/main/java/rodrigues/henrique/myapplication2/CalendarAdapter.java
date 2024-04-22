package rodrigues.henrique.myapplication2;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder>{ //might need to change to just 'class'
    private final ArrayList<LocalDate> days;
    private final OnItemListener onItemListener;
    private final Context context;

    public CalendarAdapter(ArrayList<LocalDate> days, OnItemListener onItemListener, Context context) {
        this.days = days;
        this.onItemListener = onItemListener;
        this.context = context;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (days.size() > 15){ // Month view
            //layoutParams.height = (int) (parent.getHeight() * 0.166666); // Each cell is a sixth of the full view
            layoutParams.height = (int) (parent.getHeight() * 0.177777);
        }
        else{ // Week view
            layoutParams.height = (int) parent.getHeight();
        }

        return new CalendarViewHolder(view, onItemListener, days);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        final LocalDate date = days.get(position);

        if (date == null){
            holder.dayOfMonth.setText("");
        }
        else{
            String calendarDateString = date.toString();
            String[] calendarDateParts = calendarDateString.split("-");
            String calendarDay = calendarDateParts[2];
            String calendarMonth = calendarDateParts[1];
            String calendarYear = calendarDateParts[0];

            holder.dayOfMonth.setText(String.valueOf(date.getDayOfMonth()));

            ArrayList<EventStrings> storedEvents = CalendarUtils.getStoredEvents(context);
            for(int i = 0; i < storedEvents.size(); i++) {
                String[] eventDateParts = (storedEvents.get(i).getDate()).split(" ");
                String eventDay = eventDateParts[0];
                String eventMonth = String.valueOf(CalendarUtils.getMonthInt(eventDateParts[1]) + 1);
                String eventYear = eventDateParts[2];

                if (eventMonth.length() == 1) {
                    eventMonth = "0" + eventMonth;
                }

                if (isSameDate(calendarDay, calendarMonth, calendarYear, eventDay, eventMonth, eventYear)) {
                    holder.eventCircle.setVisibility(View.VISIBLE);
                }
            }

            ArrayList<LogStrings> storedLogs = CalendarUtils.getStoredLogs(context);
            for(int i = 0; i < storedLogs.size(); i++) {
                String[] logDateParts = (storedLogs.get(i).getDate()).split(" ");
                String logDay = logDateParts[0];
                String logMonth = String.valueOf(CalendarUtils.getMonthInt(logDateParts[1]) + 1);
                String logYear = logDateParts[2];

                if (logMonth.length() == 1) {
                    logMonth = "0" + logMonth;
                }

                if (isSameDate(calendarDay, calendarMonth, calendarYear, logDay, logMonth, logYear)) {
                    holder.logCircle.setVisibility(View.VISIBLE);
                }
            }

            if (date.equals(CalendarUtils.selectedDate)) {
                holder.parentView.setBackgroundColor(Color.LTGRAY); // When date is clicked turn background light gray
            }
        }
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public interface OnItemListener{
        void onItemClick(int position, LocalDate date);
    }
    private boolean isSameDate(String calendarDay, String calendarMonth, String calendarYear, String comparisonDay, String comparisonMonth, String comparisonYear) {
        boolean check = Integer.parseInt(calendarDay)  == Integer.parseInt(comparisonDay) && Integer.parseInt(calendarMonth)  == Integer.parseInt(comparisonMonth) && Integer.parseInt(calendarYear)  == Integer.parseInt(comparisonYear);
        return check;
    }
}
