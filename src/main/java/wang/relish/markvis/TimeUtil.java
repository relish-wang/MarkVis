package wang.relish.markvis;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author relish
 * @since 2017/08/29
 */
public final class TimeUtil {

    private TimeUtil(){
        throw new UnsupportedOperationException("can not instantiate a tool class.");
    }

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ENGLISH);

    static String longToString(long time){
        return DATE_FORMAT.format(new Date(time));
    }

}
