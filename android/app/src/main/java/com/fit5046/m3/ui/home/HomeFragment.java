package com.fit5046.m3.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fit5046.m3.MainViewModel;
import com.fit5046.m3.R;
import com.fit5046.m3.entity.Memoir;
import com.fit5046.m3.lib.Utils;
import com.fit5046.m3.lib.VerticleItemDecoration;

import java.util.List;

// Fragment for home screen.
public class HomeFragment extends Fragment {

    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_home, container, false);

        // get user id from main view model and init its own view model with the id.
        if (getActivity() == null) return null;
        MainViewModel mainModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
        if (mainModel.getUserId().getValue() == null) return null;

        HomeViewModel model = new ViewModelProvider(this).get(HomeViewModel.class);
        model.init(mainModel.getUserId().getValue());

        model.getMemoirs().observe(getViewLifecycleOwner(), this::setRecyclerView);
        model.getUserFname().observe(getViewLifecycleOwner(), this::greeting);

        TextView tv_date = root.findViewById(R.id.tv_date);
        tv_date.setText(String.format("%s %s", getString(R.string.today), Utils.getCurrentDate()));

        return root;
    }

    // display a greeting message using the user first name.
    private void greeting(String fname) {
        TextView tv_hello = root.findViewById(R.id.tv_hello);
        tv_hello.setText(String.format("%s %s", getString(R.string.hello), fname));
    }

    // used to set up the recycler view for the top five score movies in this year.
    private void setRecyclerView(List<Memoir> memoirs) {
        RecyclerView rv = root.findViewById(R.id.recycler);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(manager);
        HomeRvAdapter adapter = new HomeRvAdapter(getActivity(), memoirs);
        rv.addItemDecoration(new VerticleItemDecoration(50));
        rv.setAdapter(adapter);
    }
}
