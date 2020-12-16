package de.bensoft.bukkit.buku.cmd.util.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CUSTDEV3 on 01/11/2020.
 */
public class CommandArguments {

    private final List<String> arguments = new ArrayList<>();

    public CommandArguments(final String input) {


        boolean inQuote = false;
        String currentArg = "";

        for (int i = 0; i < input.length(); i++) {

            final String c = input.substring(i, i + 1);
            if (i == input.length() - 1) {
                currentArg += c;
                arguments.add(currentArg);
                break;
            }

            if ((!inQuote && c.equals(" "))) {
                arguments.add(currentArg);
                currentArg = "";
                continue;
            }

            if (c.equals("\"") && !inQuote) {
                inQuote = true;
                continue;
            } else if (c.equals("\"") && inQuote) {
                inQuote = false;
                continue;
            }
            currentArg += c;
        }

    }

    public List<String> getArguments() {
        return arguments;
    }
}
