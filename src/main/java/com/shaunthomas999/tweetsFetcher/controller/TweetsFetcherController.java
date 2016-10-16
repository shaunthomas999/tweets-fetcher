package com.shaunthomas999.tweetsFetcher.controller;

import com.shaunthomas999.tweetsFetcher.model.TweetsFetcherDisplayUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.twitter.api.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.List;

/**
 * Created by shaunthomas on 15/10/16.
 */
@Controller
@RequestMapping("/")
public class TweetsFetcherController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private Twitter twitter;

    private ConnectionRepository connectionRepository;

    @Value("${app.tweetsFetcher.twitterUser}")
    private String twitterUser;

    @Inject
    public TweetsFetcherController(Twitter twitter, ConnectionRepository connectionRepository) {
        this.twitter = twitter;
        this.connectionRepository = connectionRepository;
    }

    /**
     * Handles GET method request on root URL and displays tweets from the configured twitter user
     * @param tweetsFetcherDisplayUser - Model to use in the page
     * @return view name
     */
    @RequestMapping(method= RequestMethod.GET)
    public String home(@ModelAttribute TweetsFetcherDisplayUser tweetsFetcherDisplayUser) {
        try {
            // Establish connection if not already
            if(connectionRepository.findPrimaryConnection(Twitter.class) == null) {
                LOGGER.info("Going to perform authentication");
                return "redirect:/connect/twitter";
            }

            // Set properties into TweetsFetcherDisplayUser model from TwitterProfile
            TwitterProfile twitterProfile = twitter.userOperations().getUserProfile(twitterUser);
            tweetsFetcherDisplayUser.setId(twitterProfile.getId());
            tweetsFetcherDisplayUser.setName(twitterProfile.getName());
            tweetsFetcherDisplayUser.setScreenName(twitterProfile.getScreenName());
            tweetsFetcherDisplayUser.setBackgroundImageUrl(twitterProfile.getBackgroundImageUrl());

            // Get tweets - twitter API allows only to fetch upto 200 tweets at a time
            List<Tweet> tweets= twitter.timelineOperations().getUserTimeline(tweetsFetcherDisplayUser.getId(), 200);

            // Set tweets and tweet count for display
            tweetsFetcherDisplayUser.setTweets(tweets);
            tweetsFetcherDisplayUser.setTweetsCount(tweets.size());

            // Creating StreamListener for twitter user
            StreamListener streamListener = new StreamListener() {
                @Override
                public void onTweet(Tweet tweet) {
                    LOGGER.info("onTweet: " + tweet.getText());
                    List<Tweet> displayUserTweets = tweetsFetcherDisplayUser.getTweets();
                    displayUserTweets.add(tweet);
                    tweetsFetcherDisplayUser.setTweets(displayUserTweets);
                }

                @Override
                public void onDelete(StreamDeleteEvent streamDeleteEvent) {
                    LOGGER.info("onDelete");
                }

                @Override
                public void onLimit(int i) {
                    LOGGER.info("onLimit");
                }

                @Override
                public void onWarning(StreamWarningEvent streamWarningEvent) {
                    LOGGER.info("onWarning");
                }
            };

            // Set the twitter profile to stream for
            FilterStreamParameters filterStreamParameters = new FilterStreamParameters();
            filterStreamParameters.follow(tweetsFetcherDisplayUser.getId());

            // Create stream for twitter profile
            twitter.streamingOperations().filter(filterStreamParameters, Arrays.asList(streamListener));
            Thread.sleep(1000);

        } catch (InterruptedException interruptedException) {
            LOGGER.error(interruptedException.getMessage());
        }

        return "home";
    }
}
