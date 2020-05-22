package Telegram.Commands;


import Telegram.TelegramConstants;
import Telegram.TelegramParams;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.logging.BotLogger;

import java.util.ArrayList;
import java.util.List;


public class ShowHiddenCommand extends BotCommand {

    public static final String LOGTAG = "SHOW_COMMAND";

    public ShowHiddenCommand() {
        super("show", "Показывать скрытые файлы");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        if (chat.getId().equals(TelegramConstants.getChatId())) {
            SendMessage answer = new SendMessage();
            answer.setChatId(chat.getId().toString());
            String ans = "Показывать скрытые файлы? Сейчас файлы ";
            if (TelegramParams.isShowHidden())
                ans = ans + "показываются.";
            else ans = ans + "скрыты.";
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
            keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Да").setCallbackData("yes"));
            keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Нет").setCallbackData("no"));
            List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
            rowList.add(keyboardButtonsRow1);
            inlineKeyboardMarkup.setKeyboard(rowList);
            answer.setReplyMarkup(inlineKeyboardMarkup);
            answer.setText(ans);
            try {
                absSender.execute(answer);
            } catch (TelegramApiException e) {
                BotLogger.error(LOGTAG, e);
            }
        }
    }
}