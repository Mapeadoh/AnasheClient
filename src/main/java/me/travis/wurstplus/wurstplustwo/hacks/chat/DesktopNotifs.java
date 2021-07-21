package me.travis.wurstplus.wurstplustwo.hacks.chat;

import java.awt.Image;
import java.awt.AWTException;
import java.awt.Toolkit;
import java.awt.SystemTray;
import java.awt.TrayIcon;

import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;

public class DesktopNotifs extends WurstplusHack
{
    public DesktopNotifs() {
        super(WurstplusCategory.WURSTPLUS_MISC);
        this.name = "Desktop Notifs";
        this.tag = "Desktop Notifs";
        this.description = "ASDDDD";
    }

    @Override
    public void enable() {
        if (DesktopNotifs.mc.player != null) {
            sendNotification("hello", TrayIcon.MessageType.NONE);
        }
    }

    public static void sendNotification(final String message, final TrayIcon.MessageType messageType) {
        final SystemTray tray = SystemTray.getSystemTray();
        final Image image = Toolkit.getDefaultToolkit().createImage("images/background.png");
        final TrayIcon icon = new TrayIcon(image, "AnasheClient+");
        icon.setImageAutoSize(true);
        icon.setToolTip("Floppa");
        try {
            tray.add(icon);
        }
        catch (AWTException e) {
            e.printStackTrace();
        }
        icon.displayMessage("Anashe", message, messageType);
    }
}
