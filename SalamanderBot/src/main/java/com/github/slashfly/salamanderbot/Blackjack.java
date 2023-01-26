package com.github.slashfly.salamanderbot;

import discord4j.core.object.command.ApplicationCommandInteraction;
import java.util.ArrayList;

public class Blackjack {

    public static ArrayList<Object> player = new ArrayList<>();
    public static ArrayList<Object> dealer = new ArrayList<>();
    public static ArrayList<Object> currentMessage = new ArrayList<>();
    
    public static void Array(String[] args){ 
    // add enough elements to the blackjack arraylist to hold the snowflake index numbers
        for (int i = 0; i < 40000; i++) {
            player.add(0, new Object());
            dealer.add(0, new Object());
        }
    }
    
    public static String dealer(ApplicationCommandInteraction acid, int currentBlackjack) {
        dealer.add(currentBlackjack, new Object());
        int cards = 0;

        // draw cards until either the total is 17 or higher, or the dealer has 3 cards
        while (true) {
            if (cards < 3 || dealer.get(currentBlackjack).getHand() < 17) {
                dealer.get(currentBlackjack).setHand(dealer.get(currentBlackjack).getHand() + (int) ((Math.random() * (11 - 1)) + 1));
                cards = cards + 1;
            } else {
                break;
            }
        }

        StringBuilder dealerCards = new StringBuilder();
        dealerCards.append(dealer.get(currentBlackjack).getHand());
        return dealerCards.toString();
    }
    
    public static String player(ApplicationCommandInteraction acid, int currentBlackjack) {
        // draw 2 cards
        player.get(currentBlackjack).setHand(player.get(currentBlackjack).getHand() + (int) ((Math.random() * (11 - 1)) + 1));
        player.get(currentBlackjack).setHand(player.get(currentBlackjack).getHand() + (int) ((Math.random() * (11 - 1)) + 1));
        
        StringBuilder playerCards = new StringBuilder();
        playerCards.append(player.get(currentBlackjack).getHand());
        return playerCards.toString();
    }
    
    public static String hit(ApplicationCommandInteraction acid, int currentBlackjack) {
        player.get(currentBlackjack).setHand(player.get(currentBlackjack).getHand() + (int) ((Math.random() * (11 - 1)) + 1));

        StringBuilder playerCards = new StringBuilder();
        playerCards.append(player.get(currentBlackjack).getHand());
        return playerCards.toString();
    }

    public static String stand(ApplicationCommandInteraction acid, int currentBlackjack) {
        StringBuilder result = new StringBuilder();

        // result 1: dealer had more cards than player and didn't bust
        if (dealer.get(currentBlackjack).getHand() > player.get(currentBlackjack).getHand() && dealer.get(currentBlackjack).getHand() < 22) {
            result.append(1);
            // result 2: player busted
        } else if (player.get(currentBlackjack).getHand() > 21) {
            result.append("**You lost with **`" + player.get(currentBlackjack).getHand() + "`** vs **`" + dealer.get(currentBlackjack).getHand() + "`**!**!");
            // result 3: both busted
        } else if (player.get(currentBlackjack).getHand() > 21 && dealer.get(currentBlackjack).getHand() > 21) {
            result.append(3);
            // result 4: same amount of cards
        } else if (player.get(currentBlackjack).getHand() == dealer.get(currentBlackjack).getHand()) {
            result.append(4);
            // result 5: dealer had a total of 21 and player did not
        } else if (dealer.get(currentBlackjack).getHand() == 21 && player.get(currentBlackjack).getHand() != 21) {
            result.append(5);
            // result 6: player had a total of 21 and dealer did not
        } else if (player.get(currentBlackjack).getHand() == 21 && dealer.get(currentBlackjack).getHand() != 21) {
            result.append(6);
        }

        dealer.remove(currentBlackjack);
        player.remove(currentBlackjack);
        return result.toString();
    }
}
