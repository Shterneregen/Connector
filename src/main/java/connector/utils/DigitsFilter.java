package connector.utils;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class DigitsFilter extends DocumentFilter {

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
            throws BadLocationException {

        StringBuilder buffer = new StringBuilder(string);
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
    public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attrs)
            throws BadLocationException {
        StringBuilder buffer;
        if (string != null) {
            buffer = new StringBuilder(string);
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
