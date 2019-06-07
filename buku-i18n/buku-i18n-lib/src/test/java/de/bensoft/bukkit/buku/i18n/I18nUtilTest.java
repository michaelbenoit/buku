package de.bensoft.bukkit.buku.i18n;

import de.bensoft.bukkit.buku.i18n.msg.TestMessage;
import org.bukkit.entity.Player;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import static org.assertj.core.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
public class I18nUtilTest {

    @Mock
    private Player player;

    @Test
    public void testTranslateMessageForPlayer() {
        Mockito.when(player.getLocale()).thenReturn("en");
        final String s = I18nUtil.translateMessage(player, TestMessage.TEST_MESSAGE_1);
        assertThat(s).isEqualTo("blabla");
    }

}
