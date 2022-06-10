package com.ascent.util;

/**
 * socket编程所涉及的标志
 * @author ascent
 * @version 1.0
 */
public interface ProtocolPort {
	/**
	 * 获取所有商品分类名称标志
	 */
	public static final int OP_GET_PRODUCT_CATEGORIES = 100;

	/**
	 * 根据分类名称获得该分类下的所有商品对象标志
	 */
	public static final int OP_GET_PRODUCTS = 101;

	/**
	 * 获取用户标志
	 */
	public static final int OP_GET_USERS = 102;

	/**
	 * 注册用户标志
	 */
	public static final int OP_ADD_USERS = 103;

	/**
	 * 添加产品标志
	 */
	public static final int OP_ADD_PRODUCT = 104;

	/**
	 * 获取所有商品对象的标志
	 */
	public static final int OP_GET_ALL_PRODUCTS = 105;

	/**
	 * 删除商品对象的标志
	 */
	public static final int OP_DELETE_PRODUCT = 106;

	/**
	 * 修改商品的对象的标志
	 */
	public static final int OP_CHANGE = 107;

	/**
	 * 默认端口号
	 */
	public static final int DEFAULT_PORT = 5150;

	/**
	 * 默认服务器地址
	 */
	public static final String DEFAULT_HOST = "localhost";
}
