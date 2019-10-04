package com.asset.jupiter.JUPITER.Model.Entities;

import java.io.Serializable;
import javax.persistence.*;
import javax.persistence.Table;


/**
 * The persistent class for the ITEM_INDEXCLASS database table.
 *
 */
@Entity
@Table(name="ITEM_INDEXCLASS")
@NamedQuery(name="ItemIndexclass.findAll", query="SELECT i FROM ItemIndexclass i")
public class ItemIndexclass implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ItemIndexclassPK id;

	//bi-directional many-to-one association to Item
	@ManyToOne
	@JoinColumn(name="ITEM_ID" , nullable=false, insertable=false, updatable=false)
	private Item item;

	public ItemIndexclass() {
	}

	public ItemIndexclass(ItemIndexclassPK id, Item item) {
		this.id = id;
		this.item = item;
	}

	public ItemIndexclassPK getId() {
		return this.id;
	}

	public void setId(ItemIndexclassPK id) {
		this.id = id;
	}

	public Item getItem() {
		return this.item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	@Override
	public String toString() {
		return "ItemIndexclass{" +
				"id=" + id +
				", item=" + item +
				'}';
	}
}