package com.example.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.massageDetail;
import com.example.myapplication.model.UserModal;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter extends RecyclerView.Adapter<Adapter.Holder> {

    Context context;
    ArrayList<UserModal> list;

    public Adapter(Context context, ArrayList<UserModal> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.singlerow2,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        UserModal user1 = list.get(position);
        holder.headline.setText(user1.getName());
        holder.email.setText(user1.getEmail());
        Glide.with(context).load(user1.getPropic()).circleCrop().into(holder.img);
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent (context, massageDetail.class );
            intent.putExtra("userid" , user1.getUid());
            intent.putExtra("dp" , user1.getPropic());
            intent.putExtra("name" , user1.getName());

            context.startActivity(intent);

        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class Holder extends RecyclerView.ViewHolder{
        TextView headline,email;
        CircleImageView img;

        public Holder(@NonNull View itemView) {
            super(itemView);
            headline = itemView.findViewById(R.id.nametext12);
            email = itemView.findViewById(R.id.coursetext12);
            img=itemView.findViewById(R.id.img12);



        }
    }
}
