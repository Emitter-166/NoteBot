package org.example;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bson.Document;

import javax.xml.crypto.Data;
import java.util.Arrays;

public class takeNote extends ListenerAdapter {
    static EmbedBuilder note = new EmbedBuilder();
    static StringBuilder images = new StringBuilder();
    StringBuilder titleBuilder = null;
    StringBuilder authorBuilder = null;
    StringBuilder kqBuilder = null;
    StringBuilder notesBuilder = null;
    StringBuilder summaryBuilder = null;
    StringBuilder linkBuilder = null;

    @Override
    public void onMessageReceived(MessageReceivedEvent e){
        String args[] = e.getMessage().getContentRaw().split(" ");
            //all the more commands
            if(args[0].equalsIgnoreCase("n")){
                switch (args[1]) {
                    case "h":
                        //help
                        e.getChannel().sendMessage(
                                "`t` **topic** \n" +
                                        "`q` **keyword/questions** \n" +
                                        "`n` **notes** \n" +
                                        "`s` **summary** \n" +
                                        "`a` **attachment** \n" +
                                        "`l` ** attachment link** \n" +
                                        "`i` **sets id of the note**" +
                                        "`./` **to show the file** \n" +
                                        "`c` **to clear current note** \n" +
                                        "`notes` **to see all the notes**").queue();
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                        e.getMessage().delete().queue();
                        break;

                    case "i":
                        //setting id
                        authorBuilder = new StringBuilder();
                        for (int i = 2; i < args.length; i++) {
                            authorBuilder.append(args[i] + " ");
                        }
                        note.setAuthor(authorBuilder.toString());
                        e.getChannel().sendMessage("`id set!`").queue();
                        e.getMessage().delete().queue();
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                        e.getMessage().delete().queue();
                        break;
                    
                    case "t":
                        //setting up title
                        titleBuilder = new StringBuilder();
                        for (int i = 2; i < args.length; i++) {
                            titleBuilder.append(args[i] + " ");
                        }
                        note.setTitle(titleBuilder.toString());
                        e.getChannel().sendMessage("`title set!`").queue();
                        e.getMessage().delete().queue();
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                        e.getMessage().delete().queue();
                        break;

                    case "q":
                        //setting up keywords and questions
                        kqBuilder = new StringBuilder();
                        for (int i = 2; i < args.length; i++) {
                            kqBuilder.append(args[i] + " ");
                        }
                        note.addField("Questions/Keywords: ", String.format("```%s```", kqBuilder), false);
                        e.getChannel().sendMessage("`Keywords/Questions set!`").queue();
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                        e.getMessage().delete().queue();

                        break;

                    case "n":
                        //setting up notes
                        notesBuilder = new StringBuilder();
                        for (int i = 2; i < args.length; i++) {
                            notesBuilder.append(args[i]+ " ");
                        }
                        note.addField("Notes: ", String.format("```%s```", notesBuilder), false);
                        e.getChannel().sendMessage("`Notes set!`").queue();
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                        e.getMessage().delete().queue();

                        break;

                    case "s":
                        //setting up summary
                        summaryBuilder = new StringBuilder();
                        for (int i = 2; i < args.length; i++) {
                            summaryBuilder.append(args[i]+ " ");
                        }
                        note.addField("Summary: ", String.format("```%s```", summaryBuilder), false);
                        e.getChannel().sendMessage("`Summary set!`").queue();

                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                        e.getMessage().delete().queue();
                        break;

                    case "a":
                        //setting up attachment
                        //attachments
                        e.getMessage().getAttachments().forEach(attachment ->  images.append(attachment.getUrl() + " "));

                        //links
                        for (int i = 2; i < args.length; i++) {
                            images.append(args[i]+ " ");
                        }
                        e.getChannel().sendMessage("`Attachment set!`").queue();
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                        e.getMessage().delete().queue();
                        break;

                    case "l":
                        //setting up links
                        linkBuilder = new StringBuilder();
                        for (int i = 2; i < args.length; i++) {
                            linkBuilder.append(args[i]+ " ");
                        }
                        note.addField("Links:", linkBuilder.toString(), true);
                        e.getChannel().sendMessage("`Link set!`").queue();
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                        e.getMessage().delete().queue();
                        break;

                    case "./":
                        //showing the note
                        try{
                            e.getChannel().sendMessageEmbeds(note.build()).queue();
                            try{
                                e.getChannel().sendMessage(images.toString()).queue();
                            }catch(Exception exception){}
                        }catch (IllegalStateException exception){
                            e.getChannel().sendMessage("h").queue();
                        }
                        e.getMessage().delete().queue();
                        try {
                            String noteId =  String.format("%s_%s-",authorBuilder.toString(), e.getAuthor().getId()).replace(" ", "");
                            String notesId = noteId.replace("-", "");
                            Database.set(e.getAuthor().getId(),"userId","notes", noteId, true );
                            Database.set(notesId, "noteId", "title", titleBuilder.toString(), false);
                            Database.set(notesId, "noteId", "questions",kqBuilder.toString(), false);
                            Database.set(notesId, "noteId", "notes", notesBuilder.toString(), false);
                            Database.set(notesId, "noteId", "summary", summaryBuilder.toString(), false);
                            Database.set(notesId, "noteId", "attachments", images.toString(), false);
                            Database.set(notesId, "noteId", "links", linkBuilder.toString(), false);


                        } catch (InterruptedException | NullPointerException exception) {
                        }
                        break;

                    case "c":
                        note.clear();
                        images.delete(0, images.length());
                        e.getChannel().sendMessage("`Note cleared!`").queue();
                        
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                        e.getMessage().delete().queue();
                        break;

                    case "notes":
                        Document userDoc = Database.get(e.getAuthor().getId(), "userId");
                        StringBuilder notesString = new StringBuilder();
                        String userNotes[] =  userDoc.get("notes").toString().split("-");
                        for(int i = 0; i < userNotes.length; i++){
                            notesString.append(String.format("%s. `%s` \n", i + 1 ,userNotes[i].split("_")[0]));
                        }


                        EmbedBuilder notesBuilder = new EmbedBuilder()
                                .setTitle("Notes:")
                                .setDescription(notesString.toString());
                        e.getMessage().replyEmbeds(notesBuilder.build())
                                .mentionRepliedUser(false)
                                .queue();
                        e.getMessage().delete().queue();
                        break;
                }
            }



        try{
            if (args[1].equalsIgnoreCase("set!`") || args[1].equalsIgnoreCase("cleared!`")) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                try{
                    e.getMessage().delete().queue();
                }catch (Exception exception){}

            }else if (args.length == 24) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                e.getMessage().delete().queue();
            }
        }catch (IndexOutOfBoundsException exception){}

    }
}

