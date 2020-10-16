package de.bensoft.bukkit.buku.cmd.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class QualifiedName extends ArrayList<String> {

    public QualifiedName(String[] input) {
        this.addAll(Arrays.asList(input));
    }

    public QualifiedName(String input) {
        this.addAll(Arrays.asList(input.split(" ")));
    }

    public QualifiedName(List<String> input) {
        this.addAll(input);
    }

    public QualifiedName skip(int amount) {
        return new QualifiedName(IntStream.range(amount, this.size())
                .mapToObj(this::get)
                .collect(Collectors.toList()));
    }

    @Override
    public String toString() {
        return stream().collect(Collectors.joining(" "));
    }
}
