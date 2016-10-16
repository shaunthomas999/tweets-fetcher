package com.shaunthomas999.tweetsFetcher.controller;

import com.shaunthomas999.tweetsFetcher.model.TweetsFetcherDisplayUser;
import com.shaunthomas999.tweetsFetcher.twitterApiImpl.TweetsFetcherStreamListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.twitter.api.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
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

    @Inject
    public TweetsFetcherController(Twitter twitter, ConnectionRepository connectionRepository) {
        this.twitter = twitter;
        this.connectionRepository = connectionRepository;
    }

    @RequestMapping(method= RequestMethod.GET)
    public String home(@ModelAttribute TweetsFetcherDisplayUser tweetsFetcherDisplayUser) {
        try {
            if(connectionRepository.findPrimaryConnection(Twitter.class) == null) {
                LOGGER.info("Going to perform authentication");
                return "redirect:/connect/twitter";
            }

            // Set the screenName
            TwitterProfile twitterProfile = twitter.userOperations().getUserProfile("shaunthomas999");
            tweetsFetcherDisplayUser.setScreenName(twitterProfile.getScreenName());

            // Set the tweets
            List<Tweet> tweets= twitter.timelineOperations().getUserTimeline(twitterProfile.getId(),30);
            tweetsFetcherDisplayUser.setTweets(tweets);

            // Stream for tweets
            FilterStreamParameters filterStreamParameters = new FilterStreamParameters();
            filterStreamParameters.follow(twitterProfile.getId());
            StreamListener streamListener2 = new StreamListener() {
                @Override
                public void onTweet(Tweet tweet) {
                    LOGGER.info("onTweet2: " + tweet.getText());
                    LOGGER.info("Tweets count2:" + tweetsFetcherDisplayUser.getTweets().size());
                    List<Tweet> displayUserTweets = tweetsFetcherDisplayUser.getTweets();
                    displayUserTweets.add(tweet);
                    tweetsFetcherDisplayUser.setTweets(displayUserTweets);
                    LOGGER.info("Tweets count3:" + tweetsFetcherDisplayUser.getTweets().size());
                }

                @Override
                public void onDelete(StreamDeleteEvent streamDeleteEvent) {
                    LOGGER.info("onDelete2: ");
                }

                @Override
                public void onLimit(int i) {
                    LOGGER.info("onLimit2: ");
                }

                @Override
                public void onWarning(StreamWarningEvent streamWarningEvent) {
                    LOGGER.info("onWarning2: ");
                }
            };
            //twitter.streamingOperations().filter(filterStreamParameters, Arrays.asList(new TweetsFetcherStreamListener()));
            twitter.streamingOperations().filter(filterStreamParameters, Arrays.asList(streamListener2));
            Thread.sleep(1000);

        } catch (InterruptedException interruptedException) {
            LOGGER.error(interruptedException.getMessage());
        }

        return "home";
    }
}
