package com.realcgh.newdawn.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

import java.util.List;

public class createCharacter implements ICommand{
    @Override
    public String getName() {
        return "character-creator";
    }

    @Override
    public String getDescription() {
        return "Creates a character";
    }

    @Override
    public List<OptionData> getOptions() {
        return null;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        TextInput name = TextInput.create("name-field","Name", TextInputStyle.SHORT)
                .setRequired(true)
                .setMinLength(1)
                .setMaxLength(100)
                .build();

        TextInput race = TextInput.create("race-field", "Race", TextInputStyle.SHORT)
                .setRequired(true)
                .setMinLength(1)
                .setMaxLength(100)
                .build();

        TextInput age = TextInput.create("age-field", "Age", TextInputStyle.SHORT)
                .setRequired(false)
                .setPlaceholder("Not Required: Leave Blank if no valid statement")
                .build();

        TextInput abilities = TextInput.create("abilities-field", "Abilities", TextInputStyle.SHORT)
                .setRequired(false)
                .setPlaceholder("Not Required: Leave Blank if no valid statement")
                .setMinLength(1)
                .setMaxLength(100)
                .build();

        TextInput background = TextInput.create("background-field", "Background", TextInputStyle.PARAGRAPH)
                .setRequired(true)
                .setPlaceholder("Provide some needed background.")
                .build();

        Modal modal = Modal.create("character-modal", "describe your character")
                .addComponents(ActionRow.of(name), ActionRow.of(race), ActionRow.of(age), ActionRow.of(abilities), ActionRow.of(background))
                .build();

        event.replyModal(modal).queue();
    }
}
