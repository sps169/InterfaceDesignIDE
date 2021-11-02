import com.formdev.flatlaf.FlatDarkLaf;
import documents.Document;
import view.IDEFrame;

import java.util.Arrays;

public class Main {
	public static void main (String[] args){
		FlatDarkLaf.setup();
		IDEFrame frame = new IDEFrame();
		frame.setVisible(true);
	}
}
