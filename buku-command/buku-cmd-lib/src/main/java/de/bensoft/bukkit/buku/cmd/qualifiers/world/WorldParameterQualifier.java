package de.bensoft.bukkit.buku.cmd.qualifiers.world;

import de.bensoft.bukkit.buku.cmd.api.qualifier.AbstractQualifier;
import de.bensoft.bukkit.buku.cmd.api.qualifier.QualifierException;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.text.MessageFormat;

/**
 * Created by CUSTDEV3 on 01/11/2020.
 */
@WorldParameter
public class WorldParameterQualifier extends AbstractQualifier<World> {

    @Override
    public World getValue(String input) throws QualifierException {
        if (input == null) {
            return null;
        }

        final World world = Bukkit.getWorld(input);
        if (world == null) {
            throw new QualifierException(
                    MessageFormat.format(
                            "No world found for name \"{0}\"",
                            input
                    )
            );
        }
        return world;
    }

}
