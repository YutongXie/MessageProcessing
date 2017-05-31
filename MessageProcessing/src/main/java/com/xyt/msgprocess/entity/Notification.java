package com.xyt.msgprocess.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Notification implements Cloneable {
	private Type type;
	private String productType;
	private BigDecimal value;
	private Date saleTime;
	private int occurrence;
	private Adjustment adjustment;

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public Date getSaleTime() {
		return saleTime;
	}

	public void setSaleTime(Date saleTime) {
		this.saleTime = saleTime;
	}

	public int getOccurrence() {
		return occurrence;
	}

	public void setOccurrence(int occurrence) {
		this.occurrence = occurrence;
	}

	public Adjustment getAdjustment() {
		return adjustment;
	}

	public void setAdjustment(Adjustment adj) {
		this.adjustment = adj;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Notification [type=" + type + ", productType=" + productType + ", value=" + value + ", saleTime="
				+ saleTime + ", occurrence=" + occurrence + ", adjustment=" + adjustment + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((adjustment == null) ? 0 : adjustment.hashCode());
		result = prime * result + occurrence;
		result = prime * result + ((productType == null) ? 0 : productType.hashCode());
		result = prime * result + ((saleTime == null) ? 0 : saleTime.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Notification other = (Notification) obj;
		if (adjustment == null) {
			if (other.adjustment != null)
				return false;
		} else if (!adjustment.equals(other.adjustment))
			return false;
		if (occurrence != other.occurrence)
			return false;
		if (productType == null) {
			if (other.productType != null)
				return false;
		} else if (!productType.equals(other.productType))
			return false;
		if (saleTime == null) {
			if (other.saleTime != null)
				return false;
		} else if (!saleTime.equals(other.saleTime))
			return false;
		if (type != other.type)
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	public Notification clone() {
		Notification newNoti = new Notification();
		newNoti.setType(this.type);
		newNoti.setProductType(this.getProductType());
		newNoti.setValue(this.getValue());
		newNoti.setOccurrence(this.getOccurrence());
		if (this.getAdjustment() != null) {
			Adjustment newAdj = new Adjustment();
			newAdj.setOperation(this.getAdjustment().getOperation());
			newAdj.setValue(this.getAdjustment().getValue());
			newNoti.setAdjustment(newAdj);
		}
		return newNoti;
	}

	public List<Notification> clone(int num) {
		List<Notification> notiList = new ArrayList<Notification>();
		for (int i = 0; i < num; i++) {
			notiList.add(clone());
		}
		return notiList;
	}

}
