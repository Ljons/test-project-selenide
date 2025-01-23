package enums;

public enum EnvProperties {
    TESTRAIL_ENABLE_FLAG(""),
    TESTRAIL_RUN_ID(""),
    BUILD_NUMBER(""),
    CONTAINER_ENGINE("true"),
    CONTAINER_URL(""),
    SERVICES_ENV(""),
    WEB_ENV(""),
    TEST_ENV("qa");

    private String name;

    EnvProperties(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public String getValue()
    {
        return System.getProperty(this.toString());
    }

    public void setValue(String value)
    {
        System.setProperty(this.name(), value);
    }
}
