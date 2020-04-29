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
import com.nathangawith.umkc.financeapp.activites.RegisterActivity;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class RegisterEntryArrayAdapter extends ArrayAdapter<RegisterEntry> {

    private RegisterActivity registerActivity;

    public RegisterEntryArrayAdapter(@NonNull RegisterActivity registerActivity, @NonNull Context context, int resource, @NonNull ArrayList<RegisterEntry> registerEntry) {
        super(context, resource, registerEntry);
        this.registerActivity = registerActivity;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.component_register_entry, parent, false);
        }

        RegisterEntry currentItem = getItem(position);

        ImageView imgTransaction = listItemView.findViewById(R.id.imgTransaction);
        imgTransaction.setImageResource(currentItem.getImgResID());

        TextView lblTransactionDescription = listItemView.findViewById(R.id.lblTransactionDescription);
        lblTransactionDescription.setText(currentItem.getDescription());

        TextView lblTransactionAmount = listItemView.findViewById(R.id.lblTransactionAmount);
        lblTransactionAmount.setText(currentItem.getAmount());

        TextView lblTransactionDate = listItemView.findViewById(R.id.lblTransactionDate);
        lblTransactionDate.setText(currentItem.getDate());

        TextView lblLabel1 = listItemView.findViewById(R.id.lblLabel1);
        lblLabel1.setText(currentItem.getLabel1());

        TextView lblLabel2 = listItemView.findViewById(R.id.lblLabel2);
        lblLabel2.setText(currentItem.getLabel2());

        TextView lblValue1 = listItemView.findViewById(R.id.lblValue1);
        lblValue1.setText(currentItem.getValue1());

        TextView lblValue2 = listItemView.findViewById(R.id.lblValue2);
        lblValue2.setText(currentItem.getValue2());

        ImageButton btnEdit = listItemView.findViewById(R.id.btnEdit);
        btnEdit.setImageResource(R.drawable.edit_black);

        ImageButton btnDelete = listItemView.findViewById(R.id.btnDelete);
        btnDelete.setImageResource(R.drawable.delete_black);

        RegisterEntryArrayAdapter me = this;
        listItemView.findViewById(R.id.btnEdit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(currentItem);
                if (currentItem != null) {
                    System.out.println(currentItem.getDescription());
                    System.out.println(currentItem.getDate());
                    System.out.println(currentItem.getAmount());
                    System.out.println(currentItem.getTransactionID());
                }
                me.registerActivity.btnEditClick(currentItem);
            }
        });
        listItemView.findViewById(R.id.btnDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                me.registerActivity.btnDeleteClick(currentItem);
            }
        });

        return listItemView;
    }

}
