package com.ascent.bean;

/**
 * ʵ����User�����������û�����Ϣ��
 * @author ascent
 * @version 1.0
 */
// �̳е�java��io�����л��ӿڣ�����һ���սӿ�
public class User implements java.io.Serializable
{
	// ���л��汾id
	private static final long serialVersionUID = 1L;

	private String username; // �û���

	private String password; // ����

	private int authority; // �û�Ȩ��

	/**
	 * Ĭ�Ϲ��췽��
	 */
	public User() {}

	/**
	 * �����������Ĺ��췽��
	 * @param username �û���
	 * @param password ����
	 */
	public User(String username, String password)
	{
		this.username = username;
		this.password = password;
	}

	/**
	 * �����в������췽��
	 * @param username �û���
	 * @param password ����
	 * @param authority �û�Ȩ��
	 */
	public User(String username, String password, int authority)
	{
		this.username = username;
		this.password = password;
		this.authority = authority;
	}
	
	/**
	 * @return the authority
	 * ���Ƿ���Ȩ��
	 */
	public int getAuthority()
	{
		return authority;
	}

	/**
	 * �����û�Ȩ��
	 * @param authority the authority to set
	 */
	public void setAuthority(int authority)
	{
		this.authority = authority;
	}

	/**
	 * �����û�������
	 * @return the password
	 */
	public String getPassword()
	{
		return password;
	}

	/**
	 * �����û�����
	 * @param password the password to set
	 */
	public void setPassword(String password)
	{
		this.password = password;
	}

	/**
	 * �����û���
	 * @return the username
	 */
	public String getUsername()
	{
		return username;
	}

	/**
	 * �����û���
	 * @param username the username to set
	 */
	public void setUsername(String username)
	{
		this.username = username;
	}

	@Override
	/**
	 * �����û��������룬���ַ���Ϊ���壬���ÿո����
	 */
	public String toString()
	{
		return this.getUsername() + "  " + this.getPassword();
	}
}
