package com.android.manoj.contactsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pkmmte.view.CircularImageView;

import java.util.ArrayList;

/**
 * Created by manojc on 8/7/2017.
 */

public class ContactAdapter extends ArrayAdapter<User> {

    ArrayList<User> mList;
    Context mContext;
    LayoutInflater inflater;

    public ContactAdapter(@NonNull Context context, @NonNull ArrayList<User> objects) {
        super(context, 0, objects);
        this.mContext = context;
        this.mList=objects;

    }

    static class ViewHolder {
        public CircularImageView mPhoto;
        public TextView mName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        if (inflater == null) {

            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        if (convertView == null) {

            convertView = inflater.inflate(R.layout.contact_list_row, null);

            viewHolder = new ViewHolder();

            viewHolder.mPhoto = (CircularImageView) convertView.findViewById(R.id.personPhoto);
            viewHolder.mName = (TextView) convertView.findViewById(R.id.name);

            convertView.setTag(viewHolder);

        } else {

            viewHolder = (ViewHolder) convertView.getTag();
        }

        User mUser=mList.get(position);

        // viewHolder.mPhoto.setImageBitmap(mUser.getPhoto());
        viewHolder.mName.setText(mUser.getName());

        return convertView;
    }

}
