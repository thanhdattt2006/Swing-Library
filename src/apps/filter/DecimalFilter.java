package apps.filter;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;


public class DecimalFilter extends DocumentFilter {

	@Override
    public void insertString(FilterBypass fb, int offset,
                             String text, AttributeSet attr)
            throws BadLocationException {

        if (text == null) return;

        Document doc = fb.getDocument();
        String oldText = doc.getText(0, doc.getLength());

        String newText = oldText.substring(0, offset)
                + text
                + oldText.substring(offset);

        if (newText.matches("\\d*(\\.\\d*)?")) {
            super.insertString(fb, offset, text, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length,
                        String text, AttributeSet attrs)
            throws BadLocationException {

        if (text == null) {
            super.replace(fb, offset, length, text, attrs); // cho phép xóa
            return;
        }

        Document doc = fb.getDocument();
        String oldText = doc.getText(0, doc.getLength());

        String newText = oldText.substring(0, offset)
                + text
                + oldText.substring(offset + length);

        if (newText.matches("\\d*(\\.\\d*)?")) {
            super.replace(fb, offset, length, text, attrs);
        }
    }
}

