package com.github.slashfly.salamanderbot;

import discord4j.core.object.command.ApplicationCommandInteraction;
import java.util.ArrayList;

public class Blackjack {

    public static ArrayList<Object> player = new ArrayList<Object>();
    public static ArrayList<Object> dealer = new ArrayList<Object>();
    
    private static String dealer(ApplicationCommandInteraction acid) {
        dealer.add(new Object());
        int cards = 0;

        // draw cards until either the total is 17 or higher, or the dealer has 3 cards
        while (true) {
            if (cards < 3 || dealer.get(0).getHand() < 17) {
                dealer.get(0).setHand(dealer.get(0).getHand() + (int) ((Math.random() * (11 - 1)) + 1));
                cards = cards + 1;
            } else {
                break;
            }
        }

        StringBuilder result = new StringBuilder();
        result.append(dealer.get(0).getHand());
        dealer.remove(0);
        return result.toString();
    }

    private static String hit(ApplicationCommandInteraction acid) {
        player.get(0).setHand(player.get(0).getHand() + (int) ((Math.random() * (11 - 1)) + 1));
        
        StringBuilder result = new StringBuilder();
        result.append(player.get(0).getHand());
        return result.toString();
    }

    private static String stand(ApplicationCommandInteraction acid) {

        StringBuilder result = new StringBuilder();
        return result.toString();
    }
}
