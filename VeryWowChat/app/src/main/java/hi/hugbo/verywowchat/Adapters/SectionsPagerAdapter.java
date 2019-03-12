package hi.hugbo.verywowchat.Adapters;

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

import hi.hugbo.verywowchat.Fragments.MyChatroomListFragment;
import hi.hugbo.verywowchat.Fragments.SearchChatroomListFragment;
import hi.hugbo.verywowchat.controllers.ChatRoomMessageActivity;
import hi.hugbo.verywowchat.controllers.R;

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
            // list of chatrooms I am member/admin/owner of
            return MyChatroomListFragment.newInstance();
        }
        // else its we display the public chat fragment
        else {
            // search for chatrooms you are not a member of
            return SearchChatroomListFragment.newInstance();
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