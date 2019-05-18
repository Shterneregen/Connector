package connector.utils;

import connector.constant.Switch;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class ProjectProperties {

    private static final String LANGUAGE_FILE = "language_file";
    private static final String SERVER_ICON = "server_icon";
    private static final String CLIENT_ICON = "client_icon";
    private static final String BACKGROUND = "background";
    private static final String SERVER_NAME = "server_name";
    private static final String CLIENT_NAME = "client_name";
    private static final String SOUND_FILE = "sound_file";
    private static final String SOUND_SETTING = "sound_setting";
    private static final String POP_UP_SETTING = "pop_up_setting";

    private static final String S = System.getProperty("file.separator");
    private static final String CONFIG_FILE_NAME = "config.properties";
    private static final String DEFAULT_ICON = "java.png";

    private static final Logger LOG = Logger.getLogger(ProjectProperties.class.getName());

    private Properties properties;
    private static Properties langFile;
    private String LANGUAGE_FILE_NAME;
    private String PATH;

    private Boolean isOuterProperties;

    public String SERVER_NAME_SELECT;
    public String CLIENT_NAME_SELECT;

    public File SOUND_FILE_FILE;

    public Image SERVER_IMAGE;
    public Image CLIENT_IMAGE;
    public Image CLIENT_BACKGROUND = null;

    public Switch SOUND_SWITCH;
    public Switch POP_UP_SWITCH;

    private static ProjectProperties instance;

    public static synchronized ProjectProperties getInstance() {
        if (instance == null) {
            instance = new ProjectProperties();
        }
        return instance;
    }

    public static String getString(String str) {
        return langFile.getProperty(str);
    }

    private ProjectProperties() {
        this.isOuterProperties = false;
        properties = new Properties();
        langFile = new Properties();
        try {
            PATH = getCurrentDir();
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Cannot get current Dir");
        }

        try (FileInputStream propertiesStream = new FileInputStream(PATH + CONFIG_FILE_NAME)) {
            properties.load(propertiesStream);
            isOuterProperties = true;
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Cannot load outer properties");
        }

        // Если из внешних загрузить не получилось, берём проперти из jar
        if (!isOuterProperties) {
            try (InputStream source = getClass().getResourceAsStream("/" + CONFIG_FILE_NAME)) {
                properties.load(source);
            } catch (Exception ex) {
                LOG.log(Level.SEVERE, "Cannot load inner properties", ex);
            }
        }

        SERVER_NAME_SELECT = properties.getProperty(SERVER_NAME);
        CLIENT_NAME_SELECT = properties.getProperty(CLIENT_NAME);

        SOUND_SWITCH = properties.getProperty(SOUND_SETTING).toLowerCase().equals(Switch.ON.getMode())
                ? Switch.ON
                : Switch.OFF;
        POP_UP_SWITCH = properties.getProperty(POP_UP_SETTING).toLowerCase().equals(Switch.ON.getMode())
                ? Switch.ON
                : Switch.OFF;

        LANGUAGE_FILE_NAME = properties.getProperty(LANGUAGE_FILE);
        try {
            if (isOuterProperties) {
                SERVER_IMAGE = Toolkit.getDefaultToolkit().getImage(PATH + properties.getProperty(SERVER_ICON));
                CLIENT_IMAGE = Toolkit.getDefaultToolkit().getImage(PATH + properties.getProperty(CLIENT_ICON));
                CLIENT_BACKGROUND = Toolkit.getDefaultToolkit().getImage(PATH + properties.getProperty(BACKGROUND));
                SOUND_FILE_FILE = new File(PATH + properties.getProperty(SOUND_FILE));
                try (FileInputStream stringStream = new FileInputStream(PATH + LANGUAGE_FILE_NAME)) {
                    langFile.load(stringStream);
                } catch (IOException e) {
                    LOG.log(Level.SEVERE, "Cannot load outer resources", e);
                }
            } else {
                SERVER_IMAGE = ImageIO.read(getClass().getResourceAsStream("/images/" + DEFAULT_ICON));
                CLIENT_IMAGE = ImageIO.read(getClass().getResourceAsStream("/images/" + DEFAULT_ICON));
                CLIENT_BACKGROUND = ImageIO.read(getClass().getResourceAsStream("/images/" + properties.getProperty(BACKGROUND)));
                try (InputStream inputStream = getClass().getResourceAsStream("/" + LANGUAGE_FILE_NAME)) {
                    langFile.load(inputStream);
                } catch (Exception e) {
                    LOG.log(Level.SEVERE, "Cannot load inner resources", e);
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Exception while load resources", e);
        }
    }

    public Image buildBackground() {
        return Toolkit.getDefaultToolkit().getImage(PATH + properties.getProperty(BACKGROUND));
    }

    private String getCurrentDir() throws IOException {
        File currentDir = new File(".");
        return currentDir.getCanonicalPath() + S;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public Properties getLangFile() {
        return langFile;
    }
}
