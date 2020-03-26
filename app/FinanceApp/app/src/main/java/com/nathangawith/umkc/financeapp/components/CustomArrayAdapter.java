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

public class CustomArrayAdapter extends ArrayAdapter<CustomList> {

    public CustomArrayAdapter(@NonNull Context context, int resource, @NonNull ArrayList<CustomList> customList) {
        super(context, resource, customList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.custom_list, parent, false);
        }

        CustomList currentItem = getItem(position);

        ImageView imgTransaction = listItemView.findViewById(R.id.imgTransaction);
        imgTransaction.setImageResource(currentItem.getImgResID());

        TextView lblTransactionDescription = listItemView.findViewById(R.id.lblTransactionDescription);
        lblTransactionDescription.setText(currentItem.getTransactionDescription());

        TextView lblTransactionAmount = listItemView.findViewById(R.id.lblTransactionAmount);
        lblTransactionAmount.setText(currentItem.getTransactionAmount());

        return listItemView;
    }

}
