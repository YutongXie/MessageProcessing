package com.xyt.msgprocess.entity;

public enum Type {
	SALES("for sale"), PURCHASE("for purchase");
	private String comments;

	private Type(String comments) {
		this.setComments(comments);
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

}
