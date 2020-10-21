package com.alexkirillov.alitabot.models.keyboards;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class ResponseKeyboards {
    public synchronized void setDeployChoiceButtonsMain(SendMessage sendMessage){
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
        keyboardRowOne.add(new KeyboardButton("Стрижки"));
        //btn 2
        KeyboardRow keyboardRowTwo = new KeyboardRow();
        keyboardRowTwo.add(new KeyboardButton("Ногти"));
        //btn 3
        KeyboardRow keyboardRowThree = new KeyboardRow();
        keyboardRowThree.add(new KeyboardButton("Брови&Ресницы"));
        //btn 4
        KeyboardRow keyboardRowFour = new KeyboardRow();
        keyboardRowFour.add(new KeyboardButton("Депиляция Воском"));
        //btn 5
        KeyboardRow keyboardRowFive = new KeyboardRow();
        keyboardRowFive.add(new KeyboardButton("Тату&Татуаж"));
        //btn 6
        KeyboardRow keyboardRowSix = new KeyboardRow();
        keyboardRowSix.add(new KeyboardButton("Макияж"));
        //btn 7
        KeyboardRow keyboardRowSeven = new KeyboardRow();
        keyboardRowSix.add(new KeyboardButton("Эстетическая Косметология"));
        //adding rows to the btn list
        keyboard.add(keyboardRowOne);
        keyboard.add(keyboardRowTwo);
        keyboard.add(keyboardRowThree);
        keyboard.add(keyboardRowFour);
        keyboard.add(keyboardRowFive);
        keyboard.add(keyboardRowSix);
        keyboard.add(keyboardRowSeven);
        //setting a keyboard
        replyKeyboardMarkup.setKeyboard(keyboard);
    }
    public synchronized void setDeployChoiceButtonsHairstyles(SendMessage sendMessage){
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
        keyboardRowOne.add(new KeyboardButton("Мужские"));
        //btn 2
        KeyboardRow keyboardRowTwo = new KeyboardRow();
        keyboardRowTwo.add(new KeyboardButton("Женские"));
        //adding rows to the btn list
        keyboard.add(keyboardRowOne);
        keyboard.add(keyboardRowTwo);
        //setting a keyboard
        replyKeyboardMarkup.setKeyboard(keyboard);
    }
    public synchronized void setDeployChoiceButtonsNails(SendMessage sendMessage){
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
        keyboardRowOne.add(new KeyboardButton("Маникюр"));
        //btn 2
        KeyboardRow keyboardRowTwo = new KeyboardRow();
        keyboardRowTwo.add(new KeyboardButton("Педикюр"));
        //btn 3
        KeyboardRow keyboardRowThree = new KeyboardRow();
        keyboardRowThree.add(new KeyboardButton("Дизайн Ногтей"));
        //btn 4
        KeyboardRow keyboardRowFour = new KeyboardRow();
        keyboardRowFour.add(new KeyboardButton("Наращивание Гелем"));
        //btn 5
        KeyboardRow keyboardRowFive = new KeyboardRow();
        keyboardRowFive.add(new KeyboardButton("Комплекс услуг со скидкой"));
        //adding rows to the btn list
        keyboard.add(keyboardRowOne);
        keyboard.add(keyboardRowTwo);
        keyboard.add(keyboardRowThree);
        keyboard.add(keyboardRowFour);
        keyboard.add(keyboardRowFive);
        //setting a keyboard
        replyKeyboardMarkup.setKeyboard(keyboard);
    }
    public synchronized void setDeployChoiceButtonsEyebrows(SendMessage sendMessage){
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
        keyboardRowOne.add(new KeyboardButton("Оформление Бровей"));
        //btn 2
        KeyboardRow keyboardRowTwo = new KeyboardRow();
        keyboardRowTwo.add(new KeyboardButton("Наращивание&Восстановление"));
        //adding rows to the btn list
        keyboard.add(keyboardRowOne);
        keyboard.add(keyboardRowTwo);
        //setting a keyboard
        replyKeyboardMarkup.setKeyboard(keyboard);
    }
    public synchronized void setDeployChoiceButtonsEstCosm(SendMessage sendMessage){
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
        keyboardRowOne.add(new KeyboardButton("Уход лица&Массажи"));
        //btn 2
        KeyboardRow keyboardRowTwo = new KeyboardRow();
        keyboardRowTwo.add(new KeyboardButton("Пилинги"));
        //btn 3
        KeyboardRow keyboardRowThree = new KeyboardRow();
        keyboardRowThree.add(new KeyboardButton("Аппаратные методики&Чистки"));
        //adding rows to the btn list
        keyboard.add(keyboardRowOne);
        keyboard.add(keyboardRowTwo);
        keyboard.add(keyboardRowThree);
        //setting a keyboard
        replyKeyboardMarkup.setKeyboard(keyboard);
    }

}
