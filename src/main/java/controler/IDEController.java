package controler;

import documents.Console;
import documents.Document;
import javax.swing.*;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class IDEController {
	private Document currentDocument;
	private Console consoleController;
	private boolean isWindows;
	private String content;
	private UndoManager undoManager;

	public IDEController ()
	{
		this.isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
		this.consoleController = new Console();
		this.currentDocument = null;
		this.content = "";
		this.undoManager = new UndoManager();
		undoManager.setLimit(100);
	}

	public UndoManager getUndoManager() {
		return undoManager;
	}

	public Console getConsoleController() {
		return consoleController;
	}

	public void compile() {
		if(wantSave()) {
			save();
			ProcessBuilder pb = new ProcessBuilder();
			Process process;

			if(isWindows) {
				pb.command("cmd.exe", "/c", "javac", currentDocument.getFile().toFile().getAbsolutePath());
			}else{
				pb.command("sh", "-c", "javac", currentDocument.getFile().toFile().getAbsolutePath());
			}
			try {
				process = pb.start();
				this.consoleController.outputStream(process.getInputStream());
				if (process.getErrorStream().available() > 0) {
					this.consoleController.print("Something went wrong...");
				}
				while(process.isAlive()) {
					try {
						Thread.sleep(1);
					}catch(InterruptedException e) {
						e.printStackTrace();
					}
				}
			} catch (IOException e) {
				this.consoleController.print("Unable to run command");
			}
		}
	}

	public void execute () {
		if(wantSave()) {
			save();
			ProcessBuilder pb = new ProcessBuilder();
			Process process;

			if (isWindows) {
				pb.command("cmd.exe", "/c", "java", currentDocument.getFile().toFile().getAbsolutePath());
			} else {
				pb.command("sh", "-c", "java", currentDocument.getFile().toFile().getAbsolutePath());
			}
			try {
				process = pb.start();
				this.consoleController.outputStream(process.getInputStream());
				if (process.getErrorStream().available() > 0) {
					this.consoleController.print("Something went wrong...");
				}
				while (process.isAlive()) {
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			} catch (IOException e) {
				this.consoleController.print("Unable to run command");
			}
		}
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

	public String paste() {
		Transferable data = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
		try {
			if (data != null && data.isDataFlavorSupported(DataFlavor.stringFlavor)) {
				return data.getTransferData(DataFlavor.stringFlavor)+"";
			}
		}catch (UnsupportedFlavorException | IOException ex) {
			System.err.println("Wrong paste flavour");
		}
		return null;
	}

	public void copy(String toCopy) {
		StringSelection selection = new StringSelection(""+toCopy);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
	}

	public void undo() {
		if (undoManager.canUndo()) {
			undoManager.undo();
		}
	}

	public void redo() {
		if (undoManager.canRedo()) {
			undoManager.redo();
		}
	}
}
