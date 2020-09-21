package com.example.vlog;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.ActivityChooserView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

public class RecyclerList extends RecyclerView.Adapter<RecyclerList.ViewHolder>{


    ArrayList<Helper> arrayList;
    Context context;

    public RecyclerList(ArrayList<Helper> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater;
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.postshow, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String Title = arrayList.get(position).getTitle();
        String content=arrayList.get(position).getContent();
        String ImageUri=arrayList.get(position).getImageUri();
        holder.Title.setText(Title);
        holder.Content.setText(content);
        Uri st=Uri.parse(ImageUri);
        Picasso.get().load(st).placeholder(R.drawable.person).into(holder.ImageView);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public  class  ViewHolder extends RecyclerView.ViewHolder {

        TextView Title,Content;
        ImageView ImageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Title=itemView.findViewById(R.id.TitleR);
            Content=itemView.findViewById(R.id.contentR);
            ImageView=itemView.findViewById(R.id.ImageViewR);
        }
    }
}
