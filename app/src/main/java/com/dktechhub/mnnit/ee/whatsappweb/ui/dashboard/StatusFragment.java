package com.dktechhub.mnnit.ee.whatsappweb.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dktechhub.mnnit.ee.whatsappweb.R;
import com.dktechhub.mnnit.ee.whatsappweb.Status;
import com.dktechhub.mnnit.ee.whatsappweb.StatusItemAdapter;

import java.util.ArrayList;

public class StatusFragment extends Fragment {


    RecyclerView photos,videos;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_status, container, false);
        photos=root.findViewById(R.id.images);
        videos=root.findViewById(R.id.videos);
        ArrayList<Status> items=new ArrayList<>();
        items.add(new Status());
        items.add(new Status());items.add(new Status());
        items.add(new Status());items.add(new Status());
        items.add(new Status());items.add(new Status());
        items.add(new Status());items.add(new Status());
        items.add(new Status());
        StatusItemAdapter photoAdapter=new StatusItemAdapter();
        StatusItemAdapter videosAdapter=new StatusItemAdapter();
        photoAdapter.addStatusItem(items);
        videosAdapter.addStatusItem(items);

        photos.setLayoutManager(new GridLayoutManager(getContext(),4));
        photos.setAdapter(photoAdapter);
        videos.setLayoutManager(new GridLayoutManager(getContext(),4));
        videos.setAdapter(videosAdapter);
        return root;
    }
}