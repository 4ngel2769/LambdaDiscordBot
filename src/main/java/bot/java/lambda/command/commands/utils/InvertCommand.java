package bot.java.lambda.command.commands.utils;

import bot.java.lambda.command.commandCategory.HelpCategory;
import bot.java.lambda.command.commandCategory.ImageUtil;
import bot.java.lambda.command.commandType.ImageUtilCommand;

public class InvertCommand implements ImageUtilCommand {
    @Override
    public ImageUtil getUtil() {
        return ImageUtil.INVERT;
    }

    @Override
    public String getName() {
        return "invert";
    }

    @Override
    public String getHelp(String prefix) {
        return "Inverts the supplied image";
    }

    @Override
    public HelpCategory getHelpCategory() {
        return HelpCategory.UTIL;
    }
}
