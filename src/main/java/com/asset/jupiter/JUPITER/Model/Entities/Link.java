package com.asset.jupiter.JUPITER.Model.Entities;

import java.io.Serializable;
import javax.persistence.*;
import javax.persistence.Table;
import java.math.BigDecimal;


/**
 * The persistent class for the LINKS database table.
 * 
 */
@Entity
@Table(name="LINKS")
@NamedQuery(name="Link.findAll", query="SELECT l FROM Link l")
public class Link implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private LinkPK id;

	@Column(nullable=false, length=10)
	private String indexclass;

	@Column(nullable=false, precision=12)
	private BigDecimal pos;

	public Link() {
	}

	public Link(LinkPK id, String indexclass, BigDecimal pos) {
		this.id = id;
		this.indexclass = indexclass;
		this.pos = pos;
	}

	public LinkPK getId() {
		return this.id;
	}

	public void setId(LinkPK id) {
		this.id = id;
	}

	public String getIndexclass() {
		return this.indexclass;
	}

	public void setIndexclass(String indexclass) {
		this.indexclass = indexclass;
	}

	public BigDecimal getPos() {
		return this.pos;
	}

	public void setPos(BigDecimal pos) {
		this.pos = pos;
	}

	@Override
	public String toString() {
		return "Link{" +
				"id=" + id +
				", indexclass='" + indexclass + '\'' +
				", pos=" + pos +
				'}';
	}
}