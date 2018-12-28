package com.ll.spring.boot.core.util.entity;

import java.util.List;

public class ExcelEntity {

	private List<Entity> entitys;

	public ExcelEntity() {
	}

	public ExcelEntity(List<Entity> entitys) {
		this.entitys = entitys;
	}

	public List<Entity> getEntitys() {
		return entitys;
	}

	public void setEntitys(List<Entity> entitys) {
		this.entitys = entitys;
	}

	@Override
	public String toString() {
		return "ExcelFooter [entitys=" + entitys + "]";
	}

}
