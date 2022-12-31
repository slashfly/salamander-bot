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
		
            GatewayDiscordClient client = DiscordClientBuilder.create(token).build().login().block();
            
	    List<String> commands = List.of("roll.json");
            try {
                new GlobalCommandRegistrar(client.getRestClient()).registerCommands(commands);
            } catch (Exception e) {
            }
            
            /* command deletion
	    long applicationId = client.getRestClient().getApplicationId().block();
	    long commandId = 1056358447244906496L;
	    client.getRestClient().getApplicationService()
	        .deleteGlobalApplicationCommand(applicationId, commandId)
	        .subscribe();
		*/
		 
	    // command responses
	    client.on(new ReactiveEventAdapter() {
                    @Override
	    	public Publisher<?> onChatInputInteraction(ChatInputInteractionEvent event) {
	    		if (event.getCommandName().equals("roll")) {
                            final User author = event.getInteraction().getUser();
                            String roll = roll(event.getInteraction().getCommandInteraction().get());
                            return event.reply(author.getMention() + ", You rolled **" + roll + "**!");
	    		}
	    		return Mono.empty();
	    	}
	    }).blockLast();
	}
	
	private static String roll(ApplicationCommandInteraction acid) {
		long max = acid.getOption("max")
                    .flatMap(ApplicationCommandInteractionOption::getValue)
                    .map(ApplicationCommandInteractionOptionValue::asLong)
                    .orElse(100L);
		StringBuilder result = new StringBuilder();
		result.append((int) ((Math.random() * (max - 1)) + 1));
		return result.toString();
	}
}
