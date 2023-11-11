package rodrigues.henrique.myapplication2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MyListFragment extends ListFragment {
    int mCurCheckPosition = 0;
    boolean mSingleActivity;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setListAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_activated_1,
                DummyData.DATA_HEADINGS));

        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        View contentFrame = getActivity().findViewById(R.id.content);
        mSingleActivity = contentFrame != null && contentFrame.getVisibility() == View.VISIBLE; //setting boolean value depending if landscape or note

        if (savedInstanceState != null) {
            //restore last state for checked position
            mCurCheckPosition = savedInstanceState.getInt("curChoice",0);
        }

        if (mSingleActivity) {
            showContent(mCurCheckPosition);
        }
        else {
            getListView().setItemChecked(mCurCheckPosition,true);
        }
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        showContent(position); //this tells where in the list we clicked
    }
    void showContent(int index) {
        mCurCheckPosition = index;

        if(mSingleActivity) {
            getListView().setItemChecked(index,true);

            //check what fragment is currently shown, replace if needed.
            ListItemFragment content = (ListItemFragment) getFragmentManager().findFragmentById(R.id.content);
            if (content == null || content.getShownIndex() != index){
                //Make new fragment show this selection

                content = ListItemFragment.newInstance(index);

                //Execute a transaction, replacing any existing fragment
                //with this one inside the frame
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content, content);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragmentTransaction.commit();
            }
        }
        else {
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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("curChoice", mCurCheckPosition);
    }
}
