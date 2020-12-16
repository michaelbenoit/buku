package de.bensoft.bukkit.buku.config.cfg;

import de.bensoft.bukkit.buku.config.api.ConfigurationClass;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CUSTDEV3 on 03/11/2020.
 */
@ConfigurationClass
public class TestConfig {

    private String simpleStringValue;
    private List<String> simpleStringList = new ArrayList<>();
    private TestSub testSub;
    private List<TestSub> testSubList = new ArrayList<>();

    public List<TestSub> getTestSubList() {
        return testSubList;
    }

    public void setTestSubList(List<TestSub> testSubList) {
        this.testSubList = testSubList;
    }

    public TestSub getTestSub() {
        return testSub;
    }

    public void setTestSub(TestSub testSub) {
        this.testSub = testSub;
    }

    public String getSimpleStringValue() {
        return simpleStringValue;
    }

    public void setSimpleStringValue(String simpleStringValue) {
        this.simpleStringValue = simpleStringValue;
    }

    public List<String> getSimpleStringList() {
        return simpleStringList;
    }

    public void setSimpleStringList(List<String> simpleStringList) {
        this.simpleStringList = simpleStringList;
    }
}
