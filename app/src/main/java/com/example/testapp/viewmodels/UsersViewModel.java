package com.example.testapp.viewmodels;

import android.util.Log;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.testapp.configs.PreferencesHelper;
import com.example.testapp.models.ResultResponse;
import com.example.testapp.models.UserModel;
import com.example.testapp.models.UserResponse;
import com.example.testapp.repository.TestAppRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class UsersViewModel extends ViewModel {

    @Inject
    PreferencesHelper preferencesHelper;

    private TestAppRepository repository;

    private MutableLiveData<List<UserModel>> usersResponseMutableLiveData = new MutableLiveData<>();

    @ViewModelInject
    public UsersViewModel(TestAppRepository repository) {
        this.repository = repository;
    }


    public MutableLiveData<List<UserModel>> getUsersData() {
        return usersResponseMutableLiveData;
    }

    public void getUsers() {

        ArrayList<UserModel> list = new ArrayList<>();

        repository.getUsers()
                .subscribeOn(Schedulers.io())
                .map(userResponse -> {
                    list.clear();
                    for (ResultResponse response : userResponse.getResults()) {
                        list.add(new UserModel(response.getPicture().getMedium(),
                                response.getName().getFirst(),
                                response.getName().getLast(),
                                response.getEmail()));
                    }

                    return list;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> usersResponseMutableLiveData.setValue(result),
                        error-> Log.e("TAG", "error getting users: " + error.getMessage()));

    }
}
