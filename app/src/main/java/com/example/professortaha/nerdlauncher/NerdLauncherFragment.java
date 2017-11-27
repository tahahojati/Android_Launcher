package com.example.professortaha.nerdlauncher;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by ProfessorTaha on 11/26/2017.
 */

public class NerdLauncherFragment extends Fragment{
    private RecyclerView mRecyclerView;
    private final static  String TAG = "NerdLauncherFragment";
    public static NerdLauncherFragment newInstance() {
        Bundle args = new Bundle();

        NerdLauncherFragment fragment = new NerdLauncherFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_nerd_launcher, container, false);
        mRecyclerView = v.findViewById(R.id.app_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setupAdapter();
        return v;
    }

    private void setupAdapter() {
        Intent startUpIntent = new Intent(Intent.ACTION_MAIN);
        PackageManager pm = getActivity().getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(startUpIntent, 0);
        Log.i(TAG, "Found "+activities.size()+" activities");
        Collections.sort(activities, new Comparator<ResolveInfo>() {
            @Override
            public int compare(ResolveInfo o1, ResolveInfo o2) {
                PackageManager pm = getActivity().getPackageManager();
                return String.CASE_INSENSITIVE_ORDER.compare(
                        o1.loadLabel(pm).toString(),
                        o2.loadLabel(pm).toString()
                );
            }
        });
        mRecyclerView.setAdapter(new ActivityAdapter(activities));
    }


    //Recycler view stuff:
    private class ActivityHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ResolveInfo mResolveInfo;
        private TextView mNameTextView;
        public ActivityHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mNameTextView = (TextView) itemView;
        }
        public void bind(ResolveInfo ri){
            mResolveInfo = ri;
            PackageManager pm = getActivity().getPackageManager();
            String appName = mResolveInfo.loadLabel(pm).toString();
            mNameTextView.setText(appName);
        }

        @Override
        public void onClick(View v) {
            ActivityInfo ai = mResolveInfo.activityInfo;
            Intent i = new Intent(Intent.ACTION_MAIN).setClassName(ai.applicationInfo.packageName, ai.name);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
    }

    private class ActivityAdapter extends RecyclerView.Adapter<ActivityHolder> {
        private final List<ResolveInfo> mActivities;

        private ActivityAdapter(List<ResolveInfo> activities) {
            mActivities = activities;
        }

        @Override
        public ActivityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater li = LayoutInflater.from(getActivity());
            ActivityHolder ah = new ActivityHolder(li.inflate(R.layout.simple_list_item_1, parent, false));
            return ah;
        }
        @Override
        public void onBindViewHolder(ActivityHolder holder, int position) {
            holder.bind(mActivities.get(position));
        }
        @Override
        public int getItemCount() {
            return mActivities.size();
        }
    }
}
