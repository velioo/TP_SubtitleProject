package org.elsys.subs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.text.ParseException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.border.MatteBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.MaskFormatter;

import org.eclipse.wb.swing.FocusTraversalOnArray;

public class GuiBase {

	public JFrame frame;
	private JTable subtitleTable;
	private JTextField subtitleNumTextField;
	private JTextField changeTextField;
	
	public GuiBase() {
		initialize();
	}

	@SuppressWarnings("unused")
	private void initialize() {
		
		frame = new JFrame();
		frame.setTitle("Subtitle Magic");
		frame.setBounds(100, 100, 1100, 700);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		// Panels
		JPanel tablePanel = new JPanel();
		tablePanel.setPreferredSize(new Dimension(1000, 450));
		tablePanel.setLayout(new BorderLayout());
		frame.getContentPane().add(tablePanel, BorderLayout.CENTER);
		
		JPanel othersPanel = new JPanel();
		othersPanel.setPreferredSize(new Dimension(200, 191));
		frame.getContentPane().add(othersPanel, BorderLayout.NORTH);
		othersPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel textPane = new JPanel();
		textPane.setPreferredSize(new Dimension(10, 100));
		othersPanel.add(textPane, BorderLayout.SOUTH);
		textPane.setLayout(new BorderLayout(0, 0));
		
		JTextArea subtitleArea = new JTextArea();
		subtitleArea.setFont(new Font("Arial", Font.PLAIN, 16));
		subtitleArea.setTabSize(4);
		subtitleArea.setBorder( new MatteBorder(3, 3, 3, 3, (Color) SystemColor.activeCaption));
		textPane.add(subtitleArea, BorderLayout.CENTER);
		
		JPanel buttonsPane = new JPanel();
		othersPanel.add(buttonsPane, BorderLayout.CENTER);
		buttonsPane.setLayout(null);
		//
		
		// Setup mask for the textfields
		MaskFormatter mask = null;
		try {
			mask = new MaskFormatter("##:##:##,###");
			mask.setPlaceholderCharacter('0');
		} catch (ParseException e) {
			e.printStackTrace();
		}

		JFormattedTextField startTextField = new JFormattedTextField(mask);
		startTextField.setToolTipText("Start time");
		startTextField.setBounds(87, 60, 90, 20);
		buttonsPane.add(startTextField);
		
		JFormattedTextField endTextField = new JFormattedTextField(mask);
		endTextField.setValue(new String("00:00:02,000"));
		endTextField.setToolTipText("End time");
		endTextField.setBounds(181, 60, 90, 20);
		buttonsPane.add(endTextField);
		
		JTextField durationTextField = new JTextField();
		durationTextField.setToolTipText("Duration");
		durationTextField.setBounds(275, 60, 90, 20);
		buttonsPane.add(durationTextField);
		
		subtitleNumTextField = new JTextField();
		subtitleNumTextField.setText("1");
		subtitleNumTextField.setEditable(false);
		subtitleNumTextField.setToolTipText("Subtitle number");
		subtitleNumTextField.setBounds(45, 60, 32, 20);
		buttonsPane.add(subtitleNumTextField);
		subtitleNumTextField.setColumns(10);
		
		// Run it 1 time to set duration textfield
			ListenerForStartEnd startListener = new ListenerForStartEnd(subtitleTable, startTextField, endTextField, durationTextField);
		//
		
		// Make the Table
		subtitleTable = new JTable() {
			
			private static final long serialVersionUID = 1L;

			@SuppressWarnings({ "unchecked", "rawtypes" })
			public Class getColumnClass(int column)
    		{
    			return getValueAt(0, column).getClass();
    		}

    		public Component prepareRenderer(
    			TableCellRenderer renderer, int row, int column)
    		{
    			Component c = super.prepareRenderer(renderer, row, column);
    			JComponent jc = (JComponent)c;

    			if (!isRowSelected(row))
    				c.setBackground(row % 2 == 0 ? getBackground() : new Color(206,255,231));
    			else
    				jc.setBorder(new MatteBorder(0, 0, 0, 0, Color.WHITE) );

    			return c;
    		}
			
		};
		
		TableProperties tableProperties = new TableProperties(subtitleTable);
		
		UIDefaults defaults = UIManager.getDefaults( );
		defaults.put("ScrollBar.thumb", defaults);
		defaults.put("ScrollBar.track", defaults); 
		
		JScrollPane scrollTablePane = new JScrollPane(subtitleTable);
		tablePanel.add(scrollTablePane, BorderLayout.CENTER);
		scrollTablePane.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{subtitleTable}));
		
		// Add table actionListener
		subtitleTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
	        public void valueChanged(ListSelectionEvent event) {
	        	if (!event.getValueIsAdjusting()){
	        		ListenerForTable tableListener = new ListenerForTable(subtitleArea, subtitleTable, subtitleNumTextField, startTextField, endTextField, durationTextField);
	        	}
	        }
	    });
		//
		
		// Adding subtitle to JTable	
			AddSubs addsubs = new AddSubs(subtitleArea, subtitleTable, subtitleNumTextField, startTextField, endTextField, durationTextField, scrollTablePane);
		//
			
		// Make the textfield which will contain the seconds by which will be moved forwards or backwards the subtitles
			
			changeTextField = new JTextField();
			changeTextField.setBounds(470, 60, 55, 20);
			changeTextField.setToolTipText("Seconds by which the marked subtitles will be moved");
			buttonsPane.add(changeTextField);
			changeTextField.setColumns(10);
		//
		
		// Make the forwards and backwards buttons
			JButton backwardsButton = new JButton("");
			JButton forwardsButton = new JButton("");
			
			ButtonsProperties buttons = new ButtonsProperties(backwardsButton, forwardsButton);
			
			backwardsButton.setToolTipText("Move the marked subtitles backwards");
			forwardsButton.setToolTipText("Move the marked subtitles forwards");
			
			backwardsButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ButtonsListener buttonsListener = new ButtonsListener(subtitleTable, changeTextField, "backwards");
				}
			});
			
			forwardsButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ButtonsListener buttonsListener = new ButtonsListener(subtitleTable, changeTextField, "forwards");
				}
			});
			
			buttonsPane.add(backwardsButton);
			buttonsPane.add(forwardsButton);
			
		//
			
		// Adding mouse listeners for buttons
			backwardsButton.addMouseListener(new java.awt.event.MouseAdapter() {
			    public void mouseEntered(java.awt.event.MouseEvent evt) {
			    	backwardsButton.setBorder(BorderFactory.createBevelBorder(0, Color.GREEN, Color.GREEN));
			    }

			    public void mouseExited(java.awt.event.MouseEvent evt) {
			    	backwardsButton.setBorder(BorderFactory.createEmptyBorder());
			    }
			});
			
			forwardsButton.addMouseListener(new java.awt.event.MouseAdapter() {
			    public void mouseEntered(java.awt.event.MouseEvent evt) {
			    	forwardsButton.setBorder(BorderFactory.createBevelBorder(0, Color.GREEN, Color.GREEN));
			    }

			    public void mouseExited(java.awt.event.MouseEvent evt) {
			    	forwardsButton.setBorder(BorderFactory.createEmptyBorder());
			    }
			});
		//
		
			
		// Add TabKey Listener for the subtitle Are
			SubtitleAreaTabKeyListener subAreaListener = new SubtitleAreaTabKeyListener(subtitleArea);
		//
			
		//Start, end, duration ActionListeners, FocusListeners, MouseListeners
			
			startTextField.addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent event) {
						ListenerForStartEnd startListener = new ListenerForStartEnd(subtitleTable, startTextField, endTextField, durationTextField);
					}
				}
			);
			
			startTextField.addFocusListener(
				new FocusListener() {

					@Override
					public void focusGained(FocusEvent e) {}

					@Override
					public void focusLost(FocusEvent arg0) {
						ListenerForStartEnd startListener = new ListenerForStartEnd(subtitleTable, startTextField, endTextField, durationTextField);
					}
					
				}
			);
			
			
			endTextField.addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent event) {
						ListenerForStartEnd endListener = new ListenerForStartEnd(subtitleTable ,startTextField, endTextField, durationTextField);
					}
				}
			);
			
			endTextField.addFocusListener(
					new FocusListener() {

						@Override
						public void focusGained(FocusEvent e) {}

						@Override
						public void focusLost(FocusEvent arg0) {
							ListenerForStartEnd startListener = new ListenerForStartEnd(subtitleTable, startTextField, endTextField, durationTextField);
						}
						
					}
				);
			
			durationTextField.addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent event) {
						ListenerForDuration durationListener = new ListenerForDuration(subtitleTable, startTextField, endTextField, durationTextField);
					}
				}
			);
			
			durationTextField.addFocusListener(
					new FocusListener() {

						@Override
						public void focusGained(FocusEvent e) {
							JTextField field = ((JTextField)e.getComponent());
							durationTextField.setCaretPosition(field.getDocument().getLength());
						}

						@Override
						public void focusLost(FocusEvent arg0) {
							ListenerForDuration durationListener = new ListenerForDuration(subtitleTable, startTextField, endTextField, durationTextField);
						}
						
					}
				);
			
		//
		
		// Make the Menubar
		JMenuBar menuBar = new JMenuBar();
		MenuBarProperties menuproperties = new MenuBarProperties(menuBar);

		frame.setJMenuBar(menuBar);
		//
		
		JMenu mnFile = new JMenu("  File  ");
		menuBar.add(mnFile);

		JMenuItem mntmNewSub = new JMenuItem("New Subtitles...");
		mnFile.add(mntmNewSub);

		// Open subtitles ActionListener
		JMenuItem mntmOpenSub = new JMenuItem("Open Subtitles...");
		mntmOpenSub.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					FileOpenRead fo = new FileOpenRead(subtitleTable);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		//
		
		mnFile.add(mntmOpenSub);

		JMenuItem mntmSave = new JMenuItem("Save Subtitles");
		mnFile.add(mntmSave);

		JMenuItem mntmSaveSubtitlesAs = new JMenuItem("Save Subtitles as...");
		mntmSaveSubtitlesAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				FileSaveClose fileSaveClose = new FileSaveClose(subtitleTable, startTextField, endTextField);
			}
		});
		mnFile.add(mntmSaveSubtitlesAs);

		JSeparator fileSeparator1 = new JSeparator();
		mnFile.add(fileSeparator1);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);

		JMenu mnEdit = new JMenu("  Edit  ");
		menuBar.add(mnEdit);

		JMenuItem mntmUndo = new JMenuItem("Undo");
		mnEdit.add(mntmUndo);

		JMenuItem mntmRedo = new JMenuItem("Redo");
		mnEdit.add(mntmRedo);

		JMenuItem mntmCutLines = new JMenuItem("Cut Lines");
		mnEdit.add(mntmCutLines);

		JMenuItem mntmCopyLines = new JMenuItem("Copy Lines");
		mnEdit.add(mntmCopyLines);

		JMenuItem mntmPasteLines = new JMenuItem("Paste Lines");
		mnEdit.add(mntmPasteLines);

		JSeparator editSeparator1 = new JSeparator();
		mnEdit.add(editSeparator1);

		JMenuItem mntmFind = new JMenuItem("Find...");
		mnEdit.add(mntmFind);

		JMenuItem mntmFindAndReplace = new JMenuItem("Find and Replace...");
		mnEdit.add(mntmFindAndReplace);

		JMenu mnSubtitle = new JMenu("Subtitle");
		menuBar.add(mnSubtitle);

		JMenuItem mntmDeleteLines = new JMenuItem("Delete Lines");
		mnSubtitle.add(mntmDeleteLines);

		JSeparator subtitlesSeparator1 = new JSeparator();
		mnSubtitle.add(subtitlesSeparator1);

		JMenuItem mntmSelectAll = new JMenuItem("Select All");
		mnSubtitle.add(mntmSelectAll);

		JMenu mnVideo = new JMenu(" Video ");
		menuBar.add(mnVideo);

		JMenuItem mntmOpenVideo = new JMenuItem("Open Video...");
		mnVideo.add(mntmOpenVideo);

		JSeparator videoSeparator1 = new JSeparator();
		mnVideo.add(videoSeparator1);

		JMenuItem mntmJumpTo = new JMenuItem("Jump To...");
		mnVideo.add(mntmJumpTo);

		JMenu mnView = new JMenu(" View");
		menuBar.add(mnView);

		JMenuItem mntmOptions = new JMenuItem("Options");
		mnView.add(mntmOptions);
		
		InitialFocusSetter.setInitialFocus(frame, subtitleArea);
		frame.pack();
		
	}
	
	static class InitialFocusSetter {

		public static void setInitialFocus(Window w, Component c) {
			w.addWindowListener(new FocusSetter(c));
		}

		public static class FocusSetter extends WindowAdapter {

			Component initialComponent;
			
			FocusSetter(Component c) {
				initialComponent = c;
			}

			public void windowOpened(WindowEvent e) {

				initialComponent.requestFocus();
				e.getWindow().removeWindowListener(this);

			}

		}
	}
}