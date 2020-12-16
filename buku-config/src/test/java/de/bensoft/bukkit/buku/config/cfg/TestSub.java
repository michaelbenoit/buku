package de.bensoft.bukkit.buku.config.cfg;

import de.bensoft.bukkit.buku.config.api.ConfigurationClass;

/**
 * Created by CUSTDEV3 on 03/11/2020.
 */
@ConfigurationClass
public class TestSub {

    private String value;

    private SubType1 subType1;


    public SubType1 getSubType1() {
        return subType1;
    }

    public void setSubType1(SubType1 subType1) {
        this.subType1 = subType1;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public TestSub withValue(final String value) {
        this.value = value;
        return this;
    }
}
