package com.ll.spring.boot.core.util.entity;

import java.util.List;

public class Entity {

	private int colIndex;
	private String text;
	private List<Integer> mergeIndexList;

	public Entity() {
	}

	public Entity(int colIndex, String text) {
		this.colIndex = colIndex;
		this.text = text;
	}

	public Entity(int colIndex, String text, List<Integer> mergeIndexList) {
		this.colIndex = colIndex;
		this.text = text;
		this.mergeIndexList = mergeIndexList;
	}

	public int getColIndex() {
		return colIndex;
	}

	public void setColIndex(int colIndex) {
		this.colIndex = colIndex;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<Integer> getMergeIndexList() {
		return mergeIndexList;
	}

	public void setMergeIndexList(List<Integer> mergeIndexList) {
		this.mergeIndexList = mergeIndexList;
	}

	@Override
	public String toString() {
		return "Entity [colIndex=" + colIndex + ", text=" + text + ", mergeIndexList=" + mergeIndexList + "]";
	}

}