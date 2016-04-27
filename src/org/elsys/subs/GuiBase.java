package org.elsys.subs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.MaskFormatter;

import org.eclipse.wb.swing.FocusTraversalOnArray;

import uk.co.caprica.vlcj.discovery.NativeDiscovery;

public class GuiBase {

	public JFrame frame;
	private JTable subtitleTable;
	@SuppressWarnings("unused")
	private AddSubs addSubs = null;
	private JTextArea subtitleArea;
	private JTextField subtitleNumTextField;
	private JTextField changeTextField;
	private FileOpenRead fileOpenRead;
	private FileSaveClose fileSaveClose;
	private ListenerForVideo video = null;
	@SuppressWarnings("unused")
	private ListenerForTable tableListener = null;
	private int currentSelectedRow; // Will be used to get the selected row if there is actually is and save it's index
	private JPopupMenu popup = new JPopupMenu();
	private int resizeOpenCount = 0, resizeNewCount = 0;
	
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
		
		boolean found = new NativeDiscovery().discover();
		// Panels
		JPanel tablePanel = new JPanel();
		tablePanel.setPreferredSize(new Dimension(1000, 500));
		tablePanel.setLayout(new BorderLayout());
		frame.getContentPane().add(tablePanel, BorderLayout.CENTER);
		
		JPanel othersPanel = new JPanel();
		othersPanel.setPreferredSize(new Dimension(200, 150));
		frame.getContentPane().add(othersPanel, BorderLayout.NORTH);
		othersPanel.setLayout(new BorderLayout());
		
		JPanel textPane = new JPanel();
		textPane.setPreferredSize(new Dimension(10, 100));
		othersPanel.add(textPane, BorderLayout.SOUTH);
		textPane.setLayout(new BorderLayout());
		
		subtitleArea = new JTextArea();
		subtitleArea.setPreferredSize(new Dimension(200, 200));
		subtitleArea.setFont(new Font("Arial", Font.PLAIN, 16));
		subtitleArea.setTabSize(4);
		subtitleArea.setBorder( new MatteBorder(3, 3, 3, 3, (Color) SystemColor.activeCaption));
		textPane.add(subtitleArea, BorderLayout.CENTER);
		
		JPanel buttonsPane = new JPanel();
		buttonsPane.setPreferredSize(new Dimension(100, 50));
		othersPanel.add(buttonsPane, BorderLayout.NORTH);
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
		startTextField.setBounds(86, 11, 90, 20);
		buttonsPane.add(startTextField);
		
		JFormattedTextField endTextField = new JFormattedTextField(mask);
		endTextField.setValue(new String("00:00:02,000"));
		endTextField.setToolTipText("End time");
		endTextField.setBounds(181, 11, 90, 20);
		buttonsPane.add(endTextField);
		
		JTextField durationTextField = new JTextField();
		durationTextField.setToolTipText("Duration");
		durationTextField.setBounds(276, 11, 90, 20);
		buttonsPane.add(durationTextField);
		
		subtitleNumTextField = new JTextField();
		subtitleNumTextField.setText("1");
		subtitleNumTextField.setEditable(false);
		subtitleNumTextField.setToolTipText("Subtitle number");
		subtitleNumTextField.setBounds(37, 11, 39, 20);
		buttonsPane.add(subtitleNumTextField);
		subtitleNumTextField.setColumns(10);
		
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
		
		// Run it 1 time to set duration textfield
			currentSelectedRow = subtitleTable.getSelectedRow();
			ListenerForStartEnd startListener = new ListenerForStartEnd(subtitleTable, startTextField, endTextField, durationTextField, currentSelectedRow);
		//
		
		//Adding checkboxes to synchronize Start textField with Video's current time and seeking 
			JCheckBox synchCheckBox = new JCheckBox("");
			synchCheckBox.setToolTipText("Synchronize video's current time with the start text field");
			synchCheckBox.setBounds(528, 11, 21, 21);
			buttonsPane.add(synchCheckBox);
			
			JCheckBox seekCheckBox = new JCheckBox("");
			seekCheckBox.setToolTipText("Toggle automatically seeking video to the start time of selected lines");
			seekCheckBox.setBounds(554, 11, 21, 21);
			buttonsPane.add(seekCheckBox);
		//
			
		// Adding focus listener for subtitleArea
		subtitleArea.addFocusListener(
			new FocusListener() {
				@Override
				public void focusGained(FocusEvent e) {
					currentSelectedRow = subtitleTable.getSelectedRow();
				}

				@Override
				public void focusLost(FocusEvent e) {
					
				}
				
			}
		);
			
		//
			
		// Make the textfield which will contain the seconds by which will be moved forwards or backwards the subtitles
			
			changeTextField = new JTextField();
			changeTextField.setBounds(428, 11, 55, 20);
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
			backwardsButton.addMouseListener(new MouseAdapter() {
			    public void mouseEntered(MouseEvent evt) {
			    	backwardsButton.setBorder(BorderFactory.createBevelBorder(0, Color.GREEN, Color.GREEN));
			    }

			    public void mouseExited(MouseEvent evt) {
			    	backwardsButton.setBorder(BorderFactory.createEmptyBorder());
			    }
			});
			
