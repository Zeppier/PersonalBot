package Telegram.Handlers;


import Telegram.Commands.AudioPathCommand;
import Telegram.Commands.FilePathCommandCommand;
import Telegram.Commands.HelpCommand;
import Telegram.Service.Emoji;
import Telegram.TelegramConstants;
import Telegram.TelegramParams;
import org.apache.commons.io.FileUtils;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Audio;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.logging.BotLogger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static Telegram.Service.Instruments.replaceForbidenChars;


public class CommandsHandler extends TelegramLongPollingCommandBot {
    public static final String LOGTAG = "COMMANDSHANDLER";

    public CommandsHandler(String botUsername) {
        super(botUsername);
        register(new FilePathCommandCommand());
        register(new AudioPathCommand());
        HelpCommand helpCommand = new HelpCommand(this);
        register(new HelpCommand(this));

        registerDefaultAction((absSender, message) -> {
            sendMessage(message.getChatId(), "Мы не знаем команду '" + message.getText() + "'." + Emoji.FACE_WITH_STUCK_OUT_TONGUE_AND_TIGHTLY_CLOSED_EYES);
            helpCommand.execute(absSender, message.getFrom(), message.getChat(), new String[] {});
        });
    }



    @Override
    public void processNonCommandUpdate(Update update) {

        if (update.hasMessage() && TelegramConstants.getChatId().equals(update.getMessage().getChatId())) {
            Message message = update.getMessage();
            if (message.hasText()) {
                try {
                    sendDocUploadingAFile(message.getChatId(), new File(message.getText()), "Вот ваш файл");
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            else if (message.hasDocument()) {
                if (message.getDocument().getFileSize() < TelegramParams.getMaxSize()) {
                    String uploadedFileId = update.getMessage().getDocument().getFileId();
                    String filename = replaceForbidenChars(update.getMessage().getDocument().getFileName());
                    download(filename, uploadedFileId, TelegramParams.getFilesPath());
                } else sendMessage(message.getChatId(), "Файл слишком большой. Лимит 20 МБ");
            }
            else if (message.hasAudio()) {
                if (message.getAudio().getFileSize() < TelegramParams.getMaxSize()) {
                    Audio audio = update.getMessage().getAudio();
                    String uploadedFileId = audio.getFileId();
                    String filename = replaceForbidenChars(audio.getTitle()+"_"+audio.getPerformer()+".mp3");
                    download(filename, uploadedFileId, TelegramParams.getAudioPath());
                }
            }
        }
    }
    private void sendDocUploadingAFile(Long chatId, File save, String caption) throws TelegramApiException {
        SendDocument sendDocumentRequest = new SendDocument();
        sendDocumentRequest.setChatId(chatId);
        sendDocumentRequest.setDocument(save);
        sendDocumentRequest.setCaption(caption);
        execute(sendDocumentRequest);
    }
    private void sendMessage(Long chatId, String text) {
        if (!text.isEmpty()) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText(text);
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                BotLogger.error(LOGTAG, e);
            }
        }
    }

    private void download(String filename, String uploadedFileId, String path) {
        GetFile uploadedFile = new GetFile();
        uploadedFile.setFileId(uploadedFileId);
        try {
            String uploadedFilePath = execute(uploadedFile).getFilePath();
            File localFile = new File(path+filename);
            InputStream is = new URL("https://api.telegram.org/file/bot"+ getBotToken()+"/"+uploadedFilePath).openStream();
            FileUtils.copyInputStreamToFile(is, localFile);
        } catch (IOException | TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotToken() {
        return TelegramConstants.getTOKEN();
    }

}