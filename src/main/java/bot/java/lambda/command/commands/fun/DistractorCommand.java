package bot.java.lambda.command.commands.fun;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.commandCategory.HelpCategory;
import bot.java.lambda.command.commandType.ICommand;

import java.util.Objects;

public class DistractorCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        ctx.getMessage().delete().queue();
        ctx.getChannel().sendMessage(Objects.requireNonNull(ctx.getJDA().getGuildById(755433534495391805L)).getEmotesByName("Distractor", true).get(0).getAsMention() + "").queue();
    }

    @Override
    public String getName() {
        return "distract";
    }

    @Override
    public String getHelp(String prefix) {
        return "Distracts Users";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.FUN;
    }
}
