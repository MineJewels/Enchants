package org.minejewels.jewelspickaxes.menu.items;

import lombok.Data;
import net.abyssdev.abysslib.builders.ItemBuilder;
import org.minejewels.jewelspickaxes.enchant.Enchant;

@Data
public class PickaxeMenuItem {

    private final Enchant enchant;
    private final ItemBuilder item;
    private final int slot;
}
