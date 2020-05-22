package Telegram;

public class TelegramParams {

    final static long MAX_SIZE = 20971520;

    static String filesPath = "A:/Telegram/files/";
    static String audioPath = "A:/Telegram/audio/";


    public static String getAudioPath() {
        return audioPath;
    }

    public static boolean setAudioPath(String audioPath) {
        if(!audioPath.isEmpty() && audioPath.contains(":/") && audioPath.endsWith("/")){
            TelegramParams.audioPath = audioPath;
            return true;
        }
        return false;
    }

    public static String getFilesPath() {
        return filesPath;
    }

    public static boolean setFilesPath(String filesPath) {
        if(!filesPath.isEmpty() && filesPath.contains(":/") && filesPath.endsWith("/")){
            TelegramParams.filesPath = filesPath;
            return true;
        }
        return false;
    }

    public static long getMaxSize() {
        return MAX_SIZE;
    }


}
