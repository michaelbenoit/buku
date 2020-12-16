package de.bensoft.bukkit.buku.config;

import de.bensoft.bukkit.buku.config.api.BukuConfigService;
import de.bensoft.bukkit.buku.config.cfg.SubType1;
import de.bensoft.bukkit.buku.config.cfg.TestConfig;
import de.bensoft.bukkit.buku.config.cfg.TestSub;
import org.junit.Test;

import java.io.File;

/**
 * Created by CUSTDEV3 on 03/11/2020.
 */
public class BukuConfigServiceTest {

    @Test
    public void test() throws Exception {

        final String relPath = getClass().getProtectionDomain().getCodeSource().getLocation().getFile();
        final File targetDir = new File(relPath + "../");

        final File file = new File(targetDir, "test.yaml");

        final TestConfig config = BukuConfigService.getConfig(
                file,
                TestConfig.class
        );

        config.setTestSub(new TestSub());
        config.getTestSub().setValue("bla1");

        config.setSimpleStringValue("blabla1");

        config.getSimpleStringList().add("test1");
        config.getSimpleStringList().add("test2");
        config.getSimpleStringList().add("test3");

        config.getTestSubList().add(new TestSub().withValue("aa"));
        config.getTestSubList().get(0).setSubType1(new SubType1().withTestString("test"));

    }
}
