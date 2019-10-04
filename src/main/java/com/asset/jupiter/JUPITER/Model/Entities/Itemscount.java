package com.asset.jupiter.JUPITER.Model.Entities;

import java.io.Serializable;
import javax.persistence.*;
import javax.persistence.Table;
import java.math.BigDecimal;


/**
 * The persistent class for the ITEMSCOUNT database table.
 * 
 */
@Entity
@Table(name="ITEMSCOUNT")
@NamedQuery(name="Itemscount.findAll", query="SELECT i FROM Itemscount i")
public class Itemscount implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ItemscountPK id;

	@Column(nullable=false, precision=20)
	private BigDecimal itemscount;

	public Itemscount() {
	}

	public Itemscount(ItemscountPK id, BigDecimal itemscount) {
		this.id = id;
		this.itemscount = itemscount;
	}

	public ItemscountPK getId() {
		return this.id;
	}

	public void setId(ItemscountPK id) {
		this.id = id;
	}

	public BigDecimal getItemscount() {
		return this.itemscount;
	}

	public void setItemscount(BigDecimal itemscount) {
		this.itemscount = itemscount;
	}

	@Override
	public String toString() {
		return "Itemscount{" +
				"id=" + id +
				", itemscount=" + itemscount +
				'}';
	}
}