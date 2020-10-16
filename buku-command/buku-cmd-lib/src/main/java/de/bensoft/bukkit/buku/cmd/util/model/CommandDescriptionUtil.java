package de.bensoft.bukkit.buku.cmd.util.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CUSTDEV3 on 16/10/2020.
 */
public class CommandDescriptionUtil {


    public static List<BukuCommandDescription> flatten(final List<BukuCommandDescription> descriptionList) {
        final List<BukuCommandDescription> output = new ArrayList<>();
        descriptionList.stream().forEach(element -> doFlatten(element, output));
        return output;
    }

    private static void doFlatten(final BukuCommandDescription description,
                                  final List<BukuCommandDescription> descriptionList) {
        descriptionList.add(description);
        description.getChildren().forEach(c -> doFlatten(c, descriptionList));
    }
}
