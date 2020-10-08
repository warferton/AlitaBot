package com.alexkirillov.alitabot;

import com.alexkirillov.alitabot.models.client.Client;
import com.alexkirillov.alitabot.models.logging.MessageLog;

import com.mongodb.*;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

@SuppressWarnings("deprecation")
public class AlitaBot extends TelegramLongPollingBot {

    //        instantiate DB access
    MongoClient mongoClient = new MongoClient();
    DB database = mongoClient.getDB("Database");

    /**receives a users message
     * @param update received poll
     **/
    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            //logger setup
            String user_name = update.getMessage().getChat().getUserName();
            String user_first_name = update.getMessage().getChat().getFirstName();
            String user_last_name = update.getMessage().getChat().getLastName();
            long user_id = update.getMessage().getChat().getId();
            log(user_name, user_id, "",
                    checkIfUserExists(user_name,user_first_name,user_last_name),
                    user_first_name, user_last_name);


            //METHOD CONTROL
            switch (message) {
                case "/start":
                    log(user_name, user_id, message,
                            sendStartMsg(update.getMessage().getChatId()), user_first_name, user_last_name);
                    break;


                case "/pricelist":
                    log(user_name, user_id, message,
                            sendPriceList(update.getMessage().getChatId()), user_first_name, user_last_name);
                    break;
                //TODO contacts display (possibly will be scrapped)
                case "/contacts":
                    //  sendWorkers(update.getMessage().getChatId());
                    break;

                case "/address":
                    log(user_name, user_id, message,
                            sendAddress(update.getMessage().getChatId()), user_first_name, user_last_name);
                    break;
                case "/promotions":
                   log(user_name, user_id, message,
                           sendPromotionsList(update.getMessage().getChatId()), user_first_name, user_last_name);
                    break;

                default:
                    SendMessage unknown_command_message = new SendMessage()
                            .setChatId(update.getMessage().getChatId())
                            .setText("Unknown command");
                    try {
                        execute(unknown_command_message);
                        break;
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
            }
        }

    }

    private String checkIfUserExists(String user_name, String user_first_name, String user_last_name){
        DBCollection collection = database.getCollection("clients");
        long clients_found = collection.count(new BasicDBObject("username", user_name));
        //if user is new, generate new UUID and add him to the DataBase
        if(clients_found == 0){
            UUID local_user_id = UUID.randomUUID();
            Client new_client = new Client(user_name, user_first_name, user_last_name, local_user_id);
            collection.insert(new_client.toDBObject());
            return "New client profile created. Client id = " + local_user_id;
        }
        else{
            return "User Exists";
        }
    }

    private void log(String user_name, long user_id, String message_in, String response_code,
                     String user_first_name, String user_last_name){
        DBCollection collection = database.getCollection("messageLogs");
        collection.insert(
                new MessageLog(user_name, user_id, user_first_name,
                        user_last_name, message_in, response_code).toDBObject());
    }

    private synchronized String sendStartMsg(long chatId) {
        DBCollection collection = database.getCollection("misc");
        DBCursor cursor = collection.find(new BasicDBObject("name", "welcome message"));
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        String message = Objects.requireNonNull(cursor.one()).get("data").toString();
        sendMessage.setText(message.replace("/start", ""));
        try{
            execute(sendMessage);
            return "success";
        }catch (TelegramApiException e ) {
            return "Error occurred: " + Arrays.toString(e.getStackTrace());
        }
    }

    /**
     * Sends address, working hours, and Google Maps link to the client
     * message - actual address with name of the place and working hours included
     * google_maps_url - google maps link to the same place
     * @param chatId Id of a current chat session
     * @return void
     */
    private synchronized String sendAddress(long chatId){
        //address is stored in the 'misc' collection
        DBCollection collection = database.getCollection("misc");
        DBCursor cursor = collection.find(new BasicDBObject("name", "address"));
        String message = Objects.requireNonNull(cursor.one()).get("data").toString();
        String google_maps_url = Objects.requireNonNull(cursor.one()).get("url").toString();
        SendMessage sendMessage = new SendMessage()
                                        .setChatId(chatId)
                                        .setText(message+google_maps_url);
        try{
            execute(sendMessage);
            return "success";
        }catch (TelegramApiException e ){
            return "Error occurred: " + Arrays.toString(e.getStackTrace());
        }
    }
    //TODO N0T TESTED
    private synchronized String sendPriceList(long chatId){
        DBCollection collection = database.getCollection("services");
        DBCursor cursor = collection.find();
        String message = cursor.toString();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        try {
            execute(sendMessage);
            return "success";
        }catch (TelegramApiException e){
            return "Error occured: " + Arrays.toString(e.getStackTrace());
        }
    }
    //TODO N0T TESTED
    private synchronized String sendPromotionsList(long chatId){
        DBCollection collection = database.getCollection("promotions");
        DBCursor cursor = collection.find();
        String message= cursor.toString();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        try{
            execute(sendMessage);
            return "success";
        }catch(TelegramApiException e){
            return "Error occured: " + Arrays.toString(e.getStackTrace());
        }
    }



    /**BOT inners**/
    @Override
    public String getBotUsername() {
        return "Bot Name";
    }

    @Override
    public String getBotToken() {
        return "Bot Token";
    }

    //TODO Do I really need that ?
    public synchronized void setDeployButtons(SendMessage sendMessage){
        //keyboard mockup
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        //Button List
        List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();
        //btn 1
        KeyboardRow keyboardRowOne = new KeyboardRow();
        keyboardRowOne.add(new KeyboardButton("Start"));
        //btn 2
        KeyboardRow keyboardRowTwo = new KeyboardRow();
        keyboardRowTwo.add(new KeyboardButton("Prices"));
        //adding rows to the btn list
        keyboard.add(keyboardRowOne);
        keyboard.add(keyboardRowTwo);
        //setting a keyboard
        replyKeyboardMarkup.setKeyboard(keyboard);
    }
}
