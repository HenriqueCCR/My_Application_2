package rodrigues.henrique.myapplication2;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

public class MyViewModel extends AndroidViewModel {
    private LiveData<ArrayList<Item>> mItems;
    private LiveData<Item> mSelectedItem;
    private ItemsRepository mItemRepository;
    private int mSelectedIndex;

    public MyViewModel(@NonNull Application pApplication) {
        super(pApplication);
        mItemRepository = ItemsRepository.getInstance(getApplication());
    }

    public LiveData<ArrayList<Item>> getItems(){
        if(mItems == null) {
            mItems = mItemRepository.getItems();
        }
        return mItems; // This Live data is "read only"
    }

    public LiveData<Item> getItem(int pItemIndex){
        return mItemRepository.getItem(pItemIndex);
    }

    public void selectItem(int pIndex) {
        if(pIndex != mSelectedIndex || mSelectedItem == null) {
            mSelectedIndex = pIndex;
            mSelectedItem = getItem(mSelectedIndex);
        }
    }

    public LiveData<Item> getSelectedItem() {
        return mSelectedItem;
    }
}
