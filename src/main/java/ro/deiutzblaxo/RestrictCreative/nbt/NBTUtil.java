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

}
