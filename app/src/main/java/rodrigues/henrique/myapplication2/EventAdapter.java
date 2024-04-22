package rodrigues.henrique.myapplication2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class EventAdapter extends ArrayAdapter<Event> {
    Toast t;

    public EventAdapter(@NonNull Context context, List<Event> events) {
        super(context, 0, events);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Event event = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.cell, parent, false);
        }

        TextView eventCellTextView = convertView.findViewById(R.id.cellTextView);
        Button deleteButton = convertView.findViewById(R.id.deleteButton);

        String eventTitle = event.getName() + " " + event.getTime();
        eventCellTextView.setText(eventTitle);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click
                // You can access position or data related to this row here
                String toast = "Deleted Event item: " + event.getName() + " " + event.getTime();
                makeToast(toast);

                // Assuming you have a method to remove the event from your data source
                remove(event);

                // Notify the adapter that the data set has changed
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    private void makeToast (String s) {
        if (t != null) t.cancel();
        t = Toast.makeText(getContext(), s, Toast.LENGTH_SHORT);
        t.show();
    }
}
