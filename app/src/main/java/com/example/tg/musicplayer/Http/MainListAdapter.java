package com.example.tg.musicplayer.Http;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tg.musicplayer.R;

import java.util.Collections;
import java.util.List;

public class MainListAdapter extends RecyclerView.Adapter<MainListAdapter.MainListViewHolder> {

    private List<FileItem> data = Collections.emptyList();
    private Context context;
    private MainItemClickListener listener;

    public MainListAdapter(List<FileItem> data, Context context, MainItemClickListener listener) {
        this.data = data;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public MainListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mainlistview, parent, false);
        return new MainListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MainListViewHolder holder, int position) {
        holder.tv_fileName.setText(data.get(position).fileName);
        if (!data.get(position).isFile) {
            holder.iv_fileType.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.directory));
        } else {
            if (!data.get(position).isGoBack)
                holder.iv_fileType.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.music));
            else
                holder.iv_fileType.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.scrollupicon));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class MainListViewHolder extends RecyclerView.ViewHolder {

        TextView tv_fileName;
        ImageView iv_fileType;

        public MainListViewHolder(View itemView) {
            super(itemView);
            tv_fileName = (TextView) itemView.findViewById(R.id.tv_fileName);
            iv_fileType = (ImageView) itemView.findViewById(R.id.iv_fileType);
            tv_fileName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(data.get(getAdapterPosition()), getAdapterPosition());
                }
            });//end of ClickListener.
        }
    }

    public interface MainItemClickListener {
        public void onClick(FileItem item, int position);
    }


}


