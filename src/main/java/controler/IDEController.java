package controler;

import documents.Document;
import javax.swing.*;

public class IDEController {
	private Document currentDocument;
	private String content;

	public IDEController ()
	{
		this.currentDocument = null;
		this.content = "";
	}

	public void newDocument ()
	{
		if (!this.content.equals("")) {
			if (JOptionPane.showConfirmDialog(null, "Do you want to save the current file?") == JOptionPane.YES_OPTION) {
				save();
				JOptionPane.showMessageDialog(null, "Saved");
			}else{
				JOptionPane.showMessageDialog(null, "Not saved");
			}
			this.currentDocument = null;
		}
		this.content = "";
	}

	public void save ()
	{
		if (!(this.content.equals("") || this.currentDocument == null)) {
			this.currentDocument.setContent(this.content);
			this.currentDocument.saveDocument();
		}else if (this.currentDocument == null){
			saveAs();
		}
	}

	public boolean saveAs() {
		JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
		boolean result = false;
		if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			this.currentDocument = new Document(chooser.getSelectedFile().getAbsolutePath());
			this.currentDocument.setContent(this.content);
			result = this.currentDocument.saveDocument();
		}
		return result;
	}

	public boolean wantSave () {
		return JOptionPane.showConfirmDialog(null, "Do you want to save current file?") == 0;
	}
	public void openDocument ()
	{
		if (!this.content.equals("")){
			if (wantSave()) {
				this.save();
			}
		}
		JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
		chooser.showOpenDialog(null);
		if (chooser.getSelectedFile().canRead()) {
			this.currentDocument = new Document(chooser.getSelectedFile().getAbsolutePath());
			this.currentDocument.readDocument();
			this.content = this.currentDocument.getContent();
		}else{
			System.err.println("File can't be read.");
		}
	}

	public void updateContent (String content) {
		this.content = content;
	}

	public String getContent () {
		return this.content;
	}
}
