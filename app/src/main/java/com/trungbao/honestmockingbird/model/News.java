package com.trungbao.honestmockingbird.model;

import com.trungbao.honestmockingbird.SharedInfo;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * Created by baotrungtn on 9/11/17.
 */

public class News implements Serializable{
    private String mId;
    private String mTitle;
    private String mUrl;
    private Date mPubDate;
    private String mPubSource;
    private String mFingerprint;
    private int mBuyVoteCount;
    private int mSellVoteCount;
    private int mHoldVoteCount;
    private int mFactVoteCount;
    private int mOpinionVoteCount;
    private int mUpVoteCount;
    private int mDownVoteCount;
    private String mTradeVote;
    private String mFactOpinionVote;
    private String mUpDownVote;

    public News(String title, String url, Date pubDate, String pubSource, String fingerprint,
                int buyVoteCount, int sellVoteCount, int holdVoteCount,
                int factVoteCount, int opinionVoteCount, int upVoteCount, int downVoteCount) {
        mId = UUID.randomUUID().toString();
        mTitle = title;
        mUrl = url;
        mPubDate = pubDate;
        mPubSource = pubSource;
        mFingerprint = fingerprint;
        mBuyVoteCount = buyVoteCount;
        mSellVoteCount = sellVoteCount;
        mHoldVoteCount = holdVoteCount;
        mFactVoteCount = factVoteCount;
        mOpinionVoteCount = opinionVoteCount;
        mUpVoteCount = upVoteCount;
        mDownVoteCount = downVoteCount;
        mTradeVote = SharedInfo.NEUTRAL_VOTE;
        mFactOpinionVote = SharedInfo.NEUTRAL_VOTE;
        mUpDownVote = SharedInfo.NEUTRAL_VOTE;
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
        mFactVoteCount = new Random().nextInt(1000);
        mOpinionVoteCount = new Random().nextInt(1000);
        mUpVoteCount = new Random().nextInt(1000);
        mDownVoteCount = new Random().nextInt(1000);
        mTradeVote = SharedInfo.NEUTRAL_VOTE;
        mFactOpinionVote = SharedInfo.NEUTRAL_VOTE;
        mUpDownVote = SharedInfo.NEUTRAL_VOTE;
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

    public int getFactVoteCount() {
        return mFactVoteCount;
    }

    public void setFactVoteCount(int factVoteCount) {
        mFactVoteCount = factVoteCount;
    }

    public int getOpinionVoteCount() {
        return mOpinionVoteCount;
    }

    public void setOpinionVoteCount(int opinionVoteCount) {
        mOpinionVoteCount = opinionVoteCount;
    }

    public int getUpVoteCount() {
        return mUpVoteCount;
    }

    public void setUpVoteCount(int upVoteCount) {
        mUpVoteCount = upVoteCount;
    }

    public int getDownVoteCount() {
        return mDownVoteCount;
    }

    public void setDownVoteCount(int downVoteCount) {
        mDownVoteCount = downVoteCount;
    }

    public String getUpDownVote() {
        return mUpDownVote;
    }

    public void setUpDownVote(String upDownVote) {
        mUpDownVote = upDownVote;
    }

    public String getFactOpinionVote() {
        return mFactOpinionVote;
    }

    public void setFactOpinionVote(String factOpinionVote) {
        mFactOpinionVote = factOpinionVote;
    }

    public String getTradeVote() {
        return mTradeVote;
    }

    public void setTradeVote(String tradeVote) {
        mTradeVote = tradeVote;
    }

    public void voteFactOpinion(String myVote) {
        if (mFactOpinionVote == null && myVote != null) {
            switch (myVote) {
                case SharedInfo.FACT_VOTE:
                    mFactVoteCount += 1;
                    break;
                case SharedInfo.OPINION_VOTE:
                    mOpinionVoteCount += 1;
                    break;
            }
        } else if (mFactOpinionVote != null && myVote != null) {
            switch (mFactOpinionVote) {
                case SharedInfo.NEUTRAL_VOTE:
                    switch (myVote) {
                        case SharedInfo.NEUTRAL_VOTE:
                            break;
                        case SharedInfo.FACT_VOTE:
                            mFactVoteCount += 1;
                            break;
                        case SharedInfo.OPINION_VOTE:
                            mOpinionVoteCount += 1;
                            break;

                    }
                    break;
                case SharedInfo.FACT_VOTE:
                    switch (myVote) {
                        case SharedInfo.NEUTRAL_VOTE:
                            mFactVoteCount -= 1;
                            break;
                        case SharedInfo.FACT_VOTE:  // refactoring here -1
                            break;
                        case SharedInfo.OPINION_VOTE:
                            mFactVoteCount -= 1;
                            mOpinionVoteCount += 1;
                            break;
                    }
                    break;
                case SharedInfo.OPINION_VOTE:
                    switch (myVote) {
                        case SharedInfo.NEUTRAL_VOTE:
                            mOpinionVoteCount -= 1;
                            break;
                        case SharedInfo.OPINION_VOTE:  // refactoring here -1
                            break;
                        case SharedInfo.FACT_VOTE:
                            mOpinionVoteCount -= 1;
                            mFactVoteCount += 1;
                            break;
                    }
                    break;
            }
        }

        mFactVoteCount = (mFactVoteCount < 0) ? 0 : mFactVoteCount;
        mOpinionVoteCount = (mOpinionVoteCount < 0) ? 0 : mOpinionVoteCount;

        mFactOpinionVote = myVote;
        SharedInfo.setUserFactopinionVoteRef(this, myVote);
    }

    public void voteTrade(String myVote) {

        if (mTradeVote == null && myVote != null) {
            switch (myVote) {
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
        } else if (mTradeVote != null && myVote != null) {
            switch (mTradeVote) {
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

        mTradeVote = myVote;
        SharedInfo.setUserTradeVoteRef(this, myVote);
    }

    public void voteUpDown(String myVote) {
        if (mUpDownVote == null && myVote != null) {
            switch (myVote) {
                case SharedInfo.UP_VOTE:
                    mUpVoteCount += 1;
                    break;
                case SharedInfo.DOWN_VOTE:
                    mDownVoteCount += 1;
                    break;
            }
        } else if (mUpDownVote != null && myVote != null) {
            switch (mUpDownVote) {
                case SharedInfo.NEUTRAL_VOTE:
                    switch (myVote) {
                        case SharedInfo.NEUTRAL_VOTE:
                            break;
                        case SharedInfo.UP_VOTE:
                            mUpVoteCount += 1;
                            break;
                        case SharedInfo.DOWN_VOTE:
                            mDownVoteCount += 1;
                            break;

                    }
                    break;
                case SharedInfo.UP_VOTE:
                    switch (myVote) {
                        case SharedInfo.NEUTRAL_VOTE:
                            mUpVoteCount -= 1;
                            break;
                        case SharedInfo.UP_VOTE:  // refactoring here -1
                            break;
                        case SharedInfo.DOWN_VOTE:
                            mUpVoteCount -= 1;
                            mDownVoteCount += 1;
                            break;
                    }
                    break;
                case SharedInfo.DOWN_VOTE:
                    switch (myVote) {
                        case SharedInfo.NEUTRAL_VOTE:
                            mDownVoteCount -= 1;
                            break;
                        case SharedInfo.DOWN_VOTE:  // refactoring here -1
                            break;
                        case SharedInfo.UP_VOTE:
                            mDownVoteCount -= 1;
                            mUpVoteCount += 1;
                            break;
                    }
                    break;
            }
        }

        mUpVoteCount = (mUpVoteCount < 0) ? 0 : mUpVoteCount;
        mDownVoteCount = (mDownVoteCount < 0) ? 0 : mDownVoteCount;

        mUpDownVote = myVote;
        SharedInfo.setUserUpDownVoteRef(this, myVote);
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
