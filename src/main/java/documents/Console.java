package documents;

import lombok.Data;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;

public class Console extends JTextArea {
	public void print(String message) {
		this.setText(message);
	}

	public void outputStream(InputStream stream) {
		try {
			String output = "";
			int read;
			while((read = stream.read()) != -1) {
				output+=((char)read);
			}
			this.setText(output+"\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
