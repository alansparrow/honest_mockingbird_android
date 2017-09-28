package com.trungbao.honestmockingbird;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trungbao.honestmockingbird.helper.NetworkRequester;
import com.trungbao.honestmockingbird.model.News;
import com.trungbao.honestmockingbird.model.NewsLab;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by baotrungtn on 9/17/17.
 */


public class NewsListFragment extends Fragment {
    private static final String TAG = "NewsListFragment";

    private RecyclerView mNewsRecyclerView;
    private NewsAdapter mAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
//        setRetainInstance(true);

        // from Local Time to UTC
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, 2);  // number of days to add

        new FetchDataTask().execute(
                this.getContext(),
                sdf.format(c.getTime()),
                20,
                0
        );

        Log.i(TAG, "NewsListFragment created");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_list, container, false);

        mNewsRecyclerView = (RecyclerView) view.findViewById(R.id.news_recycler_view);
        mNewsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mNewsRecyclerView.addOnScrollListener(new OnVerticalScrollListener());

        updateUI();

        Log.i(TAG, "NewsListFragment View created");

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
            Log.i(TAG, "updateUI with new mAdapter");
        } else {
            mAdapter.notifyDataSetChanged();
            Log.i(TAG, "updateUI with mAdapter != null " + mAdapter);
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

        public void setNewsList(List<News> newsList) {
            mNewsList = newsList;
        }
    }

    private class NewsHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private News mNews;

        private TextView mTitleTextView;
        private TextView mPubSourceTextView;
        private TextView mPubTimeTextView;
        private TextView mHoldTextView;
        private TextView mBuyTextView;
        private TextView mSellTextView;
        private TextView mHoldVoteCountTextView;
        private TextView mBuyVoteCountTextView;
        private TextView mSellVoteCountTextView;
        private View mHoldBtn;
        private View mBuyBtn;
        private View mSellBtn;


        public NewsHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_news_item, parent, false));
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.news_title);
            mPubSourceTextView = (TextView) itemView.findViewById(R.id.news_pub_source);
            mPubTimeTextView = (TextView) itemView.findViewById(R.id.news_pub_time);
            mHoldTextView = (TextView) itemView.findViewById(R.id.hold_btn);
            mBuyTextView = (TextView) itemView.findViewById(R.id.buy_btn);
            mSellTextView = (TextView) itemView.findViewById(R.id.sell_btn);
            mHoldVoteCountTextView = (TextView) itemView.findViewById(R.id.hold_vote_count);
            mBuyVoteCountTextView = (TextView) itemView.findViewById(R.id.buy_vote_count);
            mSellVoteCountTextView = (TextView) itemView.findViewById(R.id.sell_vote_count);
            mHoldBtn = (View) itemView.findViewById(R.id.hold_linear_layout);
            mBuyBtn = (View) itemView.findViewById(R.id.buy_linear_layout);
            mSellBtn = (View) itemView.findViewById(R.id.sell_linear_layout);


        }

        public void bind(News n) {
            mNews = n;
            mTitleTextView.setText(mNews.getTitle());
            mPubSourceTextView.setText(mNews.getPubSource());
            mPubTimeTextView.setText(mNews.getPubDate().toString());

            mHoldVoteCountTextView.setText(mNews.getHoldVoteCount() + "");
            mBuyVoteCountTextView.setText(mNews.getBuyVoteCount() + "");
            mSellVoteCountTextView.setText(mNews.getSellVoteCount() + "");

            // Set vote button, should refactor (create sub class of view)
            mHoldTextView.setTypeface(null, Typeface.NORMAL);
            mSellTextView.setTypeface(null, Typeface.NORMAL);
            mBuyTextView.setTypeface(null, Typeface.NORMAL);

            switch (mNews.getMyVote()) {
                case SharedInfo.HOLD_VOTE:
                    mHoldTextView.setTypeface(null, Typeface.BOLD);
                    break;
                case SharedInfo.BUY_VOTE:
                    mBuyTextView.setTypeface(null, Typeface.BOLD);
                    break;
                case SharedInfo.SELL_VOTE:
                    mSellTextView.setTypeface(null, Typeface.BOLD);
                    break;
                case SharedInfo.NEUTRAL_VOTE:
                    break;
            }

            mTitleTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(mNews.getUrl()));
                    i = Intent.createChooser(i, getString(R.string.open_url));
                    startActivity(i);
                }
            });

            mHoldBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new VoteTask().execute(mNews, SharedInfo.HOLD_VOTE);
                    if (mNews.getMyVote().equals(SharedInfo.HOLD_VOTE)) {
                        mNews.setMyVote(SharedInfo.NEUTRAL_VOTE);
                    } else {
                        mNews.setMyVote(SharedInfo.HOLD_VOTE);
                    }
                    Log.i(TAG, "Vote changed: " + mNews.getId() + "   " + mNews.getMyVote());
                    updateUI();
                }
            });

            mBuyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new VoteTask().execute(mNews, SharedInfo.BUY_VOTE);
                    if (mNews.getMyVote().equals(SharedInfo.BUY_VOTE)) {
                        mNews.setMyVote(SharedInfo.NEUTRAL_VOTE);
                    } else {
                        mNews.setMyVote(SharedInfo.BUY_VOTE);
                    }
                    Log.i(TAG, "Vote changed: " + mNews.getId() + "   " + mNews.getMyVote());
                    updateUI();
                }
            });

            mSellBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new VoteTask().execute(mNews, SharedInfo.SELL_VOTE);
                    if (mNews.getMyVote().equals(SharedInfo.SELL_VOTE)) {
                        mNews.setMyVote(SharedInfo.NEUTRAL_VOTE);
                    } else {
                        mNews.setMyVote(SharedInfo.SELL_VOTE);
                    }
                    Log.i(TAG, "Vote changed: " + mNews.getId() + "   " + mNews.getMyVote());
                    updateUI();
                }
            });
        }

        @Override
        public void onClick(View view) {

        }
    }

    private class OnVerticalScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (!recyclerView.canScrollVertically(1)) {
                // from Local Time to UTC
                int lastItemPos = NewsLab.get(getActivity()).getNewsList().size() - 1;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                String beforeDateStr = sdf.format(
                        NewsLab.get(getContext()).getNewsList().get(lastItemPos).getPubDate()
                );

                new FetchDataTask().execute(
                        getContext(),
                        beforeDateStr,
                        20,
                        1
                );

            } else if (!recyclerView.canScrollVertically(-1)) {
                // from Local Time to UTC
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
//
//                Calendar c = Calendar.getInstance();
//                c.setTime(new Date());
//                c.add(Calendar.DATE, 2);  // number of days to add
//
//                new FetchDataTask().execute(
//                        getContext(),
//                        sdf.format(c.getTime()),
//                        20,
//                        0
//                );
            }
            Log.i(TAG, dx + "   " + dy + " aa  " + recyclerView.canScrollVertically(1));
            Log.i(TAG, dx + "   " + dy + " bb  " + recyclerView.canScrollVertically(-1));
        }
    }

    private class VoteTask extends AsyncTask<Object, Void, Void> {

        @Override
        protected Void doInBackground(Object... objects) {
            News news = (News) objects[0];
            String voteType = (String) objects[1];
            switch (voteType) {
                case SharedInfo.HOLD_VOTE:
                    new NetworkRequester().voteHold(news);
                    break;
                case SharedInfo.BUY_VOTE:
                    new NetworkRequester().voteBuy(news);
                    break;
                case SharedInfo.SELL_VOTE:
                    new NetworkRequester().voteSell(news);
                    break;
            }

            return null;
        }
    }

    private class FetchDataTask extends AsyncTask<Object,Void, Integer> {

        @Override
        protected Integer doInBackground(Object... params) {
            Context context = (Context) params[0];
            String beforeDateStr = (String) params[1];
            int newsCount = (int) params[2];
            int resultType = (int) params[3];

            NewsLab.get(context).fetchNews(beforeDateStr, newsCount);

            return resultType;
        }

        @Override
        protected void onPostExecute(Integer resultType) {
            updateUI();
        }
    }
}
