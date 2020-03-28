package com.nathangawith.umkc.financeapp.components;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nathangawith.umkc.financeapp.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class RegisterEntryArrayAdapter extends ArrayAdapter<RegisterEntry> {

    public RegisterEntryArrayAdapter(@NonNull Context context, int resource, @NonNull ArrayList<RegisterEntry> registerEntry) {
        super(context, resource, registerEntry);
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

        TextView lblTransactionAccount = listItemView.findViewById(R.id.lblTransactionAccount);
        lblTransactionAccount.setText(currentItem.getAccount());

        TextView lblTransactionCategory = listItemView.findViewById(R.id.lblTransactionCategory);
        lblTransactionCategory.setText(currentItem.getCategory());

        return listItemView;
    }

}
