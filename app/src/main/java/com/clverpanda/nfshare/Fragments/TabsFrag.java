package com.clverpanda.nfshare.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clverpanda.nfshare.ContentShare.ContentPagerAdapter;
import com.clverpanda.nfshare.R;
import com.clverpanda.nfshare.ResourceShare.ResourcePagerAdapter;


public class TabsFrag extends Fragment {

    private static final String TYPE = "type";


    private String mViewName;

    private OnFragmentInteractionListener mListener;

    private ViewPager viewPager;
    private TabLayout tabLayout;

    public TabsFrag() {
        // Required empty public constructor
    }


    public static TabsFrag newInstance(String viewName) {
        TabsFrag fragment = new TabsFrag();
        Bundle args = new Bundle();
        args.putString(TYPE, viewName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mViewName = getArguments().getString(TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_tabs, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        if ("ContentShare".equals(mViewName))
            viewPager.setAdapter(new ContentPagerAdapter(getChildFragmentManager(), getContext()));
        else if ("ResourceShare".equals(mViewName))
            viewPager.setAdapter(new ResourcePagerAdapter(getChildFragmentManager(), getContext()));
        else
            viewPager.setAdapter(new ContentPagerAdapter(getChildFragmentManager(), getContext()));
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onContentShareInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void onContentShareInteraction(Uri uri);
    }
}
