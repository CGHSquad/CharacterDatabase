package com.realcgh.newdawn.commands;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public class roles implements ICommand{
    @Override
    public String getName() {
        return "roles";
    }

    @Override
    public String getDescription() {
        return "Display all the server roles.";
    }

    @Override
    public List<OptionData> getOptions() {
        return null;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        //makes it where it'll wait for your request, you can only reply once to a command
        event.deferReply().setEphemeral(true).queue();
        StringBuilder response = new StringBuilder();
        for (Role role: event.getGuild().getRoles()){
            response.append(role.getAsMention());
        }
        event.getHook().sendMessage(response.toString()).queue();
    }
}
