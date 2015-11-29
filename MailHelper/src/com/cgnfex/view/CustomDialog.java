package com.cgnfex.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

public class CustomDialog extends JFrame implements ActionListener {
	public CustomDialog(String msg, String title) {
		setTitle(title);
		setSize(500, 400);
//		setAlwaysOnTop(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		
		
		Toolkit tkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = tkit.getScreenSize();
		Dimension frameSize = this.getSize();
		setLocation((screenSize.width - frameSize.width) / 2,
				(screenSize.height - frameSize.height) / 2);

		JLabel label = new JLabel(msg);
		getContentPane().add(label,BorderLayout.CENTER);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("宋体",Font.PLAIN, 18));
		
		JButton btn = new JButton("停止");
		btn.setFont(new Font("宋体",Font.PLAIN, 20));
		getContentPane().add(btn, BorderLayout.SOUTH);
		btn.addActionListener(this);

		setVisible(true);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.exit(0);
	}

}
