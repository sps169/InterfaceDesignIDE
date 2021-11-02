package view;

import controler.IDEController;

import javax.swing.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;

public class IDEFrame extends JFrame {
	private final IDEFrame self;
	private final IDEController ideBrain;

	private JTextArea textArea;

	public static final String ABOUT_US = "https://github.com/sps169/";
	public static final String ICON_PATH = "src" + File.separator + "main" + File.separator + "resources" + File.separator + "images" + File.separator;
	public IDEFrame ()
	{
		this.self = this;
		this.ideBrain = new IDEController();
		this.initComponents();
		this.setVisible(true);
	}

	private void initComponents () {
		JPanel mainPanel = new JPanel(new BorderLayout(2, 2));
		//create menu
		JMenuBar menuPanel = new JMenuBar();

		JMenu contextMenuArchivo = new JMenu("Archivo");
		JMenuItem newItem = new JMenuItem("Nuevo");
		newItem.addActionListener(e -> {
			ideBrain.updateContent(textArea.getText());
			ideBrain.newDocument();
			textArea.setText(ideBrain.getContent());
		});
		contextMenuArchivo.add(newItem);
		JMenuItem openItem = new JMenuItem("Abrir");
		openItem.addActionListener(e -> {
			ideBrain.updateContent(textArea.getText());
			ideBrain.openDocument();
			textArea.setText(ideBrain.getContent());
		});
		contextMenuArchivo.add(openItem);
		JMenuItem saveItem = new JMenuItem("Guardar");
		saveItem.addActionListener(e -> {
			ideBrain.updateContent(textArea.getText());
			ideBrain.save();
		});
		contextMenuArchivo.add(saveItem);
		JMenuItem saveAsItem = new JMenuItem("Guardar como");
		saveAsItem.addActionListener(e -> {
			ideBrain.updateContent(textArea.getText());
			ideBrain.saveAs();
		});
		contextMenuArchivo.add(saveAsItem);
		JMenuItem closeItem = new JMenuItem("Cerrar");
		closeItem.addActionListener(e -> {
			if (!textArea.getText().equals(ideBrain.getContent())) {
				if (ideBrain.wantSave()){
					ideBrain.updateContent(textArea.getText());
					ideBrain.save();
				}
			}
			self.dispose();
		});
		contextMenuArchivo.add(closeItem);
		menuPanel.add(contextMenuArchivo);

		JMenu contextMenuEditar = new JMenu("Editar");
		JMenuItem copyButton = new JMenuItem("Copiar");
		copyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (textArea.getSelectedText() != null){
					ideBrain.copy(textArea.getSelectedText());
				}
			}
		});
		contextMenuEditar.add(copyButton);
		JMenuItem pasteButton = new JMenuItem("Pegar");
		pasteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String pasteContent = ideBrain.paste();
				if (pasteContent != null) {
					textArea.replaceSelection(pasteContent);
				}
			}
		});
		contextMenuEditar.add(pasteButton);
		JMenuItem cutButton = new JMenuItem("Cortar");
		cutButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(textArea.getSelectedText()!= null) {
					ideBrain.copy(textArea.getSelectedText());
					textArea.replaceSelection("");
				}
			}
		});
		contextMenuEditar.add(cutButton);
		JMenuItem undoButton = new JMenuItem("Deshacer");
		undoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ideBrain.undo();
			}
		});
		contextMenuEditar.add(undoButton);

		JMenuItem redoButton = new JMenuItem("Rehacer");
		redoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ideBrain.redo();
			}
		});
		contextMenuEditar.add(redoButton);
		menuPanel.add(contextMenuEditar);


		JMenu contextMenuAyuda = new JMenu("Ayuda");
		JMenuItem aboutUs = new JMenuItem("Sobre Nosotros");
		aboutUs.addActionListener(e -> {
			try {
				Desktop.getDesktop().browse(URI.create(ABOUT_US));
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		});
		contextMenuAyuda.add(aboutUs);
		JMenuItem help = new JMenuItem("Ayuda...");
		help.addActionListener(e -> JOptionPane.showMessageDialog(null, "A llorar a la llorería"));
		contextMenuAyuda.add(help);
		menuPanel.add(contextMenuAyuda);

		//create textArea
		JPanel textEditorPanel = new JPanel(new GridLayout(1, 2));
		textArea = new JTextArea("");
		textEditorPanel.add(textArea);
		textArea.getDocument().addUndoableEditListener(new UndoableEditListener() {
			@Override
			public void undoableEditHappened(UndoableEditEvent e) {
				textArea.getDocument().addUndoableEditListener(new UndoableEditListener(){
					public void undoableEditHappened(UndoableEditEvent evt) {
						ideBrain.getUndoManager().addEdit(evt.getEdit());
					}
				});
			}
		});

		mainPanel.add(textEditorPanel, BorderLayout.CENTER);

		//create console
		JTextArea consoleTextArea = ideBrain.getConsoleController();
		consoleTextArea.setRows(5);
		consoleTextArea.setEditable(false);
		mainPanel.add(consoleTextArea, BorderLayout.SOUTH);

		JPanel compilationPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton compiButton = new JButton();
		ImageIcon compiIcon = new ImageIcon(ICON_PATH + "hammer-pngrepo-com.png");
		compiButton.setIcon(compiIcon);
		compiButton.setSize(compiIcon.getIconWidth(), compiIcon.getIconHeight());
		compiButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ideBrain.execute();
			}
		});

		JButton runButton = new JButton();
		ImageIcon runIcon = new ImageIcon( ICON_PATH + "play-button-pngrepo-com.png");
		runButton.setIcon(runIcon);
		runButton.setSize(runIcon.getIconWidth(), runIcon.getIconHeight());
		runButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ideBrain.compile();
			}
		});
		compilationPanel.add(compiButton);
		compilationPanel.add(runButton);
		mainPanel.add(compilationPanel, BorderLayout.NORTH);

		//frame settings
		this.add(mainPanel);
		this.setJMenuBar(menuPanel);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setPreferredSize(new Dimension(800, 600));
		this.pack();
	}
}
