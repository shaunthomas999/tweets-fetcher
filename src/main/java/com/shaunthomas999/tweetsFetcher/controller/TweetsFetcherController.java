package com.shaunthomas999.tweetsFetcher.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.twitter.api.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;
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
    public String home(Model model) {
        if(connectionRepository.findPrimaryConnection(Twitter.class) == null) {
            LOGGER.info("Going to perform authentication");
            return "redirect:/connect/twitter";
        }

        TwitterProfile twitterProfile = twitter.userOperations().getUserProfile("ElsevierNews");
        model.addAttribute(twitterProfile);
        CursoredList<TwitterProfile> friends = twitter.friendOperations().getFriends(twitterProfile.getId());
        model.addAttribute("friends", friends);
        List<Tweet> tweets= twitter.timelineOperations().getUserTimeline(twitterProfile.getId(),30);
        model.addAttribute("tweets", tweets);
        
        return "home";
    }
}
