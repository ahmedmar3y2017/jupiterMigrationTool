package com.asset.jupiter.JUPITER.Model.Entities;

import java.io.Serializable;
import javax.persistence.*;
import javax.persistence.Table;
import java.math.BigDecimal;


/**
 * The persistent class for the ACCESSRULES database table.
 * 
 */
@Entity
@Table(name="ACCESSRULES")
@NamedQuery(name="Accessrule.findAll", query="SELECT a FROM Accessrule a")
public class Accessrule implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private AccessrulePK id;

	@Column(nullable=false, precision=12)
	private BigDecimal accessright;

	public Accessrule() {
	}

	public Accessrule(AccessrulePK id, BigDecimal accessright) {
		this.id = id;
		this.accessright = accessright;
	}

	public AccessrulePK getId() {
		return this.id;
	}

	public void setId(AccessrulePK id) {
		this.id = id;
	}

	public BigDecimal getAccessright() {
		return this.accessright;
	}

	public void setAccessright(BigDecimal accessright) {
		this.accessright = accessright;
	}

	@Override
	public String toString() {
		return "Accessrule{" +
				"id=" + id +
				", accessright=" + accessright +
				'}';
	}
}