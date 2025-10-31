package logger;

import java.io.IOException;
import java.util.logging.*;

public class LoggerUtil {
    
    private static boolean initialized = false; 
    
    public static final void setup(){

        Logger rootLogger = Logger.getLogger("");

        try {
            FileHandler fh = new FileHandler("manager.log", true);
            fh.setFormatter(new SimpleFormatter());
            fh.setLevel(Level.ALL);
            rootLogger.setLevel(Level.ALL);
            rootLogger.addHandler(fh);  
            initialized = true;
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        } 



    }
}
