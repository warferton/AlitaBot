package com.alexkirillov.alitabot;

import com.alexkirillov.alitabot.services.scheduling.WeekManager;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Main {
    public static void main(String[] x){
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();

        try{
            AlitaBot bot = new AlitaBot();
            telegramBotsApi.registerBot(bot);
            WeekManager week_manager = new WeekManager();
            week_manager.weekUpdate();
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        System.out.println("Bot Started");
    }
}
