package com.ascent.ui;

/**
 * 这个类是客户端启动类
 * @author ascent
 * @version 1.0
 */


public class Ascentsys
{
	public static int isAdmin = 0;//是否是管理者，1为管理者，0为普通用户
	/**
	 * 启动客户端的主方法
	 * @param args 无
	 */
	public static void main(String[] args)
	{
		LoginFrame loginFrame = new LoginFrame();
		loginFrame.setVisible(true);
	}
}