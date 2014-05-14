package com.health;

import java.util.ArrayList;
import java.util.List;

public class ContactHelp extends BaseActivity {

	public static boolean saveContact(String name, String phone) {
		String names = cache.getContactNames();
		if (names == null)// ��һ���û�
			names = name;
		else
			names = names + "\t" + name;
		if (cache.saveContactNames(names))// ����ɹ��������ٴ�绰
			return cache.saveContact(name, phone);
		return false;
	}

	/***
	 * ��ȡ��ϵ���б�
	 * 
	 * @return
	 */
	public static List<String[]> getContacts() {
		List<String[]> list = new ArrayList<String[]>();
		String names = cache.getContactNames();
		if (names == null)
			return null;
		String[] nameArray = names.split("\t");
		for (String name : nameArray) {
			list.add(new String[] { "�绰:" + cache.getContact(name) ,"����:" + name, });
		}
		return list;
	}

	}
