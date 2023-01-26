package com.github.slashfly.salamanderbot;

public class Object {
    private int hand;
    private int timesUsed;
    private int currentBlackjack;
    
    public int getHand(){
        return this.hand;
    }
    
    public void setHand(int newHand){
        this.hand = newHand;
    }
    
    public int getTimesUsed(){
        return this.timesUsed;
    }
    
    public void setTimesUsed(int newTimesUsed) {
        this.timesUsed = newTimesUsed;
    }
    
    public int getCurrentBlackjack(){
        return this.currentBlackjack;
    }
    
    public void setCurrentBlackjack(int newCurrentBlackjack) {
        this.currentBlackjack = newCurrentBlackjack;
    }
}