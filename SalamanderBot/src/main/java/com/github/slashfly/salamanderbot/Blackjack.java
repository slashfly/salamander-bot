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
        return result.toString();
    }

    private static String hit(ApplicationCommandInteraction acid) {
        player.get(0).setHand(player.get(0).getHand() + (int) ((Math.random() * (11 - 1)) + 1));
        
        StringBuilder result = new StringBuilder();
        result.append(player.get(0).getHand());
        return result.toString();
    }

    private static String stand(ApplicationCommandInteraction acid,  int dealerHand) {
        StringBuilder result = new StringBuilder();
        
        // result 1: dealer had more cards than player and didn't bust
        if(dealer.get(0).getHand() > player.get(0).getHand() && dealer.get(0).getHand() < 22){
            result.append(1);
        // result 2: player busted
        } else if(player.get(0).getHand() > 21) {
            result.append(2);
        // result 3: both busted
        } else if(player.get(0).getHand() > 21 && dealer.get(0).getHand() > 21){
            result.append(3);
        // result 4: same amount of cards
        } else if(player.get(0).getHand() == dealer.get(0).getHand()){
            result.append(4);
        // result 5: dealer had a total of 21 and player did not
        } else if(dealer.get(0).getHand() == 21 && player.get(0).getHand() != 21){
            result.append(5);
        // result 6: player had a total of 21 and dealer did not
        } else if(player.get(0).getHand() == 21 && dealer.get(0).getHand() != 21){
            result.append(6);
        }
        
        dealer.remove(0);
        player.remove(0);
        return result.toString();
    }
}
