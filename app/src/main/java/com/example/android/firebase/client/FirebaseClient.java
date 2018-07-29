package com.example.android.firebase.client;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.firebase.databaseFirebase.adapter.CustomAdapter;
import com.example.android.firebase.databaseFirebase.model.hewan;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.database.DatabaseReference;


import java.util.ArrayList;

/**
 * Created by Blackswan on 7/19/2017.
 */

public class FirebaseClient {
    Firebase firebase;
    ArrayList<hewan> hewanArrayList = new ArrayList<>();
    CustomAdapter customAdapter;
    Context c;
    String DB_URL;
    ListView listView;
     String userid;
     DatabaseReference ref;

     public FirebaseClient(Context c){
         this.c =c;
     }
    public FirebaseClient(Context c, String DB_URL, ListView listView, DatabaseReference ref) {
        this.c = c;
        this.ref=ref;
        this.DB_URL = DB_URL;
        this.listView = listView;
        Firebase.setAndroidContext(c);
        firebase = new Firebase(DB_URL);
    }

    public void savedata(String name,String info, String url) {
        hewan h = new hewan();
        String  userid = ref.push().getKey();
        h.setId_hewan(userid);
        h.setName(name);
        h.setInfo(info);
        h.setUrl(url);
        ref.child(userid).setValue(h);
    }


    public void refreshdata() {
        firebase.addChildEventListener(new com.firebase.client.ChildEventListener() {
            @Override
            public void onChildAdded(com.firebase.client.DataSnapshot dataSnapshot, String s) {
                getUpdates(dataSnapshot);

            }

            @Override
            public void onChildChanged(com.firebase.client.DataSnapshot dataSnapshot, String s) {
                getUpdates(dataSnapshot);

            }

            @Override
            public void onChildRemoved(com.firebase.client.DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(com.firebase.client.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        } );
    }

    private void getUpdates(final com.firebase.client.DataSnapshot dataSnapshot) {
        hewanArrayList.clear();
        for (com.firebase.client.DataSnapshot ds : dataSnapshot.getChildren()) {
            hewan h = new hewan();

            h.setId_hewan(ds.getValue(hewan.class).getId_hewan());
            h.setName(ds.getValue(hewan.class).getName());
            h.setInfo(ds.getValue(hewan.class).getInfo());
            h.setUrl(ds.getValue(hewan.class).getUrl());
            hewanArrayList.add(h);
        }
        if (hewanArrayList.size() > 0) {
            customAdapter = new CustomAdapter(c, hewanArrayList,ref,firebase);

            listView.setAdapter((ListAdapter) customAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    Hewan artist = hewanArrayList.get(position);
//                    showUpdateDeleteDialog(artist.getName(), artist.getInfo());

                    //     dataSnapshot.getRef().child(String.valueOf(position)).removeValue();
                    //  dataSnapshot.getRef().setValue(null);
//                    dataSnapshot.getRef().removeValue();
//                    if(dataSnapshot.child("hewan").getValue() != null){
//                        String msg = dataSnapshot.child("hewan").getValue().toString();
//                        return;
//                    }
               //     deletedata(userid);
                    Toast.makeText(c, "test aja", Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            Toast.makeText(c, "no data", Toast.LENGTH_SHORT).show();
        }

    }


}