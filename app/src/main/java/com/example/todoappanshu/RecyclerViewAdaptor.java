package com.example.todoappanshu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class RecyclerViewAdaptor extends RecyclerView.Adapter<RecyclerViewAdaptor.ViewHolder>{
    private ArrayList<String> mSchedule;
    private OnNoteListener mOnNoteListener;

    RecyclerViewAdaptor(ArrayList<String> mSchedule, OnNoteListener mOnNoteListener) {
        this.mSchedule = mSchedule;
        this.mOnNoteListener=mOnNoteListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.schedule.setText(mSchedule.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                mOnNoteListener.onNoteClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSchedule.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView schedule;
        CardView cardview;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            schedule = itemView.findViewById(R.id.name);
            cardview=itemView.findViewById(R.id.cardView);
        }
    }
    public interface OnNoteListener{
        void onNoteClick(int position);
    }
}
