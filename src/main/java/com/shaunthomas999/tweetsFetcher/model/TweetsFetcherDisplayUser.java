package com.shaunthomas999.tweetsFetcher.model;

import org.springframework.social.twitter.api.Tweet;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by shaunthomas on 16/10/16.
 */
@Component
public class TweetsFetcherDisplayUser {

    private long id;

    private String name;

    private String screenName;

    private String backgroundImageUrl;

    private List<Tweet> tweets;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getBackgroundImageUrl() {
        return backgroundImageUrl;
    }

    public void setBackgroundImageUrl(String backgroundImageUrl) {
        this.backgroundImageUrl = backgroundImageUrl;
    }

    public List<Tweet> getTweets() {
        return tweets;
    }

    public void setTweets(List<Tweet> tweets) {
        this.tweets = tweets;
    }
}
