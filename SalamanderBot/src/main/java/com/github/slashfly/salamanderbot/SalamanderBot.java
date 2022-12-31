package com.github.slashfly.salamanderbot;

import org.reactivestreams.Publisher;		

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.ReactiveEventAdapter;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteraction;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.entity.User;

import java.util.List;

import reactor.core.publisher.Mono;

public class SalamanderBot {

    public static void main(String[] args) {
		
            String token = "";
            
            // login
            GatewayDiscordClient client = DiscordClientBuilder.create(token).build().login().block();
            
            // obtain commands from json files
	    List<String> commands = List.of("roll.json");
            try {
                new GlobalCommandRegistrar(client.getRestClient()).registerCommands(commands);
            } catch (Exception e) {
            }
 
	    client.on(new ReactiveEventAdapter() {
                    @Override
	    	public Publisher<?> onChatInputInteraction(ChatInputInteractionEvent event) {
                    // get the user initating the interaction
                    final User author = event.getInteraction().getUser();
                        // all command responses
	    		if (event.getCommandName().equals("roll")) {
                            String roll = roll(event.getInteraction().getCommandInteraction().get());
                            return event.reply(author.getMention() + ", You rolled **" + roll + "**!");
	    		}
	    		return Mono.empty();
	    	}
	    }).blockLast();
	}
	
        // roll command
	private static String roll(ApplicationCommandInteraction acid) {
                // get the max number that was picked, if no number was chosen then default to 100
		long max = acid.getOption("max")
                    .flatMap(ApplicationCommandInteractionOption::getValue)
                    .map(ApplicationCommandInteractionOptionValue::asLong)
                    .orElse(100L);
                // generate random number
		StringBuilder result = new StringBuilder();
		result.append((int) ((Math.random() * (max - 1)) + 1));
		return result.toString();
	}
}
