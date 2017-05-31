package com.xyt.msgprocess.entity;

public enum Operation {
	ADD("add"), SUBSTRACT("substract"), MULTIPLY("multiply");

	private String comments;

	private Operation(String comments) {
		this.setComments(comments);
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
}
