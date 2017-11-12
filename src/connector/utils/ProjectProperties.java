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

    private static ProjectProperties instance;
//    private static final String PATH_TO_PROPERTIES = "resources/config.properties";
    private static final String CONFIG_FILE_NAME = "config.properties";
    private static final String DIR_SEPARATOR = System.getProperty("file.separator");
    private Properties projProperties;
    private Properties stringsFile;
    public static String LANGUAGE_FILE_NAME;

    public static synchronized ProjectProperties getInstance() {
        if (instance == null) {
            instance = new ProjectProperties();
        }
        return instance;
    }

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

    private ProjectProperties() {
        projProperties = new Properties();
        stringsFile = new Properties();

        //<editor-fold defaultstate="collapsed" desc="External file"> 
        // определяем текущий каталог
        File currentDir = new File(".");

        try {
            // определяем полный путь к файлу
            String sFilePath = currentDir.getCanonicalPath() + DIR_SEPARATOR + CONFIG_FILE_NAME;
            // создаем поток для чтения из файла
            FileInputStream ins = new FileInputStream(sFilePath);
            // загружаем свойства
            projProperties.load(ins);
            LANGUAGE_FILE_NAME = projProperties.getProperty("language_file");
        } catch (IOException ex) {
            Logger.getLogger(ProjectProperties.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ProjectProperties.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            String sFilePath = currentDir.getCanonicalPath() + DIR_SEPARATOR + LANGUAGE_FILE_NAME;
            FileInputStream ins = new FileInputStream(sFilePath);
            stringsFile.load(ins);
        } catch (IOException ex) {
            Logger.getLogger(ProjectProperties.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ProjectProperties.class.getName()).log(Level.SEVERE, null, ex);
        }
        //</editor-fold>

//        FileInputStream fileInputStream;
//        try {
//            //обращаемся к файлу и получаем данные
//            fileInputStream = new FileInputStream(PATH_TO_PROPERTIES);
//            projProperties.load(fileInputStream);
//
//            //печатаем полученные данные в консоль
//            System.out.println("language: " + projProperties.getProperty("language"));
//        } catch (IOException e) {
//            System.out.println("Ошибка в программе: файл " + PATH_TO_PROPERTIES + " не обнаружено");
//            e.printStackTrace();
//        }
    }
}
