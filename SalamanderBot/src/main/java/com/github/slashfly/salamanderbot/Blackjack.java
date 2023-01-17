package com.github.slashfly.salamanderbot;

import discord4j.core.object.command.ApplicationCommandInteraction;
import java.util.ArrayList;

public class Blackjack {

    public static ArrayList<Object> player = new ArrayList<Object>();
    public static ArrayList<Object> dealer = new ArrayList<Object>();

    private static String playerCards(ApplicationCommandInteraction acid) {

        StringBuilder result = new StringBuilder();
        return result.toString();
    }

    private static String playerTotal(ApplicationCommandInteraction acid) {

        StringBuilder result = new StringBuilder();
        return result.toString();
    }

    private static String dealer(ApplicationCommandInteraction acid) {
        dealer.add(new Object());
        
        //draw 2 cards
        dealer.get(0).setHand((int) ((Math.random() * (11 - 1)) + 1));
        dealer.get(0).setHand(dealer.get(0).getHand() + (int) ((Math.random() * (11 - 1)) + 1));
        
        //draw a third card if total is under 17
        if (dealer.get(0).getHand() < 17) {
            dealer.get(0).setHand(dealer.get(0).getHand() + (int) ((Math.random() * (11 - 1)) + 1));
        }
        
        StringBuilder result = new StringBuilder();
        result.append(dealer.get(0).getHand());
        return result.toString();
    }

    private static String hit(ApplicationCommandInteraction acid) {

        StringBuilder result = new StringBuilder();
        return result.toString();
    }

    private static String stand(ApplicationCommandInteraction acid) {

        StringBuilder result = new StringBuilder();
        return result.toString();
    }
}
