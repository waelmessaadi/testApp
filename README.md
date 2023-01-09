# testApp
MVVM with Hilt, RxJava 3, Retrofit, Room, Live Data and Data Binding

In this article, we will see how to implement MVVM architecture with Hilt, RxJava, Retrofit, Room, Live Data, and View Binding.

In this project, we will fetch details form a REST API and then use Room to save it offline. Since a number of concepts are being used here we will look at each of them one by one.

Let's dive into code.

###### First, add all the required dependencies to your project.

```
plugins {
    id 'com.android.application'
    id 'com.jakewharton.butterknife'
    id 'dagger.hilt.android.plugin'
    id 'androidx.navigation.safeargs'
}

android {
    compileSdk 32

    defaultConfig {
        applicationId "com.example.testapp"
        minSdk 21
        targetSdk 32
        versionCode 1
        versionName "1.0"

        multiDexEnabled true

        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }

    packagingOptions{
        pickFirst "androidsupportmultidexversion.txt"
    }

    buildFeatures {
        dataBinding true
        viewBinding true
    }
}

dependencies {

    implementation 'androidx.appcompat:appcompat:1.5.1'
    implementation 'com.google.android.material:material:1.7.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'

    def room_version = "2.2.5"
    def nav_version = "2.3.0-beta01"

    implementation "androidx.recyclerview:recyclerview:1.1.0"
    implementation 'com.android.support:multidex:1.0.3'

    // Hilt
    implementation "com.google.dagger:hilt-android:2.28-alpha"
    annotationProcessor 'com.google.dagger:hilt-android-compiler:2.28-alpha'
    implementation 'androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha01'
    annotationProcessor 'androidx.hilt:hilt-compiler:1.0.0-alpha01'

    //RxJava
    implementation 'io.reactivex.rxjava3:rxandroid:3.0.0'
    implementation 'io.reactivex.rxjava3:rxjava:3.0.0'

    // Retrofit
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
    implementation "com.github.akarnokd:rxjava3-retrofit-adapter:3.0.0"

    // ViewModel
    implementation 'androidx.lifecycle:lifecycle-viewmodel:2.2.0'

    // LiveData
    implementation 'androidx.lifecycle:lifecycle-livedata:2.2.0'
    implementation "androidx.lifecycle:lifecycle-livedata-ktx:2.2.0"

    // Room
    implementation "androidx.room:room-runtime:$room_version"
    annotationProcessor "androidx.room:room-compiler:$room_version"
    implementation "androidx.room:room-rxjava2:$room_version"

    // Navigation
    implementation "androidx.navigation:navigation-fragment:$nav_version"
    implementation "androidx.navigation:navigation-ui:$nav_version"

    // ButterKnife
    implementation 'com.jakewharton:butterknife:10.2.3'
    annotationProcessor 'com.jakewharton:butterknife-compiler:10.2.3'

    implementation 'com.squareup.picasso:picasso:2.8'

    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit:1.1.5'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
}

```

To setup hilt, you can also find the instructions from this link :
https://developer.android.com/training/dependency-injection/hilt-android

###### Setting up API service
To fetch details we are using RandomUserApi here :
- Random Users:
    - https://randomuser.me/api (REST / JSON)
    - https://randomuser.me/documentation
    
    
package com.example.testapp.interfaces;

import com.example.testapp.models.UserResponse;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;

public interface TestAppApiServices {
    @GET("api")
    Observable<UserResponse> getUsers();
}

As you can see the return type is Observable will see more about RxJava later in the article.

Our User and UserResponse model class look like this:

package com.example.testapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserResponse {
    @SerializedName("results")
    @Expose
    private List<ResultResponse> results = null;

    public List<ResultResponse> getResults() {
        return results;
    }

    public void setResults(List<ResultResponse> results) {
        this.results = results;
    }

}


package com.example.testapp.models;

