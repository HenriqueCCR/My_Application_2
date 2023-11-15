package rodrigues.henrique.myapplication2;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

public class MyViewModel extends AndroidViewModel {
    private MutableLiveData<ArrayList<Item>> mItems;
    private MutableLiveData<Item> mSelectedItem;
    private int mSelectedIndex;

    public MyViewModel(@NonNull Application pApplication) {
        super(pApplication);
        getItems();
    }

    private void generateItems() {
        ArrayList<Item> items = new ArrayList<Item>();
        items.add(new Item("First","Link1","15/11/23","BleepBloopBlurp1"));
        items.add(new Item("Second","Link1","16/11/23","BleepBloopBlurp2"));
        items.add(new Item("Third","Link1","17/11/23","BleepBloopBlurp3"));
        items.add(new Item("Fourth","Link1","18/11/23","BleepBloopBlurp4"));
        items.add(new Item("Fifth","Link1","19/11/23","BleepBloopBlurp5"));
        mItems.setValue(items);
    }

    public LiveData<ArrayList<Item>> getItems(){
        if(mItems == null) {
            mItems = new MutableLiveData<ArrayList<Item>>();
            generateItems();
            selectItem(0);
        }
        return mItems; // This Live data is "read only"
    }

    public void selectItem(int pIndex) {
        mSelectedIndex = pIndex;
        Item selectedItem = mItems.getValue().get(mSelectedIndex);
        mSelectedItem = new MutableLiveData<Item>();
        mSelectedItem.setValue(selectedItem);
    }

    public LiveData<Item> getSelectedItem() {
        if (mSelectedItem == null) {
            mSelectedItem = new MutableLiveData<Item>();
            selectItem(mSelectedIndex);
        }
        return mSelectedItem;
    }
}
