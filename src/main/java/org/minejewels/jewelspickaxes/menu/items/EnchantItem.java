package org.minejewels.jewelspickaxes.menu.items;

import lombok.Data;
import org.minejewels.jewelspickaxes.enchant.Enchant;

@Data
public class EnchantItem {

    private final Enchant enchant;
    private final int amount, slot;

}
