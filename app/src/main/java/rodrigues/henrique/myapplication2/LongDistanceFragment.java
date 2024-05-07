package rodrigues.henrique.myapplication2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class LongDistanceFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(RunningInformationActivity.windowTitle)
                .setMessage(RunningInformationActivity.popupInfoWindowTextView.getText())
                .setPositiveButton("OK", null); // You can add buttons here if needed
        return builder.create();
    }
}
