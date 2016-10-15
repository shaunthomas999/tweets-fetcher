package com.shaunthomas999.tweetsFetcher.controller;

import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.twitter.api.CursoredList;
import org.springframework.social.twitter.api.FilterStreamParameters;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;

/**
 * Created by shaunthomas on 15/10/16.
 */
@Controller
@RequestMapping("/")
public class TweetsFetcherController {

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
            return "redirect:/connect/twitter";
        }

        model.addAttribute(twitter.userOperations().getUserProfile());
        CursoredList<TwitterProfile> friends = twitter.friendOperations().getFriends();
        model.addAttribute("friends", friends);
        FilterStreamParameters filterStreamParameters = new FilterStreamParameters();
        twitter.streamingOperations().filter();
        return "home";
    }
}
