package com.ascent.bean;

/**
 * 实体类User，用来描述用户的信息类
 * @author ascent
 * @version 1.0
 */
// 继承的java的io的序列化接口，这是一个空接口
public class User implements java.io.Serializable
{
	// 序列化版本id
	private static final long serialVersionUID = 1L;

	private String username; // 用户名

	private String password; // 密码

	private int authority; // 用户权限

	/**
	 * 默认构造方法
	 */
	public User() {}

	/**
	 * 带两个参数的构造方法
	 * @param username 用户名
	 * @param password 密码
	 */
	public User(String username, String password)
	{
		this.username = username;
		this.password = password;
	}

	/**
	 * 带所有参数构造方法
	 * @param username 用户名
	 * @param password 密码
	 * @param authority 用户权限
	 */
	public User(String username, String password, int authority)
	{
		this.username = username;
		this.password = password;
		this.authority = authority;
	}
	
	/**
	 * @return the authority
	 * 就是返回权限
	 */
	public int getAuthority()
	{
		return authority;
	}

	/**
	 * 设置用户权限
	 * @param authority the authority to set
	 */
	public void setAuthority(int authority)
	{
		this.authority = authority;
	}

	/**
	 * 返回用户的密码
	 * @return the password
	 */
	public String getPassword()
	{
		return password;
	}

	/**
	 * 设置用户密码
	 * @param password the password to set
	 */
	public void setPassword(String password)
	{
		this.password = password;
	}

	/**
	 * 返回用户名
	 * @return the username
	 */
	public String getUsername()
	{
		return username;
	}

	/**
	 * 设置用户名
	 * @param username the username to set
	 */
	public void setUsername(String username)
	{
		this.username = username;
	}

	@Override
	/**
	 * 返回用户名和密码，用字符串为载体，并用空格隔开
	 */
	public String toString()
	{
		return this.getUsername() + "  " + this.getPassword();
	}
}
