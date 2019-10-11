package tiab.is.hard.SlidingMenuLayout;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import tiab.is.hard.BigBoyPageTabs.FridgeAdapter;
import tiab.is.hard.R;

import static tiab.is.hard.BigBoyPageTabs.BigBoyPage.canned;
import static tiab.is.hard.BigBoyPageTabs.BigBoyPage.miscellaneous;
import static tiab.is.hard.BigBoyPageTabs.BigBoyPage.spices;
import static tiab.is.hard.BigBoyPageTabs.BigBoyPage.vegetables;
import static tiab.is.hard.BigBoyPageTabs.BigBoyPage.fruits;
import static tiab.is.hard.BigBoyPageTabs.BigBoyPage.dairy;
// TODO: Ingredient Types - Importing


public class ContentFragment extends Fragment {

    private static final String KEY_TITLE = "title";
    private static final String KEY_INDICATOR_COLOR = "indicator_color";
    private static final String KEY_DIVIDER_COLOR = "divider_color";

    public static RecyclerView rvContactsvegetables;
    public static FridgeAdapter adapter1;
    public static RecyclerView rvContactsspices;
    public static FridgeAdapter adapter2;
    public static RecyclerView rvContactsmiscellaneous;
    public static FridgeAdapter adapter3;
    public static RecyclerView rvContactscanned;
    public static FridgeAdapter adapter4;
    public static RecyclerView rvContactsfruits;
    public static FridgeAdapter adapter5;
    public static RecyclerView rvContactsdairy;
    public static FridgeAdapter adapter6;
    // TODO: Ingredient Types - Declaration (RecyclerViews & Adapters)


    public static ContentFragment newInstance(CharSequence title, int indicatorColor,
                                              int dividerColor) {
        Bundle bundle = new Bundle();
        bundle.putCharSequence(KEY_TITLE, title);
        bundle.putInt(KEY_INDICATOR_COLOR, indicatorColor);
        bundle.putInt(KEY_DIVIDER_COLOR, dividerColor);

        ContentFragment fragment = new ContentFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.contentfragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();

        if (args != null) {

            //for use in separating into categories
            CharSequence temp = args.getCharSequence(KEY_TITLE);
            assert temp != null;
            String temper = temp.toString();

            if(temper.equals(getString(R.string.vegetables))){
                rvContactsvegetables = view.findViewById(R.id.rvContacts);
                adapter1 = new FridgeAdapter(vegetables);
                rvContactsvegetables.setAdapter(adapter1);
                rvContactsvegetables.setLayoutManager(new LinearLayoutManager(getContext()));
                rvContactsvegetables.setItemViewCacheSize(1000);

            } else if(temper.equals(getString(R.string.spices))){
                rvContactsspices = view.findViewById(R.id.rvContacts);
                adapter2 = new FridgeAdapter(spices);
                rvContactsspices.setAdapter(adapter2);
                rvContactsspices.setLayoutManager(new LinearLayoutManager(getContext()));
                rvContactsspices.setItemViewCacheSize(1000);

            } else if(temper.equals(getString(R.string.miscellaneous))){
                rvContactsmiscellaneous = view.findViewById(R.id.rvContacts);
                adapter3 = new FridgeAdapter(miscellaneous);
                rvContactsmiscellaneous.setAdapter(adapter3);
                rvContactsmiscellaneous.setLayoutManager(new LinearLayoutManager(getContext()));
                rvContactsmiscellaneous.setItemViewCacheSize(1000);

            } else if(temper.equals(getString(R.string.canned))){
                rvContactscanned = view.findViewById(R.id.rvContacts);
                adapter4 = new FridgeAdapter(canned);
                rvContactscanned.setAdapter(adapter4);
                rvContactscanned.setLayoutManager(new LinearLayoutManager(getContext()));
                rvContactscanned.setItemViewCacheSize(1000);
            } else if(temper.equals(getString(R.string.fruits))){
                rvContactsfruits = view.findViewById(R.id.rvContacts);
                adapter5 = new FridgeAdapter(fruits);
                rvContactsfruits.setAdapter(adapter5);
                rvContactsfruits.setLayoutManager(new LinearLayoutManager(getContext()));
                rvContactsfruits.setItemViewCacheSize(1000);

            } else if(temper.equals(getString(R.string.dairy))){
                rvContactsdairy = view.findViewById(R.id.rvContacts);
                adapter6 = new FridgeAdapter(dairy);
                rvContactsdairy.setAdapter(adapter6);
                rvContactsdairy.setLayoutManager(new LinearLayoutManager(getContext()));
                rvContactsdairy.setItemViewCacheSize(1000);
            }
            // TODO: Ingredient Types - Display The Currently Viewed Category

        }
    }
}
