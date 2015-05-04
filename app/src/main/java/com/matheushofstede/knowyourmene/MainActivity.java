package com.matheushofstede.knowyourmene;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.support.v4.view.MenuItemCompat;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ActionItemTarget;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.melnykov.fab.FloatingActionButton;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MainActivity extends ActionBarActivity implements SearchView.OnQueryTextListener {


    public String queryy;
    SQLHelper entry = new SQLHelper(MainActivity.this, queryy);


    int armengue = 0;
    public static final String PREFS_NAME = "com.matheushofstede.knowyourmene_preferences";
    boolean firsttime;
    public String grid_columns;
    public int grid_columns2;
    SharedPreferences sp;

    FloatingActionButton fab;


    DrawerLayout mDrawerLayout;
    //RecyclerView mDrawerList;
    ActionBarDrawerToggle mDrawerToggle;
    String[] mDrawerListItems;
    private Spinner spinner_nav;


    GridView mylist;
    ProgressDialog pd;
    AlertDialog conpd;
    private List<Post> names;
    private ImageLoader mImageLoader;
    boolean status = false;
    final String[] error_message = {"Falha ao sumonar os menes, verifique a conexão com a internet (coloque crédito)", "Que tal não roubar a internet do vizinho?", "Sua operadora é Tim?", "Ta no meio do mato parça?", "Não posso fazer nada por vc...", "Sua internet é tão ruim que vou chamar de iphone", "Meça suas frases de erro parça", "Leve essas frases na brincadeira, serio", "Me siga no Google Cátion!"};



    private String[] mDrawerTitles;

    private FragmentNavigationDrawer dlDrawer;

    public PostValue menes = new PostValue();

    Toolbar toolbar;

    ActionBarDrawerToggle actionBarDrawerToggle;
    DrawerLayout drawerLayout;
    private ListView mDrawerList;



    // nav drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;

    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;

    private SearchView mSearchView;
    MenuItem searchItem;

    ShowcaseView svfab, svsearch;
    ViewTarget tfab, tspinner;
    Target viewTarget;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        GoogleAnalytics.getInstance(this).newTracker("UA-62027103-1");

        Tracker tracker = ((Analytics)getApplication()).getTracker(Analytics.TrackerName.APP_TRACKER);

        tracker.setScreenName("Main Activity");
        //tracker.send(new HitBuilders.AppViewBuilder().build());


        tracker.send(new HitBuilders.EventBuilder().setCategory("MainActivity").setAction("onCreate").build());

        // Send a screen view.
        tracker.send(new HitBuilders.AppViewBuilder().build());






        fab = (FloatingActionButton)findViewById(R.id.fab);
        tfab = new ViewTarget(R.id.fab, this);
        tspinner = new ViewTarget(R.id.spinner_nav, this);
        tfab = new ViewTarget(R.id.fab, this);
        viewTarget = new Target() {
            @Override
            public Point getPoint() {
                return new ViewTarget(toolbar.findViewById(R.id.action_search)).getPoint();
            }
        };


        RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lps.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        int margin = ((Number) (getResources().getDisplayMetrics().density * 12)).intValue();
        lps.setMargins(margin, margin, margin, margin);



        svfab = new ShowcaseView.Builder(this, true)
                .setTarget(tfab)
                .setContentTitle("Atualizar menes")

                .setContentText("Toque nesse botão para receber menes fresquinhos (também acessivel nos Ajustes do app)")
                .setStyle(R.style.Semi_Transparent_ShowcaseView)

                .hideOnTouchOutside()
                .setShowcaseEventListener(new OnShowcaseEventListener() {
                    @Override
                    public void onShowcaseViewHide(ShowcaseView showcaseView) {
                        svfab.setVisibility(View.GONE);
                        showOverlayTutorialTwo();

                    }

                    @Override
                    public void onShowcaseViewDidHide(ShowcaseView showcaseView) {

                    }

                    @Override
                    public void onShowcaseViewShow(ShowcaseView showcaseView) {

                    }
                })
                .build();

        svfab.setButtonPosition(lps);



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                    new PostAsync().execute();





            }
        });
        mTitle = mDrawerTitle = getTitle();

        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        // nav drawer icons from resources
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        navDrawerItems = new ArrayList<NavDrawerItem>();

        // adding nav drawer items to array
        // Home
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        // Find People
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        // Photos
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        // Communities, Will add a counter here
       // navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1), true, "22"));
        // Pages
       // navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
        // What's hot, We  will add a counter here
       // navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), true, "50+"));


        // Recycle the typed array
        navMenuIcons.recycle();

        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);

        // enabling action bar app icon and behaving it as toggle button
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,

                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        }

         class SlideMenuClickListener implements
                ListView.OnItemClickListener {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                // display view for selected nav drawer item
                //displayView(position);
            }
        }
    }

        /**
         * Diplaying fragment view for selected nav drawer list item
         * */
    private void displayView(int position) {
        // update the main content by replacing fragments

        switch (position) {
            case 0:

                break;
            case 1:


                break;
            case 2:

                break;

            default:
                break;
        }



        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                        Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_LONG).show();



                        break;
                    case 1:
                        Intent pa = new Intent(getApplicationContext(), UserSettingActivity.class);
                        startActivity(pa);




                        break;
                    case 2:
                        Toast.makeText(MainActivity.this, "Sobre", Toast.LENGTH_LONG).show();



                        break;



                }

                mDrawerLayout.closeDrawer(mDrawerList);
            }
        });


        // enable ActionBar app icon to behave as action to toggle nav drawer
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        drawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.app_name,R.string.app_name);

        drawerLayout.setDrawerListener(actionBarDrawerToggle);




        spinner_nav = (Spinner) findViewById(R.id.spinner_nav);
        sp = getSharedPreferences(PREFS_NAME,0);
        firsttime = sp.getBoolean("firsttime", true);
        this.grid_columns = sp.getString("grid_columns", "3");
        Log.i("TAG",grid_columns);
        //menes.setGrid_columns(grid_columns);
        grid_columns2 = Integer.parseInt(grid_columns);

        //additemto spinner estaria qui

       /* // Find our drawer view
        dlDrawer = (FragmentNavigationDrawer) findViewById(R.id.drawer_layout);
        // Setup drawer view
        dlDrawer.setupDrawerConfiguration((ListView) findViewById(R.id.lvDrawer), toolbar,
                R.layout.drawer_list_item, R.id.flContent);
        // Add nav items
       // dlDrawer.addNavItem("First", "First Fragment", MainActivity.class);
       // dlDrawer.addNavItem("Second", "Second Fragment", SecondFragment.class);
       // dlDrawer.addNavItem("Third", "Third Fragment", ThirdFragment.class);
        // Select default
        if (savedInstanceState == null) {
            dlDrawer.selectDrawerItem(0);
        }

*/



        status = checkInternetConnection();


        if (status)
        {

        }
        else
        {
           Intent i = new Intent(getApplicationContext(),NoConnection.class);
           startActivity(i);
            finish();

        }

        addItemsToSpinner();






















        //SQLHelper entry = new SQLHelper(MainActivity.this);

    }


        private boolean checkInternetConnection() {
            ConnectivityManager connectivity = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

            if (connectivity != null)
            {
                NetworkInfo[] inf = connectivity.getAllNetworkInfo();
                if (inf != null)
                    for (int i = 0; i < inf.length; i++)
                        if (inf[i].getState() == NetworkInfo.State.CONNECTED)
                        {
                            return true;
                        }

            }
            return false;
        }


    class PostAsync extends AsyncTask<Void, Void, Void> {

        XMLHelper helper;

        @Override
        protected void onPreExecute() {
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("firsttime",false);
            editor.commit();


            pd = new ProgressDialog(MainActivity.this);

            pd.setMessage("Baixando os mene tudo...");
            pd.setCancelable(false);
            pd.show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            //parsar o xml
            helper = new XMLHelper();
            helper.get();

            //preparar a database
            entry.open();
            entry.clearDatabase();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            for(PostValue post : helper.knowyourmene){


                String link = post.getLink();
                String tag1 = post.getTag1();
                String tag2 = post.getTag2();
                String tag3 = post.getTag3();



                entry.create(link, tag1, tag2, tag3);
                //colocar fora do for pra ver oq acontece
                populate();


            }
            //fecha database
            entry.close();
            //progress dialog fecha
            pd.dismiss();
        }



    }





    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions, menu);



        MenuItem searchItem = menu.findItem(R.id.action_search);
        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {

                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {

                Intent i = getIntent().addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                overridePendingTransition(0, 0);
                startActivity(i);
                return false;
            }
        });
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        setupSearchView(searchItem);

        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Toast.makeText(MainActivity.this, "PEGO", Toast.LENGTH_SHORT).show();
                return false;
            }
        });









        return true;
    }
    private void setupSearchView(MenuItem searchItem) {

        if (isAlwaysExpanded()) {
            mSearchView.setIconifiedByDefault(true);
        } else {
            searchItem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM
                    | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        }

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            List<SearchableInfo> searchables = searchManager.getSearchablesInGlobalSearch();

            SearchableInfo info = searchManager.getSearchableInfo(getComponentName());
            for (SearchableInfo inf : searchables) {
                if (inf.getSuggestAuthority() != null
                        && inf.getSuggestAuthority().startsWith("applications")) {
                    info = inf;
                }
            }
            mSearchView.setSearchableInfo(info);
        }

        mSearchView.setOnQueryTextListener(this);



    }


    public boolean onQueryTextChange(String newText) {

        return false;
    }

    public boolean onQueryTextSubmit(String query) {

        try{
            query = query.substring(0,3);

        }catch(StringIndexOutOfBoundsException e){
            Log.e("SEARCHVIEW","Exeption catched!, siginifica querys menos especificas");



        }



        this.queryy = query;
        armengue = 5;
        entry.open();
        populate();
        entry.close();

        return false;
    }





        protected boolean isAlwaysExpanded() {
            return false;
        }




    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {

            case android.R.id.home:

                NavUtils.navigateUpFromSameTask(this);
                Toast.makeText(this, "Ação de vootar aqui", Toast.LENGTH_SHORT).show();




                return true;





        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("deprecation")
    public void populate(){
        Cursor cursor = null;
        if (armengue == 0){
            cursor = entry.Recent();
        }
        if (armengue == 1){
            cursor = entry.getAllRows();
        }
        if (armengue == 2){
            cursor = entry.Recent();
        }
        if (armengue == 3){
            cursor = entry.Random();
        }
        if (armengue == 4){
            cursor = entry.Recent();
        }
        if (armengue == 5){
            cursor = entry.Query(queryy);

        }







        names = new ArrayList<Post>();

        cursor.moveToFirst();


        while (cursor.isAfterLast() == false) {
            names.add(new Post(cursor.getString(cursor.getColumnIndex("link")), (cursor.getString(cursor.getColumnIndex("tag1"))), (cursor.getString(cursor.getColumnIndex("tag2"))),(cursor.getString(cursor.getColumnIndex("tag3")))));
            cursor.moveToNext();

        }

        DisplayImageOptions mDisplayImageOptions = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.noimage)
                .showImageOnFail(R.drawable.noimage)
                .showImageOnLoading(R.drawable.loading)
                .cacheInMemory(false)
                .cacheOnDisk(true)

                .build();


        ImageLoaderConfiguration conf = new ImageLoaderConfiguration.Builder(MainActivity.this)
                .defaultDisplayImageOptions(mDisplayImageOptions)
                .memoryCacheSize(50 * 1024 *1024)

                .threadPoolSize(5)
                .writeDebugLogs()

                .build();

        mImageLoader = ImageLoader.getInstance();
        mImageLoader.init(conf);

        mylist = (GridView)findViewById(R.id.gridview);

        mylist.setNumColumns(grid_columns2);
        this.grid_columns = sp.getString("grid_columns", "3");






        mylist.setAdapter(new AdapterListView(MainActivity.this, names, mImageLoader, grid_columns ));

        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener(){


            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position,
                                    long id) {
                Intent i = new Intent(getApplicationContext(), FullImageActivity.class);
                //fazer o trabalho de verificar a postion em comprar com o name Array aqui (menos trabalho)
                //String fullimage = names.get(position).getUrlimg();
                //Log.i("POSITION", "Exeption:"+position + " " + fullimage);
                //String text = names.get(position);





                //position++;
                //Post text = names.get(position);
                //Log.i("POSITION", "Exeption:"+position + " " + text);
                Post link = names.get(position);
                Log.i("POSITION", "Exeption:" + position + " " + link.link);
                String link1 = link.link;
                //String tag1 = link.tag1;




                i.putExtra("id", link1);
                //i.putExtra("tag", tag1);
                startActivity(i);

            }
        });

        fab.attachToListView(mylist);
    }

    public void addItemsToSpinner() {

        ArrayList<String> list = new ArrayList<String>();
        list.add("Recente");
        list.add("Todas");
        list.add("+Baixadas");
        list.add("Aleatório");
        list.add("Favoritas");


        // Custom ArrayAdapter with spinner item layout to set popup background

        CustomSpinnerAdapter spinAdapter = new CustomSpinnerAdapter(
                getApplicationContext(), list);



        // Default ArrayAdapter with default spinner item layout, getting some
        // view rendering problem in lollypop device, need to test in other
        // devices

  /*
   * ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(this,
   * android.R.layout.simple_spinner_item, list);
   * spinAdapter.setDropDownViewResource
   * (android.R.layout.simple_spinner_dropdown_item);
   */

        spinner_nav.setAdapter(spinAdapter);

        spinner_nav.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View v,
                                       int position, long id) {
                // On selecting a spinner item
                switch (position){
                    //fazer a quela parada de checar se é a primeira vez que o app inicia
                    //isso la em cima
                    //agr aqui
                    //se primeira vez = true
                    //NAO roda o que tem aqui
                    //no case 0
                    case 0:

                        if(firsttime == true){
                            armengue = 0;
                            Toast.makeText(MainActivity.this, "Primeira vez que o app é aberta", Toast.LENGTH_LONG).show();
                            new PostAsync().execute();
                            firsttime = sp.getBoolean("firsttime",false);

                        }else{
                            armengue = 0;
                            entry.open();
                            populate();
                            entry.close();

                        }
                        break;

                    case 1:
                        armengue = 1;
                        entry.open();
                        populate();
                        entry.close();
                        break;

                    case 2:
                        armengue = 2;
                        entry.open();
                        populate();
                        entry.close();
                        break;

                    case 3:
                        armengue = 3;
                        entry.open();
                        populate();
                        entry.close();
                        break;
                    case 4:
                        armengue = 4;
                        entry.open();
                        populate();
                        entry.close();
                        break;





                }
                /* Showing selected spinner item
                Toast.makeText(getApplicationContext(), "Selected  : " + item,
                        Toast.LENGTH_LONG).show(); */
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

    }


    /***
     * Called when invalidateOptionsMenu() is triggered

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.navigation_drawer).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }
     */

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }









    @Override
    protected void onStart() {
        super.onStop();

        GoogleAnalytics.getInstance(this).reportActivityStart(this);

    }


    @Override
    protected void onStop() {
        super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }

    public void showOverlayTutorialTwo(){

        svsearch = new ShowcaseView.Builder(this, true)
                .setTarget(viewTarget)
                .setContentTitle("Procurar Menes")

                .setContentText("Use palavras chaves para procurar por menes específicos.")
                .setStyle(R.style.Semi_Transparent_ShowcaseView)

                .hideOnTouchOutside()
                .setShowcaseEventListener(new OnShowcaseEventListener() {
                    @Override
                    public void onShowcaseViewHide(ShowcaseView showcaseView) {
                        svfab.setVisibility(View.GONE);
                        showOverlayTutorialThree();

                    }

                    @Override
                    public void onShowcaseViewDidHide(ShowcaseView showcaseView) {

                    }

                    @Override
                    public void onShowcaseViewShow(ShowcaseView showcaseView) {

                    }
                })
                .build();

    }



    public void showOverlayTutorialThree(){

        svsearch = new ShowcaseView.Builder(this, true)
                .setTarget(tspinner)
                .setContentTitle("Mudar categoria")

                .setContentText("Mostrar todos menes, favoritos, mais baixados (ToDo) ou mostrar alguns menes aleatórios.")
                .setStyle(R.style.Semi_Transparent_ShowcaseView)
                .doNotBlockTouches()
                .hideOnTouchOutside()
                .setShowcaseEventListener(new OnShowcaseEventListener() {
                    @Override
                    public void onShowcaseViewHide(ShowcaseView showcaseView) {
                        svfab.setVisibility(View.GONE);


                    }

                    @Override
                    public void onShowcaseViewDidHide(ShowcaseView showcaseView) {

                    }

                    @Override
                    public void onShowcaseViewShow(ShowcaseView showcaseView) {

                    }
                })
                .build();

    }


}