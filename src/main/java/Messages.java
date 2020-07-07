import java.util.HashMap;
import java.util.Map;

public abstract class Messages {
    public static String noCurrencyInCity() {
        return "В городе отсутствуют банки, принимающие данную валюту";
    }

    public static String helpInfo() {
        return "Я умею выводить курс валюты в различных регионах РФ, для этого в сообщении укажите город и валюту." +
                "\n\nНапример:\n\"Покажи курс доллара в Саратове\"\n\"Каков курс Саратовского доллара\"\n\n" +
                "Также я могу выводить курсы валют в цб за определенную дату, для этого в сообщении укажите \"цб\"" +
                " и дату в формате дд.мм.гггг\n\n Для запроса в группах введите:\n/<ваш запрос> @rouble_rate_bot";
    }

    public static String noCity() {
        return "Не могу найти город в вашем запросе.\n\n" +
                "Для получения списка всех поддерживаемых городов введите команду /city";
    }

    public static String noCurrency() {
        return "Не могу найти валюту в вашем запросе.\n\n" +
                "Для получения списка всех поддерживаемых валют введите команду /currency";
    }

    public static String dateError() {
        return "Беды с датой, пожалуйста, введите существующую дату в формате дд.мм.гггг";
    }

    public static String wrongDate() {
        return "К сожалению, введенная дата опережает текущее время";
    }

    public static String typeText() {
        return "Я умею воспринимать только текстовые сообщения";
    }

    public static String getCities(Map<?, ?> data) {
        StringBuilder result = new StringBuilder();
        for (HashMap.Entry<?, ?> entry : data.entrySet()) {
            if (!entry.getKey().equals("Цб")) {
                result.append(entry.getKey()).append("\n");
            }
        }
        return result.toString();
    }

    public static String getCurrencies() {
        return "Доллар (USD)\nЕвро (EUR)\nФунт стерлингов (GBP)\nЯпонская йена (JPY)\nКитайский юань (CNY)";
    }
}
