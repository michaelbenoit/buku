package de.bensoft.bukkit.buku.cmd;

import de.bensoft.bukkit.buku.cmd.util.CommandUtil;
import org.bukkit.entity.Player;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class BukuCommandTest {

    @Mock
    private Player player;

    @Test
    public void simpleSubCommandTest() {
        final List<AbstractBukuCommand> rootCommands = CommandUtil
                .getRootCommands("de.bensoft.bukkit.buku.cmd.test.simple");
        assertThat(rootCommands).isNotEmpty();

        final String[] args = new String[]{"test", "sub"};
        final boolean res = rootCommands.get(0).onCommand(player, null, "", args);
        assertThat(res).isTrue();
    }

}
