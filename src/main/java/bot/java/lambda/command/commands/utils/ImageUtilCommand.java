package bot.java.lambda.command.commands.utils;

import bot.java.lambda.command.CommandContext;
import bot.java.lambda.command.ICommand;
import bot.java.lambda.utils.Utils;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import okhttp3.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

enum ImageUtil {
    BLACK_AND_WHITE("b&w", "Black and White"),
    BLUR("blur", "Blurred "),
    DARKEN("darken", "Darkened"),
    INVERT("invert", "Inverted"),
    PIXELATE("pixelate", "Pixelated");

    private final String url;
    private final String done;

    ImageUtil(String url, String done) {
        this.url = url;
        this.done = done;
    }

    public String getUrl() {
        return url;
    }

    public String getDone() {
        return done;
    }
}

public interface ImageUtilCommand extends ICommand {
    @Override
    default void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final List<String> args = ctx.getArgs();

        if (args.isEmpty()) {
            channel.sendMessage("Please provide a URL").queue();
            return;
        }

        if (Utils.isNotUrl(args.get(0))) {
            final Message message = ctx.getMessage();
            if (message.getMentionedMembers().isEmpty()) {
                if (message.getEmotes().isEmpty()) {
                    channel.sendMessage("Provide the correct image url").queue();
                    return;
                }
                final String imageUrl = message.getEmotes().get(0).getImageUrl();
                try {
                    sendImage(channel, imageUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            final String effectiveAvatarUrl = message.getMentionedMembers().get(0).getUser().getEffectiveAvatarUrl();
            try {
                sendImage(channel, effectiveAvatarUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        try {
            sendImage(channel, args.get(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendImage(TextChannel channel, String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody req = new FormBody.Builder()
                .add("image", url)
                .build();

        Request request = new Request.Builder()
                .url("https://apis.duncte123.me/filters/" + this.getUtil().getUrl())
                .post(req)
                .build();

        Call call = client.newCall(request);
        try (Response res = call.execute()) {
            if (res.code() == 429) {
                channel.sendMessage("I've been  rate-limited.\nPlease try again later.").queue();
                return;
            }

            if (res.code() == 422) {
                channel.sendMessage("Image URL provided does not exists").queue();
                return;
            }

            assert res.body() != null;
            channel.sendMessage("Here's you " + this.getUtil().getDone() + " image")
                    .addFile(Objects.requireNonNull(res.body()).bytes(), "ResultImage.png").queue();
        }
    }

    ImageUtil getUtil();
}
