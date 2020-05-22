package Telegram.Commands;


import Telegram.TelegramParams;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.logging.BotLogger;


public class FilePathCommandCommand extends BotCommand {

    public static final String LOGTAG = "FILE_PATH_COMMAND";

    public FilePathCommandCommand() {
        super("file_path", "Установить путь, куда сохранять файлы");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        SendMessage answer = new SendMessage();
        answer.setChatId(chat.getId().toString());
        String ans;
        if(strings.length!=0) {
            if (TelegramParams.setFilesPath(strings[0]))
                ans = "Путь обновлен на: " + strings[0];
            else ans = "Путь указан неверно. Текущий путь: " + TelegramParams.getFilesPath();
        }
        else ans = "Текущий путь: " + TelegramParams.getFilesPath();
        answer.setText(ans);
        try {
            absSender.execute(answer);
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
    }
}