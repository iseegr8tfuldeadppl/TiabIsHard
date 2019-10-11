package tiab.is.hard.BigBoyPageTabs;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import tiab.is.hard.MainActivity;
import tiab.is.hard.R;
import tiab.is.hard.Register;
import tiab.is.hard.SQL.SQL;
import tiab.is.hard.SQL.SQLVariables;
import tiab.is.hard.SlidingMenuLayout.SlidingTabsColorsFragment;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static tiab.is.hard.BigBoyPageTabs.TypesAdapter.plate;
import static tiab.is.hard.MainActivity.print;
import static tiab.is.hard.SlidingMenuLayout.SlidingTabsColorsFragment.mViewPager;

public class BigBoyPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static boolean platespage = false, fridgepage = false, possiblepage = true;
    boolean found_ingredient;

    Animation slide_in_from_right;
    Animation slidedown;

    View headerLayout;
    FrameLayout FridgePage;
    RelativeLayout PossiblePlatesPage;
    RelativeLayout loading_background;
    NestedScrollView allplatesPage;

    GoogleSignInClient mGoogleSignInClient;
    FirebaseUser user;
    DatabaseReference SelectedItemsRef;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference UserReferences;
    GoogleSignInAccount account;
    Task<GoogleSignInAccount> task;
    DatabaseReference MaintenanceReferences;

    DrawerLayout mDrawer;
    ActionBarDrawerToggle mToggle;
    NavigationView navigation;

    public static String current_tab;
    static String current_search_word = "";
    String more_apps_link = "https://play.google.com/store/apps/developer?id=SCUFFEDBOTS";
    String userer;
    public static String SELECTED_TABLE;
    String validemail;
    String email;
    static String[] IngredientNames;
    static String ingredient_english;
    String android_ingredients_path = Environment.getExternalStorageDirectory() + File.separator  + "Android/data/tiab.is.hard/files/ingredients/";
    String android_random_path_to_make_profil_pic_null = Environment.getExternalStorageDirectory() + File.separator  + "Android/data/tiab.is.hard/files/random/";
    String android_profilpic_path;
    String input;

    public static File ingredients;
    File random;
    File profilpic;

    public static int iwasclicked=0;
    static final int GOOGLE_SIGN_IN = 123;
    int unselected_tab_background_color;
    int unselected_tab_text_color;
    int selected_tab_background_color;
    int selected_tab_text_color;

    TextView add_ingredients2;
    TextView name;
    TextView allplatesTabSelector;
    TextView PossiblePlatesTabSelector;
    TextView FridgePageTabSelector;
    TextView searcher;

    Toolbar toolbar1;

    ImageView magni;
    ImageView display;
    Bitmap bitmap;
    Uri uri;

    EditText searchinput;

    static PlatesAdapter adapter3;
    static PlatesAdapter search_adapter;
    static PlatesAdapter adapter4;
    TypesAdapter typesAdapter;
    TypesAdapter typesAdapter2;

    public static RecyclerView rvContactsPossiblePlates;
    static RecyclerView rvContactsAllPlates;
    RecyclerView rvContactsplateTypes;
    RecyclerView rvContactsplateTypes2;

    public static List<ListViewItem2> allPlates;
    static List<ListViewItem2> search_array;
    public static List<ListViewItem2> possible_items;
    public static List<ListViewItem> selected_items;
    public static List<ListViewItem> AllIngredients;
    private List<PlateType> plateTypes;
    public static List<BigBoyPage.ListViewItem2> current_tab_plates;

    LinearLayoutManager HorizontalRecyclerView;
    LinearLayoutManager HorizontalRecyclerView2;

    public static List<BigBoyPage.ListViewItem> vegetables;
    public static List<BigBoyPage.ListViewItem> spices;
    public static List<BigBoyPage.ListViewItem> miscellaneous;
    public static List<BigBoyPage.ListViewItem> canned;
    public static List<BigBoyPage.ListViewItem> fruits;
    public static List<BigBoyPage.ListViewItem> dairy;
    // TODO: Ingredient Types - Declaration

    Button login;
    Button yes, no;
    public static Button save_ingredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_boy_page);
        // Only works with storage permission
        /*
        new File(DIRECTORY_DOWNLOADS).mkdirs();
        File file = new File(DIRECTORY_DOWNLOADS);
        if (!file.exists()) { try { boolean lol = file.createNewFile(); } catch (IOException ignored) {} }
        */

        // Initializations
        slidedown = AnimationUtils.loadAnimation(this, R.anim.slidedown);
        slide_in_from_right = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_from_right);
        HorizontalRecyclerView = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        HorizontalRecyclerView2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        yes = findViewById(R.id.yes);
        no = findViewById(R.id.no);
        loading_background = findViewById(R.id.loading_background);
        unselected_tab_background_color = getResources().getColor(R.color.unselected_tab);
        unselected_tab_text_color = getResources().getColor(R.color.unselected_text_color);
        selected_tab_background_color = getResources().getColor(R.color.selected_tab);
        selected_tab_text_color = getResources().getColor(R.color.selected_text_color);
        toolbar1= findViewById(R.id.toolbar1);
        search_array = new ArrayList<>();
        search_adapter = new PlatesAdapter(search_array);
        rvContactsPossiblePlates = findViewById(R.id.rvContactsPossiblePlates);
        rvContactsAllPlates = findViewById(R.id.rvContactsAllPlates);
        rvContactsplateTypes = findViewById(R.id.rvContactsplateTypes);
        rvContactsplateTypes2 = findViewById(R.id.rvContactsplateTypes2);
        rvContactsplateTypes.setLayoutManager(HorizontalRecyclerView);
        rvContactsplateTypes2.setLayoutManager(HorizontalRecyclerView2);
        searchinput = findViewById(R.id.searchinput);
        searcher = findViewById(R.id.searcher);
        magni = findViewById(R.id.magni);
        database = FirebaseDatabase.getInstance();
        save_ingredients = findViewById(R.id.save_ingredients);
        allplatesTabSelector = findViewById(R.id.allplates);
        PossiblePlatesTabSelector = findViewById(R.id.whatcanicook);
        FridgePageTabSelector = findViewById(R.id.fridge);
        navigation = findViewById(R.id.navigation);
        headerLayout = navigation.getHeaderView(0);
        display = headerLayout.findViewById(R.id.photo);
        name = headerLayout.findViewById(R.id.name);
        login = headerLayout.findViewById(R.id.login);
        AllIngredients = new ArrayList<>();
        rvContactsPossiblePlates = findViewById(R.id.rvContactsPossiblePlates);
        possible_items = new ArrayList<>();
        FridgePage = findViewById(R.id.sample_content_fragment);
        allplatesPage = findViewById(R.id.allplatesPage);
        PossiblePlatesPage = findViewById(R.id.PossiblePlatesPage);
        allPlates = new ArrayList<>();
        add_ingredients2 = findViewById(R.id.add_ingredients2);
        selected_items = new ArrayList<>(); //selected ingredients
        mDrawer = findViewById(R.id.mDrawer);
        ingredients = new File(android_ingredients_path);
        random = new File(android_random_path_to_make_profil_pic_null);
        plateTypes = new ArrayList<>();

        vegetables = new ArrayList<>();
        spices = new ArrayList<>();
        miscellaneous = new ArrayList<>();
        canned = new ArrayList<>();
        fruits = new ArrayList<>();
        dairy = new ArrayList<>();
        // TODO: Ingredient Types - Initialization

        // set the custom actionbar
        setSupportActionBar(toolbar1);
        if(getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(possiblepage) searcher.setText(getString(R.string.searchpossibleplates));
        else if(platespage) searcher.setText(getString(R.string.searchallplates));
        else if(fridgepage) searcher.setText(getString(R.string.searchingredients));

        // setup the side menu variable work
        setNavigationViewListener();
        mToggle = new ActionBarDrawerToggle(this, mDrawer, R.string.open, R.string.close);
        mDrawer.addDrawerListener(mToggle);
        mToggle.syncState();
        login.setOnClickListener(v -> SignInGoogle());

        // google sign in work
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            user = mAuth.getCurrentUser();
            validemail = Objects.requireNonNull(mAuth.getCurrentUser().getEmail()).replace(".", "");
            UserReferences = database.getReference("users/" + validemail);
            SelectedItemsRef = UserReferences.child("selected_items");
            userer = Objects.requireNonNull(user.getEmail()).replace(".", "").replace("@", "");
            android_profilpic_path = Environment.getExternalStorageDirectory() + File.separator  + "Android/data/tiab.is.hard/files/random/" + userer + ".png";
            profilpic = new File(android_profilpic_path);
            if(profilpic.exists()){
                login.setVisibility(INVISIBLE);
                display.setVisibility(View.VISIBLE);
                name.setVisibility(View.VISIBLE);
                bitmap = BitmapFactory.decodeFile(android_profilpic_path);
                display.setImageBitmap(bitmap);
                email = user.getEmail();
                name.setText(email);
            } else
                downloadMeSenpai(this, userer + ".png", "random", String.valueOf(user.getPhotoUrl()));
        } else {
            ImageView display = headerLayout.findViewById(R.id.photo);
            Button login = headerLayout.findViewById(R.id.login);
            display.setVisibility(INVISIBLE);
            name.setVisibility(INVISIBLE);
            login.setVisibility(View.VISIBLE);
            bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + File.separator + "Android/data/tiab.is.hard/files/random/iknowthiswillbeinvalidjustsopicisinvisible.png");
            display.setImageBitmap(bitmap);
        }

        setupAllPlatesTab();
        setupPossiblePlatesTab();
        setupFridgeTab();
        setupPlateTypesMenus();
    }

    public void setupPlateTypesMenus(){
        BigBoyPage.SELECTED_TABLE = "PLATE_TYPES";
        SQLVariables.mydb = new SQL(this);
        SQLVariables.resulter = SQLVariables.mydb.getAllDate();

        if(SQLVariables.resulter.getCount()>0  && SQLVariables.resulter!=null){
            while(SQLVariables.resulter.moveToNext())
                plateTypes.add(new PlateType(){{ TYPE = SQLVariables.resulter.getString(1); }});
        }
        assert SQLVariables.resulter != null;
        SQLVariables.resulter.close();
        SQLVariables.mydb.close();

        typesAdapter = new TypesAdapter(plateTypes);
        rvContactsplateTypes.setAdapter(typesAdapter);

        typesAdapter2 = new TypesAdapter(plateTypes);
        rvContactsplateTypes2.setAdapter(typesAdapter2);
    }


    public void SignInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful())
                        updateUI(mAuth.getCurrentUser());
                    else
                        updateUI(null);
                });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_SIGN_IN) {
            task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                account = task.getResult(ApiException.class);
                if (account != null) firebaseAuthWithGoogle(account);
            } catch (ApiException ignored) {}
        }
    }
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            userer = Objects.requireNonNull(user.getEmail()).replace(".", "").replace("@", "");
            email = user.getEmail();
            profilpic = new File(Environment.getExternalStorageDirectory() + File.separator  + "Android/data/tiab.is.hard/files/random/" + userer + ".png");
            if(profilpic.exists()){
                login.setVisibility(INVISIBLE);
                display.setVisibility(View.VISIBLE);
                name.setVisibility(View.VISIBLE);
                bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + File.separator + "Android/data/tiab.is.hard/files/random/" + userer + ".png");
                display.setImageBitmap(bitmap);
                email = user.getEmail();
                name.setText(email);
            } else
                downloadMeSenpai(this, userer + ".png", "random", String.valueOf(user.getPhotoUrl()));
        } else
            print(this, getString(R.string.plsgifinternet), 1);
    }


    public void downloadMeSenpai(final Context context, String DISPLAY, String destinationDirectory, String url) {
        DownloadManager downloadmanager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, DISPLAY);
        final long downloadID = downloadmanager.enqueue(request);

        BroadcastReceiver onComplete=new BroadcastReceiver() {
            public void onReceive(Context ctxt, Intent intent) {
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (downloadID == id) {
                    user = mAuth.getCurrentUser();
                    headerLayout = navigation.getHeaderView(0);
                    bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + File.separator + "Android/data/tiab.is.hard/files/random/" + userer + ".png");
                    display.setImageBitmap(bitmap);
                    email = user.getEmail();
                    name.setText(email);
                    login.setVisibility(INVISIBLE);
                    display.setVisibility(View.VISIBLE);
                    name.setVisibility(View.VISIBLE);
                }
            }
        };
        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }


    @Override
    public void onBackPressed() {
        if(searchinput.getVisibility()==VISIBLE) {
            // hide keyboard
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchinput.getWindowToken(), 0);

            rvContactsAllPlates.setNestedScrollingEnabled(false);
            rvContactsAllPlates.setAdapter(adapter3);
            searchinput.setVisibility(INVISIBLE);
            searcher.setVisibility(VISIBLE);
            magni.setImageDrawable(getResources().getDrawable(R.drawable.magnifying_glass));
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            mToggle.setDrawerIndicatorEnabled(true);
            mToggle.syncState();
        }
    }

    public void setupAllPlatesTab(){
        // Pull All Plates From SQL
        BigBoyPage.SELECTED_TABLE = "ALL_PLATES";
        SQLVariables.mydb = new SQL(this);
        SQLVariables.resulter = SQLVariables.mydb.getAllDate();

        if(SQLVariables.resulter.getCount()>0  && SQLVariables.resulter!=null){
            while(SQLVariables.resulter.moveToNext()){
                allPlates.add(new ListViewItem2(){{
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
        }
        assert SQLVariables.resulter != null;
        SQLVariables.resulter.close();
        SQLVariables.mydb.close();

        current_tab_plates = allPlates;
        adapter3 = new PlatesAdapter(current_tab_plates);
        rvContactsAllPlates.setNestedScrollingEnabled(false);
        rvContactsAllPlates.setAdapter(adapter3);
        rvContactsAllPlates.setLayoutManager(new LinearLayoutManager(this));
    }


    public void setupPossiblePlatesTab(){
        pull_ingredients_from_SQL();
        pull_possible_plates_from_SQL();
        pull_selected_ingredients_from_SQL_with_a_few_satefy_checks();

        // Run List
        rvContactsPossiblePlates = findViewById(R.id.rvContactsPossiblePlates);
        adapter4 = new PlatesAdapter(BigBoyPage.possible_items);
        rvContactsPossiblePlates.setAdapter(adapter4);
        rvContactsPossiblePlates.setLayoutManager(new LinearLayoutManager(this));
    }
    public void pull_selected_ingredients_from_SQL_with_a_few_satefy_checks(){
        // Checking if selected ingredients are none
        // Pull All Selected Ingredients from SQL
        BigBoyPage.SELECTED_TABLE = "SELECTED_INGREDIENTS";
        SQLVariables.mydb = new SQL(this);
        SQLVariables.resulter = SQLVariables.mydb.getAllDate();

        boolean found = false;
        if(SQLVariables.resulter.getCount()>0  && SQLVariables.resulter!=null){
            while(SQLVariables.resulter.moveToNext()){
                // Check in all ingredients if selected does exist (incase of removal of items from full ingredient list in Firebase by le me)
                for (int j = 0; j < BigBoyPage.AllIngredients.size(); j++) {
                    if(!found) {
                        if (SQLVariables.resulter.getString(1).equals(BigBoyPage.AllIngredients.get(j).NAME))
                            found = true;
                        else found = false;
                    }
                }
                // If after the looping we haven't found anthn theeeen delete it
                if(!found)
                    SQLVariables.mydb.deleteData(SQLVariables.resulter.getString(1));
                else{
                    selected_items.add(new BigBoyPage.ListViewItem(){{
                        NAME = SQLVariables.resulter.getString(1);
                        DISPLAY = SQLVariables.resulter.getString(2);
                        CATEGORY = SQLVariables.resulter.getString(3);
                    }});
                }
            }
        }

        assert SQLVariables.resulter != null;
        SQLVariables.resulter.close();
        SQLVariables.mydb.close();
    }
    public void pull_possible_plates_from_SQL(){
        // POSSIBLE PLATES PAGE
        BigBoyPage.SELECTED_TABLE = "PLATES_I_CAN_COOK";
        SQLVariables.mydb = new SQL(this);
        SQLVariables.resulter = SQLVariables.mydb.getAllDate();

        if(SQLVariables.resulter.getCount()>0  && SQLVariables.resulter!=null){
            add_ingredients2.setVisibility(INVISIBLE);
            while(SQLVariables.resulter.moveToNext()){
                possible_items.add(new ListViewItem2(){{
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
        }
        assert SQLVariables.resulter != null;
        SQLVariables.resulter.close();
        SQLVariables.mydb.close();
    }
    public void pull_ingredients_from_SQL(){
        // Pull All Ingredints from SQL to check if selected contains things that aren't in entire list due to updates
        BigBoyPage.SELECTED_TABLE = "ALL_INGREDIENTS";
        SQLVariables.mydb = new SQL(this);
        SQLVariables.resulter = SQLVariables.mydb.getAllDate();

        if(SQLVariables.resulter.getCount()>0  && SQLVariables.resulter!=null){
            while(SQLVariables.resulter.moveToNext()){
                AllIngredients.add(new ListViewItem(){{
                    NAME = SQLVariables.resulter.getString(1);
                    DISPLAY = SQLVariables.resulter.getString(2);
                    CATEGORY = SQLVariables.resulter.getString(3);
                }});

            }
        }
        assert SQLVariables.resulter != null;
        SQLVariables.resulter.close();
        SQLVariables.mydb.close();
    }
    public void setupFridgeTab(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        SlidingTabsColorsFragment fragment = new SlidingTabsColorsFragment();
        transaction.replace(R.id.sample_content_fragment, fragment);
        transaction.commit();

        pull_ingredients_from_SQL_and_split_them_into_categories();
        // Add function to that hifd button
        save_ingredients.setOnClickListener(v -> addClicked());
    }
    public void pull_ingredients_from_SQL_and_split_them_into_categories(){
        // Pull all ingredients from SQL
        BigBoyPage.SELECTED_TABLE = "ALL_INGREDIENTS";
        SQLVariables.mydb = new SQL(this);
        SQLVariables.resulter = SQLVariables.mydb.getAllDate();

        if(SQLVariables.resulter.getCount()>0  && SQLVariables.resulter!=null){
            while(SQLVariables.resulter.moveToNext()){
                if(SQLVariables.resulter.getString(3).equals(getString(R.string.vegetables))){
                    vegetables.add(new BigBoyPage.ListViewItem(){{
                        NAME = SQLVariables.resulter.getString(1);
                        DISPLAY = SQLVariables.resulter.getString(2);
                        CATEGORY = SQLVariables.resulter.getString(3);
                    }});
                } else if(SQLVariables.resulter.getString(3).equals(getString(R.string.spices))){
                    spices.add(new BigBoyPage.ListViewItem(){{
                        NAME = SQLVariables.resulter.getString(1);
                        DISPLAY = SQLVariables.resulter.getString(2);
                        CATEGORY = SQLVariables.resulter.getString(3);
                    }});
                } else if(SQLVariables.resulter.getString(3).equals(getString(R.string.miscellaneous))){
                    miscellaneous.add(new BigBoyPage.ListViewItem(){{
                        NAME = SQLVariables.resulter.getString(1);
                        DISPLAY = SQLVariables.resulter.getString(2);
                        CATEGORY = SQLVariables.resulter.getString(3);
                    }});
                } else if(SQLVariables.resulter.getString(3).equals(getString(R.string.canned))){
                    canned.add(new BigBoyPage.ListViewItem(){{
                        NAME = SQLVariables.resulter.getString(1);
                        DISPLAY = SQLVariables.resulter.getString(2);
                        CATEGORY = SQLVariables.resulter.getString(3);
                    }});
                } else if(SQLVariables.resulter.getString(3).equals(getString(R.string.fruits))){
                    fruits.add(new BigBoyPage.ListViewItem(){{
                        NAME = SQLVariables.resulter.getString(1);
                        DISPLAY = SQLVariables.resulter.getString(2);
                        CATEGORY = SQLVariables.resulter.getString(3);
                    }});
                } else if(SQLVariables.resulter.getString(3).equals(getString(R.string.dairy))){
                    dairy.add(new BigBoyPage.ListViewItem(){{
                        NAME = SQLVariables.resulter.getString(1);
                        DISPLAY = SQLVariables.resulter.getString(2);
                        CATEGORY = SQLVariables.resulter.getString(3);
                    }});
                }
                // TODO: Ingredient Types - Filling

            }
        }
        assert SQLVariables.resulter != null;
        SQLVariables.resulter.close();
        SQLVariables.mydb.close();
    }
    public void reloadPossiblePlatesTab(List<BigBoyPage.ListViewItem2> possiblePlatesBaby) {
        PlatesAdapter adapter4 = new PlatesAdapter(possiblePlatesBaby);
        rvContactsPossiblePlates.setAdapter(adapter4);
        rvContactsPossiblePlates.setLayoutManager(new LinearLayoutManager(this));
    }
    public List<BigBoyPage.ListViewItem2> extractPossiblePlates(){
        find_possible_plates();

        push_possible_plates_onto_SQL();

        return possible_items;
    }
    public void find_possible_plates(){
        for(int i=0; i<allPlates.size(); i++){
            boolean stop = false;
            boolean possible = false;
            String[] allIngredientsInPlate = allPlates.get(i).INGREDIENTS.split(",");
            String[] allAmountsInPlate = allPlates.get(i).AMOUNTS.split(",");
            for (int j = 1; j < allIngredientsInPlate.length; j++) {
                if(!stop) {
                    String priority_of_ingredient = allAmountsInPlate[j].split(":")[1];
                    if (priority_of_ingredient.equals("1")) {
                        for (int z = 0; z < selected_items.size(); z++) {
                            if (allIngredientsInPlate[j].equals(selected_items.get(z).NAME))
                                possible = true;
                        }
                        if (!possible)
                            stop = true;
                    }
                }
                possible = false;
            }
            if(!stop)
                possible_items.add(allPlates.get(i));
        }
    }
    public void push_possible_plates_onto_SQL(){
        // SAVE PLATES THAT I CAN COOK
        BigBoyPage.SELECTED_TABLE = "PLATES_I_CAN_COOK";
        SQLVariables.mydb = new SQL(this);
        SQLVariables.mydb.deleteEverything();
        if (possible_items.size() > 0) {
            for (int i = 0; i < possible_items.size(); i++) {
                SQLVariables.mydb.insertPlate(
                        possible_items.get(i).NAME,
                        possible_items.get(i).DISPLAY,
                        possible_items.get(i).CALORIES,
                        possible_items.get(i).TIME,
                        possible_items.get(i).INGREDIENTS,
                        possible_items.get(i).AMOUNTS,
                        possible_items.get(i).STEPS,
                        possible_items.get(i).VIDEOS,
                        possible_items.get(i).CATEGORIES);
            }
        }
        SQLVariables.mydb.close();
    }
    public void saveSelectedItemsInSQL(){
        BigBoyPage.SELECTED_TABLE = "SELECTED_INGREDIENTS";
        SQLVariables.mydb = new SQL(this);
        SQLVariables.mydb.deleteEverything();
        if(selected_items.size()>0){
            for(int i=0; i<selected_items.size(); i++){
                SQLVariables.mydb.insertData(selected_items.get(i).NAME,
                        selected_items.get(i).DISPLAY,
                        selected_items.get(i).CATEGORY);
            }
        }
        SQLVariables.mydb.close();
    }


    public void addClicked() {
        // Display hifd button
        if(save_ingredients.getVisibility()==VISIBLE) {
            save_ingredients.startAnimation(slidedown);
            slidedown.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    save_ingredients.setVisibility(INVISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }

        saveSelectedItemsInSQL();
        Toast.makeText(this, getString(R.string.hifded), Toast.LENGTH_SHORT).show();
        possible_items = new ArrayList<>();
        reloadPossiblePlatesTab(extractPossiblePlates());
        if(possible_items.size() <= 0)
            add_ingredients2.setVisibility(View.VISIBLE);
        else
            add_ingredients2.setVisibility(INVISIBLE);
    }


    public void fridgeClicked(View view) {

        searcher.setText(getString(R.string.searchingredients));
        current_search_word = "";
        fridgepage = true;
        platespage = false;
        possiblepage = false;
        FridgePage.setVisibility(View.VISIBLE);
        allplatesPage.setVisibility(INVISIBLE);
        PossiblePlatesPage.setVisibility(INVISIBLE);
        FridgePageTabSelector.setTextColor(selected_tab_text_color);
        FridgePageTabSelector.setBackgroundColor(selected_tab_background_color);
        allplatesTabSelector.setTextColor(unselected_tab_text_color);
        allplatesTabSelector.setBackgroundColor(unselected_tab_background_color);
        PossiblePlatesTabSelector.setTextColor(unselected_tab_text_color);
        PossiblePlatesTabSelector.setBackgroundColor(unselected_tab_background_color);

        // to avoid glitching when switching between pages while search bar is open
        if(searcher.getVisibility()!=VISIBLE)
            ingredient_search();
    }


    public void allplatesClicked(View view) {

        searcher.setText(getString(R.string.searchallplates));
        platespage = true;
        fridgepage = false;
        possiblepage = false;
        FridgePage.setVisibility(INVISIBLE);
        allplatesPage.setVisibility(View.VISIBLE);
        PossiblePlatesPage.setVisibility(INVISIBLE);
        FridgePageTabSelector.setTextColor(unselected_tab_text_color);
        FridgePageTabSelector.setBackgroundColor(unselected_tab_background_color);
        allplatesTabSelector.setBackgroundColor(selected_tab_background_color);
        allplatesTabSelector.setTextColor(selected_tab_text_color);
        PossiblePlatesTabSelector.setBackgroundColor(unselected_tab_background_color);
        PossiblePlatesTabSelector.setTextColor(unselected_tab_text_color);

        // to avoid glitching when switching between pages while search bar is open
        if(searcher.getVisibility()!=VISIBLE)
            all_plates_search();

        // Animate plate types menu
        rvContactsplateTypes.startAnimation(slide_in_from_right);
        slide_in_from_right.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                rvContactsplateTypes.setVisibility(VISIBLE);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
    }


    public void whatcanicookClicked(View view) {

        searcher.setText(getString(R.string.searchpossibleplates));
        current_search_word = "";
        possiblepage = true;
        fridgepage = false;
        platespage = false;
        FridgePage.setVisibility(INVISIBLE);
        allplatesPage.setVisibility(INVISIBLE);
        PossiblePlatesPage.setVisibility(View.VISIBLE);
        FridgePageTabSelector.setBackgroundColor(unselected_tab_background_color);
        FridgePageTabSelector.setTextColor(unselected_tab_text_color);
        allplatesTabSelector.setBackgroundColor(unselected_tab_background_color);
        allplatesTabSelector.setTextColor(unselected_tab_text_color);
        PossiblePlatesTabSelector.setBackgroundColor(selected_tab_background_color);
        PossiblePlatesTabSelector.setTextColor(selected_tab_text_color);

        // to avoid glitching when switching between pages while search bar is open
        if(searcher.getVisibility()!=VISIBLE)
            possible_plates_search();

        // Animate plate types menu
        rvContactsplateTypes2.startAnimation(slide_in_from_right);
        slide_in_from_right.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                rvContactsplateTypes2.setVisibility(VISIBLE);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
    }


    public void logoutClicked() {
        if(mAuth.getCurrentUser()!=null) {
            loading_background.setVisibility(VISIBLE);
            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loading_background.setVisibility(INVISIBLE);
                    bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + File.separator + "Android/data/tiab.is.hard/files/random/iknowthiswillbeinvalidjustsopicisinvisible.png");
                    display.setImageBitmap(bitmap);
                    Logout();
                }
            });
            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { loading_background.setVisibility(INVISIBLE); }
            });
        } else
            print(getApplicationContext(), getString(R.string.yourenotloggedin), 1);
    }
    private void Logout() {
        FirebaseAuth.getInstance().signOut();
        mGoogleSignInClient.signOut().addOnCompleteListener(this, task -> LogoutAfterAct());
    }

    private void LogoutAfterAct(){
        login.setVisibility(View.VISIBLE);
        display.setVisibility(INVISIBLE);
        name.setVisibility(INVISIBLE);
        print(this, getString(R.string.loggedout), 1);
    }


    private void setNavigationViewListener() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.backup: {
                if(save_ingredients.getVisibility()==VISIBLE)
                    print(this, getString(R.string.savechangesfirst), 1);
                else
                    start_backingup_from_firebase_with_a_empty_firebase_check();
                break;
            }
            case R.id.restore: {
                if (mAuth.getCurrentUser() != null)
                    start_restoring_into_firebase();
                else
                    print(this, getString(R.string.loginfirstpls), 1);
                break;
            }

            case R.id.invite: {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                String body = getString(R.string.invitepitch) + "\n\n" + getString(R.string.invitelink);
                share.putExtra(Intent.EXTRA_TEXT, body);
                startActivity(Intent.createChooser(share, getString(R.string.invitefriends)));
                break;
            }

            case R.id.more: {
                Intent more = new Intent(Intent.ACTION_VIEW, Uri.parse(more_apps_link));
                startActivity(more);
                break;
            }

            case R.id.logout: {
                logoutClicked();
                break;
            }
        }
        //close navigation drawer
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void start_backingup_from_firebase_with_a_empty_firebase_check(){
        MaintenanceReferences = database.getReference("maintenance");
        MaintenanceReferences.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String maintenance = dataSnapshot.getValue(String.class);
                assert maintenance!=null;
                if (maintenance.equals("no") || maintenance.equals("yes")) {
                    if (mAuth.getCurrentUser() != null) {
                        user = mAuth.getCurrentUser();
                        database = FirebaseDatabase.getInstance();
                        validemail = Objects.requireNonNull(mAuth.getCurrentUser().getEmail()).replace(".", "");
                        UserReferences = database.getReference("users/" + validemail);
                        SelectedItemsRef = UserReferences.child("selected_items");
                        SelectedItemsRef.setValue(null);
                        for (int i = 0; i < selected_items.size(); i++) {
                            String ingredientKey = SelectedItemsRef.push().getKey();
                            assert ingredientKey != null;
                            UserReferences.child("selected_items").child(ingredientKey).child("NAME").setValue(selected_items.get(i).NAME);
                            UserReferences.child("selected_items").child(ingredientKey).child("DISPLAY").setValue(selected_items.get(i).DISPLAY);
                            UserReferences.child("selected_items").child(ingredientKey).child("CATEGORY").setValue(selected_items.get(i).CATEGORY);
                        }
                        print(getApplicationContext(), getString(R.string.saved), 1);
                    } else
                        print(getApplicationContext(), getString(R.string.loginfirstpls), 1);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
    public void start_restoring_into_firebase(){
        user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        validemail = Objects.requireNonNull(mAuth.getCurrentUser().getEmail()).replace(".", "");
        UserReferences = database.getReference("users/" + validemail);
        UserReferences.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot child = dataSnapshot.child("selected_items");
                if(child.getChildrenCount()==0)
                    print(getApplicationContext(), getString(R.string.noselectedingredientsinaccount), 1);
                selected_items = new ArrayList<>();
                for (DataSnapshot childofchild : child.getChildren()) {
                    selected_items.add(new ListViewItem() {{
                        NAME = Objects.requireNonNull(childofchild.child("NAME").getValue()).toString();
                        DISPLAY = Objects.requireNonNull(childofchild.child("DISPLAY").getValue()).toString();
                        CATEGORY = Objects.requireNonNull(childofchild.child("CATEGORY").getValue()).toString();
                    }});
                }
                saveSelectedItemsInSQL();

                // Refresh possible plates
                possible_items = new ArrayList<>();
                reloadPossiblePlatesTab(extractPossiblePlates());
                if(possible_items.size() <= 0)
                    add_ingredients2.setVisibility(View.VISIBLE);
                else
                    add_ingredients2.setVisibility(INVISIBLE);

                // display finished
                print(getApplicationContext(), getString(R.string.loaded), 1);

                // Update the selected ingredients to the loaded one
                // To avoid  a noticable lag i added a delay before refreshing
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        SlidingTabsColorsFragment fragment = new SlidingTabsColorsFragment();
                        transaction.replace(R.id.sample_content_fragment, fragment);
                        transaction.commit();
                        if(save_ingredients.getVisibility()==VISIBLE) {
                            save_ingredients.startAnimation(slidedown);
                            slidedown.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {}
                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    save_ingredients.setVisibility(INVISIBLE); }
                                @Override
                                public void onAnimationRepeat(Animation animation) {}
                            });
                        }
                    }
                }, 500);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    public void searchClicked(View view) {
        if(searchinput.getVisibility()!=VISIBLE)
            display_search_menu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mToggle.onOptionsItemSelected(item)) { return true; }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    public void display_search_menu(){
        searchinput.setVisibility(VISIBLE);

        // Prepare for search
        // EditText setup
        searchinput.setEnabled(true);
        // show keyboard
        InputMethodManager imm = (InputMethodManager)   getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        searchinput.requestFocus();

        searcher.setVisibility(INVISIBLE);
        mToggle.setDrawerIndicatorEnabled(false);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        magni.setImageDrawable(getResources().getDrawable(R.drawable.x));
        searchinput.setVisibility(VISIBLE);

        // Handle search according to current page
        if(platespage)
            all_plates_search();
        else if(possiblepage)
            possible_plates_search();
        else if(fridgepage)
            ingredient_search();
    }
    public void ingredient_search(){
        searchinput.setHint(getString(R.string.searchingredients));
        searchinput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                input = s.toString();
                if(!input.equals(""))
                    find_input_in_ingredient_types(input);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
    public void find_input_in_ingredient_types(String s){
        found_ingredient = false;

        // TODO: Ingredient Types - Searcher
        for(int i=0; i<vegetables.size();i++){
            if(vegetables.get(i).NAME.contains(s) ||
               vegetables.get(i).DISPLAY.contains(s) ||
               vegetables.get(i).CATEGORY.contains(s)) {
                found_ingredient = true;
                if (mViewPager != null)
                    SlidingTabsColorsFragment.mViewPager.setCurrentItem(0);
                break;
            }
        }
        if(!found_ingredient){
            for(int i=0; i<spices.size();i++){
                if(spices.get(i).NAME.contains(s) ||
                   spices.get(i).DISPLAY.contains(s) ||
                   spices.get(i).CATEGORY.contains(s)) {
                    found_ingredient = true;
                    if (mViewPager != null)
                        SlidingTabsColorsFragment.mViewPager.setCurrentItem(1);
                    break;
                }
            }
        }
        if(!found_ingredient){
            for(int i=0; i<canned.size();i++){
                if(canned.get(i).NAME.contains(s) ||
                   canned.get(i).DISPLAY.contains(s) ||
                   canned.get(i).CATEGORY.contains(s)) {
                    found_ingredient = true;
                    if (mViewPager != null)
                        SlidingTabsColorsFragment.mViewPager.setCurrentItem(2);
                    break;
                }
            }
        }
        if(!found_ingredient){
            for(int i=0; i<miscellaneous.size();i++){
                if(miscellaneous.get(i).NAME.contains(s) ||
                   miscellaneous.get(i).DISPLAY.contains(s) ||
                   miscellaneous.get(i).CATEGORY.contains(s)) {
                    if (mViewPager != null)
                        SlidingTabsColorsFragment.mViewPager.setCurrentItem(3);
                    break;
                }
            }
        }
        if(!found_ingredient){
            for(int i=0; i<fruits.size();i++){
                if(fruits.get(i).NAME.contains(s) ||
                        fruits.get(i).DISPLAY.contains(s) ||
                        fruits.get(i).CATEGORY.contains(s)) {
                    if (mViewPager != null)
                        SlidingTabsColorsFragment.mViewPager.setCurrentItem(3);
                    break;
                }
            }
        }
        if(!found_ingredient){
            for(int i=0; i<dairy.size();i++){
                if(dairy.get(i).NAME.contains(s) ||
                        dairy.get(i).DISPLAY.contains(s) ||
                        dairy.get(i).CATEGORY.contains(s)) {
                    if (mViewPager != null)
                        SlidingTabsColorsFragment.mViewPager.setCurrentItem(3);
                    break;
                }
            }
        }
    }
    public void possible_plates_search(){
        searchinput.setHint(getString(R.string.searchpossibleplates));
        searchinput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                input = s.toString();
                current_search_word = input;
                if(input.equals("")){
                    // Pull the plates for the current tab
                    BigBoyPage.current_tab_plates = new ArrayList<>();
                    for(int i=0; i<possible_items.size(); i++){
                        plate = possible_items.get(i);
                        if(plate.CATEGORIES.contains(current_tab))
                            BigBoyPage.current_tab_plates.add(plate);
                    }

                    rvContactsPossiblePlates.setAdapter(adapter4);
                } else {
                    search_array = new ArrayList<>();
                    for (int i = 0; i < possible_items.size(); i++) {
                        ingredient_english = "";
                        ListViewItem2 item = possible_items.get(i);
                        IngredientNames = item.INGREDIENTS.split(",");

                        // Bring all french words of ingredients in plate to also search in
                        for(int j=1;j<IngredientNames.length;j++){
                            for(int z=0; z<possible_items.size(); z++) {
                                ListViewItem ingredient = AllIngredients.get(z);
                                if (IngredientNames[j].equals(ingredient.NAME))
                                    ingredient_english += ingredient.DISPLAY;
                            }
                        }

                        if (ingredient_english.contains(input) ||
                                item.NAME.contains(input) ||
                                item.INGREDIENTS.contains(input) ||
                                item.DISPLAY.contains(input))
                            search_array.add(item);
                    }
                    search_adapter = new PlatesAdapter(search_array);
                    rvContactsPossiblePlates.setAdapter(search_adapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void all_plates_search(){
        searchinput.setHint(getString(R.string.searchallplates));
        searchinput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                input  = s.toString();
                current_search_word = input;
                if(input.equals("")){

                    // Pull the plates for the current tab
                    BigBoyPage.current_tab_plates = new ArrayList<>();
                    for(int i=0; i<allPlates.size(); i++){
                        plate = allPlates.get(i);
                        if(plate.CATEGORIES.contains(current_tab))
                            BigBoyPage.current_tab_plates.add(plate);
                    }

                    adapter3 = new PlatesAdapter(current_tab_plates);
                    rvContactsAllPlates.setAdapter(adapter3);
                } else {
                    search_array = new ArrayList<>();
                    for (int i = 0; i < current_tab_plates.size(); i++) {
                        ingredient_english = "";
                        ListViewItem2 item = current_tab_plates.get(i);
                        IngredientNames = item.INGREDIENTS.split(",");

                        // Bring all french words of ingredients in plate to also search in
                        for(int j=1;j<IngredientNames.length;j++){
                            for(int z=0; z<AllIngredients.size(); z++) {
                                ListViewItem ingredient = AllIngredients.get(z);
                                if (IngredientNames[j].equals(ingredient.NAME))
                                    ingredient_english += ingredient.DISPLAY;
                            }
                        }

                        if (ingredient_english.contains(input) ||
                                item.NAME.contains(input) ||
                                item.INGREDIENTS.contains(input) ||
                                item.DISPLAY.contains(input))
                            search_array.add(item);
                    }
                    search_adapter = new PlatesAdapter(search_array);
                    rvContactsAllPlates.setAdapter(search_adapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        // hide keyboard
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchinput.getWindowToken(), 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(searchinput.getVisibility() == VISIBLE){
            // show keyboard
            InputMethodManager imm = (InputMethodManager)   getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            searchinput.requestFocus();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // hide keyboard
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchinput.getWindowToken(), 0);
    }

    public void exitsearchClicked(View view) {
        if(searchinput.getVisibility()==VISIBLE) {

            // Reset current search word string
            current_search_word = "";

            // hide keyboard
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchinput.getWindowToken(), 0);

            rvContactsAllPlates.setAdapter(adapter3);
            searchinput.setVisibility(INVISIBLE);
            searcher.setVisibility(VISIBLE);
            magni.setImageDrawable(getResources().getDrawable(R.drawable.magnifying_glass));
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            mToggle.setDrawerIndicatorEnabled(true);
            mToggle.syncState();
        } else
            display_search_menu();
    }


    public static class ListViewItem2{
        public String INGREDIENTS;
        public String AMOUNTS;
        public String NAME;
        public String CALORIES;
        public String DISPLAY;
        public String TIME;
        public String STEPS;
        public String VIDEOS;
        public String CATEGORIES;
    }

    public static class ListViewItem{
        public String NAME;
        public String DISPLAY;
        public String CATEGORY;
    }

    public static class PlateType{
        public String TYPE;
    }

}