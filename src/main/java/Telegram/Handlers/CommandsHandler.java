package Telegram.Handlers;


import Telegram.Commands.*;
import Telegram.Service.Emoji;
import Telegram.TelegramConstants;
import Telegram.TelegramParams;
import com.google.inject.internal.cglib.core.$CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Audio;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.logging.BotLogger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

import static Telegram.Service.Instruments.*;


public class CommandsHandler extends TelegramLongPollingCommandBot {
    public static final String LOGTAG = "COMMANDSHANDLER";

    public CommandsHandler(String botUsername) {
        super(botUsername);
        register(new FilePathCommand());
        register(new AudioPathCommand());
        register(new CdCommand());
        register(new ShowHiddenCommand());
        HelpCommand helpCommand = new HelpCommand(this);
        register(new HelpCommand(this));

        registerDefaultAction((absSender, message) -> {
            if (message.getChatId().equals(TelegramConstants.getChatId())) {
                sendMessage(message.getChatId(), "Мы не знаем команду '" + message.getText() + "'." + Emoji.FACE_WITH_STUCK_OUT_TONGUE_AND_TIGHTLY_CLOSED_EYES);
                helpCommand.execute(absSender, message.getFrom(), message.getChat(), new String[]{});
            } else sendMessage(message.getChatId(), "У вас нет доступа к данному боту.");
        });
    }



    @Override
    public void processNonCommandUpdate(Update update) {

        if (update.hasMessage() && TelegramConstants.getChatId().equals(update.getMessage().getChatId())) {

            Message message = update.getMessage();
            if (message.hasText()) {
                switch (message.getText()) {
                    case "Список файлов":
                        File dir = new File(TelegramParams.getCurrentDir());
                        StringBuilder answer = new StringBuilder();
                        if (dir.exists()) {
                            File[] files = dir.listFiles();
                            if (files != null) {
                                Arrays.sort(files, Comparator.comparing(File::isFile));
                                for (File direct : files) {
                                    if (!direct.isHidden() || TelegramParams.isShowHidden()) {
                                        if (direct.isDirectory()) {
                                            if (direct.list() != null)
                                                answer.append(Emoji.ENVELOPE).append(" ").append(direct.getName()).append(" - ").append(Objects.requireNonNull(direct.list()).length).append("\n");
                                        } else if (direct.isFile()) {
                                            answer.append(Emoji.PENCIL).append(" ").append(direct.getName()).append(" - ").append(longToSize(direct.length())).append("\n");
                                        }
                                    }
                                }
                                sendMessage(message.getChatId(), answer.toString());
                            }
                        }
                        break;
                    case "Старт":
                        MediaKeyStartPause();
                        break;
                    default:
                        if (Emoji.PLAY_PAUSE.toString().contains(message.getText())) {
                            MediaKeyStartPause();
                            sendMessage(message.getChatId(), "Сигнал отправлен.");
                        } else if (Emoji.NEXT.toString().contains(message.getText())) {
                            MediaKeyForward();
                            sendMessage(message.getChatId(), "Сигнал отправлен.");
                        } else if (Emoji.PREVIOUS.toString().contains(message.getText())) {
                            MediaKeyBack();
                            sendMessage(message.getChatId(), "Сигнал отправлен.");
                        } else {
                            File file;
                            if (message.getText().contains(":/")) {
                                file = new File(message.getText());
                            } else file = new File(TelegramParams.getCurrentDir() + message.getText());
                            if (file.exists()) {
                                try {
                                    sendDocUploadingAFile(message.getChatId(), file, "Вот ваш файл");
                                } catch (TelegramApiException e) {
                                    e.printStackTrace();
                                }
                            } else sendMessage(message.getChatId(), "Файл не найден.");
                        }
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
        }else if(update.hasCallbackQuery()) {
            if(update.getCallbackQuery().getData().equals("yes")) {
                TelegramParams.setShowHidden(true);
                sendMessage(update.getCallbackQuery().getMessage().getChatId(), "Скрытые файлы будут показываться.");
            }
            else if(update.getCallbackQuery().getData().equals("no")) {
                TelegramParams.setShowHidden(false);
                sendMessage(update.getCallbackQuery().getMessage().getChatId(), "Скрытые файлы показываться не будут.");
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
        if (chatId.equals(TelegramConstants.getChatId())) {
            if (!text.isEmpty()) {
                SendMessage sendMessage = new SendMessage();
                setButtons(sendMessage);
                sendMessage.setChatId(chatId);
                sendMessage.setText(text);
                try {
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    BotLogger.error(LOGTAG, e);
                }
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