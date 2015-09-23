package com.swipeandflip.swipeandflip;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class ScreenSlidePageFragment extends Fragment {

    public static final String ARG_PAGE = "pagenumber";
    private int mPageNumber;
    /**
     * Whether or not we're showing the back of the card (otherwise showing the front).
     */
    private boolean mShowingBack = false;
    private FragmentManager fragmentManager;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static ScreenSlidePageFragment create(int pageNumber) {
        ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ScreenSlidePageFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.fragment_screen_slide_page, container, false);

        fragmentManager = getChildFragmentManager();

        fragmentManager
                .beginTransaction()
                .add(R.id.container, new CardFrontFragment())
                .commit();

        FrameLayout frameLayout = (FrameLayout) rootView.findViewById(R.id.container);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipCard();
            }
        });

        // Monitor back stack changes to ensure mShowingBack has correct value
        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                mShowingBack = (fragmentManager.getBackStackEntryCount() > 0);
            }
        });
        return rootView;
    }

    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return mPageNumber;
    }

    private void flipCard() {
        if (mShowingBack) {
            fragmentManager.popBackStack();
            return;
        }

        // Flip to the back.
        mShowingBack = true;
        fragmentManager
                .beginTransaction()
                .setCustomAnimations(
                        R.animator.card_flip_right_in, R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in, R.animator.card_flip_left_out)
                .replace(R.id.container, new CardBackFragment())
                .addToBackStack(null)
                .commit();
    }

    public static class CardFrontFragment extends Fragment{
        public CardFrontFragment() {}

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_card_front, container, false);
        }
    }

    public static class CardBackFragment extends Fragment{
        public CardBackFragment() {}

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_card_back, container, false);
        }
    }
}