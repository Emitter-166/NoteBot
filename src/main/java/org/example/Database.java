package org.example;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.NoSuchElementException;

public class Database extends ListenerAdapter {
    public static MongoCollection collection;

    @Override
    public void onReady(ReadyEvent e) {
        String uri = System.getenv("uri");
        MongoClientURI clientURI = new MongoClientURI(uri);
        MongoClient client = new MongoClient(clientURI);
        MongoDatabase database = client.getDatabase("personal");
        collection = database.getCollection("personal");

    }

//there will be special slot just to store all the reminder embed indexes

    public static void set(String Id, String field, String Key, String value, boolean isAdd) throws InterruptedException {
        updateDB(Id, field, Key, value, isAdd);
    }


    public static Document get(String Id, String field) {
        return (Document) collection.find(new Document(field, Id)).cursor().next();
    }



    private static void createDB(String Id) {
        //server config
        Document document = new Document("serverId", Id)
                .append("countingChannel", "none")
                .append("hasRewards", false)
                .append("beforeReward", 0)
                .append("admins", "0")
                .append("action", "0-message")
                .append("emoji", ":sparkles:");

        collection.insertOne(document);

    }

    private static void createNoteDB(String name) {
        //server config
        Document document = new Document("Note", name)
                .append("countingChannel", "none")
                .append("hasRewards", false)
                .append("beforeReward", 0)
                .append("admins", "0")
                .append("action", "0-message")
                .append("emoji", ":sparkles:");

        collection.insertOne(document);

    }

    private static void createUserDB(String userId) {
        //user config
        Document document = new Document("userId", userId)
                .append("notes", " ") // it will store all the note names I take, and will point to another document, format: author*userid-
                .append("reminders", "") //it will store note names of the note for reminding
                .append("archivedReminders", "") //it will store archived reminders, and will make sure if someone wants to make the same reminders again, they get notified
                .append("goals", "") //it will also have goal names and will point to another document
                .append("archived goals", "") //it will store all the archived notes, and will make sure if someone wants to make the same goal again, they get notified
                .append("routines", "") //routine names which will point to another document of this collection
                .append("bulkRoutines", "") //will store the routines I haven't completed
                .append("archivedRoutines", "") //all the routines that have been done
                .append("limits", ""); //will have status names and will refer to another document


        collection.insertOne(document);
    }


    private static void updateDB(String Id, String field, String key, String value, boolean isAdd) throws InterruptedException {
        //for server
        Document document = null;
        try {
            document = (Document) collection.find(new Document(field, Id)).cursor().next();
        } catch (NoSuchElementException exception) {
            if (field.equalsIgnoreCase("serverId")) {
                createDB(Id);
            } else if (field.equalsIgnoreCase("userId")) {
                createUserDB(Id);
            } else {
                Document customDocument = new Document(field, Id)
                        .append(key, value);
                collection.insertOne(customDocument);
                Thread.sleep(300);
            }
            document = (Document) collection.find(new Document(field, Id)).cursor().next();
            if (!isAdd) {
                Document Updatedocument = new Document(key, value);
                Bson updateKey = new Document("$set", Updatedocument);
                collection.updateOne(document, updateKey);
            } else {
                Document Updatedocument = new Document(key, document.get(key) + value);
                Bson updateKey = new Document("$set", Updatedocument);
                collection.updateOne(document, updateKey);
            }

        }

    }
}