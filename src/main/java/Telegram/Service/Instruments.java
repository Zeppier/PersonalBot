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
}
