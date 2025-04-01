package com.phantomhive.exil.hellopics.Img_Editor.Editor_Adapters;

import android.annotation.SuppressLint;
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
import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.ViewsActivities.AdjustImageActivity;
import com.phantomhive.exil.hellopics.Img_Editor.Interface.itemOnClick;
import com.phantomhive.exil.hellopics.R;

import java.util.ArrayList;

public class AdjustToolsAdapter extends RecyclerView.Adapter<AdjustToolsAdapter.MyViewHolder> {

    ArrayList<AdjustImageActivity.Adjust_Tools_Name> adjust_tools_names;
    Context context;
    itemOnClick itemOnClick;
    private int selectedPosition = RecyclerView.NO_POSITION;

    public AdjustToolsAdapter(ArrayList<AdjustImageActivity.Adjust_Tools_Name> adjust_tools_names, Context context, itemOnClick itemOnClick) {
        this.adjust_tools_names = adjust_tools_names;
        this.context = context;
        this.itemOnClick = itemOnClick;
    }
    @NonNull
    @Override
    public AdjustToolsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View View = LayoutInflater.from(context).inflate(R.layout.text_image_tools_item,parent,false);
        return new AdjustToolsAdapter.MyViewHolder(View);
    }

    @Override
    public void onBindViewHolder(@NonNull AdjustToolsAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(context)
                .load(adjust_tools_names.get(position).getRToolsImg())
                .into(holder.imageButton);
        holder.textView.setText(adjust_tools_names.get(position).getRToolName());
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemOnClick.ToolInAdjustItemOnClick(adjust_tools_names.get(position).getRToolName());

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
        return adjust_tools_names.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageButton imageButton;
        TextView textView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageButton = itemView.findViewById(R.id.imageView_Tool_Text);
            textView = itemView.findViewById(R.id.textView_Tool_text);
        }
    }
}
