package Telegram.Commands;


import Telegram.TelegramConstants;
import Telegram.TelegramParams;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.logging.BotLogger;


public class FilePathCommand extends BotCommand {

    public static final String LOGTAG = "FILE_PATH_COMMAND";

    public FilePathCommand() {
        super("file_path", "Установить путь, куда сохранять файлы");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        if (chat.getId().equals(TelegramConstants.getChatId())) {
            SendMessage answer = new SendMessage();
            answer.setChatId(chat.getId().toString());
            String ans;
            if (strings.length != 0) {
                if (TelegramParams.setFilesPath(String.join(" ", strings)))
                    ans = "Путь обновлен на: " + TelegramParams.getFilesPath();
                else ans = "Путь указан неверно. Текущий путь: " + TelegramParams.getFilesPath();
            } else ans = "Текущий путь: " + TelegramParams.getFilesPath();
            answer.setText(ans);
            try {
                absSender.execute(answer);
            } catch (TelegramApiException e) {
                BotLogger.error(LOGTAG, e);
            }
        }
    }
}