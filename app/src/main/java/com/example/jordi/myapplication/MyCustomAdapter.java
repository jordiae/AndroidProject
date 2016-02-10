package com.example.jordi.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyCustomAdapter extends RecyclerView.Adapter<MyCustomAdapter.AdapterViewHolder>{

    Context ctx;

    ArrayList<User> contactos;

    MyCustomAdapter(Context context){

        ctx = context;

        LoginDataBaseAdapter loginDataBaseAdapter;
        loginDataBaseAdapter = new LoginDataBaseAdapter(context);
        loginDataBaseAdapter = loginDataBaseAdapter.open();
        String users[] = loginDataBaseAdapter.getAllUsers();
        int ranks[] = loginDataBaseAdapter.returnDescOrderedRanks();
        int nUsers = loginDataBaseAdapter.getNumberOfUsers();

        // The loop starts from the bottom in order to show users in ascendant order
        // (could have been implemented in the LoginDB adapter)
        contactos = new ArrayList<>();
        for (int i = nUsers-2; i >= 0; --i) {
            if (ranks[i] != 0)
                contactos.add(new User(loginDataBaseAdapter.getUri(users[i]),users[i],String.valueOf(ranks[i])));
        }
        loginDataBaseAdapter.close();
    }


    @Override
    public MyCustomAdapter.AdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        // Inflate each element
        View view = inflater.inflate(R.layout.rowlayout, viewGroup, false);
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyCustomAdapter.AdapterViewHolder adapterViewholder, int position) {
        String iconLayout = contactos.get(position).getIcon();
        Bitmap bitmap = BitmapFactory.decodeFile(iconLayout);
        BitmapDrawable drawable = new BitmapDrawable(ctx.getResources(), bitmap);
        adapterViewholder.icon.setImageDrawable(drawable);
        adapterViewholder.name.setText(contactos.get(position).getName());
        adapterViewholder.rank.setText(contactos.get(position).getRank());

    }

    @Override
    public int getItemCount() {
        // We return the size of the viewholder. Default: return 0 -> empty view
        return contactos.size();
    }


    public class AdapterViewHolder extends RecyclerView.ViewHolder {
        // Scrolling...

        public ImageView icon;
        public TextView name;
        public TextView rank;
        public View v;
        public AdapterViewHolder(View itemView) {
            super(itemView);
            this.v = itemView;
            this.icon = (ImageView) itemView.findViewById(R.id.icon);
            this.name = (TextView) itemView.findViewById(R.id.name);
            this.rank = (TextView) itemView.findViewById(R.id.rank);
        }
    }
}