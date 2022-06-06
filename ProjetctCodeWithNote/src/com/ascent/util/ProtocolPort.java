package com.ascent.util;

/**
 * socket������漰�ı�־
 * @author ascent
 * @version 1.0
 */
public interface ProtocolPort {
	/**
	 * ��ȡ������Ʒ�������Ʊ�־
	 */
	public static final int OP_GET_PRODUCT_CATEGORIES = 100;

	/**
	 * ���ݷ������ƻ�ø÷����µ�������Ʒ�����־
	 */
	public static final int OP_GET_PRODUCTS = 101;

	/**
	 * ��ȡ�û���־
	 */
	public static final int OP_GET_USERS = 102;

	/**
	 * ע���û���־
	 */
	public static final int OP_ADD_USERS = 103;

	/**
	 * Ĭ�϶˿ں�
	 */
	public static final int DEFAULT_PORT = 5150;

	/**
	 * Ĭ�Ϸ�������ַ
	 */
	public static final String DEFAULT_HOST = "localhost";
}
