package connector.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProjectProperties {

    private static final Logger LOG = Logger.getLogger(ProjectProperties.class.getName());

    private static final String LANGUAGE_FILE = "language_file";
    private static final String SERVER_ICON = "server_icon";
    private static final String CLIENT_ICON = "client_icon";
    private static final String BACKGROUND = "background";
    private static final String SERVER_NAME = "server_name";
    private static final String CLIENT_NAME = "client_name";
    private static final String SOUND_FILE = "sound_file";
    private static final String IS_SOUND_ON = "is_sound_on";
    private static final String IS_POP_UP_ON = "is_pop_up_on";

    private static final String S = System.getProperty("file.separator");
    private static final String CONFIG_FILE_NAME = "config.properties";

    private static Properties langFile;
    private static String currentDir;

    private static String serverTrayTitle;
    private static String clientTrayTitle;

    private static File soundFile;

    private static Image serverTrayImage;
    private static Image clientTrayImage;
    private static Image clientBackground = null;

    private static boolean soundOn;
    private static boolean popUpOn;

    private ProjectProperties() {
    }

    public static String getString(String str) {
        return langFile.getProperty(str);
    }

    public static void refreshProjectProperties() {
        Boolean isOuterProperties = false;
        Properties properties = new Properties();
        langFile = new Properties();
        try {
            currentDir = getCurrentDir();
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Cannot get current Dir");
        }

        try (FileInputStream propertiesStream = new FileInputStream(currentDir + CONFIG_FILE_NAME)) {
            properties.load(propertiesStream);
            isOuterProperties = true;
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Cannot load outer properties");
        }

        // Если из внешних загрузить не получилось, берём проперти из jar
        if (!isOuterProperties) {
            try (InputStream source = ProjectProperties.class.getResourceAsStream("/" + CONFIG_FILE_NAME)) {
                properties.load(source);
            } catch (Exception ex) {
                LOG.log(Level.SEVERE, "Cannot load inner properties", ex);
            }
        }

        serverTrayTitle = properties.getProperty(SERVER_NAME);
        clientTrayTitle = properties.getProperty(CLIENT_NAME);

        soundOn = Boolean.parseBoolean(properties.getProperty(IS_SOUND_ON).toLowerCase());
        popUpOn = Boolean.parseBoolean(properties.getProperty(IS_POP_UP_ON).toLowerCase());

        String languageFileName = properties.getProperty(LANGUAGE_FILE);
        try {
            if (isOuterProperties) {
                serverTrayImage = getImageFromCurrentDir(properties.getProperty(SERVER_ICON));
                clientTrayImage = getImageFromCurrentDir(properties.getProperty(SERVER_ICON));
                clientBackground = getImageFromCurrentDir(properties.getProperty(SERVER_ICON));
                soundFile = new File(currentDir + properties.getProperty(SOUND_FILE));
                try (FileInputStream stringStream = new FileInputStream(currentDir + languageFileName)) {
                    langFile.load(stringStream);
                } catch (IOException e) {
                    LOG.log(Level.SEVERE, "Cannot load outer resources", e);
                }
            } else {
                serverTrayImage = getImageFromJar(properties.getProperty(SERVER_ICON));
                clientTrayImage = getImageFromJar(properties.getProperty(CLIENT_ICON));
                clientBackground = getImageFromJar(properties.getProperty(BACKGROUND));
                try (InputStream inputStream = ProjectProperties.class.getResourceAsStream("/" + languageFileName)) {
                    langFile.load(inputStream);
                } catch (Exception e) {
                    LOG.log(Level.SEVERE, "Cannot load inner resources", e);
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Exception while load resources", e);
        }
    }

    private static Image getImageFromCurrentDir(String imageName) {
        return Toolkit.getDefaultToolkit().getImage(currentDir + imageName);
    }

    private static Image getImageFromJar(String imageName) throws IOException {
        return serverTrayImage = ImageIO.read(ProjectProperties.class.getResourceAsStream("/images/" + imageName));
    }

    private static String getCurrentDir() throws IOException {
        File currentDir = new File(".");
        return currentDir.getCanonicalPath() + S;
    }

    public static String getServerTrayTitle() {
        return serverTrayTitle;
    }

    public static String getClientTrayTitle() {
        return clientTrayTitle;
    }

    public static File getSoundFile() {
        return soundFile;
    }

    public static Image getServerTrayImage() {
        return serverTrayImage;
    }

    public static Image getClientTrayImage() {
        return clientTrayImage;
    }

    public static Image getClientBackground() {
        return clientBackground;
    }

    public static boolean isSoundOn() {
        return soundOn;
    }

    public static boolean isPopUpOn() {
        return popUpOn;
    }
}
