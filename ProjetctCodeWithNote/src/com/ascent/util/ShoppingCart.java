package com.ascent.util;

import java.util.ArrayList;
import com.ascent.bean.Product;

/**
 * ���ﳵ
 * @author ascent
 * @version 1.0
 */
public class ShoppingCart {

	/**
	 * ��Ź�����Ʒ��Ϣ
	 */
	private static ArrayList<Product> shoppingList = new ArrayList<Product>();

	/**
	 * ��ȡ���й�����Ʒ��Ϣ
	 * @return shoppingList
	 */
	public ArrayList<Product> getShoppingList() {
		return this.shoppingList;
	}

	/**
	 * �����Ʒ�����ﳵ
	 * @param myProduct
	 */
	public void addProduct(Product myProduct) {
		Product product;
		boolean bo = false;
		for (int i = 0; i < shoppingList.size(); i++) {
			product = shoppingList.get(i);
			if (myProduct.getProductname().trim().equals(product.getProductname().trim())) {
				bo = true;
				break;
			}
		}
		if (!bo) {
			shoppingList.add(myProduct);
		}
	}

	/**
	 * ��չ��ﳵ��������Ʒ
	 */
	public void clearProduct() {
		shoppingList.clear();
	}

}
