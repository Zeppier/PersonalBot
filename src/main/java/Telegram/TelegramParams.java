package Telegram;

import java.io.File;

public class TelegramParams {

    final static long MAX_SIZE = 20971520;

    static String filesPath = "A:/Telegram/files/";
    static String audioPath = "A:/Telegram/audio/";
    static String currentDir = "C:/";
    static boolean showHidden = false;

    public static boolean isShowHidden() {
        return showHidden;
    }

    public static void setShowHidden(boolean showHidden) {
        TelegramParams.showHidden = showHidden;
    }

    public static String getAudioPath() {
        return audioPath;
    }

    public static boolean setAudioPath(String audioPath) {
        if (!audioPath.isEmpty() && new File(audioPath).exists() ) {
            TelegramParams.audioPath = audioPath;
            if(!TelegramParams.audioPath.endsWith("/"))
                TelegramParams.audioPath = TelegramParams.audioPath + "/";
            return true;
        } else if (new File(TelegramParams.audioPath + audioPath).exists()) {
            TelegramParams.audioPath = TelegramParams.audioPath+ audioPath;
            if(!TelegramParams.audioPath.endsWith("/"))
                TelegramParams.audioPath = TelegramParams.audioPath + "/";
            return true;
        }
        return false;
    }

    public static String getFilesPath() {
        return filesPath;
    }


    public static boolean setFilesPath(String filesPath) {

        if (!filesPath.isEmpty() && new File(filesPath).exists()) {
            TelegramParams.filesPath = filesPath;
            if(!TelegramParams.filesPath.endsWith("/"))
                TelegramParams.filesPath = TelegramParams.filesPath + "/";
            return true;
        } else if (new File(TelegramParams.filesPath + filesPath).exists()) {
            TelegramParams.filesPath = TelegramParams.filesPath + filesPath;
            if(!TelegramParams.filesPath.endsWith("/"))
                TelegramParams.filesPath = TelegramParams.filesPath + "/";
            return true;
        }
        return false;
    }

    public static String getCurrentDir() {
        return currentDir;
    }

    public static boolean setCurrentDir(String currentDir) {

        if (!currentDir.isEmpty() && new File(currentDir).exists()) {
            TelegramParams.currentDir = currentDir;
            if(!TelegramParams.currentDir.endsWith("/"))
                TelegramParams.currentDir = TelegramParams.currentDir + "/";
            return true;
        } else if (new File(TelegramParams.currentDir + currentDir).exists()) {
            TelegramParams.currentDir = TelegramParams.currentDir + currentDir;
            if(!TelegramParams.currentDir.endsWith("/"))
                TelegramParams.currentDir = TelegramParams.currentDir + "/";
            return true;
        }
        return false;
    }

    public static long getMaxSize() {
        return MAX_SIZE;
    }

}
