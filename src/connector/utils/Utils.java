package connector.utils;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JLabel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
// Класс с дополнительными функциями. Вынесены сюда, чтобы не засорять основной код.
public class Utils {

    private static final String SOUND_MSG = "resources/sounds/Blocked.wav";
     private static final URL soundURL = Utils.class.getResource(SOUND_MSG);
     // Фильтр для поля IP, на данный момент не используется.
    public class DocumentFilterForIP extends DocumentFilter {

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {

            StringBuffer buffer = new StringBuffer(string);
            for (int i = buffer.length() - 1; i >= 0; i--) {
                char ch = buffer.charAt(i);
                if (!Character.isDigit(ch) && ch != '-') {
                    buffer.deleteCharAt(i);
                }
            }
            string = buffer.toString();
            super.insertString(fb, offset, string, attr);
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attrs) throws BadLocationException {
            StringBuffer buffer = null;
            if (string != null) {
                buffer = new StringBuffer(string);
                //int num = 0;
                for (int i = buffer.length() - 1; i >= 0; i--) {

                    char ch = buffer.charAt(i);
                    if (!Character.isDigit(ch) && ch != '.') {
                        buffer.deleteCharAt(i);
                    }
                }
                string = buffer.toString();
            }
            super.replace(fb, offset, length, string, attrs);
        }
    }
    // Фильтр для поля порта, позволяет вводить только цифры.
    public class DocumentFilterForPort extends DocumentFilter {

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {

            StringBuffer buffer = new StringBuffer(string);
            for (int i = buffer.length() - 1; i >= 0; i--) {
                char ch = buffer.charAt(i);
                if (!Character.isDigit(ch)) {
                    buffer.deleteCharAt(i);
                }
            }
            string = buffer.toString();
            super.insertString(fb, offset, string, attr);
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attrs) throws BadLocationException {
            StringBuffer buffer = null;
            if (string != null) {
                buffer = new StringBuffer(string);
                for (int i = buffer.length() - 1; i >= 0; i--) {

                    char ch = buffer.charAt(i);
                    if (!Character.isDigit(ch)) {
                        buffer.deleteCharAt(i);
                    }
                }
                string = buffer.toString();
            }
            super.replace(fb, offset, length, string, attrs);
        }
    }
    // Воспроизводит звук.
    public static void PlaySound() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        // процедура проигрывающая один заранее заданный файл
        //AudioInputStream stream = AudioSystem.getAudioInputStream(new File("womp.wav")); // создаём аудио поток из файла
//        AudioInputStream stream = AudioSystem.getAudioInputStream(new File(SOUND_MSG)); // создаём аудио поток из файла
        AudioInputStream stream = AudioSystem.getAudioInputStream(soundURL);
        DataLine.Info info = new DataLine.Info(Clip.class, stream.getFormat()); // получаем информацию о звуке из потока
        Clip clip = (Clip) AudioSystem.getLine(info); // инициализируем проигрыватель
        clip.open(stream); // воспроизводим файл
        clip.start(); // закрываем проигрыватель
    }
    
    // Удаляет управляющие символы из строки.
    public static String removeTheTrash(String s) {
        char[] buf = new char[1024];
        int length = s.length();
        char[] oldChars = (length < 1024) ? buf : new char[length];
        s.getChars(0, length, oldChars, 0);
        int newLen = 0;
        for (int j = 0; j < length; j++) {
            char ch = oldChars[j];
            if (ch >= ' ') {
                oldChars[newLen] = ch;
                newLen++;
            }
        }
        if (newLen != length) {
            s = new String(oldChars, 0, newLen);
        }
        return s;
    }
    // На данный момент не используется.
    public class StatusBar extends JLabel {

        public StatusBar() {
            super();
            super.setPreferredSize(new Dimension(100, 16));
            setMessage("Ready");
        }

        public void setMessage(String message) {
            setText(" " + message);
        }

    }

}
