/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connector.utils;

import connector.constant.Switch;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Класс для загрузки настройки программы, сделан синглтоном
 *
 * @author Yura
 */
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
    private static String PATH;

    private static final String PATH_TO_INNER_PROPERTIES = "resources/config.properties";
    private static final String CONFIG_FILE_NAME = "config.properties";
    private static final String DIR_SEPARATOR = System.getProperty("file.separator");
    private static ProjectProperties instance;
    private Properties projProperties;
    private Properties stringsFile;
    private static String LANGUAGE_FILE_NAME;

    public static String SERVER_NAME_SELECT;
    public static String CLIENT_NAME_SELECT;

    public static File SOUND_FILE_FILE;

    public static Image SERVER_IMAGE;
    public static Image CLIENT_IMAGE;
    public static Image CLIENT_BACKGROUND;

    public static Switch SOUND_SWITCH;
    public static Switch POP_UP_SWITCH;

//    private static final String CLIENT_BACKGROUND = "../resources/images/fon33.jpg";
    public static synchronized ProjectProperties getInstance() {
        if (instance == null) {
            instance = new ProjectProperties();
        }
        return instance;
    }

    private ProjectProperties() {
        projProperties = new Properties();
        stringsFile = new Properties();

        // определяем текущий каталог
        File currentDir = new File(".");
        FileInputStream propertieStream = null;
        FileInputStream stringStream = null;
        try {
            PATH = currentDir.getCanonicalPath() + DIR_SEPARATOR;
            // создаем поток для чтения из файла
            propertieStream = new FileInputStream(PATH + CONFIG_FILE_NAME);
            projProperties.load(propertieStream);

            LANGUAGE_FILE_NAME = projProperties.getProperty(LANGUAGE_FILE);

            SERVER_NAME_SELECT = projProperties.getProperty(SERVER_NAME);
            SERVER_IMAGE = Toolkit.getDefaultToolkit()
                    .getImage(PATH + projProperties.getProperty(SERVER_ICON));

            CLIENT_NAME_SELECT = projProperties.getProperty(CLIENT_NAME);
            CLIENT_IMAGE = Toolkit.getDefaultToolkit()
                    .getImage(PATH + projProperties.getProperty(CLIENT_ICON));

            CLIENT_BACKGROUND = Toolkit.getDefaultToolkit()
                    .getImage(PATH + projProperties.getProperty(BACKGROUND));

            SOUND_FILE_FILE = new File(PATH + projProperties.getProperty(SOUND_FILE));

            SOUND_SWITCH = projProperties.getProperty(SOUND_SETTING).toLowerCase().equals(Switch.ON.getMode())
                    ? Switch.ON
                    : Switch.OFF;

            POP_UP_SWITCH = projProperties.getProperty(POP_UP_SETTING).toLowerCase().equals(Switch.ON.getMode())
                    ? Switch.ON
                    : Switch.OFF;

            stringStream = new FileInputStream(PATH + LANGUAGE_FILE_NAME);
            stringsFile.load(stringStream);
        } catch (Exception ex) {
            /*Загрузка пропертей из jar*/
            try (FileInputStream fileInputStream = new FileInputStream(PATH_TO_INNER_PROPERTIES)) {
                projProperties.load(fileInputStream);
                Logger.getLogger(ProjectProperties.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FileNotFoundException ex1) {
                Logger.getLogger(ProjectProperties.class.getName()).log(Level.SEVERE, null, ex1);
            } catch (IOException ex1) {
                Logger.getLogger(ProjectProperties.class.getName()).log(Level.SEVERE, null, ex1);
            } finally {
                try {
                    propertieStream.close();
                } catch (IOException ex1) {
                    Logger.getLogger(ProjectProperties.class.getName())
                            .log(Level.SEVERE, "Cannot close propertieStream", ex1);
                }
                try {
                    stringStream.close();
                } catch (IOException ex1) {
                    Logger.getLogger(ProjectProperties.class.getName())
                            .log(Level.SEVERE, "Cannot close stringStream", ex1);
                }
            }
        }
    }

    //<editor-fold defaultstate="collapsed" desc="get-set">
    public Properties getProjProperties() {
        return projProperties;
    }

    public void setProjProperties(Properties projProperties) {
        this.projProperties = projProperties;
    }

    public Properties getStringsFile() {
        return stringsFile;
    }

    public void setStringsFile(Properties stringsFile) {
        this.stringsFile = stringsFile;
    }
    //</editor-fold>
}
