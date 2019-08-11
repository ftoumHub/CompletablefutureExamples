package app.task;

import app.data.Base;
import app.util.Util;

public class Task {

    public static <T extends Base> T doTask(Long duration, T t){
        try{
            Thread.sleep(duration);
            Util.printThreadDiagnostics(duration, t.val);
            return t;
        } catch(InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}