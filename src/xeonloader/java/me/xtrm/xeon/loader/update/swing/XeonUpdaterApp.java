package me.xtrm.xeon.loader.update.swing;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import me.xtrm.xeon.loader.update.IUpdaterCallback;

public class XeonUpdaterApp implements IUpdaterCallback {

	private JFrame frmDeltaClient;
	private JProgressBar progressBar;
	private JLabel lblNewLabel;
	
	public XeonUpdaterApp() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		frmDeltaClient = new JFrame();
		frmDeltaClient.setTitle("Delta Client - Updater");
		frmDeltaClient.setSize(530, 120);
		frmDeltaClient.setLocationRelativeTo(null);
		frmDeltaClient.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmDeltaClient.setUndecorated(true);
		frmDeltaClient.setResizable(false);
		
		frmDeltaClient.getContentPane().setBackground(Color.BLACK);
		frmDeltaClient.getContentPane().setLayout(null);
		
		JLabel lblUpdatingDeltaClient = new JLabel("Updating Delta Client...");
		lblUpdatingDeltaClient.setFont(new Font("Comfortaa", Font.PLAIN, 29));
		lblUpdatingDeltaClient.setHorizontalAlignment(SwingConstants.CENTER);
		lblUpdatingDeltaClient.setBackground(Color.BLACK);
		lblUpdatingDeltaClient.setForeground(Color.WHITE);
		lblUpdatingDeltaClient.setBounds(208, 170 - 94, 312, 39);
		frmDeltaClient.getContentPane().add(lblUpdatingDeltaClient);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(30, 120 - 94, 470, 25);
		progressBar.setUI(new XProgressBar());
		progressBar.setBorderPainted(false);
		progressBar.setBorder(null);
		frmDeltaClient.getContentPane().add(progressBar);
		
		lblNewLabel = new JLabel();
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setForeground(Color.BLACK);
		lblNewLabel.setBounds(227, 30, 56, 14);
		frmDeltaClient.getContentPane().add(lblNewLabel);
	}

	@Override
	public void setMax(int size) {
		progressBar.setMaximum(size);
	}

	@Override
	public void setCurrent(int curr) {
		progressBar.setValue(curr);
		
		int max = progressBar.getMaximum();
		int percentage = (curr / max) * 100;
		lblNewLabel.setText(percentage + "%");
	}
	
	@Override
	public int getCurrent() {
		return progressBar.getValue();
	}
	
	@Override
	public void finished() {
		frmDeltaClient.setVisible(false);
	}

	@Override
	public void start() {
		frmDeltaClient.setVisible(true);
	}
}
