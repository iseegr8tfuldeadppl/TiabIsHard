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

import static tiab.is.hard.MainActivity.print;

public class PlatePageStepAdapter extends RecyclerView.Adapter<PlatePageStepAdapter.ViewHolder> {


    private String divider;
    private List<PlatePage.Step> mitems;
    private LayoutInflater mInflater;

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView step;
        TextView count;

        ViewHolder(View itemView) {
            super(itemView);
            step = itemView.findViewById(R.id.step);
            count = itemView.findViewById(R.id.count);
        }
    }

    PlatePageStepAdapter(Context context, List<PlatePage.Step> items) {
        mitems = items;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public PlatePageStepAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View contactView = mInflater.inflate(R.layout.steps_of_a_plate_sample, parent, false);
        return new PlatePageStepAdapter.ViewHolder(contactView); }
    @Override
    public int getItemCount() { return mitems.size(); }


    @Override
    public void onBindViewHolder(@NonNull final PlatePageStepAdapter.ViewHolder viewHolder, final int position) {
        final PlatePage.Step item = mitems.get(position);
        divider = viewHolder.itemView.getContext().getString(R.string.divider);
        viewHolder.step.setText(item.TEXT + ".");
        viewHolder.count.setText(divider + "  " + item.POSITION);
    }


}