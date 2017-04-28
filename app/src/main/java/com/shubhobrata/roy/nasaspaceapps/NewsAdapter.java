package com.shubhobrata.roy.nasaspaceapps;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by shubh on 4/28/2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private List<NewsHolder> sh;
    private LayoutInflater inflater;
    private Context c;

    public NewsAdapter(Context c,List<NewsHolder> nh) {
        this.c = c;
        this.sh = nh;
        inflater = LayoutInflater.from(c);
    }

    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.news_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NewsAdapter.ViewHolder holder, int position) {
            NewsHolder nh = sh.get(position);
            holder.link.setText(nh.getUrl());
            holder.title.setText(nh.getTitle());
    }

    @Override
    public int getItemCount() {
        return sh.size();
    }

    public void updateList(List <NewsHolder> alarmsHolder)
    {
        sh.clear();
        sh.addAll(alarmsHolder);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView link ;
        private TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            link=(TextView) itemView.findViewById(R.id.link);
            title=(TextView) itemView.findViewById(R.id.title);
        }

    }

}
