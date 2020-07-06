import org.telegram.telegrambots.ApiContextInitializer;

public class Main {
    public static void main(String[] args) {
        ApiContextInitializer.init();
        Bot moneyBot= new Bot();
        moneyBot.startBot();
    }
}
