package hi.hugbo.verywowchat.Adapters;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import hi.hugbo.verywowchat.Fragments.ChatroomListFragment;
import hi.hugbo.verywowchat.Models.ChatroomService;
import hi.hugbo.verywowchat.Models.ChatroomServiceImplementation;
import hi.hugbo.verywowchat.controllers.ChatRoomMessageActivity;
import hi.hugbo.verywowchat.controllers.R;
import hi.hugbo.verywowchat.entities.Chatroom;

/**
 * @Author Róman
 * The SectionsPagerAdapter responsibility is to return a fragment corresponding to
 * one of the tabs that the user selected.
 * */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        /*getItem is called to instantiate the fragment for the given page.
          Return a PlaceholderFragment (defined as a static inner class below).
          (NOTE ! that the Fragment next to the current fragment (i.e fragment at pos+1)
          will be also loaded in the memory, so if u start at fragment 0 (displayed) then
          fragment 1 will be also loaded in memory but not fragment 2) */

        // if the position is 0 then we display Friends Fragment
        if(position == 0) {
            return PlaceholderFragment.newInstance(position + 1);
        }
        // if the position is 1 then we display User Chat Fragment
        else if(position == 1) {
            return ChatroomListFragment.newInstance();
            //return PlaceholderFragment.newInstance(position + 1);
        }
        // else its we display the public chat fragment
        else {
            return PlaceholderFragment.newInstance(position + 1);
        }
    }

    @Override
    public int getCount() {
        // we have a total of 3 pages so we return 3
        return 3;
    }


    /** THIS is a placeholder fragment because we dont have the correct fragments implemented yet */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_home_page, container, false);
            TextView textView = rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            Button openChat = rootView.findViewById(R.id.openChat);
            openChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(ChatRoomMessageActivity.newIntent(getActivity(),"rruchat"));
                }
            });
            return rootView;
        }
    }
}