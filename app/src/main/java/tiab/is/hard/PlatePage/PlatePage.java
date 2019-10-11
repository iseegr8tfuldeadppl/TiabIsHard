package tiab.is.hard.PlatePage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import tiab.is.hard.BigBoyPageTabs.BigBoyPage;
import tiab.is.hard.R;
import tiab.is.hard.Round.RoundedImageView;
import tiab.is.hard.SQL.SQL;
import tiab.is.hard.SQL.SQLVariables;

public class PlatePage extends YouTubeBaseActivity {


    Bundle received;
    String plate;
    String[] VideoLinks;
    BigBoyPage.ListViewItem2 item;
    List<Ingredient> Ingredients;
    List<Step> Steps;
    List<Video> Videos;
    RoundedImageView display;
    TextView title;
    String[] IngredientNames;
    String[] IngredientQuantities;
    String StepPosition;
    String[] Step;
    String StepText;
    String[] splitstep;
    RecyclerView StepsList;
    RecyclerView IngredientList;
    RecyclerView VideosList;
    PlatePageIngredientAdapter adapter;
    PlatePageStepAdapter adapter2;
    PlatePageVideoAdapter adapter3;
    GridLayoutManager mGridLayout;
    Intent share;
    String body;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plate_page);

        // Initialization
        share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");

        display = findViewById(R.id.display);
        title = findViewById(R.id.title);

        Videos = new ArrayList<>();
        VideosList = findViewById(R.id.VideosList);
        adapter3 = new PlatePageVideoAdapter(this, Videos);
        VideosList.setNestedScrollingEnabled(false);
        VideosList.setLayoutManager(new LinearLayoutManager(this));

        Steps = new ArrayList<>();
        StepsList = findViewById(R.id.StepsList);
        StepsList.setNestedScrollingEnabled(false);
        StepsList.setLayoutManager(new LinearLayoutManager(this));
        adapter2 = new PlatePageStepAdapter(this, Steps);

        IngredientList = findViewById(R.id.IngredientList);
        Ingredients = new ArrayList<>();
        adapter = new PlatePageIngredientAdapter(this, Ingredients);
        mGridLayout = new GridLayoutManager(this, 3);
        IngredientList.setNestedScrollingEnabled(false);
        IngredientList.setLayoutManager(mGridLayout);

        receive_selected_plate();

        // Display title
        title.setText(item.NAME);

        display_plate_pic();
        display_ingredients();
        load_display_of_ingredients();
        display_steps();
        display_videos();

        IngredientList.setAdapter(adapter);
        StepsList.setAdapter(adapter2);
        VideosList.setAdapter(adapter3);
    }


    public void receive_selected_plate(){
        // Receive selected plate
        received = getIntent().getExtras();
        assert received != null;
        plate = received.getString("plate");
        item = find_plate_in_SQL(plate);
    }


    public void display_plate_pic(){
        // Display image
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + File.separator + "Android/data/tiab.is.hard/files/plates/" + item.DISPLAY, options);
        display.setImageBitmap(bitmap);
    }


    public void display_steps(){
        // Display steps
        Step = item.STEPS.split("_");
        for(int i=1; i<Step.length; i++){
            splitstep = Step[i].split(":");
            StepPosition = splitstep[0];
            StepText = splitstep[1];
            final int it = i;
            Steps.add(new Step() {{
                TEXT = StepText;
                POSITION = StepPosition;
            }});
        }
    }
    public void display_videos(){
        // Display videos
        VideoLinks = item.VIDEOS.split(",");
        for(int i=1; i<VideoLinks.length; i++){
            final int it = i;
            Videos.add(new Video() {{
                LINK = VideoLinks[it];
            }});
        }
    }
    public void display_ingredients(){
        // Display Ingredients with their quantities
        IngredientNames = item.INGREDIENTS.split(",");
        IngredientQuantities = item.AMOUNTS.split(",");
        for(int i=1; i<IngredientNames.length; i++){
            final int it = i;
            Ingredients.add(new Ingredient() {{
                NAME = IngredientNames[it];
                QUANTITY = IngredientQuantities[it];
            }});
        }
    }

    public void load_display_of_ingredients(){
        BigBoyPage.SELECTED_TABLE = "ALL_INGREDIENTS";
        SQLVariables.mydb = new SQL(getApplicationContext());
        SQLVariables.resulter = SQLVariables.mydb.getAllDate();
        while(SQLVariables.resulter.moveToNext()){
            for(int i=0; i<Ingredients.size(); i++){
                if(SQLVariables.resulter.getString(1).equals(Ingredients.get(i).NAME)){
                    Ingredient item = Ingredients.get(i);
                    item.DISPLAY = SQLVariables.resulter.getString(2);
                    Ingredients.set(i, item);
                }
            }
        }
    }

    public BigBoyPage.ListViewItem2 find_plate_in_SQL(String selected_plate){
        List<BigBoyPage.ListViewItem2> allPlates = new ArrayList<>();
        BigBoyPage.SELECTED_TABLE = "ALL_PLATES";
        SQLVariables.mydb = new SQL(getApplicationContext());
        SQLVariables.resulter = SQLVariables.mydb.getAllDate();
        while(SQLVariables.resulter.moveToNext()){
            allPlates.add(new BigBoyPage.ListViewItem2(){{
                NAME = SQLVariables.resulter.getString(1);
                DISPLAY = SQLVariables.resulter.getString(2);
                CALORIES = SQLVariables.resulter.getString(3);
                TIME = SQLVariables.resulter.getString(4);
                INGREDIENTS = SQLVariables.resulter.getString(5);
                AMOUNTS = SQLVariables.resulter.getString(6);
                STEPS = SQLVariables.resulter.getString(7);
                VIDEOS = SQLVariables.resulter.getString(8);
                CATEGORIES = SQLVariables.resulter.getString(9);
            }});
        }
        SQLVariables.mydb.close();

        for(int i=0; i<allPlates.size(); i++){
            BigBoyPage.ListViewItem2 item = allPlates.get(i);
            if(selected_plate.equals(item.NAME))
                return item;
        }
        return null;
    }

    public void shareClicked(View view) {
        body = "Bodde Bodde\nlol";
        share.putExtra(Intent.EXTRA_TEXT, body);
        startActivity(Intent.createChooser(share, getString(R.string.shareplate)));
    }


    static class Ingredient{
        String NAME;
        String QUANTITY;
        String DISPLAY;
    }

    static class Step{
        String TEXT;
        String POSITION;
    }

    static class Video{
        String LINK;
    }
}
