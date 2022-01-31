package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.MassageModal;
import com.example.myapplication.model.UserModal;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class chatAdapter extends RecyclerView.Adapter{

    android.content.Context context;
    ArrayList<MassageModal> massageModals;

    public chatAdapter(Context context, ArrayList<MassageModal> massageModals) {
        this.context = context;
        this.massageModals = massageModals;
    }
    int SENDER_VIEW_TYPE= 1;
    int RECIEVER_VIEW_TYPE= 2;



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == SENDER_VIEW_TYPE){
            View view = LayoutInflater.from(context).inflate(R.layout.sample_sender,parent,false);
            return new SenderViewHolder(view);
        }
        else{
                View view = LayoutInflater.from(context).inflate(R.layout.sample_recever,parent,false);
                return new RecieverViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {

        if(massageModals.get(position).getUid().equals(FirebaseAuth.getInstance().getUid())){
            return SENDER_VIEW_TYPE;
        }
        else
        {
            return RECIEVER_VIEW_TYPE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MassageModal massageModal = massageModals.get(position);
        if (holder.getClass()==SenderViewHolder.class){
            ((SenderViewHolder)holder).senderMassage.setText(massageModal.getMassage());
        }
        else {
            ((RecieverViewHolder)holder).recieverMassage.setText(massageModal.getMassage());
        }
    }

    @Override
    public int getItemCount() {
        return massageModals.size();
    }



    public class RecieverViewHolder extends RecyclerView.ViewHolder{
        TextView recieverMassage,recieverTime;

        public RecieverViewHolder(@NonNull View itemView) {
            super(itemView);
            recieverMassage = itemView.findViewById(R.id.recevermasg);
            recieverTime = itemView.findViewById(R.id.recevertime);
        }
    }
    public class SenderViewHolder extends RecyclerView.ViewHolder{
        TextView senderMassage,senderTime;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
           senderMassage = itemView.findViewById(R.id.sendertext);
            senderTime = itemView.findViewById(R.id.sendertime);
        }
    }
}
