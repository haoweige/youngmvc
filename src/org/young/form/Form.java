package org.young.form;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Form {

	private static final long serialVersionUID = 1L;

	private Map<String, Object> items;
	private Set<String> names;

	public Form() {
		items = new HashMap<String, Object>();
		names = new HashSet<String>();
	}

	public Object getProperty(String name) {
		return items.get(name);
	}

	public void setProperty(String name, Object value) {
		items.put(name, value);
		names.add(name);
	}

	public Set<String> getPropertyNames() {
		return names;
	}

}
