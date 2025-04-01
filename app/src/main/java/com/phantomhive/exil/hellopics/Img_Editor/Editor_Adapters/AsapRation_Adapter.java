package com.phantomhive.exil.hellopics.Img_Editor.Editor_Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.phantomhive.exil.hellopics.Img_Editor.Interface.itemOnClick;
import com.phantomhive.exil.hellopics.Img_Editor.GetAndSetToolsNames.AsapRatio_ND;
import com.phantomhive.exil.hellopics.R;

import java.util.ArrayList;

public class AsapRation_Adapter extends RecyclerView.Adapter<AsapRation_Adapter.MyViewHolder> {
    Context context;
    ArrayList<AsapRatio_ND> asapRatio_nds;
    itemOnClick itemOnClick;
    private int selectedPosition = RecyclerView.NO_POSITION;
    public AsapRation_Adapter(Context context, ArrayList<AsapRatio_ND> asapRatio_nds, itemOnClick itemOnClick) {
        this.context = context;
        this.asapRatio_nds = asapRatio_nds;
        this.itemOnClick = itemOnClick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View View = LayoutInflater.from(context).inflate(R.layout.asaprasion_item,parent,false);
        return new MyViewHolder(View);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context)
                .load(asapRatio_nds.get(position).getAsapRatioImg_H())
                .into(holder.imageButton);
        holder.textView.setText(asapRatio_nds.get(position).getAsapRatioName_H());
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemOnClick.ToolInAsapRatioItemOnclick(asapRatio_nds.get(position).getAsapRatioName_H());

                // Update selected position
                int previousSelected = selectedPosition;
                selectedPosition = holder.getAbsoluteAdapterPosition();

                // Notify adapter of the changes
                notifyItemChanged(previousSelected);
                notifyItemChanged(selectedPosition);
            }
        });

        // Set the background color based on selection
        if (selectedPosition == position) {
            holder.itemView.setBackgroundColor(Color.parseColor("#A6A061"));
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    @Override
    public int getItemCount() {
        return asapRatio_nds.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageButton imageButton;
        TextView textView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageButton = itemView.findViewById(R.id.imageButton_AsapRotion);
            textView = itemView.findViewById(R.id.textView_AsapNameorNombre);
        }
    }
}
