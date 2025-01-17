package Telegram.Commands;

import Telegram.TelegramConstants;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.ICommandRegistry;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.logging.BotLogger;

/**
 * This command helps the user to find the command they need
 *
 * @author Timo Schulz (Mit0x2)
 */
public class HelpCommand extends BotCommand {

    private static final String LOGTAG = "HELPCOMMAND";

    private final ICommandRegistry commandRegistry;

    public HelpCommand(ICommandRegistry commandRegistry) {
        super("help", "Помощь");
        this.commandRegistry = commandRegistry;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        if (chat.getId().equals(TelegramConstants.getChatId())) {

            StringBuilder helpMessageBuilder = new StringBuilder("<b>Помощь</b>\n");
            helpMessageBuilder.append("Вы можете использовать следующие команды:\n\n");

            for (IBotCommand botCommand : commandRegistry.getRegisteredCommands()) {
                helpMessageBuilder.append(botCommand.toString()).append("\n\n");
            }

            SendMessage helpMessage = new SendMessage();
            helpMessage.setChatId(chat.getId().toString());
            helpMessage.enableHtml(true);
            helpMessage.setText(helpMessageBuilder.toString());

            try {
                absSender.execute(helpMessage);
            } catch (TelegramApiException e) {
                BotLogger.error(LOGTAG, e);
            }
        }
    }
}
