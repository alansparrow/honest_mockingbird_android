package com.trungbao.honestmockingbird.model;

import com.trungbao.honestmockingbird.SharedInfo;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * Created by baotrungtn on 9/11/17.
 */

public class News {
    private String mId;
    private String mTitle;
    private String mUrl;
    private Date mPubDate;
    private String mPubSource;
    private String mFingerprint;
    private int mBuyVoteCount;
    private int mSellVoteCount;
    private int mHoldVoteCount;
    private String mMyVote;

    public News(String title, String url, Date pubDate, String pubSource, String fingerprint, int buyVoteCount, int sellVoteCount, int holdVoteCount) {
        mId = UUID.randomUUID().toString();
        mTitle = title;
        mUrl = url;
        mPubDate = pubDate;
        mPubSource = pubSource;
        mFingerprint = fingerprint;
        mBuyVoteCount = buyVoteCount;
        mSellVoteCount = sellVoteCount;
        mHoldVoteCount = holdVoteCount;
        mMyVote = SharedInfo.NEUTRAL_VOTE;
    }

    public News() {
        mId = UUID.randomUUID().toString();
        mTitle = "Title " + mId;
        mUrl = "Url " + mId;
        mPubDate = new Date();
        mPubSource = "Pub Source " + mId;
        mFingerprint = "Fingerprint " + mId;
        mBuyVoteCount = new Random().nextInt(1000);
        mSellVoteCount = new Random().nextInt(1000);
        mHoldVoteCount = new Random().nextInt(1000);
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public Date getPubDate() {
        return mPubDate;
    }

    public void setPubDate(Date pubDate) {
        mPubDate = pubDate;
    }

    public String getPubSource() {
        return mPubSource;
    }

    public void setPubSource(String pubSource) {
        mPubSource = pubSource;
    }

    public String getFingerprint() {
        return mFingerprint;
    }

    public void setFingerprint(String fingerprint) {
        mFingerprint = fingerprint;
    }

    public int getBuyVoteCount() {
        return mBuyVoteCount;
    }

    public void setBuyVoteCount(int buyVoteCount) {
        mBuyVoteCount = buyVoteCount;
    }

    public int getSellVoteCount() {
        return mSellVoteCount;
    }

    public void setSellVoteCount(int sellVoteCount) {
        mSellVoteCount = sellVoteCount;
    }

    public int getHoldVoteCount() {
        return mHoldVoteCount;
    }

    public void setHoldVoteCount(int holdVoteCount) {
        mHoldVoteCount = holdVoteCount;
    }

    public String getMyVote() {
        return mMyVote;
    }

    public void setMyVote(String myVote) {
        if (myVote != null && mMyVote != null) {
            switch (mMyVote) {
                case SharedInfo.NEUTRAL_VOTE:
                    switch (myVote) {
                        case SharedInfo.NEUTRAL_VOTE:
                            break;
                        case SharedInfo.HOLD_VOTE:
                            mHoldVoteCount += 1;
                            break;
                        case SharedInfo.BUY_VOTE:
                            mBuyVoteCount += 1;
                            break;
                        case SharedInfo.SELL_VOTE:
                            mSellVoteCount += 1;
                            break;
                    }
                    break;
                case SharedInfo.HOLD_VOTE:
                    switch (myVote) {
                        case SharedInfo.NEUTRAL_VOTE:
                            mHoldVoteCount -= 1;
                            break;
                        case SharedInfo.HOLD_VOTE:
                            break;
                        case SharedInfo.BUY_VOTE:
                            mHoldVoteCount -= 1;
                            mBuyVoteCount += 1;
                            break;
                        case SharedInfo.SELL_VOTE:
                            mHoldVoteCount -= 1;
                            mSellVoteCount += 1;
                            break;
                    }
                    break;
                case SharedInfo.BUY_VOTE:
                    switch (myVote) {
                        case SharedInfo.NEUTRAL_VOTE:
                            mBuyVoteCount -= 1;
                            break;
                        case SharedInfo.HOLD_VOTE:
                            mBuyVoteCount -= 1;
                            mHoldVoteCount += 1;
                            break;
                        case SharedInfo.BUY_VOTE:
                            break;
                        case SharedInfo.SELL_VOTE:
                            mBuyVoteCount -= 1;
                            mSellVoteCount += 1;
                            break;
                    }
                    break;
                case SharedInfo.SELL_VOTE:
                    switch (myVote) {
                        case SharedInfo.NEUTRAL_VOTE:
                            mSellVoteCount -= 1;
                            break;
                        case SharedInfo.HOLD_VOTE:
                            mSellVoteCount -= 1;
                            mHoldVoteCount += 1;
                            break;
                        case SharedInfo.BUY_VOTE:
                            mSellVoteCount -= 1;
                            mBuyVoteCount += 1;
                            break;
                        case SharedInfo.SELL_VOTE:
                            break;
                    }
                    break;
            }
        }

        mHoldVoteCount = (mHoldVoteCount < 0) ? 0 : mHoldVoteCount;
        mBuyVoteCount = (mBuyVoteCount < 0) ? 0 : mBuyVoteCount;
        mSellVoteCount = (mSellVoteCount < 0) ? 0 : mSellVoteCount;

        mMyVote = myVote;
        SharedInfo.setUserVote(getId(), myVote);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        String newLine = System.getProperty("line.separator");

        result.append( this.getClass().getName() );
        result.append( " Object {" );
        result.append(newLine);
        Field[] fields = this.getClass().getDeclaredFields();

        for ( Field field : fields  ) {
            result.append("  ");
            try {
                result.append( field.getName() );
                result.append(": ");
                result.append( field.get(this) );
            } catch ( IllegalAccessException ex ) {
                System.out.println(ex);
            }
            result.append(newLine);
        }
        result.append("}");

        return result.toString();
    }
}
