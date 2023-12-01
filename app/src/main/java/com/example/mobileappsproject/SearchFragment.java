package com.example.mobileappsproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchFragment extends Fragment {
    private FirebaseFirestore Database = FirebaseFirestore.getInstance();
    private Map<String, String> UUIDMap;
    private List<String> Usernames;
    private ArrayAdapter<String> UsernameAdapter;

    public SearchFragment() {
    }
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        UUIDMap = new HashMap<>();
        Usernames = new ArrayList<>();
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize our array adapter and fetch the list
        UsernameAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1, Usernames);
        ListView usernameList = view.findViewById(R.id.usernameList);
        usernameList.setAdapter(UsernameAdapter);
        // Setup a onlick listen so that clicking an a username in the list redirects the user
        usernameList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String uuid = UUIDMap.get(Usernames.get(position));
                Bundle bundle = new Bundle();
                bundle.putString(UserProfile.USERID, uuid);
                // Navigate to that users profile
                Navigation.findNavController(view).navigate(R.id.userProfile, bundle);
                // Close the search bar
                ((MainActivity)getActivity()).ToolbarSearch.onActionViewCollapsed();
            }
        });
        // Fetch all users from the database
        Database.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // For each user fetched
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String username = (String)document.getData().getOrDefault("Username", "Error");
                        // put their data into the hashmap and list
                        UUIDMap.put(username, document.getId());
                        Usernames.add(username);
                    }
                    UsernameAdapter.notifyDataSetChanged();
                }
            }
        });

        // grab the tool bar so we can setup the filtering of usernames
        ((MainActivity)getActivity()).ToolbarSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                FilterUsernameList(newText);
                return false;
            }
        });
    }

    public void FilterUsernameList(String username)
    {
        if(UsernameAdapter != null)
        {
            UsernameAdapter.getFilter().filter(username);
        }
    }
}