package rodrigues.henrique.myapplication2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class LogAdapter extends ArrayAdapter<Log> {
    public LogAdapter(@NonNull Context context, List<Log> logs) { super(context, 0, logs); }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Log logs = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.cell, parent, false);
        }

        TextView logCellTextView = convertView.findViewById(R.id.cellTextView);

        String logTitle = logs.getName() + " " + logs.getDistance() + " " + logs.getTime();
        logCellTextView.setText(logTitle);
        return convertView;
    }
}
