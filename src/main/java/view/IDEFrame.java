package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class IDEFrame extends JFrame {
	public JPanel mainPanel;

	public JMenuBar menuPanel;
	public JMenu contextMenu;

	public JPanel textEditorPanel;
	public JTextArea textArea;

	public JTextArea consoleTextArea;

	public IDEFrame ()
	{
		this.initComponents();
		this.setVisible(true);
	}

	private void initComponents () {
		mainPanel = new JPanel(new BorderLayout(2, 2));
		//create menu
		menuPanel = new JMenuBar();
		contextMenu = new JMenu("Repito");
		contextMenu.add(new JMenuItem("REALIDAD"));
		contextMenu.add(new JMenuItem("FICCION"));
		menuPanel.add(contextMenu);

		//create textArea
		textEditorPanel = new JPanel(new GridLayout(1, 2));
		textArea = new JTextArea("Write here oni-chan");
		textEditorPanel.add(textArea);

		mainPanel.add(textEditorPanel, BorderLayout.CENTER);

		//create console
		consoleTextArea = new JTextArea("uwu consola");
		consoleTextArea.setRows(5);
		mainPanel.add(consoleTextArea, BorderLayout.SOUTH);

		//frame settings
		this.add(mainPanel);
		this.setJMenuBar(menuPanel);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setPreferredSize(new Dimension(800, 600));
		this.pack();
	}
}
