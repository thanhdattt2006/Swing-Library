package apps.filter;

import javax.swing.text.*;

public class NumberOnlyFilter extends DocumentFilter {

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
            throws BadLocationException {

        if (text == null) {
            super.replace(fb, offset, length, text, attrs); // cho phép xóa
            return;
        }

        if (text.matches("\\d*")) {
            super.replace(fb, offset, length, text, attrs);
        }
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr)
            throws BadLocationException {

        if (text == null) return;

        if (text.matches("\\d+")) {
            super.insertString(fb, offset, text, attr);
        }
    }
}

