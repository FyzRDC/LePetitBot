package fr.fyz.lpb.commands.list;

import java.util.ArrayList;

import fr.fyz.lpb.commands.ITCommand;
import fr.fyz.lpb.pluralkit.PluralManager;
import fr.fyz.lpb.pluralkit.PluralMember;
import fr.fyz.lpb.pluralkit.PluralProfile;
import fr.fyz.lpb.pluralkit.PluralSystem;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class CommandPlural implements ITCommand {

	@Override
	public String getName() {
		return "plural";
	}

	@Override
	public String getDescription() {
		return "Permet d'utiliser et de configurer le module de Pluralit�";
	}

	@Override
	public OptionData[] getOptions() {
		return new OptionData[] { new OptionData(OptionType.STRING, "argument", "(system,member,format help)", true),
				new OptionData(OptionType.STRING, "command", "(create, name, delete, select, list)", true),
				new OptionData(OptionType.STRING, "name", "Nom du param�tre"),
				new OptionData(OptionType.STRING, "valeur", "Valeur utilisable dans certaines commandes") };
	}

	@Override
	public CommandRunnable getRunnable() {
		return new CommandRunnable() {

			@Override
			public void onCommand(SlashCommandInteractionEvent event) {

				String interacting = event.getOption("argument").getAsString();

				if (interacting.equalsIgnoreCase("system") || interacting.equalsIgnoreCase("member")
						|| interacting.equalsIgnoreCase("format")) {

					PluralProfile pp = PluralManager.getProfile(event.getMember().getId());

					if (pp == null) {
						PluralManager.saveProfile(event.getMember().getId(),
								new PluralProfile(new ArrayList<>(), null));
					}

					switch (event.getOption("command").getAsString()) {
					case "create":
						if (interacting.equalsIgnoreCase("system")) {
							if (event.getOption("name") == null) {
								event.reply("Erreur : Le nom doit �tre d�fini").setEphemeral(true).queue();
								return;
							}
							String name = event.getOption("name").getAsString();
							if (pp.getSystemByName(name) == null) {
								PluralSystem system = new PluralSystem(name, new ArrayList<>());
								pp.getSystems().add(system);
								if (pp.getSelected() == null) {
									pp.setSelected(system);
								}
								PluralManager.saveProfile(event.getMember().getId(), pp);
								event.reply("Le syst�me " + name + " a �t� cr�� !").setEphemeral(true).queue();
							} else {
								event.reply("Erreur : Le syst�me " + name + " existe d�j�").setEphemeral(true).queue();
							}
						} else if (interacting.equalsIgnoreCase("member")) {
							String name = event.getOption("name").getAsString();
							if (pp.getSelected() == null) {
								event.reply("Erreur : Aucun syst�me n'est s�lectionn�").setEphemeral(true).queue();
								return;
							}
							PluralSystem system = pp.getSelected();
							if (pp.getSelected().getMemberByName(name) == null) {
								PluralMember member = new PluralMember(name, new ArrayList<>(),
										event.getUser().getAvatarUrl());
								system.getMembers().add(member);
								pp.updateSystem(system);
								PluralManager.saveProfile(event.getUser().getId(), pp);
								event.reply(
										"Le membre " + name + " a �t� cr�� sur le syst�me " + system.getSystemName())
										.setEphemeral(true).queue();
							} else {
								event.reply("Erreur : Le membre " + name + " existe d�j�").setEphemeral(true).queue();
							}
						} else {
							String prefix = event.getOption("valeur").getAsString();
							if (pp.getSelected() == null) {
								event.reply("Erreur : Aucun syst�me n'est s�lectionn�").setEphemeral(true).queue();
								return;
							}
							PluralSystem system = pp.getSelected();
							
							if(event.getOption("valeur") == null) {
								event.reply("Erreur : La valeur n'est pas d�fini, elle doit correspondre au format � cr�er").setEphemeral(true).queue();
								return;
							}
							
							PluralMember member = system.getMemberByName(event.getOption("name").getAsString());
							if(member == null) {
								event.reply("Erreur : Le membre est invalide !").setEphemeral(true).queue();
								return;
							}
							if (!member.getPrefix().contains(prefix)) {
								if (system.getMemberByPrefix(prefix) == null) {
									if(prefix.contains("text")) {
										member.addPrefix(prefix);
										pp.updateSystem(system);
									} else {
										event.reply("Erreur : Le format doit obligatoirement contenir le mot \"text\", example : [text] prendra en compte les messages au format [message]").setEphemeral(true).queue();
									}
								} else {
									event.reply("Erreur : Le format est d�j� utilis� sur un autre membre du syst�me")
											.setEphemeral(true).queue();
								}
							} else {
								event.reply("Erreur : Le format est d�j� dans la liste.").setEphemeral(true).queue();
							}

						}
						break;
					case "name":
						if (interacting.equalsIgnoreCase("system")) {
							if (pp.getSelected() == null) {
								event.reply("Erreur : Aucun syst�me n'est s�lectionn�").setEphemeral(true).queue();
								return;
							}
							PluralSystem system = pp.getSelected();
							if (event.getOption("name") == null) {
								event.reply("Erreur : Le nom doit �tre d�fini").setEphemeral(true).queue();
								return;
							}
							String name = event.getOption("name").getAsString();
							if (pp.getSystemByName(name) != null) {
								event.reply("Erreur : Le nom de syst�me est d�j� pris").setEphemeral(true).queue();
								return;
							}
							system.setSystemName(name);
							pp.updateSystem(pp.getSelected(), system);
						} else {
							if (pp.getSelected() == null) {
								event.reply("Erreur : Aucun syst�me n'est s�lectionn�").setEphemeral(true).queue();
								return;
							}
							PluralSystem system = pp.getSelected();
							if (event.getOption("name") == null) {
								event.reply("Erreur : Le nom doit �tre d�fini").setEphemeral(true).queue();
								return;
							}
							String name = event.getOption("name").getAsString();
							if (system.getMemberByName(name) != null) {
								event.reply("Erreur : Le nom est d�j� pris dans le syst�me").setEphemeral(true).queue();
								return;
							}
							if (event.getOption("valeur") == null) {
								event.reply("Erreur : Vous devez sp�cifier l'ancien nom du membre du syst�me")
										.setEphemeral(true).queue();
								return;
							}
							if (system.getMemberByName(event.getOption("valeur").getAsString()) == null) {
								event.reply("Erreur : Le nom du membre est invalide").setEphemeral(true).queue();
								return;
							}
							system.getMemberByName(event.getOption("valeur").getAsString()).setName(name);
							pp.updateSystem(system);
						}

						break;
					case "delete":

						break;
					case "select":

						break;
					case "list":
						if (interacting.equalsIgnoreCase("system")) {

							String response = "Voici la liste des syst�mes :\n";

							for (PluralSystem system : pp.getSystems()) {
								response += "- " + system.getSystemName() + "\n";
							}

							event.reply(response).setEphemeral(true).queue();
						} else {

							String response = "Voici la liste des membres du syst�me s�lectionn� :\n";

							for (PluralMember member : pp.getSelected().getMembers()) {
								response += "- " + member.getName() + "\n";
							}

							event.reply(response).setEphemeral(true).queue();
						}
						break;
					default:
						event.reply("Erreur, commande introuvable ! /plural help pour plus d'informations")
								.setEphemeral(true).queue();
						break;
					}

				} else if (interacting.equalsIgnoreCase("help")) {

				} else {
					event.reply("Erreur, commande introuvable ! /plural help pour plus d'informations")
							.setEphemeral(true).queue();
				}
			}
		};
	}

}
