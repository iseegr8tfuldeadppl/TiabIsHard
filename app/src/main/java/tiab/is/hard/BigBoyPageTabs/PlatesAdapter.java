package tiab.is.hard.BigBoyPageTabs;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.io.File;
import java.util.List;
import tiab.is.hard.PlatePage.PlatePage;
import tiab.is.hard.R;

public class PlatesAdapter extends RecyclerView.Adapter<PlatesAdapter.ViewHolder> {


    private List<BigBoyPage.ListViewItem2> mitems;

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView ingredient_name;
        tiab.is.hard.Round.RoundedImageView ingredient_display;
        RelativeLayout ingredient_listing;
        TextView calories;
        TextView cooking_time;

        ViewHolder(View itemView) {
            super(itemView);

            ingredient_name = itemView.findViewById(R.id.ingredient_name);
            calories = itemView.findViewById(R.id.calories);
            cooking_time = itemView.findViewById(R.id.cooking_time);
            ingredient_display = itemView.findViewById(R.id.ingredient_display);
            ingredient_listing = itemView.findViewById(R.id.ingredient_listing);
        }
    }

    PlatesAdapter(List<BigBoyPage.ListViewItem2> items) { mitems = items; }


    @NonNull
    @Override
    public PlatesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.plate_item, parent, false);
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
        // Step 1: Display info and image
        final BigBoyPage.ListViewItem2 item = mitems.get(position);
        viewHolder.ingredient_name.setText(item.NAME);
        viewHolder.calories.setText(viewHolder.itemView.getContext().getString(R.string.calories) + " " + item.CALORIES);
        viewHolder.cooking_time.setText(item.TIME + "min");

        try {
            //int resId = viewHolder.itemView.getContext().getResources().getIdentifier(item.DISPLAY , "drawable", viewHolder.itemView.getContext().getPackageName());
            //viewHolder.ingredient_display.setImageResource(resId);
            //Bitmap bitmap = decodeSampledBitmapFromPath(Environment.getExternalStorageDirectory() + File.separator + "Android/data/tiab.is.hard/files/ingredients/" + item.DISPLAY, 60, 60); // original 60 60
            //BitmapDrawable ob = new BitmapDrawable(viewHolder.itemView.getResources(), bitmap);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + File.separator + "Android/data/tiab.is.hard/files/plates/" + item.DISPLAY, options);
            viewHolder.ingredient_display.setImageBitmap(bitmap);
        } catch(Exception ignored){}

        // Step 2: Set a click listener
        viewHolder.ingredient_listing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent display_tutorial = new Intent(viewHolder.itemView.getContext(), PlatePage.class);
                Bundle b=new Bundle();
                b.putString("plate", item.NAME);
                display_tutorial.putExtras(b);
                viewHolder.itemView.getContext().startActivity(display_tutorial);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mitems.size();
    }
}