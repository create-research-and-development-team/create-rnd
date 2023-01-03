package com.klarkson.creaternd.api;

import com.klarkson.creaternd.content.config.ConfigHandler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

//TODO Figure out a stable way to fetch logs dynamically (file paths and use file reader w/ said file path) and convert to bin, and upload to server where the backend will handle converting it back into text and format the logs.
@Mod.EventBusSubscriber
public class logging {
    public static boolean logCollection (String Reason) throws IOException {
      if (ConfigHandler.LOGGING.logging.get()) {
          String BinSend;
          StringBuilder sb = new StringBuilder();
          char[] chars = Reason.toCharArray();
          for (char c : chars) {
              String binary = Integer.toBinaryString(c);
              String formatted = String.format("%8s", binary);
              String output = formatted.replaceAll(" ", "0");
              sb.append(output);
          }
          BinSend =  sb.toString();
        try {
            //TODO Make it check and grab user (client), server could cause issues with this
            if (Minecraft.getInstance().player == null) {
                System.out.println("Failed to send log information as the player is null and Minecraft probably not fully loaded");
                return false;
            }
            String apiConstructor = "https://api.obsidiancorestudios.com/crad/bugreport/?useragent=crnd&log="+BinSend+":logtext"+"&ign="+Minecraft.getInstance().player.getDisplayName().getString();
            System.out.println(apiConstructor);
            URL api = new URL(apiConstructor);
            //hey people who know the api, any way I could grab the logs path and put it up there? Thx - JQ
            URLConnection yc = api.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    yc.getInputStream()));
            in.close();
        } catch (NullPointerException ex) {
           return logCollectionAnnon(Reason);
        }
      } else {
          System.out.println("User has opted out of logs, request skipped.");
        return false;
      }
        return true;
    }
    //This is a fallback for the logging service if a log event occurs when the username is not available.
    private static boolean logCollectionAnnon (String Reason) {
        if (ConfigHandler.LOGGING.logging.get()) {
            String BinSend;
            StringBuilder sb = new StringBuilder();
            char[] chars = Reason.toCharArray();
            for (char c : chars) {
                String binary = Integer.toBinaryString(c);
                String formatted = String.format("%8s", binary);
                String output = formatted.replaceAll(" ", "0");
                sb.append(output);
            }
            BinSend =  sb.toString();
            try {
                //TODO Make it check and grab user (client), server could cause issues with this
                String apiConstructor = "https://api.obsidiancorestudios.com/crad/bugreport/?useragent=crnd&log="+ BinSend +":logtext"+"&ign=ANNON";
                System.out.println(apiConstructor);
                URL api = new URL(apiConstructor);
                //hey people who know the api, any way I could grab the logs path and put it up there? Thx - JQ
                URLConnection yc = api.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        yc.getInputStream()));
                in.close();
            } catch (Exception exp) {
                System.out.println("Failed to contact log endpoint with error: " + exp);
                return false;
            }
        } else {
            System.out.println("User has opted out of logs, request skipped.");
        }
        return true;
    }

/*
//below needs a valid event that doesn't call over and over again.
    @SubscribeEvent
    public static void onEvent(EntityJoinLevelEvent event) throws IOException {
        //Please note this is a testing command, real usage cases will look different.
           if (PREVENTREPEAT > 0) {
               //Working on a way to make this smoother so it won't inf loop.
               if(logCollection("CR&D has started.  Test")) {
                   PREVENTREPEAT --;
               } else {
                   System.out.println("Something went wrong, we'll try again in a moment.");
               }
           }
    }
    Was testing, this will be deleted soon.
 */
@SubscribeEvent
public static void handleGuiOpen(ScreenEvent.Opening screenOpenEvent) {
    try {
        logCollection("Create RND + Minecraft has started, this is a debug message.");
    } catch (IOException e) {
        e.printStackTrace();
    }
}
}


//Thanks to https://careydevelopment.us/blog/java-how-to-convert-a-string-to-binary for the binary conversion stuff!
