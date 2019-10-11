package tiab.is.hard;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import tiab.is.hard.BigBoyPageTabs.BigBoyPage;
import tiab.is.hard.SQL.SQLVariables;
import tiab.is.hard.SQL.SQL;

public class MainActivity extends AppCompatActivity {

    BroadcastReceiver onComplete;

    StorageReference mStorageRef;
    DatabaseReference PlatesReferences;
    DatabaseReference MaintenanceReferences;
    DatabaseReference IngredientsReferences;
    DatabaseReference PlateTypesReferences;
    FirebaseDatabase database;
    List<BigBoyPage.ListViewItem2> allPlates;
    List<BigBoyPage.ListViewItem> allIngredients;
    int num_of_plates = 0;
    int num_of_ingredients = 0;
    File ingredients;
    File plates;
    boolean all_pictures_are_there = false;
    ProgressBar loading_bar;
    LinearLayout loading_screen;
    RelativeLayout loading_background;
    Integer one_hundred_percent;
    Integer current = 0;
    boolean val = false;
    boolean alreadystarted = false;
    String maintenance;
    String are_u_actually_loading = "no";
    boolean a_download_was_queued = false;
    boolean first_launch = false;
    FirebaseAuth mAuth;
    boolean not_logged_in = false;
    FirebaseUser user;
    List<BigBoyPage.ListViewItem> selected_items;
    List<BigBoyPage.PlateType> plateTypes;

    Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check if we're logged in/user asked to never show login page anymore
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if(user==null)
            not_logged_in = true;

        // Check if settings table is empty, if so then fill it up
        BigBoyPage.SELECTED_TABLE = "SETTINGS";
        SQLVariables.mydb = new SQL(this);
        SQLVariables.resulter = SQLVariables.mydb.getAllDate();
        if(SQLVariables.resulter.getCount()==0){
            SQLVariables.mydb.insertSetting("never_show_register_page_again", "no");
        } else {
            String never_show_register_page_again = get_a_setting_from_SQL("never_show_register_page_again");
            assert never_show_register_page_again!=null;
            if(never_show_register_page_again.equals("yes"))
                not_logged_in = false;
        }

        // Step 1: Initializations
        loading_bar = findViewById(R.id.loading_bar);
        loading_screen = findViewById(R.id.loading_screen);
        loading_background = findViewById(R.id.loading_background);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        database = FirebaseDatabase.getInstance();
        selected_items = new ArrayList<>();
        plateTypes = new ArrayList<>();

        // Text Data References
        PlatesReferences = database.getReference("plates");
        MaintenanceReferences = database.getReference("maintenance");
        IngredientsReferences = database.getReference("ingredients");
        PlateTypesReferences = database.getReference("plate_types");
        allPlates = new ArrayList<>();
        allIngredients = new ArrayList<>();
        ingredients = new File(Environment.getExternalStorageDirectory() + File.separator + "Android/data/tiab.is.hard/files/ingredients/");
        plates = new File(Environment.getExternalStorageDirectory() + File.separator + "Android/data/tiab.is.hard/files/plates/");

        // Step 2: perform a check if every single image is in the folders
        load_all_ingredients_from_SQL("ALL_INGREDIENTS");
        load_all_plates_from_SQL("ALL_PLATES");
        if(ingredients.exists() && plates.exists() && num_of_plates>0 && num_of_ingredients>0)
            all_pictures_are_there = check_if_all_pics_are_there();
        else all_pictures_are_there = false;

