import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class DataParser {
    private static final Logger logger = LogManager.getLogger();

    public static String getCity(String text, HashMap<?, ?> data) {
        return getString(text, data);
    }

    public static String getCurrency(String text, HashMap<?, ?> data) {
        return getString(text, data);
    }

    public static String getLink(String text, HashMap<?, ?> data) {
        return getString(text, data);
    }

    private static String getString(String text, HashMap<?, ?> data) {
        for (HashMap.Entry<?, ?> entry : data.entrySet()) {
            Pattern pattern = Pattern.compile(entry.getKey().toString());
            Matcher matcher = pattern.matcher(text);
            if (matcher.find()) {
                return entry.getValue().toString();
            }
        }
        return null;
    }

    public static String getDate(String text) {
        Pattern pattern = Pattern.compile("[0-9]{2}\\.[0-9]{2}\\.[0-9]{4}");
        Matcher matcher = pattern.matcher(text);

        if (!matcher.find()) {
            return "0";
        }

        text = matcher.group();
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");

        try {
            Date date = formatter.parse(text);
            Date now = new Date();
            logger.debug("Date parsed");
            if (date.after(now)) {
                return "1";
            }
            return formatter.format(date);
        } catch (ParseException e) {
            logger.warn("Cant parse date" + e);
        }
        return null;
    }
}
