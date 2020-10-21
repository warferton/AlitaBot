package com.alexkirillov.alitabot;

import com.alexkirillov.alitabot.models.client.Client;
import com.alexkirillov.alitabot.models.keyboards.ResponseKeyboards;
import com.alexkirillov.alitabot.models.logging.MessageLog;

import com.alexkirillov.alitabot.services.scheduling.WeekManager;
import com.mongodb.*;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
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
    DB database = mongoClient.getDB("AlitaDB");

    //        instantiate week manager
    WeekManager week_manager = new WeekManager();

    //        appointment registration setup vars
    boolean reservation_in_process = false;
    String service_employee;
    String service;
    //        keyboards setup
    private final ResponseKeyboards keyboard = new ResponseKeyboards();


    /**
     * receives a users message
     * @param update received poll
     **/
    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            //Retrieve user info
            String user_name = update.getMessage().getChat().getUserName();
            String user_first_name = update.getMessage().getChat().getFirstName();
            String user_last_name = update.getMessage().getChat().getLastName();
            long user_id = update.getMessage().getChat().getId();
            log(user_name, user_id, "",
                    checkIfUserExists(user_name,user_first_name,user_last_name),
                    user_first_name, user_last_name);

            //APPOINTMENT REGISTRY CONTROL
            if(reservation_in_process){
                keyboard.setDeployChoiceButtonsMain(new SendMessage());
            }

            //MAIN METHOD CONTROL
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

                case "/reserve":
                    log(user_name, user_id, message,
                            startReservation(update.getMessage().getChatId()), user_first_name, user_last_name);

                default:
                    sndMsg(update.getMessage().getChatId(), "Unknown command");
            }
        }

    }

    /**
     * Check if current user in the chat is already in the database,
     * if not - adds the user to the database.
     * @param user_name - users telegram name<br/>
     * @param user_first_name - users indicated first name<br/>
     * @param user_last_name - users indicated last name<br/>
     * @return String - status code
     */
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
     * @return String - status code
     */
    private synchronized String sendAddress(long chatId){
        //address is stored in the 'misc' collection
        DBCollection collection = database.getCollection("misc");
        DBCursor cursor = collection.find(new BasicDBObject("name", "address"));
        String message = Objects.requireNonNull(cursor.one()).get("data").toString();
        String google_maps_url = Objects.requireNonNull(cursor.one()).get("url").toString();
        return sndMsg(chatId, message+google_maps_url);
    }
    //TODO N0T TESTED
    private synchronized String sendPriceList(long chatId){
        DBCollection collection = database.getCollection("services");
        DBCursor cursor = collection.find();
        String message = cursor.toString();
        return sndMsg(chatId, message);
    }
    //TODO N0T TESTED
    private synchronized String sendPromotionsList(long chatId){
        DBCollection collection = database.getCollection("promotions");
        DBCursor cursor = collection.find();
        String message = cursor.toString();
        return sndMsg(chatId, message);
    }
//TODO improve and complete the architecture
    private synchronized String startReservation(long chatId){
        DBCollection collection = database.getCollection("services");
        DBCursor cursor = collection.find();
        List<String[]> service_list = new ArrayList<>();

        //begin reservation process
        reservation_in_process = true;

        //fill the service_list to output
        for(long i = collection.getCount();i <= 0; i--){
            service_list.add(new String[]{cursor.one().get("servicename").toString()
                                +" ("+ cursor.one().get("employees")+")\n"});
        }
        String message;
        message = "Выберите вид услуги:\n";
        return sndMsg(chatId, message+service_list.toString()+"'-' to quit the process anytime");

    }

    private String sndMsg(long chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        try {
            execute(sendMessage);
            return "success";
        } catch (TelegramApiException e) {
            return "Error occurred: " + Arrays.toString(e.getStackTrace());
        }
    }

    //BOT inners
    @Override
    public String getBotUsername() {
        return "Bot Name";
    }

    @Override
    public String getBotToken() {
        return "Bot Token";
    }

}
