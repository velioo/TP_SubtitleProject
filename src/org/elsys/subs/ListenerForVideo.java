package org.elsys.subs;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.embedded.DefaultAdaptiveRuntimeFullScreenStrategy;

public class ListenerForVideo {

	private int close;
	private JPanel othersPanel;
	private JPanel tablePanel;
	private JPanel textPane;
	private JPanel videoPane;
	private JMenuBar menuBar;
	private JScrollPane scrollTablePane;
	private JFrame frame = null;
	private Dimension lastSize = new Dimension(1100,700);
	private final Lock mutex = new ReentrantLock(true);
	private EmbeddedMediaPlayerComponent mediaPlayerComponent = new EmbeddedMediaPlayerComponent();

	public ListenerForVideo(JPanel othersPanel, JPanel tablePanel, JPanel textPane, JFrame frame,
			JScrollPane scrollTablePane, JMenuBar menuBar) {
		this.othersPanel = othersPanel;
		this.scrollTablePane = scrollTablePane;
		this.tablePanel = tablePanel;
		this.textPane = textPane;
		this.menuBar = menuBar;
		this.frame = frame;
	}

	public void openVideo() {
		JFileChooser chooser = new JFileChooser();
		chooser.setAcceptAllFileFilterUsed(true);
		chooser.setMultiSelectionEnabled(false);
		int option = chooser.showOpenDialog(null);
		if (option == JFileChooser.APPROVE_OPTION) {

			othersPanel.remove(textPane);
			othersPanel.revalidate();
			othersPanel.repaint();
			othersPanel.setPreferredSize(new Dimension(200, 50));

			JPanel textPanel = new JPanel();
			textPanel.setPreferredSize(new Dimension(431, 300));
			textPanel.setLayout(new BorderLayout());
			textPane.setPreferredSize(new Dimension(250, 100));
			textPanel.add(textPane, BorderLayout.SOUTH);

			tablePanel.add(textPanel, BorderLayout.CENTER);

			tablePanel.remove(scrollTablePane);
			tablePanel.add(scrollTablePane, BorderLayout.SOUTH);
			scrollTablePane.setPreferredSize(new Dimension(100, 250));

			JPanel controlsPane = new JPanel();
			controlsPane.setPreferredSize(new Dimension(100, 30));
			controlsPane.setLayout(new FlowLayout());

			JButton rewindButton = new JButton("Rewind");
			rewindButton.setFocusPainted(false);
			controlsPane.add(rewindButton);

			JButton pauseButton = new JButton("Pause");
			pauseButton.setFocusPainted(false);
			controlsPane.add(pauseButton);

			JButton skipButton = new JButton("Skip");
			skipButton.setFocusPainted(false);
			controlsPane.add(skipButton);

			pauseButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							mediaPlayerComponent.getMediaPlayer().pause();
						}
					});
				}
			});

			rewindButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							mediaPlayerComponent.getMediaPlayer().skip(-5000);
						}
					});
				}
			});

			skipButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							mediaPlayerComponent.getMediaPlayer().skip(5000);
						}
					});
				}
			});

			Canvas videoSurface = mediaPlayerComponent.getVideoSurface();

			videoSurface.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
				}
			});

			videoSurface.addMouseWheelListener(new MouseWheelListener() {
				@Override
				public void mouseWheelMoved(MouseWheelEvent e) {
					int notches = e.getWheelRotation();
					int volume = mediaPlayerComponent.getMediaPlayer().getVolume();
					if (notches < 0) {
						if (volume != 200)
							mediaPlayerComponent.getMediaPlayer().setVolume(volume + 5);
					} else {
						if (volume != 0)
							mediaPlayerComponent.getMediaPlayer().setVolume(volume - 5);
					}
				}
			});

			videoSurface.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_SPACE) {
						mediaPlayerComponent.getMediaPlayer().pause();
					}
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						if (mediaPlayerComponent.getMediaPlayer().isFullScreen())
							mediaPlayerComponent.getMediaPlayer().setFullScreen(false);
						else
							mediaPlayerComponent.getMediaPlayer().setFullScreen(true);
					}
					if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
						mediaPlayerComponent.getMediaPlayer().setFullScreen(false);
					}
					if (e.getKeyCode() == KeyEvent.VK_LEFT) {
						mediaPlayerComponent.getMediaPlayer().skip(-5000);
					}
					if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
						mediaPlayerComponent.getMediaPlayer().skip(5000);
					}
					if (e.getKeyCode() == KeyEvent.VK_DOWN) {
						int volume = mediaPlayerComponent.getMediaPlayer().getVolume();
						if (volume != 0)
							mediaPlayerComponent.getMediaPlayer().setVolume(volume - 5);
					}
					if (e.getKeyCode() == KeyEvent.VK_UP) {
						int volume = mediaPlayerComponent.getMediaPlayer().getVolume();
						if (volume != 200)
							mediaPlayerComponent.getMediaPlayer().setVolume(volume + 5);
					}
				}
			});

			mediaPlayerComponent.getMediaPlayer().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
				@Override
				public void playing(MediaPlayer mediaPlayer) {
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							frame.setTitle(String.format("Subtitle Magic - %s",
									mediaPlayerComponent.getMediaPlayer().getMediaMeta().getTitle()));
						}
					});
				}

				@Override
				public void finished(MediaPlayer mediaPlayer) {
					int result = JOptionPane.showConfirmDialog(null, "Restart Video ?", "Video", JOptionPane.YES_NO_OPTION);
					switch (result) {
					case JOptionPane.YES_OPTION:
						mediaPlayerComponent.getMediaPlayer().setRepeat(true);
						return;
					case JOptionPane.NO_OPTION:
						System.out.println("WTF");
						close = 1;
						mediaPlayerComponent.getMediaPlayer().setRepeat(false);
						closeVideo();
						return;
					}
				}

				@Override
				public void error(MediaPlayer mediaPlayer) {
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							JOptionPane.showMessageDialog(frame, "Failed to play media", "Error", JOptionPane.ERROR_MESSAGE);
							frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
						}
					});
				}
			});

			mediaPlayerComponent.getMediaPlayer().setFullScreenStrategy(new DefaultAdaptiveRuntimeFullScreenStrategy(frame) {
					@Override
					protected void beforeEnterFullScreen() {
						frame.setSize(new Dimension(frame.getWidth() + 1,frame.getHeight() + 1));
					}

					@Override
					protected void afterExitFullScreen() {
						frame.setSize(new Dimension(frame.getWidth() - 1,frame.getHeight() - 1));
					}
			});

			videoPane = new JPanel();

			videoPane.setPreferredSize(new Dimension(550, 500));
			videoPane.setLayout(new BorderLayout());
			videoPane.add(mediaPlayerComponent, BorderLayout.CENTER);
			videoPane.add(controlsPane, BorderLayout.SOUTH);
			tablePanel.add(videoPane, BorderLayout.WEST);

			frame.addComponentListener(new ComponentAdapter() {
				@Override
				public void componentResized(ComponentEvent arg0) {
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							mutex.lock();
							Dimension currentFrameSize = frame.getSize();
			                int height = lastSize.height - currentFrameSize.height; // minus = the frame was increased vertically , plus = the frame was decreased vertically

			                if(height > 0){
			                	scrollTablePane.setPreferredSize(new Dimension(100, 250 - (height/5 + height/2)));
			                	videoPane.setPreferredSize(new Dimension(550 - (height/2 - height/100), 500));
			                } else {
			                	scrollTablePane.setPreferredSize(new Dimension(100, 250 - (height/5 + height/2)));
			                	videoPane.setPreferredSize(new Dimension(550 - (height/2 - height/100), 500));
			                }
			                mutex.unlock();
							menuBar.revalidate();
							frame.setVisible(true);
						}
					});
				}
				
			});

			//mediaPlayerComponent.setPreferredSize(new Dimension(600, 500));

			mediaPlayerComponent.getMediaPlayer().playMedia(chooser.getSelectedFile().getAbsolutePath());

			close = 1;
			frame.setVisible(true);
		}
	}

	public void closeVideo() {
		if (close == 1) {
			try {
				videoPane.removeAll();
				tablePanel.removeAll();

				tablePanel.add(scrollTablePane, BorderLayout.CENTER);
				scrollTablePane.setPreferredSize(new Dimension(452, 402));

				othersPanel.setPreferredSize(new Dimension(200, 150));
				textPane.setPreferredSize(new Dimension(10, 100));
				othersPanel.add(textPane, BorderLayout.SOUTH);

				mediaPlayerComponent.release();
				frame.setVisible(true);
				close = 0;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public EmbeddedMediaPlayerComponent getMediaComponent() {
		return mediaPlayerComponent;
	}
	
	public boolean isOpened() {
		return close == 1;
	}

}
