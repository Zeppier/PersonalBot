package Telegram.Service;

import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class Instruments {
   public static String replaceForbidenChars(String string){
       if(string.contains("\"")) string = string.replace("\"", "");
       if(string.contains("<")) string = string.replace("<", "");
       if(string.contains(">")) string = string.replace(">", "");
       if(string.contains(":")) string = string.replace(":", "");
       if(string.contains("/")) string = string.replace("/", "");
       if(string.contains("\\")) string = string.replace("\\", "");
       if(string.contains("|")) string = string.replace("|", "");
       if(string.contains("?")) string = string.replace("?", "");
       if(string.contains("*")) string = string.replace("*", "");
       return string;
   }
    public static String longToSize(Long size){
       String answer = "";
       if(size < 1024)
           answer = size.toString() + " B";
       else if(size < 1048576){
           answer = String.format("%.2f",(double) size / 1024.) + " KB";
       } else if(size < 1073741824){
           answer = String.format("%.2f",(double) size / 1048576.) + " MB";
       } else if(size < 1099511627776.){
           answer = String.format("%.2f",(double) size / 1073741824.) + " GB";
       }
       return answer;
    }
    public static void MediaKeyForward(){
        GlobalScreen.postNativeEvent(new NativeKeyEvent(2401,0,176,57369,org.jnativehook.keyboard.NativeKeyEvent.CHAR_UNDEFINED));

    }
    public static void MediaKeyBack(){
        GlobalScreen.postNativeEvent(new NativeKeyEvent(2401,0,177,57360,org.jnativehook.keyboard.NativeKeyEvent.CHAR_UNDEFINED));

    }
    public static void MediaKeyStartPause(){
        GlobalScreen.postNativeEvent(new NativeKeyEvent(2401,0,179,57378,org.jnativehook.keyboard.NativeKeyEvent.CHAR_UNDEFINED));

    }
    public synchronized static void setButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton("Список файлов"));
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add(new KeyboardButton(Emoji.PREVIOUS.toString()));
        keyboardSecondRow.add(new KeyboardButton(Emoji.PLAY_PAUSE.toString()));
        keyboardSecondRow.add(new KeyboardButton(Emoji.NEXT.toString()));

        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        replyKeyboardMarkup.setKeyboard(keyboard);
    }
}
