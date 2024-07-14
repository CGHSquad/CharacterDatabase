package com.realcgh.newdawn.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

public class userRoles implements ICommand{
    @Override
    public String getName() {
        return "user-roles";
    }

    @Override
    public String getDescription() {
        return "Gets the role of a specific user.";
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.USER, "input", "See another user's roles(optional)", false));
        return options;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        // Defer the reply to allow for processing time
        event.deferReply().setEphemeral(true).queue();

        // Retrieve the mentioned user, if any
        Member member;
        OptionMapping userOption = event.getOption("input");

        if (userOption != null) {
            member = userOption.getAsMember();
        } else {
            member = event.getMember();
        }

        if (member == null) {
            event.getHook().sendMessage("User not found!").queue();
            return;
        }

        // Build the response message
        StringBuilder response = new StringBuilder(member.getEffectiveName() + "'s roles:\n");

        for (Role role : member.getRoles()) {
            response.append(role.getAsMention());
        }

        // Send the response
        event.getHook().sendMessage(response.toString()).queue();
    }
}
