package net.bytten.zosoko.util;

public class StdoutLogger implements ILogger {

    @Override
    public void log(String msg) {
        System.out.println(msg);
    }
    
}
