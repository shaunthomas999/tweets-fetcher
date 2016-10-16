package com.shaunthomas999.tweetsFetcher.twitterApiImpl;

import com.shaunthomas999.tweetsFetcher.controller.TweetsFetcherController;
import com.shaunthomas999.tweetsFetcher.model.TweetsFetcherDisplayUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.twitter.api.StreamDeleteEvent;
import org.springframework.social.twitter.api.StreamListener;
import org.springframework.social.twitter.api.StreamWarningEvent;
import org.springframework.social.twitter.api.Tweet;

import javax.inject.Inject;
import java.lang.invoke.MethodHandles;
import java.util.List;

/**
 * Created by shaunthomas on 15/10/16.
 */
public class TweetsFetcherStreamListener implements StreamListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Inject
    private TweetsFetcherDisplayUser displayUser;

    @Inject
    private TweetsFetcherController tweetsFetcherController;

    @Override
    public void onTweet(Tweet tweet) {
        LOGGER.info("onTweet: " + tweet.getText());
        List<Tweet> displayUserTweets = displayUser.getTweets();
        displayUserTweets.add(tweet);
        displayUser.setTweets(displayUserTweets);
    }

    @Override
    public void onDelete(StreamDeleteEvent streamDeleteEvent) {
        LOGGER.info("onDelete: ");
    }

    @Override
    public void onLimit(int i) {
        LOGGER.info("onLimit: ");
    }

    @Override
    public void onWarning(StreamWarningEvent streamWarningEvent) {
        LOGGER.info("onWarning: ");
    }
}