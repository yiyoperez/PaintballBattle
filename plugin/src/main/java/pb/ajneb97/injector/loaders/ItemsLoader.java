package pb.ajneb97.injector.loaders;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.Material;
import pb.ajneb97.commons.cache.Cache;
import pb.ajneb97.commons.loader.Loader;
import pb.ajneb97.core.logger.Logger;
import pb.ajneb97.structures.game.GameItem;
import pb.ajneb97.utils.item.ItemStackBuilder;
import team.unnamed.inject.Inject;
import team.unnamed.inject.Named;

import java.util.List;

public final class ItemsLoader implements Loader {

    @Inject
    @Named("items")
    private YamlDocument items;

    @Inject
    @Named("item-cache")
    private Cache<String, GameItem> cache;

    @Override
    public void load() {
        Logger.info("Attempting to load staff items.");
        for (String item : items.getRoutesAsStrings(false)) {
            // Ignore versioning option.
            if (item.equalsIgnoreCase("config-version")) continue;

            ItemStackBuilder itemBuilder = new ItemStackBuilder(Material.matchMaterial(getMaterial(item)))
                    .amount(1)
                    .name(getName(item))
                    .lore(getLore(item));

            GameItem staffItem = new GameItem(
                    getEnabled(item),
                    itemBuilder,
                    getSlot(item));

            cache.add(item, staffItem);
        }
    }

    private boolean getEnabled(String item) {
        return items.getBoolean(item + ".ENABLED");
    }

    private String getMaterial(String item) {
        return items.getString(item + ".MATERIAL");
    }

    private String getName(String item) {
        return items.getString(item + ".NAME");
    }

    private List<String> getLore(String item) {
        return items.getStringList(item + ".LORE");
    }

    private int getSlot(String item) {
        return items.getInt(item + ".SLOT");
    }

}
