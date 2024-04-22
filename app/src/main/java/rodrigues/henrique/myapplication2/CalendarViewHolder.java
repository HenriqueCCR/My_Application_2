package rodrigues.henrique.myapplication2;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private final ArrayList<LocalDate> days;
    public final View parentView;
    public final TextView dayOfMonth;
    private final CalendarAdapter.OnItemListener onItemListener;
    public final Button eventCircle;

    public CalendarViewHolder(@NonNull View itemView, CalendarAdapter.OnItemListener onItemListener, ArrayList<LocalDate> days) {
        super(itemView);
        parentView = itemView.findViewById(R.id.parentView);
        dayOfMonth = itemView.findViewById(R.id.cellDayText);
        eventCircle = itemView.findViewById(R.id.eventCircle);
        this.onItemListener = onItemListener;
        itemView.setOnClickListener(this);
        this.days = days;

        // Event circle needs to be visible when event is created for that date
        // Add event circle to ?calendar layout? when new event is created

        /*String currentDayOfMonth = dayOfMonth.getText().toString();
        if (currentDayOfMonth.length() == 1) {
            currentDayOfMonth = "0" + currentDayOfMonth;
        }

        ArrayList<EventStrings> storedEvents = CalendarUtils.getStoredEvents(parentView.getContext());
        // Display event circle marker in calendar cell
        for(int i = 0; i < days.size(); i++) {
            // If days(i) == storedEvents.get(i).getDate()
            // eventCircle.setVisibility(View.VISIBLE);
            String date = storedEvents.get(i).getDate();
            String eventDayOfMonth = date.substring(0,2);

            if (currentDayOfMonth == eventDayOfMonth) {
                eventCircle.setVisibility(View.VISIBLE);
            }
        }*/
    }

    @Override
    public void onClick(View view) {
        onItemListener.onItemClick(getAdapterPosition(), days.get(getAdapterPosition()));
    }
}
