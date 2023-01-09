package com.example.testapp.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewbinding.ViewBinding;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.testapp.R;
import com.example.testapp.adapters.UserAdapter;
import com.example.testapp.databinding.UsersViewBinding;
import com.example.testapp.models.UserModel;
import com.example.testapp.viewmodels.UsersViewModel;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UsersView#newInstance} factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
public class UsersView extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private UsersViewBinding binding;
    private UsersViewModel viewModel;

    UserAdapter adapter;

    List<UserModel> listUsers = new ArrayList<>();

    public UsersView() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UsersView.
     */
    // TODO: Rename and change types and number of parameters
    public static UsersView newInstance(String param1, String param2) {
        UsersView fragment = new UsersView();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(UsersViewModel.class);
        initRecyclerView(view);
        observeData();
        viewModel.getUsers();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = UsersViewBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    private void observeData() {
        viewModel.getUsersData().observe(getViewLifecycleOwner(), userModels -> {
            listUsers.clear();
            listUsers.addAll(userModels);

            adapter.updateList(listUsers);

        });
    }

    private void initRecyclerView(View view) {
        binding.rcvUsers.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new UserAdapter(getContext(), listUsers);
        binding.rcvUsers.setAdapter(adapter);

        adapter.setOnItemClickListener(position -> {

            UsersViewDirections.ActionUsersViewToUserDetailsView action = UsersViewDirections.actionUsersViewToUserDetailsView();

            action.setArgFirstname(listUsers.get(position).getFirstname());

            action.setArgLastname(listUsers.get(position).getLastname());

            action.setArgEmail(listUsers.get(position).getEmail());

            action.setArgImage(listUsers.get(position).getImage());

            Navigation.findNavController(view).navigate(action);
        });

    }
}