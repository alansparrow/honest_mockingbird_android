package com.trungbao.honestmockingbird;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.trungbao.honestmockingbird.helper.NetworkRequester;
import com.trungbao.honestmockingbird.model.News;
import com.trungbao.honestmockingbird.model.NewsLab;

import org.w3c.dom.Text;

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

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mNewsRecyclerView;
    private NewsAdapter mAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
//        setRetainInstance(true);

        updateNewsList();

        Log.i(TAG, "NewsListFragment created");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            // Check if user triggered a refresh:
            case R.id.menu_refresh:
                Log.i(TAG, "Refresh menu item selected");

                // Signal SwipeRefreshLayout to start the progress indicator
                mSwipeRefreshLayout.setRefreshing(true);

                // Start the refresh background task.
                // This method calls setRefreshing(false) when it's finished.
                updateNewsList();

                return true;
        }

        // User didn't trigger a refresh, let the superclass handle this action
        return super.onOptionsItemSelected(item);
    }

    private void updateNewsList() {
        // NewsLab refresh to null
        // mAdapter point to newsList in NewsLab
        NewsLab.refresh();
        mAdapter = null;

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
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_news_list, menu);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_list, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new OnSwipeRefreshListener());

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

        private TextView mFactTextView;
        private TextView mOpinionTextView;
        private View mFactBtn;
        private View mOpinionBtn;
        private TextView mFactVoteCountTextView;
        private TextView mOpinionVoteCountTextView;

        private ImageView mVoteDownBtn;
        private ImageView mVoteUpBtn;
        private ImageView mShareBtn;
        private TextView mUpDownTextView;


        public NewsHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_news_item, parent, false));
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.news_title);
            mPubSourceTextView = (TextView) itemView.findViewById(R.id.news_pub_source);
            mPubTimeTextView = (TextView) itemView.findViewById(R.id.news_pub_time);
            mHoldTextView = (TextView) itemView.findViewById(R.id.hold);
            mBuyTextView = (TextView) itemView.findViewById(R.id.buy);
            mSellTextView = (TextView) itemView.findViewById(R.id.sell);
            mHoldVoteCountTextView = (TextView) itemView.findViewById(R.id.hold_vote_count);
            mBuyVoteCountTextView = (TextView) itemView.findViewById(R.id.buy_vote_count);
            mSellVoteCountTextView = (TextView) itemView.findViewById(R.id.sell_vote_count);
            mHoldBtn = (View) itemView.findViewById(R.id.hold_linear_layout_btn);
            mBuyBtn = (View) itemView.findViewById(R.id.buy_linear_layout_btn);
            mSellBtn = (View) itemView.findViewById(R.id.sell_linear_layout_btn);

            mFactTextView = (TextView) itemView.findViewById(R.id.fact);
            mOpinionTextView = (TextView) itemView.findViewById(R.id.opinion);
            mFactVoteCountTextView = (TextView) itemView.findViewById(R.id.fact_vote_count);
            mOpinionVoteCountTextView = (TextView) itemView.findViewById(R.id.opinion_vote_count);
            mFactBtn = (View) itemView.findViewById(R.id.fact_linear_layout_btn);
            mOpinionBtn = (View) itemView.findViewById(R.id.opinion_linear_layout_btn);

            mVoteUpBtn = (ImageView) itemView.findViewById(R.id.vote_up_btn);
            mVoteDownBtn = (ImageView) itemView.findViewById(R.id.vote_down_btn);
            mShareBtn = (ImageView) itemView.findViewById(R.id.share_btn);
            mUpDownTextView = (TextView) itemView.findViewById(R.id.updown_count_textview);

        }

        public void bind(News n) {
            mNews = n;

            mTitleTextView.setText(mNews.getTitle());
            mPubSourceTextView.setText(SharedInfo.getDomainNameFromUrl(mNews.getUrl()));
            mPubTimeTextView.setText(mNews.getPubDate().toString());

            // Set voteTrade button, should refactor (create sub class of view)
            mHoldTextView.setTypeface(null, Typeface.NORMAL);
            mSellTextView.setTypeface(null, Typeface.NORMAL);
            mBuyTextView.setTypeface(null, Typeface.NORMAL);
            mFactTextView.setTypeface(null, Typeface.NORMAL);
            mOpinionTextView.setTypeface(null, Typeface.NORMAL);

            mVoteUpBtn.setScaleX(1.0f);
            mVoteUpBtn.setScaleY(1.0f);
            mVoteDownBtn.setScaleX(1.0f);
            mVoteDownBtn.setScaleY(1.0f);

            // Only show when user voted
            if (mNews.getTradeVote() != null) {
                mHoldVoteCountTextView.setText(SharedInfo.formatNumberStringWithSuffix(mNews.getHoldVoteCount()));
                mBuyVoteCountTextView.setText(SharedInfo.formatNumberStringWithSuffix(mNews.getBuyVoteCount()));
                mSellVoteCountTextView.setText(SharedInfo.formatNumberStringWithSuffix(mNews.getSellVoteCount()));
                switch (mNews.getTradeVote()) {
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
            } else {
                mHoldVoteCountTextView.setText(getResources().getText(R.string.question_mark));
                mBuyVoteCountTextView.setText(getResources().getText(R.string.question_mark));
                mSellVoteCountTextView.setText(getResources().getText(R.string.question_mark));
            }

            // Only show when user voted
            if (mNews.getFactOpinionVote() != null) {
                mFactVoteCountTextView.setText(SharedInfo.formatNumberStringWithSuffix(mNews.getFactVoteCount()));
                mOpinionVoteCountTextView.setText(SharedInfo.formatNumberStringWithSuffix(mNews.getOpinionVoteCount()));
                switch (mNews.getFactOpinionVote()) {
                    case SharedInfo.FACT_VOTE:
                        mFactTextView.setTypeface(null, Typeface.BOLD);
                        break;
                    case SharedInfo.OPINION_VOTE:
                        mOpinionTextView.setTypeface(null, Typeface.BOLD);
                        break;
                    case SharedInfo.NEUTRAL_VOTE:
                        break;
                }
            } else {
                mFactVoteCountTextView.setText(getResources().getText(R.string.question_mark));
                mOpinionVoteCountTextView.setText(getResources().getText(R.string.question_mark));
            }

            if (mNews.getUpDownVote() != null) {
                mUpDownTextView.setText(SharedInfo.
                        formatNumberStringWithSuffix((mNews.getUpVoteCount() - mNews.getDownVoteCount())));
                switch (mNews.getUpDownVote()) {
                    case SharedInfo.UP_VOTE:
                        mVoteUpBtn.setScaleX(1.2f);
                        mVoteUpBtn.setScaleY(1.2f);
                        mVoteDownBtn.setScaleX(0.6f);
                        mVoteDownBtn.setScaleY(0.6f);
                        break;
                    case SharedInfo.DOWN_VOTE:
                        mVoteUpBtn.setScaleX(0.6f);
                        mVoteUpBtn.setScaleY(0.6f);
                        mVoteDownBtn.setScaleX(1.2f);
                        mVoteDownBtn.setScaleY(1.2f);
                        break;
                    case SharedInfo.NEUTRAL_VOTE:
                        break;
                }
            } else {
                mUpDownTextView.setText(getResources().getText(R.string.question_mark));
            }


            mTitleTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = NewsPageActivity.newIntent(getActivity(), mNews);
                    startActivity(i);
                }
            });

            mFactBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new VoteTask().execute(mNews, SharedInfo.FACT_VOTE);
                    if (mNews.getFactOpinionVote()!= null && mNews.getFactOpinionVote().equals(SharedInfo.FACT_VOTE)) {
                        mNews.voteFactOpinion(SharedInfo.NEUTRAL_VOTE);
                    } else {
                        mNews.voteFactOpinion(SharedInfo.FACT_VOTE);
                    }
                    Log.i(TAG, "Vote changed: " + mNews.getId() + "   " + mNews.getFactOpinionVote());
                    updateUI();
                }
            });

            mOpinionBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new VoteTask().execute(mNews, SharedInfo.OPINION_VOTE);
                    if (mNews.getFactOpinionVote()!= null && mNews.getFactOpinionVote().equals(SharedInfo.OPINION_VOTE)) {
                        mNews.voteFactOpinion(SharedInfo.NEUTRAL_VOTE);
                    } else {
                        mNews.voteFactOpinion(SharedInfo.OPINION_VOTE);
                    }
                    Log.i(TAG, "Vote changed: " + mNews.getId() + "   " + mNews.getFactOpinionVote());
                    updateUI();
                }
            });

            mHoldBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new VoteTask().execute(mNews, SharedInfo.HOLD_VOTE);
                    if (mNews.getTradeVote()!= null && mNews.getTradeVote().equals(SharedInfo.HOLD_VOTE)) {
                        mNews.voteTrade(SharedInfo.NEUTRAL_VOTE);
                    } else {
                        mNews.voteTrade(SharedInfo.HOLD_VOTE);
                    }
                    Log.i(TAG, "Vote changed: " + mNews.getId() + "   " + mNews.getTradeVote());
                    updateUI();
                }
            });

            mBuyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new VoteTask().execute(mNews, SharedInfo.BUY_VOTE);
                    if (mNews.getTradeVote()!= null && mNews.getTradeVote().equals(SharedInfo.BUY_VOTE)) {
                        mNews.voteTrade(SharedInfo.NEUTRAL_VOTE);
                    } else {
                        mNews.voteTrade(SharedInfo.BUY_VOTE);
                    }
                    Log.i(TAG, "Vote changed: " + mNews.getId() + "   " + mNews.getTradeVote());
                    updateUI();
                }
            });

            mSellBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new VoteTask().execute(mNews, SharedInfo.SELL_VOTE);
                    if (mNews.getTradeVote()!= null && mNews.getTradeVote().equals(SharedInfo.SELL_VOTE)) {
                        mNews.voteTrade(SharedInfo.NEUTRAL_VOTE);
                    } else {
                        mNews.voteTrade(SharedInfo.SELL_VOTE);
                    }
                    Log.i(TAG, "Vote changed: " + mNews.getId() + "   " + mNews.getTradeVote());
                    updateUI();
                }
            });

            mVoteUpBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new VoteTask().execute(mNews, SharedInfo.UP_VOTE);
                    if (mNews.getUpDownVote()!= null && mNews.getUpDownVote().equals(SharedInfo.UP_VOTE)) {
                        mNews.voteUpDown(SharedInfo.NEUTRAL_VOTE);
                    } else {
                        mNews.voteUpDown(SharedInfo.UP_VOTE);
                    }
                    Log.i(TAG, "Vote changed: " + mNews.getId() + "   " + mNews.getUpDownVote());
                    updateUI();
                }
            });

            mVoteDownBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new VoteTask().execute(mNews, SharedInfo.DOWN_VOTE);
                    if (mNews.getUpDownVote()!= null && mNews.getUpDownVote().equals(SharedInfo.DOWN_VOTE)) {
                        mNews.voteUpDown(SharedInfo.NEUTRAL_VOTE);
                    } else {
                        mNews.voteUpDown(SharedInfo.DOWN_VOTE);
                    }
                    Log.i(TAG, "Vote changed: " + mNews.getId() + "   " + mNews.getUpDownVote());
                    updateUI();
                }
            });



            mShareBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_TEXT, getNewsReport(mNews));
                    i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.news_subject));
                    i = Intent.createChooser(i, getString(R.string.share_option));
                    startActivity(i);
                }
            });
        }

        @Override
        public void onClick(View view) {

        }
    }

    private String getNewsReport(News news) {
        String newsReport = "";
        newsReport = getString(R.string.news_report, news.getTitle(), news.getUrl());
        return newsReport;
    }

    private class OnSwipeRefreshListener implements SwipeRefreshLayout.OnRefreshListener {

        @Override
        public void onRefresh() {
            updateNewsList();
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
//            Log.i(TAG, dx + "   " + dy + " aa  " + recyclerView.canScrollVertically(1));
//            Log.i(TAG, dx + "   " + dy + " bb  " + recyclerView.canScrollVertically(-1));
        }
    }

    private class VoteTask extends AsyncTask<Object, Void, Void> {

        @Override
        protected Void doInBackground(Object... objects) {
            News news = (News) objects[0];
            String voteType = (String) objects[1];
            switch (voteType) {
                case SharedInfo.FACT_VOTE:
                    new NetworkRequester().voteFact(news);
                    break;
                case SharedInfo.OPINION_VOTE:
                    new NetworkRequester().voteOpinion(news);
                    break;
                case SharedInfo.HOLD_VOTE:
                    new NetworkRequester().voteHold(news);
                    break;
                case SharedInfo.BUY_VOTE:
                    new NetworkRequester().voteBuy(news);
                    break;
                case SharedInfo.SELL_VOTE:
                    new NetworkRequester().voteSell(news);
                    break;
                case SharedInfo.UP_VOTE:
                    new NetworkRequester().voteUp(news);
                    break;
                case SharedInfo.DOWN_VOTE:
                    new NetworkRequester().voteDown(news);
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
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }
}
