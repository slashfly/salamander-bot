package com.github.slashfly.salamanderbot;

import discord4j.core.object.command.ApplicationCommandInteraction;
import java.util.ArrayList;

public class Blackjack {

    public static ArrayList<Object> player = new ArrayList<Object>();
    public static ArrayList<Object> dealer = new ArrayList<Object>();
    
    public static String dealer(ApplicationCommandInteraction acid, int bjSnowflake) {
        dealer.add(bjSnowflake, new Object());
        int cards = 0;

        // draw cards until either the total is 17 or higher, or the dealer has 3 cards
        while (true) {
            if (cards < 3 || dealer.get(bjSnowflake).getHand() < 17) {
                dealer.get(bjSnowflake).setHand(dealer.get(bjSnowflake).getHand() + (int) ((Math.random() * (11 - 1)) + 1));
                cards = cards + 1;
            } else {
                break;
            }
        }

        StringBuilder dealerCards = new StringBuilder();
        dealerCards.append(dealer.get(bjSnowflake).getHand());
        return dealerCards.toString();
    }

    public static String hit(ApplicationCommandInteraction acid, int bjSnowflake) {
        player.get(bjSnowflake).setHand(player.get(bjSnowflake).getHand() + (int) ((Math.random() * (11 - 1)) + 1));

        StringBuilder playerCards = new StringBuilder();
        playerCards.append(player.get(bjSnowflake).getHand());
        return playerCards.toString();
    }

    public static String stand(ApplicationCommandInteraction acid, int bjSnowflake) {
        StringBuilder result = new StringBuilder();

        // result 1: dealer had more cards than player and didn't bust
        if (dealer.get(bjSnowflake).getHand() > player.get(bjSnowflake).getHand() && dealer.get(bjSnowflake).getHand() < 22) {
            result.append(1);
            // result 2: player busted
        } else if (player.get(bjSnowflake).getHand() > 21) {
            result.append(2);
            // result 3: both busted
        } else if (player.get(bjSnowflake).getHand() > 21 && dealer.get(bjSnowflake).getHand() > 21) {
            result.append(3);
            // result 4: same amount of cards
        } else if (player.get(bjSnowflake).getHand() == dealer.get(bjSnowflake).getHand()) {
            result.append(4);
            // result 5: dealer had a total of 21 and player did not
        } else if (dealer.get(bjSnowflake).getHand() == 21 && player.get(bjSnowflake).getHand() != 21) {
            result.append(5);
            // result 6: player had a total of 21 and dealer did not
        } else if (player.get(bjSnowflake).getHand() == 21 && dealer.get(bjSnowflake).getHand() != 21) {
            result.append(6);
        }

        dealer.remove(bjSnowflake);
        player.remove(bjSnowflake);
        return result.toString();
    }
}
