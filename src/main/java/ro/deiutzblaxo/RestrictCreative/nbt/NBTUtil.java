package ro.deiutzblaxo.RestrictCreative.nbt;

import de.tr7zw.changeme.nbtapi.NBT;
import de.tr7zw.changeme.nbtapi.iface.ReadableItemNBT;
import org.bukkit.inventory.ItemStack;
import ro.deiutzblaxo.RestrictCreative.nbt.exception.AlreadyCreativeNBTException;

import java.util.function.Function;

public class NBTUtil {

    private static String KEY = "RestrictCreativeNBTKey";

    public static void setCreativeItem(ItemStack itemStack, String player) throws AlreadyCreativeNBTException {
        if (isCreativeItem(itemStack)) {
            throw new AlreadyCreativeNBTException();
        }
        NBT.modify(itemStack, readWriteItemNBT ->
        {
            readWriteItemNBT.setString(KEY, player);
        });

    }

    public static boolean isCreativeItem(ItemStack itemStack) {

        return NBT.get(itemStack, (Function<ReadableItemNBT, Boolean>) readableItemNBT -> readableItemNBT.hasTag(KEY));

    }

    public static void setKey(ItemStack item, String key, String value) {
        NBT.modify(item, readWriteItemNBT ->
        {
            readWriteItemNBT.setString(key, value);
        });
    }

    public static boolean isKey(ItemStack item, String key) {
        return NBT.get(item, (Function<ReadableItemNBT, Boolean>) readableItemNBT -> readableItemNBT.hasTag(key));
    }

    public static String getValue(ItemStack item, String key) {
        return NBT.get(item, (Function<ReadableItemNBT, String>) readableItemNBT -> readableItemNBT.getString(key));
    }

}
