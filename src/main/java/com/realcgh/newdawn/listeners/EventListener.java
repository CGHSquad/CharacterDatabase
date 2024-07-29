package com.realcgh.newdawn.listeners;

import com.realcgh.newdawn.database.SQLiteSource;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.unions.DefaultGuildChannelUnion;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.modals.ModalMapping;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class EventListener extends ListenerAdapter {


    /*@Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        User user = event.getUser();
        String jumpLink = event.getJumpUrl();
        String emoji = event.getReaction().getEmoji().getAsReactionCode();
        String channel = event.getChannel().getAsMention();
        DefaultGuildChannelUnion defaultchan = event.getGuild().getDefaultChannel();//gets the first channel created in the service

        String message = user.getAsMention()  + " reacted to a [message]("+jumpLink+") with " + emoji + " in the " + channel + " channel!";
        event.getChannel().asTextChannel().sendMessage(message).queue();
    }*/

    /**
     * Event fires when a message is sent in discord.
     */

    /**
     * Event fires when a new member joins a guild
     * Warning: Will not work without "Guild Members" gateway intent!
     */

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        // WILL NOT WORK WITHOUT GATEWAY INTENT!
        String avatar = event.getUser().getEffectiveAvatarUrl();
        System.out.println(avatar);
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:characters.db");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if(event.getModalId().equals("character-modal")) {
            ModalMapping nameValue = event.getValue("name-field");
            ModalMapping raceValue = event.getValue("race-field");
            ModalMapping ageValue = event.getValue("age-field");
            ModalMapping abilitiesValue = event.getValue("abilities-field");
            ModalMapping backgroundValue = event.getValue("background-field");

            String name = nameValue.getAsString();
            String race = raceValue.getAsString();
            String background = backgroundValue.getAsString();
            String userId = event.getUser().getId();

            String age, abilities;

            if (ageValue.getAsString().isBlank()){
                age = "N/A";
            } else {
                age = ageValue.getAsString();
            }
            if (abilitiesValue.getAsString().isBlank()){
                abilities = "N/A";
            } else {
                abilities = abilitiesValue.getAsString();
            }

            try {
                SQLiteSource.createCharacter(conn, userId, name, race, age, abilities,  background);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            EmbedBuilder builder = new EmbedBuilder();
            builder.addField("Name", name, false);
            builder.addField("Race", race, false);
            builder.addField("Age", age, false);
            builder.addField("Abilities", abilities, false);
            builder.addField("Background", background, false);
            builder.addField("UserID", userId, false);
            builder.setImage("https://i.imgur.com/uiM8FjE.png");

            event.replyEmbeds(builder.build()).queue();

        }
    }
}
