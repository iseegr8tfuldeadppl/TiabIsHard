package tiab.is.hard.BigBoyPageTabs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import tiab.is.hard.R;

import static tiab.is.hard.BigBoyPageTabs.BigBoyPage.IngredientNames;
import static tiab.is.hard.BigBoyPageTabs.BigBoyPage.allPlates;
import static tiab.is.hard.BigBoyPageTabs.BigBoyPage.current_search_word;
import static tiab.is.hard.BigBoyPageTabs.BigBoyPage.current_tab;
import static tiab.is.hard.BigBoyPageTabs.BigBoyPage.iwasclicked;
import static tiab.is.hard.BigBoyPageTabs.BigBoyPage.possiblepage;
import static tiab.is.hard.BigBoyPageTabs.BigBoyPage.rvContactsAllPlates;
import static tiab.is.hard.BigBoyPageTabs.BigBoyPage.rvContactsPossiblePlates;
import static tiab.is.hard.BigBoyPageTabs.BigBoyPage.search_array;

public class TypesAdapter extends RecyclerView.Adapter<TypesAdapter.ViewHolder> {

    public static BigBoyPage.ListViewItem2 plate;
    private List<BigBoyPage.PlateType> mitems;

    class ViewHolder extends RecyclerView.ViewHolder {

        Button type;

        ViewHolder(View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.type);
        }
    }

    TypesAdapter(List<BigBoyPage.PlateType> items) { mitems = items; }

    @NonNull
    @Override
    public TypesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.type_item, parent, false);
        return new ViewHolder(contactView);
    }

    @Override
    public int getItemCount() {
        return mitems.size();
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
        final BigBoyPage.PlateType item = mitems.get(position);
        viewHolder.type.setText(item.TYPE);

        viewHolder.type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iwasclicked = position;
                notifyDataSetChanged();
            }
        });
        if(iwasclicked==position) {
            current_tab = viewHolder.type.getText().toString();

            if(BigBoyPage.platespage) {
                // Pull the plates for the current tab
                BigBoyPage.current_tab_plates = new ArrayList<>();
                for (int i = 0; i < allPlates.size(); i++) {
                    plate = allPlates.get(i);
                    if (plate.CATEGORIES.contains(current_tab))
                        BigBoyPage.current_tab_plates.add(plate);
                }

                // If we're in a searching state then apply the previous search word on the array
                if (!BigBoyPage.current_search_word.equals("")) {
                    search_array = new ArrayList<>();
                    for (int i = 0; i < BigBoyPage.current_tab_plates.size(); i++) {
                        BigBoyPage.ingredient_english = "";
                        BigBoyPage.ListViewItem2 item2 = BigBoyPage.current_tab_plates.get(i);
                        IngredientNames = item2.INGREDIENTS.split(",");

                        // Bring all french words of ingredients in plate to also search in
                        for (int j = 1; j < IngredientNames.length; j++) {
                            for (int z = 0; z < BigBoyPage.AllIngredients.size(); z++) {
                                BigBoyPage.ListViewItem ingredient = BigBoyPage.AllIngredients.get(z);
                                if (IngredientNames[j].equals(ingredient.NAME))
                                    BigBoyPage.ingredient_english += ingredient.DISPLAY;
                            }
                        }

                        if (BigBoyPage.ingredient_english.contains(current_search_word) ||
                                item2.NAME.contains(current_search_word) ||
                                item2.INGREDIENTS.contains(current_search_word) ||
                                item2.DISPLAY.contains(current_search_word))
                            search_array.add(item2);
                    }
                    BigBoyPage.search_adapter = new PlatesAdapter(search_array);
                    rvContactsAllPlates.setAdapter(BigBoyPage.search_adapter);
                } else {
                    // Else If we're not searching just refresh the plates adapter
                    BigBoyPage.adapter3 = new PlatesAdapter(BigBoyPage.current_tab_plates);
                    rvContactsAllPlates.setAdapter(BigBoyPage.adapter3);
                }
            } else if(possiblepage){
                // Pull the plates for the current tab
                BigBoyPage.current_tab_plates = new ArrayList<>();
                for (int i = 0; i < BigBoyPage.possible_items.size(); i++) {
                    plate = BigBoyPage.possible_items.get(i);
                    if (plate.CATEGORIES.contains(current_tab))
                        BigBoyPage.current_tab_plates.add(plate);
                }

                // If we're in a searching state then apply the previous search word on the array
                if (!BigBoyPage.current_search_word.equals("")) {
                    search_array = new ArrayList<>();
                    for (int i = 0; i < BigBoyPage.current_tab_plates.size(); i++) {
                        BigBoyPage.ingredient_english = "";
                        BigBoyPage.ListViewItem2 item2 = BigBoyPage.current_tab_plates.get(i);
                        IngredientNames = item2.INGREDIENTS.split(",");

                        // Bring all french words of ingredients in plate to also search in
                        for (int j = 1; j < IngredientNames.length; j++) {
                            for (int z = 0; z < BigBoyPage.AllIngredients.size(); z++) {
                                BigBoyPage.ListViewItem ingredient = BigBoyPage.AllIngredients.get(z);
                                if (IngredientNames[j].equals(ingredient.NAME))
                                    BigBoyPage.ingredient_english += ingredient.DISPLAY;
                            }
                        }

                        if (BigBoyPage.ingredient_english.contains(current_search_word) ||
                                item2.NAME.contains(current_search_word) ||
                                item2.INGREDIENTS.contains(current_search_word) ||
                                item2.DISPLAY.contains(current_search_word))
                            search_array.add(item2);
                    }
                    BigBoyPage.search_adapter = new PlatesAdapter(search_array);
                    rvContactsPossiblePlates.setAdapter(BigBoyPage.search_adapter);
                } else {
                    // Else If we're not searching just refresh the plates adapter
                    BigBoyPage.adapter4 = new PlatesAdapter(BigBoyPage.current_tab_plates);
                    BigBoyPage.rvContactsPossiblePlates.setAdapter(BigBoyPage.adapter4);
                }
            }
            // Set the selected tab background
            viewHolder.type.setBackgroundDrawable(viewHolder.itemView.getContext().getResources().getDrawable(R.drawable.clicked_type_background));
        } else // Set the not selected tabs background
            viewHolder.type.setBackgroundDrawable(viewHolder.itemView.getContext().getResources().getDrawable(R.drawable.type_background));
    }


}