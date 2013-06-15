package org.young.controller;

import org.young.model.YoungModel;

public class ModelAndView {

	public static final int ACTION_TEMPLATE = 0;
	public static final int ACTION_DISPATCH = 1;
	public static final int ACTION_REDIRECT = 2;
	public static final int ACTION_STATICIZE = 3;

	private YoungModel model;
	private String view;
	private int action;

	public ModelAndView(YoungModel model, String view, int action) {
		this.model = model;
		this.view = view;
		this.action = action;
	}

	public ModelAndView(YoungModel model, String view) {
		this(model, view, ACTION_TEMPLATE);
	}

	public ModelAndView(String view) {
		this(new YoungModel(), view);
	}

	public ModelAndView(String view, int action) {
		this(new YoungModel(), view, action);
	}

	public YoungModel getModel() {
		return model;
	}

	public String getView() {
		return view;
	}

	public void setModel(YoungModel model) {
		this.model = model;
	}

	public void setView(String view) {
		this.view = view;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

}
