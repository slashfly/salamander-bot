package com.github.slashfly.salamanderbot;

import static com.github.slashfly.salamanderbot.Blackjack.player;
import static com.github.slashfly.salamanderbot.Blackjack.currentMessage;
import static com.github.slashfly.salamanderbot.Blackjack.dealer;

import org.reactivestreams.Publisher;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.ReactiveEventAdapter;
import discord4j.core.event.domain.interaction.ButtonInteractionEvent;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteraction;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.Button;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import reactor.core.publisher.Mono;

public class SalamanderBot {

    private static final Logger LOGGER = LoggerFactory.getLogger(SalamanderBot.class);

    // all response buttons
    public static Button hit = Button.success("hit", "Hit");
    public static Button stand = Button.danger("stand", "Stand");

    public static void main(String[] args) throws IOException {
        Path tokenFile = Path.of("token.txt");
        String token = Files.readString(tokenFile);

        // add empty values to the arraylists beforehand to avoid IndexOutOfBoundsException
        for (int i = 0; i < 40000; i++) {
            player.add(0, new Object());
            dealer.add(0, new Object());
        }
        currentMessage.add(0, new Object());

        // login
        GatewayDiscordClient client = DiscordClientBuilder.create(token).build().login().block();

        // obtain commands from json files
        List<String> commands = List.of("roll.json", "blackjack.json");
        try {
            new GlobalCommandRegistrar(client.getRestClient()).registerCommands(commands);
        } catch (Exception e) {
            LOGGER.error("Failed to register slash commands with Discord", e);
        }

        client.on(new ReactiveEventAdapter() {
            public Publisher<?> onChatInputInteraction(ChatInputInteractionEvent event) {
                // get the user initating the interaction
                final User author = event.getInteraction().getUser();
                // simple command responses
                if (event.getCommandName().equals("roll")) {
                    String roll = roll(event.getInteraction().getCommandInteraction().get());
                    return event.reply(author.getMention() + ", You rolled **" + roll + "**!");
                } else if (event.getCommandName().equals("blackjack")) {

                    Long customMessageId = Long.parseLong(event.getInteraction().getId().asString());

                    return event.deferReply().then(blackjackMain(event, customMessageId));
                }
                return Mono.empty();
            }

            public Publisher<?> onButtonInteraction(ButtonInteractionEvent event) {
                Long customMessageId = Long.parseLong(event.getInteraction().getId().asString());
                if (event.getCustomId().equals("hit")) {
                    return event.deferEdit().then(blackjackHit(event, customMessageId));
                } else if (event.getCustomId().equals("stand")) {
                    return event.deferEdit().then(blackjackStand(event, customMessageId));
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

    private static Mono<Message> blackjackMain(ChatInputInteractionEvent event, Long snowflake) {
        // get the user initating the interaction
        final User author = event.getInteraction().getUser();

        // use the snowflake as an index number for arraylist 
        int currentBlackjack = snowflakeConverter(snowflake);
        player.add(currentBlackjack, new Object());

        String newPlayer = Blackjack.player(event.getInteraction().getCommandInteraction().get(), currentBlackjack);

        return event.createFollowup(author.getMention() + "**, your current total is **" + "`" + newPlayer + "`.\n"
                + "What will you do?")
                .withComponents(ActionRow.of(hit, stand));
    }

    private static Mono<Message> blackjackHit(ButtonInteractionEvent event, Long snowflake) {
        // get the user initating the interaction
        final User author = event.getInteraction().getUser();
        
        int currentBlackjack = snowflakeConverter(snowflake);
        
        String playerHit = Blackjack.hit(event.getInteraction().getCommandInteraction().get(), currentBlackjack);
        if (Integer.parseInt(playerHit) < 21) {
            return event.editReply(author.getMention() + "**, your current total is **" + "`" + playerHit + "`.\n"
                    + "What will you do?")
                    .withComponents(ActionRow.of(hit, stand));
        } else if (Integer.parseInt(playerHit) > 20) {
            String loss = Blackjack.stand(event.getInteraction().getCommandInteraction().get(), currentBlackjack);
            return event.editReply(author.getMention() + loss);
        }
        return Mono.empty();
    }

    private static Mono<Message> blackjackStand(ButtonInteractionEvent event, Long snowflake) {
        // get the user initating the interaction
        final User author = event.getInteraction().getUser();

        int currentBlackjack = snowflakeConverter(snowflake);
        
        String result = Blackjack.stand(event.getInteraction().getCommandInteraction().get(), currentBlackjack);
        return event.editReply(author.getMention() + result);
    }

    private static int snowflakeConverter(Long snowflake) {
        // sqrt twice and round the snowflake to make it below the 32-bit integer limit
        // use decimal format to prevent the snowflake from being in scientific notation
        DecimalFormat df = new DecimalFormat("#");
        Double bjSnowflakeLong = Double.valueOf(snowflake);
        String bjSnowflakeString = df.format(Math.sqrt(Math.sqrt(bjSnowflakeLong)));

        int bjSnowflake = Math.round(Long.parseLong(bjSnowflakeString));
        return bjSnowflake;
    }
}
