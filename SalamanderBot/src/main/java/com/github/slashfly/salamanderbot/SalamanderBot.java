package com.github.slashfly.salamanderbot;

import static com.github.slashfly.salamanderbot.Blackjack.player;

import org.reactivestreams.Publisher;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.ReactiveEventAdapter;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteraction;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.Button;
import discord4j.core.object.entity.User;
import discord4j.common.util.Snowflake;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.util.List;

import reactor.core.publisher.Mono;

public class SalamanderBot {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(SalamanderBot.class);

    public static void main(String[] args) throws IOException {
        Path tokenFile = Path.of("token.txt");
        String token = Files.readString(tokenFile);

        // login
        GatewayDiscordClient client = DiscordClientBuilder.create(token).build().login().block();

        // obtain commands from json files
        List<String> commands = List.of("roll.json", "blackjack.json");
        try {
            new GlobalCommandRegistrar(client.getRestClient()).registerCommands(commands);
        } catch (Exception e) {
            LOGGER.error("Failed to register slash commands with Discord", e);
        }

        // all response buttons
        Button hit = Button.success("hit", "Hit");
        Button stand = Button.danger("stand", "Stand");

        client.on(new ReactiveEventAdapter() {
            @Override
            public Publisher<?> onChatInputInteraction(ChatInputInteractionEvent event) {
                // get the user initating the interaction
                final User author = event.getInteraction().getUser();
                // simple command responses
                if (event.getCommandName().equals("roll")) {
                    String roll = roll(event.getInteraction().getCommandInteraction().get());
                    return event.reply(author.getMention() + ", You rolled **" + roll + "**!");
                } else if (event.getCommandName().equals("blackjack")) {  
                    int bjSnowflake = Integer.parseInt(event.getInteraction().getMessageId().get().asString());
                    player.add(bjSnowflake, new Object());
                    Blackjack blackjack = new Blackjack();
                    
                    String playerHit = Blackjack.hit(event.getInteraction().getCommandInteraction().get());
                    return event.reply(author.getMention() + "**, your current total is **" + "`" + playerHit + "`.\n"
                            + "What will you do?")
                            .withComponents(ActionRow.of(hit, stand));
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
