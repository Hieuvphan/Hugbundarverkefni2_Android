package hi.hugbo.verywowchat.controllers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import hi.hugbo.verywowchat.adapters.InvitePagerAdapter;

/**
 * @Author Vilhelm
 * This activity displays the user's environment for managing friend requests, chatroom member
 * invites and chatroom admin invites.
 * */
public class ManageInvitesActivity extends AppCompatActivity {

    /**
     * This activity has only 1 widget ViewPager that displays different fragments
     * based on the current tab selected
     * */
    private ViewPager mViewPager;

    /**
     * This adapter is responsible for calling the correct
     * fragment based on what tab is selected
     * */
    private InvitePagerAdapter mInvitePagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_page);

        // Create the adapter that will return a fragment for each of the three  primary sections of the activity
        mInvitePagerAdapter = new InvitePagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mInvitePagerAdapter);

        // NOTE ! getting the 3 tabs we have made
        TabLayout tabLayout = findViewById(R.id.tabs);

        // set listeners so u can switch
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }

    public static Intent newIntent(Context packageContext) {
        return new Intent(packageContext, ManageInvitesActivity.class);
    }
}
