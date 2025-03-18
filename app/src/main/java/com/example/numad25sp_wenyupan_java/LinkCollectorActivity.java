package com.example.numad25sp_wenyupan_java;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LinkCollectorActivity extends AppCompatActivity implements LinkListAdapter.OnLinkClickListener {

    private static final String PREFS_NAME = "contacts_prefs";
    private static final String KEY_CONTACTS = "contacts_list";
    private static final String STATE_CONTACTS = "state_contacts";

    private RecyclerView recyclerView;
    private LinkListAdapter adapter;
    private List<Link> contactList;
    private FloatingActionButton fab;
    private SharedPreferences sharedPreferences;
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_collector);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Restore contacts from savedInstanceState if available, else from SharedPreferences
        if (savedInstanceState != null) {
            contactList = loadContactsFromState(savedInstanceState);
        } else {
            contactList = loadContacts();
        }

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new LinkListAdapter(contactList, this);
        recyclerView.setAdapter(adapter);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> showAddContactDialog());
    }

    @Override
    public void onLinkClick(Link link) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + link.getPhoneNumber()));
        startActivity(intent);
    }

    @Override
    public void onLinkDelete(Link link) {
        contactList.remove(link);
        saveContacts();
        adapter.notifyDataSetChanged();
        Snackbar.make(recyclerView, "Contact deleted", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onLinkEdit(Link link) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.edit_contact_page, null);
        EditText editName = dialogView.findViewById(R.id.edit_contact_name);
        EditText editPhone = dialogView.findViewById(R.id.edit_contact_phone);

        editName.setText(link.getName());
        editPhone.setText(link.getPhoneNumber());

        new AlertDialog.Builder(this)
                .setTitle("Edit Contact")
                .setView(dialogView)
                .setPositiveButton("Save", (dialog, which) -> {
                    link.setName(editName.getText().toString().trim());
                    link.setPhoneNumber(editPhone.getText().toString().trim());
                    saveContacts();
                    adapter.notifyDataSetChanged();
                    Snackbar.make(recyclerView, "Contact updated!", Snackbar.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showAddContactDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.edit_contact_page, null);
        EditText editName = dialogView.findViewById(R.id.edit_contact_name);
        EditText editPhone = dialogView.findViewById(R.id.edit_contact_phone);

        new AlertDialog.Builder(this)
                .setTitle("Add New Contact")
                .setView(dialogView)
                .setPositiveButton("Add", (dialog, which) -> {
                    String name = editName.getText().toString().trim();
                    String phone = editPhone.getText().toString().trim();

                    if (name.isEmpty() || phone.isEmpty()) {
                        Snackbar.make(recyclerView, "Name and phone number required!", Snackbar.LENGTH_SHORT).show();
                        return;
                    }

                    Link newContact = new Link(name, phone);
                    contactList.add(newContact);
                    adapter.notifyDataSetChanged();
                    saveContacts(); // Ensure new contact is persisted

                    Snackbar.make(recyclerView, "Contact added!", Snackbar.LENGTH_LONG)
                            .setAction("Undo", v -> {
                                contactList.remove(newContact);
                                adapter.notifyDataSetChanged();
                                saveContacts();
                                Snackbar.make(recyclerView, "Contact removed!", Snackbar.LENGTH_SHORT).show();
                            }).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void saveContacts() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = gson.toJson(contactList);
        editor.putString(KEY_CONTACTS, json);
        editor.apply();
    }

    private List<Link> loadContacts() {
        String json = sharedPreferences.getString(KEY_CONTACTS, null);
        if (json == null) {
            return new ArrayList<>();
        }
        Type type = new TypeToken<List<Link>>() {}.getType();
        return gson.fromJson(json, type);
    }

    // Save contacts when screen is rotated**
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String json = gson.toJson(contactList);
        outState.putString(STATE_CONTACTS, json);
    }

    // Load contacts when screen is restored**
    private List<Link> loadContactsFromState(Bundle savedInstanceState) {
        String json = savedInstanceState.getString(STATE_CONTACTS, null);
        if (json == null) {
            return new ArrayList<>();
        }
        Type type = new TypeToken<List<Link>>() {}.getType();
        return gson.fromJson(json, type);
    }
}
