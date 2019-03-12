package hi.hugbo.verywowchat.controllers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import hi.hugbo.verywowchat.Adapters.SectionsPagerAdapter;

/**
 * @Author Róman
 * This activity displays the user's home screen here he can swipe or pick different tabs
 * to display the desired content on top of that he can open his a side menu that displays
 * his information and the option to change it along other settings.
 *
 * This activities layout is quite big and complex its good to understand what is happening when its inflated.
 * 1. root activity (activity_user_home_page) is what allows us to use the side bar functionality
 *    and in it there are 2 references to other layouts (activity_home_page , nav_header_user_home_page)
 *    and 1 reference to a menu layout (activity_user_home_page_drawer) that will be inflated as well
 * 2. activity_home_page layout allows you to swipe left,right or choose a tab from the tab bar,
 *    the layout holds ViewPager widget which will display a fragment based on what tab the user has selected
 *    and that chosen fragment could have even more layouts
 * 3. nav_header_user_home_page layout is the side bar that will be displayed when the user chooses to open
 *    it by clicking the top left button, this layout does not hold the options that the user can choose that
 *    is in the activity_user_home_page_drawer
 * */
public class HomePageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    /**
     * This activity has only 1 widget ViewPager that displays different fragments
     * based on the current tab selected
     * */
    private ViewPager mViewPager;

    /**
     * This adapter is responsible for calling the correct
     * fragment based on what tab is selected
     * */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home_page); // inflate the BIG ASS LAYOUT

        /**----------------------------------------------------------------------------------------
         *  All the stuff needed to make the side nav bar and the tab's work
         * ----------------------------------------------------------------------------------------*/
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        drawer.addDrawerListener(new RightMenuListener());
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // Create the adapter that will return a fragment for each of the three  primary sections of the activity
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // NOTE ! getting the 3 tabs we have made
        TabLayout tabLayout = findViewById(R.id.tabs);

        // set listeners so u can switch
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        /** ----------------------------------------------------------------------------------------
         *  --------------------------------- other stuff ------------------------------------------
         *  ----------------------------------------------------------------------------------------*/
    }

    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, HomePageActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return i;
    }

    /**
     * <pre>
     *     Usage : LogOutUser(spreferencesEditor)
     *       For : spreferencesEditor is a SharedPreferences.Editor
     *     After : clears all the keys,value pairs in the Editor passed,
     *             starts LoginActivity class and clears all activities on the stack including the current one
     * </pre>
     * @param spreferencesEditor SharedPreferences.Editor
     */
    public void LogOutUser(SharedPreferences.Editor spreferencesEditor) {
        spreferencesEditor.clear();
        spreferencesEditor.commit();
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        // based on what Item the user choose we start the appropriate activity
        if (id == R.id.account_settings) {

        } else if (id == R.id.create_new_chat_room) {
            Intent intent = new Intent(this,CreateChatroomActivity.class);
            startActivity(intent);
        } else if (id == R.id.pending_requests) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /**
     * since drawer is not inflated right away it needs a listener that when the layout is inflated
     * it will do stuff like set listeners on buttons or change text etc...
     * */
    private class RightMenuListener implements android.support.v4.widget.DrawerLayout.DrawerListener {

        private Button mbgnLogOut;
        private TextView mGreeting;

        @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
        @Override
        public void onDrawerOpened(@NonNull View view) {
            final SharedPreferences UserInfo = getApplicationContext().getSharedPreferences("UserInfo", MODE_PRIVATE);
            // when the drawer is open we display a greeting messed with the user's displayname
            mGreeting = view.findViewById(R.id.userGreeting);
            mGreeting.setText("Welcome "+UserInfo.getString("displayname","ups Error"));

            /*
            *  when the drawer is open if the logout button does not have a listener on it we
            *  give it a listener that can perform a logout, this is done here because the inflated
            *  drawer will be stored in the phones memory and we dont know if it will stay that way
             * if it will stay we would be adding another listener to an existing one which we dont want
             * */
            mbgnLogOut = view.findViewById(R.id.btnLogoutUser);
            if(!mbgnLogOut.hasOnClickListeners()) {
                mbgnLogOut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LogOutUser(UserInfo.edit());
                    }
                });
            }
        }

        // --------------- dont see a use for these guys just yet--------------------------------
        @Override
        public void onDrawerSlide(@NonNull View view, float v) {

        }
        @Override
        public void onDrawerClosed(@NonNull View view) {

        }
        @Override
        public void onDrawerStateChanged(int i) {

        }
    }
}
