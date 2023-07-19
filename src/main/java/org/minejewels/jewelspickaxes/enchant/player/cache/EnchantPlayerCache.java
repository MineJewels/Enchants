package org.minejewels.jewelspickaxes.enchant.player.cache;

import net.abyssdev.me.lucko.helper.profiles.plugin.external.caffeine.cache.Cache;
import net.abyssdev.me.lucko.helper.profiles.plugin.external.caffeine.cache.Caffeine;
import org.bukkit.entity.Player;
import org.minejewels.jewelspickaxes.enchant.player.EnchantPlayer;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class EnchantPlayerCache {

    private final Cache<UUID, EnchantPlayer> playerCache;

    public EnchantPlayerCache() {
        this.playerCache = Caffeine.newBuilder()
                .expireAfterAccess(5L, TimeUnit.MINUTES).build();
    }

    public EnchantPlayer getEnchantPlayer(final Player player) {

        if (!this.playerCache.asMap().containsKey(player.getUniqueId())) {
            this.playerCache.put(player.getUniqueId(), new EnchantPlayer(player));
        }
        return this.playerCache.getIfPresent(player.getUniqueId());
    }
}