public class UserModel {
    private String image;
    private String firstname;
    private String lastname;
    private String email;

    public UserModel(String image, String firstname, String lastname, String email) {
        this.image = image;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}


###### Setting up Hilt
Base Application class: This class is necessary for the hilt and you should annotate it with @HiltAndroidApp. Don't forget to add it to the manifest file in the application tag.

<application
    android:name=".BaseApplication"
    
    
package com.example.testapp;

import android.app.Application;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class BaseApplication extends Application {
}


Now we will create a Network module. Now, what is a module? A module is a class that provides information to the hilt about how to provide an instance of a class we don't own. For hilt to instantiate objects for us we use @Inject annotation above the constructor of the class but when where to put the @Inject annotation when we don't own a class or when we are dealing with an interface which doesn't have a constructor in these cases we use @Provide annotation inside the module class to tell hilt to instantiate these objects for us. To setup module create a class NetworkModule and annotate it with @Module annotation now in hilt we have to add one more annotation which is @InsatallIn annotation and we will pass ApplicationComponent here because we want the NetworkModule to be available for us for application scope.

In the module, we will provide a method to get the TestAppApiServices object. Create a method provideTestAppApiService of TestAppApiServices return type and annotate it with @Provide annotation.

package com.example.testapp.di;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.testapp.commons.CommonKeys;
import com.example.testapp.configs.PreferencesHelper;
import com.example.testapp.interfaces.TestAppApiServices;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(ApplicationComponent.class)
public class NetworkModule {

    @Provides
    @Singleton
    public SharedPreferences providesSharedPreferences(Application application) {
        return application.getSharedPreferences(CommonKeys.APP_NAME, Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    public PreferencesHelper providesPreferencesHelper(SharedPreferences sharedPreferences) {
        return new PreferencesHelper(sharedPreferences);
    }

    @Provides
    @Singleton
    public static TestAppApiServices provideTestAppApiService() {

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        return new Retrofit.Builder()
                .baseUrl(CommonKeys.baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(okHttpClient)
                .build()
                .create(TestAppApiServices.class);
    }
}

Our app will have two fragments one to show the list of users fetched from the API and the second fragment will show the detsils of selected user which we have saved using Room.


###### Setting up Repository

package com.example.testapp.repository;

import com.example.testapp.configs.PreferencesHelper;
import com.example.testapp.dao.TestAppDao;
import com.example.testapp.db.TestAppDB;
import com.example.testapp.interfaces.TestAppApiServices;
import com.example.testapp.models.UserResponse;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;

public class TestAppRepository {
    private TestAppDao testAppDao;
    private TestAppApiServices apiService;
    private PreferencesHelper preferencesHelper;

    @Inject
    public TestAppRepository(TestAppDao testAppDao, TestAppApiServices apiService, PreferencesHelper preferencesHelper) {
        this.testAppDao = testAppDao;
        this.apiService = apiService;
        this.preferencesHelper = preferencesHelper;
    }

    // Preferences return
    public String getImage() {
        return preferencesHelper.getImage();
    }

    public void setImage(String image) {
        preferencesHelper.setImage(image);
    }

    public String getFirstName() {
        return preferencesHelper.getFirstName();
    }

    public void setFirstName(String firstName) {
        preferencesHelper.setFirstName(firstName);
    }

    public String getLastName() {
        return preferencesHelper.getLastName();
    }

    public void setLastName(String lastName) {
        preferencesHelper.setLastName(lastName);
    }

    public String getEmail() {
        return preferencesHelper.getEmail();
    }

    public void setEmail(String email) {
        preferencesHelper.setEmail(email);
    }

    public void clearAllCaches() {
        preferencesHelper.clearAllCaches();
    }

    // Apis return
    public Observable<UserResponse> getUsers() {
        return apiService.getUsers();
    }

}


###### Setting up ViewModel

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


###### Setting up Fragments 

package com.example.testapp.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
