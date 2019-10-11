package tiab.is.hard.PlatePage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.File;
import java.util.List;
import tiab.is.hard.R;

public class PlatePageIngredientAdapter extends RecyclerView.Adapter<PlatePageIngredientAdapter.ViewHolder> {


    private PlatePage.Ingredient item;
    private List<PlatePage.Ingredient> mitems;
    private LayoutInflater mInflater;

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView image;

        ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
        }
    }

    PlatePageIngredientAdapter(Context context, List<PlatePage.Ingredient> items) {
        mitems = items;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public PlatePageIngredientAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View contactView = mInflater.inflate(R.layout.ingredients_of_a_plate_sample, parent, false);
        return new PlatePageIngredientAdapter.ViewHolder(contactView); }
    @Override
    public int getItemCount() { return mitems.size(); }


    @Override
    public void onBindViewHolder(@NonNull final PlatePageIngredientAdapter.ViewHolder viewHolder, final int position) {
        item = mitems.get(position);
        viewHolder.name.setText(item.QUANTITY.split(":")[0]);

        try {
            Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + File.separator + "Android/data/tiab.is.hard/files/ingredients/" + item.DISPLAY);
            //Bitmap bitmap = decodeSampledBitmapFromPath(Environment.getExternalStorageDirectory() + File.separator + "Android/data/tiab.is.hard/files/ingredients/" + item.DISPLAY, 60, 60); // original 60 60
            viewHolder.image.setImageBitmap(bitmap);
        } catch(Exception ignored){}
    }


}