package fr.fyz.lpb.commands.list;

import java.time.Instant;

import fr.fyz.lpb.commands.ITCommand;
import fr.fyz.lpb.enums.RAINBOW;
import fr.fyz.lpb.music.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class CommandStop implements ITCommand{

	@Override
	public String getName() {
		return "stop";
	}

	@Override
	public String getDescription() {
		return "Permet de stopper la musique et de supprimer la liste d'attente";
	}

	@Override
	public OptionData[] getOptions() {
		return null;
	}

	@Override
	public CommandRunnable getRunnable() {
		return new CommandRunnable() {
			
			@Override
			public void onCommand(SlashCommandInteractionEvent event) {
				if(!event.getMember().hasPermission(Permission.VOICE_MUTE_OTHERS)) {
					EmbedBuilder eb = new EmbedBuilder();
					eb.setColor(RAINBOW.RED.getColor());
					eb.setTimestamp(Instant.now());
					eb.setDescription("Erreur.");
					eb.setTitle("Tu n'as pas la permission de faire �a !");
					event.replyEmbeds(eb.build()).setEphemeral(true).queue();
					return;
				}
				
				PlayerManager.getInstance().getMusicManager(event.getGuild()).scheduler.queue.clear();
				PlayerManager.getInstance().getMusicManager(event.getGuild()).audioPlayer.stopTrack();
				
				EmbedBuilder eb = new EmbedBuilder();
				eb.setColor(RAINBOW.YELLOW.getColor());
				eb.setTimestamp(Instant.now());
				eb.setDescription("Le lecteur a �t� stopp�.");
				eb.setTitle("Lecteur stopp�");
				event.replyEmbeds(eb.build()).setEphemeral(true).queue();
			}
		};
	}

}
