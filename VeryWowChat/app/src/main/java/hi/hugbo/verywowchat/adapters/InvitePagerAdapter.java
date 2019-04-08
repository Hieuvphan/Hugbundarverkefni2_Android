package hi.hugbo.verywowchat.adapters;

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

import hi.hugbo.verywowchat.fragments.ChatroomAdminInvitesListFragment;
import hi.hugbo.verywowchat.fragments.ChatroomInvitesListFragment;
import hi.hugbo.verywowchat.fragments.FriendRequestsListFragment;
import hi.hugbo.verywowchat.controllers.ChatRoomMessageActivity;
import hi.hugbo.verywowchat.controllers.R;
import hi.hugbo.verywowchat.models.helpers.RandomUtils;

/**
 * @Author Róman
 * The SectionsPagerAdapter responsibility is to return a fragment corresponding to
 * one of the tabs that the user selected.
 * */
public class InvitePagerAdapter extends FragmentPagerAdapter {

    public InvitePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        /*getItem is called to instantiate the fragment for the given page.
          Return a PlaceholderFragment (defined as a static inner class below).
          (NOTE ! that the Fragment next to the current fragment (i.e fragment at pos+1)
          will be also loaded in the memory, so if u start at fragment 0 (displayed) then
          fragment 1 will be also loaded in memory but not fragment 2) */

        // if the position is 0 then we display friend requests
        if(position == 0) {
            return FriendRequestsListFragment.newInstance();
        }
        // if the position is 1 then we display chatroom membership invites
        else if(position == 1) {
            return ChatroomInvitesListFragment.newInstance();
        }
        // if the position is 0 then we display chatroom admin invites
        else {
            return ChatroomAdminInvitesListFragment.newInstance();
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
         * Returns a new instance of this fragment for the given section  number.
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
            Log.d("dh", RandomUtils.getReport());
            Log.d("dh", "  !!!");
            View rootView = inflater.inflate(R.layout.fragment_home_page, container, false);
            TextView textView = rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            Button openChat = rootView.findViewById(R.id.openChat);
            openChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: suspicious activity here....
                    startActivity(ChatRoomMessageActivity.newIntent(getActivity(),"rruChat"));
                }
            });
            return rootView;
        }
    }
}