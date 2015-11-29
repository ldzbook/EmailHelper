package com.cgnfex.exception;

import java.lang.Thread.UncaughtExceptionHandler;

import javax.swing.JOptionPane;

import com.cgnfex.EmailHelper;

public class HandlerException implements UncaughtExceptionHandler {

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		// TODO Auto-generated method stub
		JOptionPane.showMessageDialog(EmailHelper.mainPain,
				e.getMessage(), "EmailHelper",
				JOptionPane.ERROR_MESSAGE);
		System.exit(0);
	}

}
