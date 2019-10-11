package tiab.is.hard.BigBoyPageTabs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.io.File;
import java.util.List;
import tiab.is.hard.R;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static tiab.is.hard.BigBoyPageTabs.BigBoyPage.selected_items;
public class FridgeAdapter extends RecyclerView.Adapter<FridgeAdapter.ViewHolder> {


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView ingredient_name;
        ImageView ingredient_display;
        RelativeLayout ingredient_listing;
        TextView ingredient_selected;
        ImageView greentick;

        ViewHolder(View itemView) {
            super(itemView);

            ingredient_name = itemView.findViewById(R.id.ingredient_name);
            ingredient_display = itemView.findViewById(R.id.ingredient_display);
            ingredient_listing = itemView.findViewById(R.id.ingredient_listing);
            ingredient_selected = itemView.findViewById(R.id.ingredient_selected);
            greentick = itemView.findViewById(R.id.greentick);
        }
    }


    private List<BigBoyPage.ListViewItem> mitems;

    public FridgeAdapter(List<BigBoyPage.ListViewItem> items) {
        mitems = items;
    }


    @NonNull
    @Override
    public FridgeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.ingredient_item, parent, false);

        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
        // Preparing entire list
        final BigBoyPage.ListViewItem item = mitems.get(position);
        viewHolder.ingredient_name.setText(item.NAME);
        //int resId = viewHolder.itemView.getContext().getResources().getIdentifier(item.DISPLAY , "drawable", viewHolder.itemView.getContext().getPackageName());
        //viewHolder.ingredient_display.setImageResource(resId);

        // The searching that's happening below is to avoid assuming the extension of the image which may vary
        try {
            //Bitmap bitmap = decodeSampledBitmapFromPath(Environment.getExternalStorageDirectory() + File.separator + "Android/data/tiab.is.hard/files/ingredients/" + item.DISPLAY, 60, 60); // original 60 60
            Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + File.separator + "Android/data/tiab.is.hard/files/ingredients/" + item.DISPLAY);
            viewHolder.ingredient_display.setImageBitmap(bitmap);
        } catch(Exception ignored){}

        // CHECK ALREADY SELECTED ITEMS
        if(selected_items.size()>0){
            for(int i = 0; i< selected_items.size(); i++){
                if(selected_items.get(i).NAME.equals(item.NAME)) {
                    viewHolder.ingredient_selected.setVisibility(VISIBLE);
                    viewHolder.greentick.setVisibility(VISIBLE);
                    viewHolder.ingredient_selected.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            // Display hifd button
                            if(BigBoyPage.save_ingredients.getVisibility()!=VISIBLE) {
                                Animation slideup = AnimationUtils.loadAnimation(viewHolder.itemView.getContext(), R.anim.slideup);
                                BigBoyPage.save_ingredients.startAnimation(slideup);
                                slideup.setAnimationListener(new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) {
                                    }

                                    @Override
                                    public void onAnimationEnd(Animation animation) {
                                        BigBoyPage.save_ingredients.setVisibility(VISIBLE);
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {
                                    }
                                });
                            }

                            // DESELECT ITEM
                            viewHolder.ingredient_selected.setVisibility(INVISIBLE);
                            viewHolder.greentick.setVisibility(INVISIBLE);
                            for (int i = 0; i < selected_items.size(); i++){
                                    if (selected_items.get(i).NAME.equals(item.NAME)){
                                        selected_items.remove(i);
                                    }
                                }

                        }
                    });
                }
            }
        }

        // PREPARE OTHER ITEMS TO BE SELECTED
        viewHolder.ingredient_listing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // SELECT ITEM
                viewHolder.ingredient_selected.setVisibility(VISIBLE);
                viewHolder.greentick.setVisibility(VISIBLE);
                selected_items.add(new BigBoyPage.ListViewItem() {{
                    DISPLAY = item.DISPLAY;
                    NAME = item.NAME;
                    CATEGORY = item.CATEGORY;
                }});

                // Display hifd button
                if(BigBoyPage.save_ingredients.getVisibility()!=VISIBLE) {
                    Animation slideup = AnimationUtils.loadAnimation(viewHolder.itemView.getContext(), R.anim.slideup);
                    BigBoyPage.save_ingredients.startAnimation(slideup);
                    slideup.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            BigBoyPage.save_ingredients.setVisibility(VISIBLE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                }

                // PREPARE OTHER ITEMS TO BE DESELECTED
                viewHolder.ingredient_selected.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewHolder.ingredient_selected.setVisibility(INVISIBLE);
                        viewHolder.greentick.setVisibility(INVISIBLE);
                        if (selected_items.size() > 0) {
                            for (int i = 0; i < selected_items.size(); i++) {
                                if (selected_items.get(i).NAME.equals(item.NAME))
                                    selected_items.remove(i);
                            }
                        }

                        // Display hifd button
                        if(BigBoyPage.save_ingredients.getVisibility()!=VISIBLE) {
                            Animation slideup = AnimationUtils.loadAnimation(viewHolder.itemView.getContext(), R.anim.slideup);
                            BigBoyPage.save_ingredients.startAnimation(slideup);
                            slideup.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {
                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    BigBoyPage.save_ingredients.setVisibility(VISIBLE);
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    public static Bitmap decodeSampledBitmapFromPath(String path, int reqWidth,
                                                     int reqHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap bmp = BitmapFactory.decodeFile(path, options);
        return bmp;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }

    @Override
    public int getItemCount() {
        return mitems.size();
    }
}