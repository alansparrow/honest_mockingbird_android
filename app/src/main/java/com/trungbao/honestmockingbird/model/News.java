package com.trungbao.honestmockingbird.model;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * Created by baotrungtn on 9/11/17.
 */

public class News {
    private UUID mId;
    private String mTitle;
    private String mUrl;
    private Date mPubDate;
    private String mPubSource;
    private String mFingerprint;
    private int mBuyVoteCount;
    private int mSellVoteCount;
    private int mHoldVoteCount;

    public News(String title, String url, Date pubDate, String pubSource, String fingerprint, int buyVoteCount, int sellVoteCount, int holdVoteCount) {
        mId = UUID.randomUUID();
        mTitle = title;
        mUrl = url;
        mPubDate = pubDate;
        mPubSource = pubSource;
        mFingerprint = fingerprint;
        mBuyVoteCount = buyVoteCount;
        mSellVoteCount = sellVoteCount;
        mHoldVoteCount = holdVoteCount;
    }

    public News() {
        mId = UUID.randomUUID();
        mTitle = "Title " + mId;
        mUrl = "Url " + mId;
        mPubDate = new Date();
        mPubSource = "Pub Source " + mId;
        mFingerprint = "Fingerprint " + mId;
        mBuyVoteCount = new Random().nextInt(1000);
        mSellVoteCount = new Random().nextInt(1000);
        mHoldVoteCount = new Random().nextInt(1000);
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
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



}
