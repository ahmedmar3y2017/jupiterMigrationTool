package com.asset.jupiter.JUPITER.Model.Entities;

import java.io.Serializable;
import javax.persistence.*;
import javax.persistence.Table;


/**
 * The persistent class for the LOOKUP_LIST_CODES database table.
 * 
 */
@Entity
@Table(name="LOOKUP_LIST_CODES")
@NamedQuery(name="LookupListCode.findAll", query="SELECT l FROM LookupListCode l")
public class LookupListCode implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="CODE_ID")
	private String codeId;

	private String code;

	@Column(name="LIST_ID")
	private String listId;

	@Column(name="\"VALUE\"")
	private String value;

	public LookupListCode() {
	}

	public String getCodeId() {
		return this.codeId;
	}

	public void setCodeId(String codeId) {
		this.codeId = codeId;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getListId() {
		return this.listId;
	}

	public void setListId(String listId) {
		this.listId = listId;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}