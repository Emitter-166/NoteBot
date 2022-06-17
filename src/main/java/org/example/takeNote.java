package org.example;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class takeNote extends ListenerAdapter {
    static EmbedBuilder note = new EmbedBuilder();
    static StringBuilder images = new StringBuilder();

    @Override
    public void onMessageReceived(MessageReceivedEvent e){

        String args[] = e.getMessage().getContentRaw().split(" ");
                switch (args[0]) {
                    case "h":
                        //help
                        e.getChannel().sendMessage(
                         "`t` **topic** \n" +
                                "`q` **keyword/questions** \n" +
                                "`n` **notes** \n" +
                                "`s` **summary** \n" +
                                "`a` **attachment** \n" +
                                "`l` ** attachment link** \n" +
                                "`./` **to show the file** \n" +
                                 "`c` **to clear current note**").queue();
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                        e.getMessage().delete().queue();
                        break;

                    case "t":
                        //setting up title
                        StringBuilder titleBuilder = new StringBuilder();
                        for (int i = 1; i < args.length; i++) {
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
                        StringBuilder kqBuilder = new StringBuilder();
                        for (int i = 1; i < args.length; i++) {
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
                        StringBuilder notesBuilder = new StringBuilder();
                        for (int i = 1; i < args.length; i++) {
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
                        StringBuilder summaryBuilder = new StringBuilder();
                        for (int i = 1; i < args.length; i++) {
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
                        for (int i = 1; i < args.length; i++) {
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
                        StringBuilder linkBuilder = new StringBuilder();
                        for (int i = 1; i < args.length; i++) {
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

