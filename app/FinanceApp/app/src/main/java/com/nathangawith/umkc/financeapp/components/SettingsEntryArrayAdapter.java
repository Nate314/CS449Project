package com.nathangawith.umkc.financeapp.components;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.nathangawith.umkc.financeapp.R;
import com.nathangawith.umkc.financeapp.activites.SettingsActivity;
import com.nathangawith.umkc.financeapp.constants.MyUtility;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsEntryArrayAdapter extends ArrayAdapter<SettingsEntry> {

    private SettingsActivity settingsActivity;

    public SettingsEntryArrayAdapter(@NonNull SettingsActivity settingsActivity, @NonNull Context context, int resource, @NonNull ArrayList<SettingsEntry> settingsEntry) {
        super(context, resource, settingsEntry);
        this.settingsActivity = settingsActivity;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.component_settings_entry, parent, false);
        }

        SettingsEntry currentItem = getItem(position);

        TextView lblDescription = listItemView.findViewById(R.id.lblDescription);
        lblDescription.setText(currentItem.getDescription());

        ImageButton btnEdit = listItemView.findViewById(R.id.btnEdit);
        btnEdit.setImageResource(R.drawable.edit_black);

        ImageButton btnDelete = listItemView.findViewById(R.id.btnDelete);
        btnDelete.setImageResource(R.drawable.delete_black);

        SettingsEntryArrayAdapter me = this;
        listItemView.findViewById(R.id.btnEdit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                me.settingsActivity.btnEditClick(currentItem);
            }
        });
        listItemView.findViewById(R.id.btnDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                me.settingsActivity.btnDeleteClick(currentItem);
            }
        });

        return listItemView;
    }

}
