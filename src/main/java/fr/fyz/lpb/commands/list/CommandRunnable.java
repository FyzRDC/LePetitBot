package fr.fyz.lpb.commands.list;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public interface CommandRunnable {

	void onCommand(SlashCommandInteractionEvent event);
	
}
