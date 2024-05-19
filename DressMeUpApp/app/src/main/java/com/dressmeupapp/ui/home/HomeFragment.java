package com.dressmeupapp.ui.home;


import static androidx.core.content.ContextCompat.getSystemService;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.content.Context;
import android.location.LocationManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.dressmeupapp.R;
import com.dressmeupapp.databinding.FragmentHomeBinding;
import com.dressmeupapp.retrofit.entities.Post;
import com.dressmeupapp.retrofit.interfaces.ApiService;
import com.dressmeupapp.retrofit.interfaces.RetrofitClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Spinner spinnerRadius;
    private PostListAdapter adapter;
    private int radius = 1;
    private LocationManager locationManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        locationManager = (LocationManager) getSystemService(getActivity(), LocationManager.class);

        recyclerView = view.findViewById(R.id.recyclerView);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        spinnerRadius = view.findViewById(R.id.spinnerRadius);

        // Set up RecyclerView
        adapter = new PostListAdapter(new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // Set up SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchListData();
            }
        });

        // Set up Spinner
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.radius_array,
                android.R.layout.simple_spinner_item
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRadius.setAdapter(spinnerAdapter);

        spinnerRadius.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedRadius = parent.getItemAtPosition(position).toString().split(" ")[0];
                radius = Integer.parseInt(selectedRadius);
                fetchListData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Fetch and display the list data from API here
        fetchListData();

        return view;
    }

    private void fetchListData() {
        swipeRefreshLayout.setRefreshing(true);
        Location loc = getLastKnownLocation();

        ApiService apiService = RetrofitClient.getClient(getActivity()).create(ApiService.class);
        Call<List<Post>> call = apiService.getPosts(Double.toString(loc.getLatitude()), Double.toString(loc.getLongitude()),
                Integer.toString(radius));
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful()) {
                    List<Post> posts = response.body();
                    adapter.updateData(posts);
                } else {

                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                System.out.println("dupa");
            }
        });
        swipeRefreshLayout.setRefreshing(false);
    }

    private Location getLastKnownLocation() {
        Location warsawLocation = new Location("Warsaw");
        warsawLocation.setLatitude(52.2297);
        warsawLocation.setLongitude(21.0122);

        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return warsawLocation;
            }
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l;
            }
        }
        return bestLocation == null ? warsawLocation : bestLocation;
    }


}
