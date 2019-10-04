package com.asset.jupiter.JUPITER.Model.Entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the "TABLES" database table.
 * 
 */
@Entity
@javax.persistence.Table(name="\"TABLES\"")
@NamedQuery(name="Table.findAll", query="SELECT t FROM Table t")
public class Table implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false, length=16)
	private String id;

	@Column(nullable=false, length=256)
	private String tablename;

	public Table() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTablename() {
		return this.tablename;
	}

	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

	@Override
	public String toString() {
		return "Table{" +
				"id='" + id + '\'' +
				", tablename='" + tablename + '\'' +
				'}';
	}
}