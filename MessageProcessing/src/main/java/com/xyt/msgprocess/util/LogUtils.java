package com.xyt.msgprocess.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class LogUtils {

	public static String extractExceptionStack(Exception ex) {
		StringWriter sw = null;
		PrintWriter pw = null;
		try {
			sw = new StringWriter();
			pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			pw.flush();
			sw.flush();
		} finally {
			if (sw != null) {
				try {
					sw.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (pw != null) {
				pw.close();
			}
		}
		return sw.toString();
	}
}
