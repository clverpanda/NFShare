package com.clverpanda.nfshare.TasksList;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clverpanda.nfshare.R;


public class DoneTasksFrag extends Fragment {


    public DoneTasksFrag() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_done_tasks, container, false);
    }

}
