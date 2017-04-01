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

    private static final String PATH_TO_PROPERTIES = "resources/config.properties";
    private static final String sFileName = "config.properties";
    private static String sDirSeparator = System.getProperty("file.separator");
    private static Properties projProperties;

    private static ProjectProperties instance;

    public static synchronized ProjectProperties getInstance() {
        if (instance == null) {
            instance = new ProjectProperties();
        }
        return instance;
    }

    public static Properties getProjProperties() {
        return projProperties;
    }

    public static void setProjProperties(Properties projProperties) {
        ProjectProperties.projProperties = projProperties;
    }

    private ProjectProperties() {
        projProperties = new Properties();
//        FileInputStream fileInputStream;
        
//      <editor-fold defaultstate="collapsed" desc="External file"> 
        // определяем текущий каталог
        File currentDir = new File(".");

        try {
            // определяем полный путь к файлу
            String sFilePath = currentDir.getCanonicalPath() + sDirSeparator + sFileName;

            // создаем поток для чтения из файла
            FileInputStream ins = new FileInputStream(sFilePath);
            // загружаем свойства
            projProperties.load(ins);
        } catch (IOException ex) {
            Logger.getLogger(ProjectProperties.class.getName()).log(Level.SEVERE, null, ex);
        } 
// </editor-fold>
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
        System.out.println("language: " + projProperties.getProperty("language")
        );
    }
}
