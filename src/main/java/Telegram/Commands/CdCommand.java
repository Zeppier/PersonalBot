package Telegram.Commands;


import Telegram.TelegramParams;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.logging.BotLogger;


public class CdCommand extends BotCommand {

    public static final String LOGTAG = "CD_COMMAND";

    public CdCommand() {
        super("cd", "Текущая дериктория");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        SendMessage answer = new SendMessage();
        answer.setChatId(chat.getId().toString());
        String ans;
        if(strings.length!=0) {
                if (TelegramParams.setCurrentDir(String.join(" ", strings)))
                    ans = "Путь обновлен на: " + TelegramParams.getCurrentDir();
                else ans = "Путь указан неверно. Текущий путь: " + TelegramParams.getCurrentDir();
        }
        else ans = "Текущий путь: " + TelegramParams.getCurrentDir();
        answer.setText(ans);
        try {
            absSender.execute(answer);
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
    }
}