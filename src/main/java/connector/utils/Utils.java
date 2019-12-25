package connector.utils;

import javax.sound.sampled.*;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Utils {

    private static final Logger LOG = Logger.getLogger(Utils.class.getName());

    private Utils() {
    }

    public static void playSound(File soundFile) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        AudioInputStream stream = AudioSystem.getAudioInputStream(soundFile);
        DataLine.Info info = new DataLine.Info(Clip.class, stream.getFormat()); // получаем информацию о звуке из потока
        Clip clip = (Clip) AudioSystem.getLine(info); // инициализируем проигрыватель
        clip.open(stream);
        clip.start();
    }

    public static String getCurrentTime() {
        long curTime = System.currentTimeMillis();
        return new SimpleDateFormat("kk:mm:ss").format(curTime);
    }

    public static String getCurrentDate() {
        long curTime = System.currentTimeMillis();
        return new SimpleDateFormat("dd.MM.yyyy").format(curTime);
    }

    /**
     * Проверяет, есть ли такой же ник в чате
     *
     * @param nic      ник для проверки
     * @param listNics список ников
     * @return true - есть в списке, false - нет в списке
     */
    public static boolean isNotUniqueNic(String nic, List<String> listNics) {
        return listNics.stream().anyMatch(n -> n.equals(nic));
    }

    public static void close(Closeable closable) {
        if (closable != null) {
            try {
                closable.close();
            } catch (IOException e) {
                LOG.log(Level.SEVERE, e.getMessage(), e);
            }
        }
    }

}
