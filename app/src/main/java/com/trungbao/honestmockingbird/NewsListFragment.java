package com.trungbao.honestmockingbird;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trungbao.honestmockingbird.model.News;
import com.trungbao.honestmockingbird.model.NewsLab;

import java.util.List;

/**
 * Created by baotrungtn on 9/17/17.
 */


public class NewsListFragment extends Fragment {

    private RecyclerView mNewsRecyclerView;
    private NewsAdapter mAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_list, container, false);

        mNewsRecyclerView = (RecyclerView) view.findViewById(R.id.news_recycler_view);
        mNewsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();

    }

    private void updateUI() {
        NewsLab newsLab = NewsLab.get(getActivity());
        List<News> newsList = newsLab.getNewsList();

        if (mAdapter == null) {
            mAdapter = new NewsAdapter(newsList);
            mNewsRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }

    }

    private class NewsAdapter extends RecyclerView.Adapter<NewsHolder>{

        private List<News> mNewsList;

        public NewsAdapter(List<News> newsList) {
            mNewsList = newsList;
        }

        @Override
        public NewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new NewsHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(NewsHolder holder, int position) {
            News news = mNewsList.get(position);
            holder.bind(news);
        }

        @Override
        public int getItemCount() {
            return mNewsList.size();
        }
    }

    private class NewsHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private News mNews;

        private TextView mTitleTextView;
        private TextView mPubSourceTextView;
        private TextView mPubTimeTextView;

        public NewsHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_news_item, parent, false));
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.news_title);
            mPubSourceTextView = (TextView) itemView.findViewById(R.id.news_pub_source);
            mPubTimeTextView = (TextView) itemView.findViewById(R.id.news_pub_time);
        }

        public void bind(News n) {
            mNews = n;
            mTitleTextView.setText(mNews.getTitle());
            mPubSourceTextView.setText(mNews.getPubSource());
            mPubTimeTextView.setText(mNews.getPubDate().toString());
        }

        @Override
        public void onClick(View view) {

        }
    }
}
