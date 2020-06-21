package com.fit5046.m3.ui.map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fit5046.m3.MainViewModel;
import com.fit5046.m3.entity.Location;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.fit5046.m3.R;

import java.util.List;

// Used for Map screen.
public class MapFragment extends Fragment implements OnMapReadyCallback{

    private GoogleMap map;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_map, container, false);

        // get user id from Main View Model and then use it to get the user address in ViewModel.
        MapViewModel model = new ViewModelProvider(this).get(MapViewModel.class);
        MainViewModel mainModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        if (mainModel.getUserId().getValue() == null) return null;
        model.init(mainModel.getUserId().getValue());

        model.getUserLoc().observe(getViewLifecycleOwner(), this::getUserLoc);
        model.getCinemaLoc().observe(getViewLifecycleOwner(), this::getCinemaLoc);

        return root;
    }

    // Display the locations of cinemas where user has been to.
    private void getCinemaLoc(List<Location> locations) {
        for (Location location: locations) {
            LatLng loc = new LatLng(location.getLat(), location.getLon());
            map.addMarker(new MarkerOptions().position(loc).title(location.getOwner()));
        }
    }

    // Display the user's address on map.
    private void getUserLoc(Location location) {
        LatLng loc = new LatLng(location.getLat(), location.getLon());
        map.addMarker(new MarkerOptions().position(loc).title(location.getOwner())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        map.moveCamera(CameraUpdateFactory.newLatLng(loc));

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
    }
}
