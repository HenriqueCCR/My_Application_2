package rodrigues.henrique.myapplication2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class LogAdapter extends ArrayAdapter<Logg> {
    Toast t;
    public LogAdapter(@NonNull Context context, List<Logg> loggs) { super(context, 0, loggs); }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Logg log = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.cell, parent, false);
        }

        //Initialize Objects in List Item
        TextView logCellTextView = convertView.findViewById(R.id.cellTextView);
        Button deleteButton = convertView.findViewById(R.id.deleteButton);

        String logTitle = log.getName() + " " + log.getDistance() + "Km" + " " + log.getTime();
        logCellTextView.setText(logTitle);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click
                // You can access position or data related to this row here
                String toast = "Deleted Event item: " + log.getName() + " " + log.getDistance() + " KM";
                makeToast(toast);

                // Remove the event from your data source
                CalendarUtils.removeLog(getContext(),log); // Remove form source
                remove(log); // Remove from visible list to update visually

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
