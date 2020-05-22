package Telegram.Service;

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

}
