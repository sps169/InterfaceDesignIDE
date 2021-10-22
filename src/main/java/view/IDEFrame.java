package view;

import com.formdev.flatlaf.FlatDarkLaf;
import controler.IDEController;

import javax.swing.*;
import javax.swing.plaf.synth.SynthLookAndFeel;
import java.awt.*;
import java.io.IOException;
import java.net.URI;

public class IDEFrame extends JFrame {
	private IDEFrame self;
	private IDEController ideBrain;
	private JPanel mainPanel;

	private JMenuBar menuPanel;

	private JMenu contextMenuArchivo;
	private JMenuItem saveItem;
	private JMenuItem saveAsItem;
	private JMenuItem openItem;
	private JMenuItem newItem;
	private JMenuItem closeItem;

	private JMenu contextMenuEditar;

	private JMenu contextMenuAyuda;
	private JMenuItem aboutUs;
	private JMenuItem help;

	private JPanel textEditorPanel;
	private JTextArea textArea;

	private JTextArea consoleTextArea;
	public static final String ABOUT_US = "https://github.com/sps169/";
	public IDEFrame ()
	{
		this.self = this;
		this.initComponents();
		this.ideBrain = new IDEController();
		this.setVisible(true);
	}

	private void initComponents () {
		mainPanel = new JPanel(new BorderLayout(2, 2));
		//create menu
		menuPanel = new JMenuBar();

		contextMenuArchivo = new JMenu("Archivo");
		newItem = new JMenuItem("Nuevo");
		newItem.addActionListener(e -> {
			ideBrain.updateContent(textArea.getText());
			ideBrain.newDocument();
			textArea.setText(ideBrain.getContent());
		});
		contextMenuArchivo.add(newItem);
		openItem = new JMenuItem("Abrir");
		openItem.addActionListener(e -> {
			ideBrain.updateContent(textArea.getText());
			ideBrain.openDocument();
			textArea.setText(ideBrain.getContent());
		});
		contextMenuArchivo.add(openItem);
		saveItem = new JMenuItem("Guardar");
		saveItem.addActionListener(e -> {
			ideBrain.updateContent(textArea.getText());
			ideBrain.save();
		});
		contextMenuArchivo.add(saveItem);
		saveAsItem = new JMenuItem("Guardar como");
		saveAsItem.addActionListener(e -> {
			ideBrain.updateContent(textArea.getText());
			ideBrain.saveAs();
		});
		contextMenuArchivo.add(saveAsItem);
		closeItem = new JMenuItem("Cerrar");
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

		contextMenuEditar = new JMenu("Editar");
		contextMenuEditar.add(new JMenuItem("Copiar"));
		contextMenuEditar.add(new JMenuItem("Pegar"));
		contextMenuEditar.add(new JMenuItem("Cortar"));
		contextMenuEditar.add(new JMenuItem("Deshacer"));
		menuPanel.add(contextMenuEditar);

		contextMenuAyuda = new JMenu("Ayuda");
		aboutUs = new JMenuItem("Sobre Nosotros");
		aboutUs.addActionListener(e -> {
			try {
				Desktop.getDesktop().browse(URI.create(ABOUT_US));
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		});
		contextMenuAyuda.add(aboutUs);
		help = new JMenuItem("Ayuda...");
		help.addActionListener(e -> JOptionPane.showMessageDialog(null, "A llorar a la llorería"));
		contextMenuAyuda.add(help);
		menuPanel.add(contextMenuAyuda);

		//create textArea
		textEditorPanel = new JPanel(new GridLayout(1, 2));
		textArea = new JTextArea("");
		textEditorPanel.add(textArea);

		mainPanel.add(textEditorPanel, BorderLayout.CENTER);

		//create console
		consoleTextArea = new JTextArea("");
		consoleTextArea.setRows(5);
		mainPanel.add(consoleTextArea, BorderLayout.SOUTH);

		//frame settings
		this.add(mainPanel);
		this.setJMenuBar(menuPanel);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setPreferredSize(new Dimension(800, 600));
		this.pack();
	}

	public JMenuItem getSaveItem() {
		return saveItem;
	}
}
