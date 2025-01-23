package helpers;

import interfaces.StringMethods;
import utils.Logger;

import java.io.FileInputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public class PropertyHelper
{
    public static Logger logger;

    public <C extends Enum<C> & StringMethods> void init(Class<C> enumClass, String path)
    {
        logger = Logger.get(this.getClass());
        try {
            InputStream input = new FileInputStream(path);
            Properties prop = new Properties();
            prop.load(input);

            setSystemProperties(enumClass, prop);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOError(new Throwable("Unable to find property file with name " + path));
        }
    }

    private <C extends Enum<C> & StringMethods> void setSystemProperties(Class<C> enumClass, Properties prop)
    {
        for (C key : enumClass.getEnumConstants()) {
            Object obj = System.getProperty(key.toString());
            if (Objects.isNull(obj) || obj.toString().isEmpty()) {
                Object propValue = prop.get(key.getName());
                if (Objects.nonNull(propValue)) {
                    System.setProperty(key.toString(), propValue.toString());
                    Logger.log(key + " = " + propValue);
                } else {
                    System.setProperty(key.name(), key.getName());
                    Logger.log(key + " = " + key.getName());
                }
            } else {
                Logger.log(key + " = " + System.getProperty(key.toString()));
            }
        }
    }
}
