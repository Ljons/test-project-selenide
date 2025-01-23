package helpers;

import enums.WebEnvProperties;
import utils.Utils;

public class WebPropertiesHelper extends PropertyHelper
{

    public static void init()
    {
        new WebPropertiesHelper().setProperties();
    }

    private void setProperties()
    {
        super.init(WebEnvProperties.class, getPath());
    }

    public static String getPath()
    {
        String env;
        if (Utils.getPropertyEnv().equals(""))
            env = Utils.getWebEnv();
        else
            env = Utils.getEnv();
        return System.getProperty("user.dir") + "/../rms-taf/src/main/resources/" + env + ".properties";
    }
}
