package rodrigues.henrique.myapplication2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MyListFragment extends ListFragment {
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setListAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_activated_1,
                DummyData.DATA_HEADINGS));

        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        showContent(position); //this tells where in the list we clicked
    }
    void showContent(int index) {
        //Create an intent for starting the DetailsActivity
        Intent intent = new Intent();

        //explicitly set the activity context and class
        //associated with the intent (context, class)
        intent.setClass(getActivity(), ItemActivity.class);

        //pass the current position
        intent.putExtra("index", index);

        startActivity(intent);
    }
}
