package org.example;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;

public class Main {
    public static void main(String[] args) throws LoginException {
        JDA jda = JDABuilder.createLight(System.getenv("token"))
                .addEventListeners(new takeNote())
                .build();
    }
}