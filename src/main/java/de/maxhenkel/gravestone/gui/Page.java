package de.maxhenkel.gravestone.gui;

import de.maxhenkel.gravestone.DeathInfo.ItemInfo;
import de.maxhenkel.gravestone.util.Tools;
import net.minecraft.util.EnumChatFormatting;

import java.util.Arrays;

public class Page {

    private final ItemInfo[] items;
    private final GUIDeathItems gui;

    public Page(ItemInfo[] it, GUIDeathItems gui) {
        this.gui = gui;
        int arraySize = 10;
        items = new ItemInfo[10];
        if (it.length < 10) {
            arraySize = it.length;
        }
        for (int i = 0; i < items.length && i < arraySize; i++) {
            items[i] = it[i];
        }
    }

    public void drawPage(int num) {
        String title = Tools.translate("gui.deathinfo.title.items");

        int titleWidth = gui.getFontRenderer().getStringWidth(title);

        gui.getFontRenderer().drawString(EnumChatFormatting.BLACK + "" + EnumChatFormatting.UNDERLINE + title, (gui.width - titleWidth) / 2, 30, 0);


        String page = Tools.translate("gui.deathinfo.page");
        page = EnumChatFormatting.DARK_GRAY + page + " " + EnumChatFormatting.DARK_GRAY + num;
        int pageWidth = gui.getFontRenderer().getStringWidth(page);

        gui.getFontRenderer().drawString(page, (gui.width - pageWidth) / 2, 43, 0);

        int y = 60;
        final int space = 12;

        for (int i = 0; i < items.length; i++) {
            ItemInfo s = items[i];

            if (s == null) {
                continue;
            }

            String name = Tools.translateItem(s.getName(), s.getMeta());

            if (name == null) {
                continue;
            }

            gui.drawItem(EnumChatFormatting.ITALIC + name, y);
            gui.drawItemSize(String.valueOf(s.getStackSize()), y);

            y = y + space;
        }
    }

    @Override
    public String toString() {
        return Arrays.toString(items);
    }

}
