import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class Bot extends TelegramLongPollingBot {
    private static final Logger logger = LogManager.getLogger();
    private static final String CITY_PATH = "cities.json";
    private static final String CURRENCY_PATH = "currencies.json";
    private static final String LINK_PATH = "links.json";
    private static final String CONFIG_PATH = "config.json";

    protected HashMap<?, ?> cities;
    protected HashMap<?, ?> currencies;
    protected HashMap<?, ?> links;
    protected HashMap <? ,?> config;

    @Override
    public String getBotUsername() {
        return DataParser.getString("USERNAME", config);
    }

    @Override
    public String getBotToken() {
        return DataParser.getString("TOKEN", config);
    }

    @Override
    public void onUpdateReceived(Update update) {
        logger.info("Update received");
        if (update == null) {
            return;
        }

        long chatId = update.getMessage().getChatId();
        if (update.getMessage().hasText()) {
            String text = update.getMessage().getText().toLowerCase();
            logger.debug("Received text: " + text);

            if (text.startsWith("/start") || text.startsWith("/help")) {
                sendMessage(chatId, Messages.helpInfo());
                logger.info("Send helpInfo message");
                return;
            }

            if (text.startsWith("/city")) {
                sendMessage(chatId, Messages.getCities(links));
                logger.info("Send list of cities message");
                return;
            }

            if (text.startsWith("/currency")) {
                sendMessage(chatId, Messages.getCurrencies());
                logger.info("Send list of currencies message");
                return;
            }

            String city = DataParser.getString(text, cities);
            logger.debug("Received city " + city);

            if (city == null) {
                sendMessage(chatId, Messages.noCity());
                logger.info("Send message noCity");
                return;
            }

            String link = DataParser.getString(city, links);
            logger.debug("Received link " + link);

            if (city.equals("Цб")) {
                String date = DataParser.getDate(text);
                logger.debug("Received date " + date);

                if (date == null) {
                    logger.warn("Date == null");
                    return;
                }
                if (date.equals("0")) {
                    sendMessage(chatId, Messages.dateError());
                    logger.info("Send dateError message");
                    return;
                }
                if (date.equals("1")) {
                    sendMessage(chatId, Messages.wrongDate());
                    logger.info("Send wrongDate message");
                    return;
                }
                String answer = HtmlParser.getCbResult(link, date);
                logger.debug("Received result message");
                sendMessage(chatId, answer);
                logger.info("Send answer");
                return;
            }

            String currency = DataParser.getString(text, currencies);
            logger.debug("Received currency " + currency);

            if (currency == null) {
                sendMessage(chatId, Messages.noCurrency());
                logger.info("Send noCurrency message");
                return;
            }

            String answer = HtmlParser.getResult(link, currency);
            logger.debug("Received result message");
            sendMessage(chatId, answer);
            logger.info("Send answer");
        }
        if (update.getMessage().isUserMessage()) {
            sendMessage(chatId, Messages.typeText());
            logger.info("Send typeText message");
        }
    }

    public void startBot() {
        TelegramBotsApi botsApi = new TelegramBotsApi();
        initializeData();
        logger.info("Data initialized");
        try {
            botsApi.registerBot(this);
            logger.info("Bot connected");
        } catch (TelegramApiRequestException e) {
            logger.error("Cant connect bot" + e);
        }
    }

    protected void initializeData() {
        try {
            cities = getMap(CITY_PATH);
        } catch (IOException e){
            logger.error("Failed initialized HashMap cities\nError message:", e);
        }
        try {
            currencies = getMap(CURRENCY_PATH);
        } catch (IOException e){
            logger.error("Failed initialized HashMap currencies\nError message:", e);
        }
        try {
            links = getMap(LINK_PATH);
        } catch (IOException e){
            logger.error("Failed initialized HashMap links\nError message:", e);
        }
        try {
            config = getMap(CONFIG_PATH);
        } catch (IOException e) {
            logger.error("Failed initialized HashMap config\nError message:", e);
        }
    }

    private HashMap <?, ?>getMap(String currentPath) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(currentPath);
        if (inputStream != null) {
            String result = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            return new Gson().fromJson(result, HashMap.class);
        }
        throw new IOException("input stream is null");
    }

    private void sendMessage(long chatId, String text) {
        try {
            execute(new SendMessage(chatId, text));
        }
        catch (TelegramApiException e) {
            logger.error("Cant send message" + e);
        }
    }
}