        final Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Step 3: Decision-making
                if(all_pictures_are_there) { launch_app(3000);
                    download_data_if_maintenance_is_off(); }
                else { download_data_if_maintenance_is_off(); first_launch = true; }
            }
        }, 1200);

    }

    private String get_a_setting_from_SQL(String SETTING){
        BigBoyPage.SELECTED_TABLE = "SETTINGS";
        SQLVariables.mydb = new SQL(this);
        SQLVariables.resulter = SQLVariables.mydb.getAllDate();
        while(SQLVariables.resulter.moveToNext()) {
            if(SQLVariables.resulter.getString(1).equals(SETTING)){
                String lol = SQLVariables.resulter.getString(2);
                SQLVariables.resulter.close();
                SQLVariables.mydb.close();
                return lol;
            }
        }
        return null;
    }


    public void launch_app(final Integer delay){
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(are_u_actually_loading.equals("no")) {
                    if(!alreadystarted) {
                        alreadystarted = true;
                        if(not_logged_in){
                                loading_bar.setProgress(100);
                                if(onComplete!=null)
                                    unregisterReceiver(onComplete);
                                Intent start = new Intent(MainActivity.this, Register.class);
                                startActivity(start);
                                ActivityCompat.finishAffinity(MainActivity.this);
                        } else {
                            loading_bar.setProgress(100);
                            if(onComplete!=null)
                                unregisterReceiver(onComplete);
                            Intent start = new Intent(MainActivity.this, BigBoyPage.class);
                            startActivity(start);
                            ActivityCompat.finishAffinity(MainActivity.this);
                        }
                    }
                }
            }
        }, delay);
    }


    public void download_data_if_maintenance_is_off() {

        // Step 1: Display loading screen
        loading_screen.setVisibility(View.VISIBLE);
        loading_background.setVisibility(View.VISIBLE);

        // Step 2: Start downloading
        MaintenanceReferences.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                maintenance = dataSnapshot.getValue(String.class);
                assert maintenance != null;
                if (maintenance.equals("no")) {
                    if(handler!=null)
                        handler.removeCallbacksAndMessages(null);
                    // Pull All Ingredients List From Firebase (If There's Internet & maintenance is off)

                    // Step 0: Display progress bar
                    loading_bar.setVisibility(View.VISIBLE);
                    loading_bar.setProgress(0);

                    // Step 1: Ingredients
                    download_ingredient_data_and_images();

                    // Step 2: Plates
                    download_plate_data_and_images();

                    // Step 3: Ingredient Types
                    download_ingredient_type_list();

                    // Step 4: Plate Types
                    download_plate_type_list();

                    // Step 3: Check if any downloads were queue
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(!first_launch) {
                                if (!a_download_was_queued) {
                                    are_u_actually_loading = "no";
                                    launch_app(100);
                                }
                            }
                        }}, 1000);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void download_plate_type_list() {
        PlateTypesReferences.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // Step 1: Download plate info from Firebase
                // reset allIngredients before using it because you've filled it up above
                plateTypes = new ArrayList<>();
                for (final DataSnapshot child : dataSnapshot.getChildren())
                    plateTypes.add(new BigBoyPage.PlateType() {{ TYPE = String.valueOf(child.getValue()); }});

                // Step 2: Save downloaded data in SQL & get the amount of plates for next step
                save_plateTypes_in_SQL(plateTypes, "PLATE_TYPES");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    public void save_plateTypes_in_SQL(List <BigBoyPage.PlateType> all, String table){
        BigBoyPage.SELECTED_TABLE = table;
        SQLVariables.mydb = new SQL(getApplicationContext());
        SQLVariables.mydb.deleteEverything();
        for (int i = 0; i < all.size(); i++)
            SQLVariables.mydb.insertPlateType(all.get(i).TYPE);
        SQLVariables.mydb.close();
    }

    public void download_ingredient_type_list(){
        PlatesReferences.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // Step 1: Download plate info from Firebase
                // reset allIngredients before using it because you've filled it up above
                allPlates = new ArrayList<>();
                for (final DataSnapshot child : dataSnapshot.getChildren())
                    download_plates(child);

                // Step 2: Save downloaded data in SQL & get the amount of plates for next step
                num_of_plates = allPlates.size();
                save_allPlates_in_SQL(allPlates, "ALL_PLATES");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }


    public void download_plate_data_and_images(){
        PlatesReferences.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // Step 1: Download plate info from Firebase
                // reset allIngredients before using it because you've filled it up above
                allPlates = new ArrayList<>();
                for (final DataSnapshot child : dataSnapshot.getChildren())
                    download_plates(child);

                // Step 2: Save downloaded data in SQL & get the amount of plates for next step
                num_of_plates = allPlates.size();
                save_allPlates_in_SQL(allPlates, "ALL_PLATES");

                // Step 3: Use that data to download the needed images;
                for(int i=0; i<allPlates.size(); i++) {
                    StorageReference riversRef = mStorageRef.child("plates/" + allPlates.get(i).DISPLAY);
                    final int finalI = i;
                    riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String Url = uri.toString();
                            download_plate_images(finalI, Url);
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
    public void download_ingredient_data_and_images(){
        IngredientsReferences.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                are_u_actually_loading = "yes";

                // Step 1: Download plate info from Firebase
                // reset allIngredients before using it because you've filled it up above
                allIngredients = new ArrayList<>();
                for (final DataSnapshot child : dataSnapshot.getChildren())
                    download_ingredients(child);

                // Step 2: Save downloaded data in SQL & get the amount of ingredients for next step
                num_of_ingredients = allIngredients.size();
                save_ingredients_in_SQL(allIngredients, "ALL_INGREDIENTS");

                // Step 3: Use that data to download the needed images;
                for(int i=0; i<allIngredients.size(); i++) {
                    StorageReference riversRef = mStorageRef.child("ingredients/" + allIngredients.get(i).DISPLAY);
                    final int finalI = i;
                    riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String Url = uri.toString();
                            download_ingredient_images(finalI, Url);
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }


    public void downloadMeSenpai(final Context context, String DISPLAY, String destinationDirectory, String url) {
        DownloadManager downloadmanager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, DISPLAY);
        final long downloadID = downloadmanager.enqueue(request);

        onComplete=new BroadcastReceiver() {
            public void onReceive(Context ctxt, Intent intent) {
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (downloadID == id && num_of_ingredients > 0 && num_of_plates > 0) {
                    all_pictures_are_there = check_if_all_pics_are_there();

                    one_hundred_percent = num_of_plates + num_of_ingredients;
                    //current++;
                    ingredients = new File(Environment.getExternalStorageDirectory() + File.separator + "Android/data/tiab.is.hard/files/ingredients/");
                    plates = new File(Environment.getExternalStorageDirectory() + File.separator + "Android/data/tiab.is.hard/files/plates/");
                    current = ingredients.list().length + plates.list().length;
                    loading_bar.setProgress(100 * current/one_hundred_percent);

                    if (all_pictures_are_there)
                        if(!alreadystarted)
                            if( current/one_hundred_percent == 1 ) { are_u_actually_loading = "no"; launch_app(1000); }
                }
            }
        };
        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }


    public void download_plates(final DataSnapshot child){
        allPlates.add(new BigBoyPage.ListViewItem2() {{
            NAME = String.valueOf(child.child("name").getValue());
            DISPLAY = String.valueOf(child.child("photo").getValue());
            CALORIES = String.valueOf(child.child("calories").getValue());
            TIME = String.valueOf(child.child("time").getValue());
            INGREDIENTS = "";
            AMOUNTS = "";
            STEPS = "";
            VIDEOS = "";
            CATEGORIES = String.valueOf(child.child("categories").getValue());
        }});

        BigBoyPage.ListViewItem2 item = allPlates.get(allPlates.size() - 1);
        for (DataSnapshot childofchild : child.child("ingredients").getChildren()) {
            item.INGREDIENTS += "," + childofchild.getKey();
            item.AMOUNTS += "," + childofchild.getValue();
        }

        for (DataSnapshot childofchild : child.child("steps").getChildren()) {
            item.STEPS += "_" + childofchild.getKey();
            item.STEPS += ":" + childofchild.getValue();
        }

        for (DataSnapshot childofchild : child.child("videos").getChildren()) {
            item.VIDEOS += "," + childofchild.child("link").getValue();
        }
        allPlates.set(allPlates.size() - 1, item);
    }
    public void download_ingredients(final DataSnapshot child){
        allIngredients.add(new BigBoyPage.ListViewItem() {{
            NAME = String.valueOf(child.getKey());
            DISPLAY = String.valueOf(child.child("photo").getValue());
            CATEGORY = String.valueOf(child.child("category").getValue());
        }});
    }


    public void save_ingredients_in_SQL(List <BigBoyPage.ListViewItem> all, String table){
        BigBoyPage.SELECTED_TABLE = table;
        SQLVariables.mydb = new SQL(getApplicationContext());
        SQLVariables.mydb.deleteEverything();
        for (int i = 0; i < all.size(); i++) {
            SQLVariables.mydb.insertData(
                    all.get(i).NAME,
                    all.get(i).DISPLAY,
                    all.get(i).CATEGORY);
        }
        SQLVariables.mydb.close();
    }
    public void save_allPlates_in_SQL(List <BigBoyPage.ListViewItem2> all, String table){
        BigBoyPage.SELECTED_TABLE = table;
        SQLVariables.mydb = new SQL(getApplicationContext());
        SQLVariables.mydb.deleteEverything();
        for (int i = 0; i < all.size(); i++) {
            SQLVariables.mydb.insertPlate(
                    all.get(i).NAME,
                    all.get(i).DISPLAY,
                    all.get(i).CALORIES,
                    all.get(i).TIME,
                    all.get(i).INGREDIENTS,
                    all.get(i).AMOUNTS,
                    all.get(i).STEPS,
                    all.get(i).VIDEOS,
                    all.get(i).CATEGORIES);
        }
        SQLVariables.mydb.close();
    }


    public boolean check_if_all_pics_are_there() {
        if(!ingredients.exists() || !plates.exists())
            return false;

        val = false;
        for(int i=0; i<allPlates.size();i++) {
            boolean alreadyexisting = false;
            for (String image : plates.list()) {
                if (allPlates.get(i).DISPLAY.contains(image)) alreadyexisting = true;
            }

            if (!alreadyexisting) { val = false; break;
            } else val = true;
        }

        for(int i=0; i<allIngredients.size();i++) {
            boolean alreadyexisting = false;
            for (String image : ingredients.list()) {
                if (allIngredients.get(i).DISPLAY.contains(image)) alreadyexisting = true;
            }

            if (!alreadyexisting) { val = false; break;
            } else val = true;
        }
        return val;
    }


    public void download_plate_images(int finalI, String Url){
        if(plates.exists()) {
            boolean alreadyexisting = false;
            for (String image : plates.list()) {
                if (image.contains(allPlates.get(finalI).DISPLAY))
                    alreadyexisting = true; }

            if (!alreadyexisting) {
                a_download_was_queued = true;
                downloadMeSenpai(MainActivity.this, allPlates.get(finalI).DISPLAY, "plates", Url);
            }
        } else {
            a_download_was_queued = true;
            downloadMeSenpai(MainActivity.this, allPlates.get(finalI).DISPLAY, "plates", Url);
        }
    }
    public void download_ingredient_images(int finalI, String Url){
        if(ingredients.exists()){
            boolean alreadyexisting = false;
            for (String image : ingredients.list()) {
                if (image.contains(allIngredients.get(finalI).DISPLAY))
                    alreadyexisting = true; }

            if (!alreadyexisting){
                a_download_was_queued = true;
                downloadMeSenpai(MainActivity.this, allIngredients.get(finalI).DISPLAY, "ingredients", Url);
            }
        } else {
            a_download_was_queued = true;
            downloadMeSenpai(MainActivity.this, allIngredients.get(finalI).DISPLAY, "ingredients", Url);
        }
    }


    public void load_all_ingredients_from_SQL(String table){
        BigBoyPage.SELECTED_TABLE = table;
        SQLVariables.mydb = new SQL(getApplicationContext());
        SQLVariables.resulter = SQLVariables.mydb.getAllDate();
        num_of_ingredients = SQLVariables.resulter.getCount();
        while (SQLVariables.resulter.moveToNext()) {
            allIngredients.add(new BigBoyPage.ListViewItem() {{
                NAME = SQLVariables.resulter.getString(1);
                DISPLAY = SQLVariables.resulter.getString(2);
                CATEGORY = SQLVariables.resulter.getString(3);
            }});
        }
        SQLVariables.mydb.close();
    }
    public void load_all_plates_from_SQL(String table){
        BigBoyPage.SELECTED_TABLE = table;
        SQLVariables.mydb = new SQL(getApplicationContext());
        SQLVariables.resulter = SQLVariables.mydb.getAllDate();
        num_of_plates = SQLVariables.resulter.getCount();
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
    }


    public static void print(Context context, Object text, Integer long_short){
        if(long_short == 1)
            Toast.makeText(context, String.valueOf(text), Toast.LENGTH_LONG).show();
        else
            Toast.makeText(context, String.valueOf(text), Toast.LENGTH_SHORT).show();
    }


}