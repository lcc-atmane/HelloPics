package com.phantomhive.exil.hellopics.Img_Editor.Editor_Adapters;


import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.phantomhive.exil.hellopics.Img_Editor.GetAndSetToolsNames.ToolsName.ModeName;
import com.phantomhive.exil.hellopics.Img_Editor.Views.Select_pice.SelectImageView;
import com.phantomhive.exil.hellopics.Img_Editor.GetAndSetToolsNames.Mode_Name;
import com.phantomhive.exil.hellopics.R;

import java.util.ArrayList;

public class CopyMode_Adapter extends RecyclerView.Adapter<CopyMode_Adapter.MyViewHolder> {
    Context context;
    ArrayList<Mode_Name> Mode_names;
    SelectImageView selectImageView;

    int selectedItemPosition = -1;
    public CopyMode_Adapter(Context context, ArrayList<Mode_Name> mode_names, SelectImageView selectImageView) {
        this.context = context;
        Mode_names = mode_names;
        this.selectImageView = selectImageView;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View View = LayoutInflater.from(context).inflate(R.layout.copymodeitem,parent,false);
        return new MyViewHolder(View);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textView.setText(Mode_names.get(position).getModename());

        if (selectedItemPosition == position) {
            holder.textView.setTextColor(Color.parseColor("#A6A061")); // Change to your desired color
        } else {
            holder.textView.setTextColor(Color.BLACK); // Reset to default color
        }

        holder.textView.setOnClickListener(v -> {

            int previousSelectedItem = selectedItemPosition;
            selectedItemPosition = position;

            // Notify the adapter about the item change to trigger a UI update
            notifyItemChanged(previousSelectedItem);
            notifyItemChanged(selectedItemPosition);
            switch (Mode_names.get(position).getModename()) {
                case ModeName.NORMAL -> selectImageView.setCopyMode(null);
                case ModeName.DARKEN -> selectImageView.setCopyMode(PorterDuff.Mode.DARKEN);
                case ModeName.LIGHTEN -> selectImageView.setCopyMode(PorterDuff.Mode.LIGHTEN);
                case ModeName.MULTIPLY -> selectImageView.setCopyMode(PorterDuff.Mode.MULTIPLY);
                case ModeName.OVERLAY -> selectImageView.setCopyMode(PorterDuff.Mode.OVERLAY);
                case ModeName.SCREEN -> selectImageView.setCopyMode(PorterDuff.Mode.SCREEN);
                case ModeName.ADD -> selectImageView.setCopyMode(PorterDuff.Mode.ADD);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Mode_names.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.CopyModeText);
        }
    }
}
