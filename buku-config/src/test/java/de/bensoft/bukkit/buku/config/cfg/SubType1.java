package de.bensoft.bukkit.buku.config.cfg;

import de.bensoft.bukkit.buku.config.api.ConfigurationClass;

/**
 * Created by CUSTDEV3 on 04/11/2020.
 */
@ConfigurationClass
public class SubType1 {

    private String testString;


    public SubType1 withTestString(final String s) {
        this.testString = s;
        return this;
    }

    public String getTestString() {
        return testString;
    }

    public void setTestString(String testString) {
        this.testString = testString;
    }
}