			forwardsButton.addMouseListener(new MouseAdapter() {
			    public void mouseEntered(MouseEvent evt) {
			    	forwardsButton.setBorder(BorderFactory.createBevelBorder(0, Color.GREEN, Color.GREEN));
			    }

			    public void mouseExited(MouseEvent evt) {
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
						currentSelectedRow = subtitleTable.getSelectedRow();
						ListenerForStartEnd endListener = new ListenerForStartEnd(subtitleTable ,startTextField, endTextField, durationTextField, currentSelectedRow);
					}
				}
			);
			
			startTextField.addFocusListener(
				new FocusListener() {
					@Override
					public void focusGained(FocusEvent e) {
						currentSelectedRow = subtitleTable.getSelectedRow();
					}

					@Override
					public void focusLost(FocusEvent e) {
						ListenerForStartEnd endListener = new ListenerForStartEnd(subtitleTable ,startTextField, endTextField, durationTextField, currentSelectedRow);
					}
					
				}
			);
			
			
			endTextField.addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent event) {
						currentSelectedRow = subtitleTable.getSelectedRow();
						ListenerForStartEnd endListener = new ListenerForStartEnd(subtitleTable ,startTextField, endTextField, durationTextField, currentSelectedRow);
					}
				}
			);
			
			endTextField.addFocusListener(
					new FocusListener() {
						@Override
						public void focusGained(FocusEvent e) {
							currentSelectedRow = subtitleTable.getSelectedRow();
						}

						@Override
						public void focusLost(FocusEvent e) {
							ListenerForStartEnd endListener = new ListenerForStartEnd(subtitleTable ,startTextField, endTextField, durationTextField, currentSelectedRow);
						}
						
					}
				);
			
			durationTextField.addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent event) {
						currentSelectedRow = subtitleTable.getSelectedRow();
						ListenerForDuration durationListener = new ListenerForDuration(subtitleTable, startTextField, endTextField, durationTextField, currentSelectedRow);
					}
				}
			);
			
			durationTextField.addFocusListener(
					new FocusListener() {
						
						@Override
						public void focusGained(FocusEvent e) {
							JTextField field = ((JTextField)e.getComponent());
							durationTextField.setCaretPosition(field.getDocument().getLength());
							currentSelectedRow = subtitleTable.getSelectedRow();
						}

						@Override
						public void focusLost(FocusEvent e) {
							ListenerForDuration durationListener = new ListenerForDuration(subtitleTable, startTextField, endTextField, durationTextField, currentSelectedRow);
						}
						
					}
				);
			
			
		//
		JMenuItem deleteItem;
		JMenuItem insertItem;
		    
		insertItem = new JMenuItem("Insert subtitle");
		insertItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				InsertSubsToTable insertSubsToTable = new InsertSubsToTable(subtitleArea, subtitleTable, subtitleNumTextField, startTextField, durationTextField, endTextField);
			}
		});
		popup.add(insertItem);
		    
		deleteItem = new JMenuItem("Delete subtitle");
		deleteItem.addActionListener(new ActionListener() {
				
			@Override
			public void actionPerformed(ActionEvent e) {
				DeleteSubsFromTable deleteSubsFromTable = new DeleteSubsFromTable(subtitleArea, subtitleTable, subtitleNumTextField, startTextField, durationTextField, endTextField, video.getMediaComponent());
			}
		});
		popup.add(deleteItem);
		subtitleTable.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e){
		        if (e.isPopupTrigger())
		        	popup.show(e.getComponent(), e.getX(), e.getY());
		    }

		    public void mouseReleased(MouseEvent e){
		        if (e.isPopupTrigger())
		        	popup.show(e.getComponent(), e.getX(), e.getY());
		    }
		});
		
			
		// Make the Menubar
		JMenuBar menuBar = new JMenuBar();
		MenuBarProperties menuproperties = new MenuBarProperties(menuBar);
		
		frame.setJMenuBar(menuBar);
		//
		
		JMenu mnFile = new JMenu("  File  ");
		menuBar.add(mnFile);

		fileSaveClose = new FileSaveClose(subtitleTable);
		
		JMenuItem mntmNewSub = new JMenuItem("New Subtitles...");
		mntmNewSub.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						int result = JOptionPane.showConfirmDialog(null,"Do you want to save ?","Save",JOptionPane.YES_NO_CANCEL_OPTION);
						switch(result){
		                case JOptionPane.YES_OPTION:
		                	fileSaveClose.saveSubtitles();
		                	resizedNew();
		                case JOptionPane.NO_OPTION:
		    				DefaultTableModel model = (DefaultTableModel) subtitleTable.getModel();
		    				model.setRowCount(0);
		    				subtitleNumTextField.setEditable(true);
		    				subtitleNumTextField.setText("1");
		    				subtitleNumTextField.setEditable(false);
		    				endTextField.setValue(new String("00:00:02,000"));
		    				startTextField.setValue(new String("00:00:00,000"));
		    				subtitleArea.setText("");
		    				resizedNew();
		                case JOptionPane.CLOSED_OPTION:
		                    return;
		                case JOptionPane.CANCEL_OPTION:
		                    return;       
						}
					}
				});
			}
		});
		mnFile.add(mntmNewSub);

		fileOpenRead = new FileOpenRead(subtitleTable);
		// Open subtitles ActionListener
		JMenuItem mntmOpenSub = new JMenuItem("Open Subtitles...");
		mntmOpenSub.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							fileOpenRead.OpenSubtitles(video.getMediaComponent());
							if(resizeOpenCount == 0) {
								frame.setSize(new Dimension(frame.getWidth() + 1,frame.getHeight() + 1));
								resizeOpenCount++;
							}
							else {
								frame.setSize(new Dimension(frame.getWidth() - 1,frame.getHeight() - 1));
								resizeOpenCount--;
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
			}
			
		});
		//
		
		mnFile.add(mntmOpenSub);

		JMenuItem mntmSave = new JMenuItem("Save Subtitles");
		mnFile.add(mntmSave);

		JMenuItem mntmSaveSubtitlesAs = new JMenuItem("Save Subtitles as...");
		mntmSaveSubtitlesAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						fileSaveClose.saveSubtitles();
					}
				});
			}
		});
		mnFile.add(mntmSaveSubtitlesAs);

		JSeparator fileSeparator1 = new JSeparator();
		mnFile.add(fileSeparator1);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(video.getMediaComponent().isValid()) {
						video.closeVideo();
					}
					File f = new File("temp.srt");
					if(f.exists() && !f.isDirectory()) {
						f.delete();
					}
					System.exit(0);
				}
			}
		);
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
		
		video = new ListenerForVideo(othersPanel, tablePanel, textPane, frame, scrollTablePane, menuBar);
		JMenuItem mntmOpenVideo = new JMenuItem("Open Video...");
		mntmOpenVideo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				KeyListener listeners[] = subtitleArea.getKeyListeners();
				subtitleArea.removeKeyListener(listeners[1]);
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						video.closeVideo();
						video = new ListenerForVideo(othersPanel, tablePanel, textPane, frame, scrollTablePane, menuBar);
						video.openVideo();
						currentSelectedRow = subtitleTable.getSelectedRow();
						addSubs = new AddSubs(frame, subtitleArea, subtitleTable, subtitleNumTextField, startTextField, endTextField, durationTextField, scrollTablePane, synchCheckBox, video.getMediaComponent(), backwardsButton, forwardsButton, changeTextField, seekCheckBox, menuBar);
						RevalidateComponents revalidateComponents = new RevalidateComponents(frame, subtitleNumTextField, startTextField, endTextField, durationTextField, backwardsButton, forwardsButton, changeTextField, synchCheckBox, seekCheckBox, menuBar);
					}
				});
				resizedNew();
			}
		});
		mnVideo.add(mntmOpenVideo);
		
		// Add table actionListener
		subtitleTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
	        public void valueChanged(ListSelectionEvent event) {
	        	if (!event.getValueIsAdjusting()){
	        		tableListener = new ListenerForTable(subtitleArea, subtitleTable, subtitleNumTextField, startTextField, endTextField, durationTextField, video.isOpened(), video.getMediaComponent(), seekCheckBox);
	        	}
	        }
	    });
		//
		
		// Adding subtitle to JTable	
			currentSelectedRow = subtitleTable.getSelectedRow();
			addSubs = new AddSubs(frame, subtitleArea, subtitleTable, subtitleNumTextField, startTextField, endTextField, durationTextField, scrollTablePane, synchCheckBox, video.getMediaComponent(), backwardsButton, forwardsButton, changeTextField, seekCheckBox, menuBar);
		//
		
		JMenuItem mntmCloseVideo = new JMenuItem("Close Video");
		mnVideo.add(mntmCloseVideo);
		mntmCloseVideo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						video.closeVideo();
					}
				});
			}
		});

		JSeparator videoSeparator1 = new JSeparator();
		mnVideo.add(videoSeparator1);

		JMenuItem mntmJumpTo = new JMenuItem("Jump To...");
		mnVideo.add(mntmJumpTo);

		JMenu mnView = new JMenu(" View");
		menuBar.add(mnView);

		JMenuItem mntmOptions = new JMenuItem("Options");
		mnView.add(mntmOptions);
		
		InitialFocusSetter.setInitialFocus(frame, subtitleArea);
		//frame.pack();
		
	}
	
	public void resizedNew() {
		if(resizeNewCount == 0) {
			frame.setSize(new Dimension(frame.getWidth() + 1,frame.getHeight() + 1));
			resizeNewCount++;
		}
		else {
			frame.setSize(new Dimension(frame.getWidth() - 1,frame.getHeight() - 1));
			resizeNewCount--;
		}
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