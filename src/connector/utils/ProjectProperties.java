/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connector.utils;

import java.io.File;
import java.io.FileInputStream;
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
    private static final String SERVER_NAME = "server_name";
    private static final String CLIENT_ICON = "client_icon";
    private static final String CLIENT_NAME = "client_name";

    private static final String CONFIG_FILE_NAME = "config.properties";
    private static final String DIR_SEPARATOR = System.getProperty("file.separator");
    private static ProjectProperties instance;
    private Properties projProperties;
    private Properties stringsFile;
    private static String LANGUAGE_FILE_NAME;

    public static String SERVER_NAME_SELECT;
    public static String SERVER_ICON_SELECT;
    public static String CLIENT_NAME_SELECT;
    public static String CLIENT_ICON_SELECT;

    public static synchronized ProjectProperties getInstance() {
        if (instance == null) {
            instance = new ProjectProperties();
        }
        return instance;
    }

    private ProjectProperties() {
        projProperties = new Properties();
        stringsFile = new Properties();

        //<editor-fold defaultstate="collapsed" desc="External file"> 
        // определяем текущий каталог
        File currentDir = new File(".");

        try {
            // определяем полный путь к файлу
            String propertiesPath = currentDir.getCanonicalPath() + DIR_SEPARATOR + CONFIG_FILE_NAME;
            // создаем поток для чтения из файла
            FileInputStream propertieStream = new FileInputStream(propertiesPath);
            projProperties.load(propertieStream);

            LANGUAGE_FILE_NAME = projProperties.getProperty(LANGUAGE_FILE);
            SERVER_NAME_SELECT = projProperties.getProperty(SERVER_NAME);
            SERVER_ICON_SELECT = projProperties.getProperty(SERVER_ICON);
            CLIENT_NAME_SELECT = projProperties.getProperty(CLIENT_NAME);
            CLIENT_ICON_SELECT = projProperties.getProperty(CLIENT_ICON);

            String languagePath = currentDir.getCanonicalPath() + DIR_SEPARATOR + LANGUAGE_FILE_NAME;
            FileInputStream stringStream = new FileInputStream(languagePath);
            stringsFile.load(stringStream);
        } catch (IOException ex) {
            Logger.getLogger(ProjectProperties.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ProjectProperties.class.getName()).log(Level.SEVERE, null, ex);
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Загрузка пропертей из jar">
//    private static final String PATH_TO_PROPERTIES = "resources/config.properties";
//        FileInputStream fileInputStream;
//        try {
//            //обращаемся к файлу и получаем данные
//            fileInputStream = new FileInputStream(PATH_TO_PROPERTIES);
//            projProperties.load(fileInputStream);
//        } catch (IOException e) {
//            System.out.println("Ошибка в программе: файл " + PATH_TO_PROPERTIES + " не обнаружено");
//            e.printStackTrace();
//        }        
        //</editor-fold>
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
