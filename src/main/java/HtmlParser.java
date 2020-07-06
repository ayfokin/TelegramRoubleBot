import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public abstract class HtmlParser {
    private static Document doc;
    private static final String[] currencies = {"usd", "eur", "gbp", "jpy", "cny"};
    private static final Logger logger = LogManager.getLogger();

    public static String getCbResult(String URL, String date) {
        int today = 0;
        date = date.replace(".", "-");
        StringBuilder result;
        URL = URL + "/" + date;

        try {
            doc = Jsoup.connect(URL).get();
            logger.debug("Connected successfully " + URL);
        } catch (IOException e) {
            URL = "https://bankiros.ru/currency/cbrf";
            logger.debug("Connection failed, try again with another link...");
            today = 1;
        }

        try {
            doc = Jsoup.connect(URL).get();
            logger.debug("Connected successfully " + URL);
        } catch (IOException e) {
            logger.error("Connection failed " + e);
        }

        Elements topics = doc.getElementsByTag("h1");
        if (topics.get(0).text().contains("сегодня") && today == 0) {
            result = new StringBuilder("К сожалению, введенной даты в базе данных цб рф не существует, " +
                    "поэтому держите " + topics.get(0).text().toLowerCase() + "\n\n");
            today = 1;
        } else {
            result = new StringBuilder(topics.get(0).text() + "\n\n");
        }

        for (String currency : currencies) {
            String id = "today_" + currency;
            Element now = doc.getElementById(id);
            Element elementOfCurrency = now.parent().parent();
            String value = elementOfCurrency.child(1).text();
            if (!value.equals("")) {
                String currencyName = elementOfCurrency.child(0).child(0).text();
                String[] currencyNames = currencyName.split(" ", 2);
                String unitCount = elementOfCurrency.child(2 + today).child(0).text();
                result.append(currencyNames[1]).append(": ");
                result.append(value).append(" RUB за ").append(unitCount).append("\n");
            }
        }
        return result.toString();
    }

    public static String getResult(String URL, String currency) {
        StringBuilder result;

        URL = getCurrencyURL(URL, currency);

        if (URL == null) {
            return Messages.noCurrencyInCity();
        }

        try {
            doc = Jsoup.connect(URL).get();
            logger.debug("Connected successfully " + URL);
        } catch (IOException e) {
            logger.error("Connection failed " + e);
        }

        Elements topics = doc.getElementsByTag("h2");
        Elements currencies = doc.getElementsByAttributeValueStarting("class", "td-center h");
        String amountOfCurrency = currencies.text();
        result = new StringBuilder(topics.get(0).text() + " (за " + amountOfCurrency + ")\n\n");

        Elements banks = doc.getElementsByAttributeValueStarting("class","productBank");
        int count = 0;

        for (Element bank : banks) {
            if (count == 5) {
                break;
            }
            String string = bank.text();
            String[] elements = string.split(" (?=[0-9])");
            result.append(elements[0]).append("\n");
            result.append("Покупка: ").append(elements[1]).append(" RUB\n");
            result.append("Продажа: ").append(elements[2]).append(" RUB\n\n");
            count++;
        }
        return result.toString();
    }

    private static String getCurrencyURL(String URL, String currency) {
        try {
            doc = Jsoup.connect(URL).get();
            logger.debug("Connected successfully " + URL);
        } catch (IOException e) {
            logger.error("Connection failed " + e);
        }
        if (isCurrencyExists(currency)) {
            return URL + '/' + currency;
        }
        return null;
    }

    private static boolean isCurrencyExists(String currency) {
        Elements elements = doc.getElementsByClass("currency currency-" + currency);
        return elements.size() != 0;
    }
}
