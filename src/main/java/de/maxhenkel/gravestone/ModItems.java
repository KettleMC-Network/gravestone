package de.maxhenkel.gravestone;

import de.maxhenkel.gravestone.items.ItemDeathInfo;
import de.maxhenkel.gravestone.items.ItemGraveStone;

public class ModItems {
    public static final ItemDeathInfo DEATH_INFO = new ItemDeathInfo();
    public static final ItemGraveStone GRAVESTONE = new ItemGraveStone(ModBlocks.GRAVESTONE);
}
