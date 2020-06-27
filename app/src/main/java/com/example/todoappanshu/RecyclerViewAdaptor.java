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
        return new ViewHolder(view, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.schedule.setText(mSchedule.get(position));

    }

    @Override
    public int getItemCount() {
        return mSchedule.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView schedule;
        CardView cardview;
        OnNoteListener onNoteListener;
        ViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            schedule = itemView.findViewById(R.id.name);
            cardview=itemView.findViewById(R.id.cardView);
            this.onNoteListener=onNoteListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());

        }
    }
    public interface OnNoteListener{
        void onNoteClick(int position);
    }
}
