package com.asset.jupiter.JUPITER.Model.Entities;

import java.io.Serializable;
import javax.persistence.*;
import javax.persistence.Table;
import java.math.BigDecimal;


/**
 * The persistent class for the FIELDS database table.
 * 
 */
@Entity
@Table(name="FIELDS")
@NamedQuery(name="Field.findAll", query="SELECT f FROM Field f")
public class Field implements Serializable {
	private static final long serialVersionUID = 1L;
	private String fieldid;
	private String autoinc;
	private String defaultvalue;
	private String externalDatasourceId;
	private String externalDisplayField;
	private String externalFilterField;
	private String externalIdField;
	private String externalTableName;
	private BigDecimal fieldorder;
	private BigDecimal filterfldorder;
	private BigDecimal keystate;
	private BigDecimal length;
	private String lookupListId;
	private BigDecimal masterfldorder;
	private BigDecimal maxvalue;
	private BigDecimal minvalue;
	private BigDecimal multivalue;
	private String name;
	private String parentFieldOrder;
	private String parentTableId;
	private BigDecimal refflddisporder;
	private BigDecimal reffldorder;
	private String reftabid;
	private String required;
	private String retain;
	private String tableId;
	private BigDecimal type;

	public Field() {
	}


	@Id
	@Column(unique=true, nullable=false, length=16)
	public String getFieldid() {
		return this.fieldid;
	}

	public void setFieldid(String fieldid) {
		this.fieldid = fieldid;
	}


	@Column(nullable=false, length=1)
	public String getAutoinc() {
		return this.autoinc;
	}

	public void setAutoinc(String autoinc) {
		this.autoinc = autoinc;
	}


	@Column(length=256)
	public String getDefaultvalue() {
		return this.defaultvalue;
	}

	public void setDefaultvalue(String defaultvalue) {
		this.defaultvalue = defaultvalue;
	}


	@Column(name="EXTERNAL_DATASOURCE_ID", length=16)
	public String getExternalDatasourceId() {
		return this.externalDatasourceId;
	}

	public void setExternalDatasourceId(String externalDatasourceId) {
		this.externalDatasourceId = externalDatasourceId;
	}


	@Column(name="EXTERNAL_DISPLAY_FIELD", length=128)
	public String getExternalDisplayField() {
		return this.externalDisplayField;
	}

	public void setExternalDisplayField(String externalDisplayField) {
		this.externalDisplayField = externalDisplayField;
	}


	@Column(name="EXTERNAL_FILTER_FIELD", length=128)
	public String getExternalFilterField() {
		return this.externalFilterField;
	}

	public void setExternalFilterField(String externalFilterField) {
		this.externalFilterField = externalFilterField;
	}


	@Column(name="EXTERNAL_ID_FIELD", length=128)
	public String getExternalIdField() {
		return this.externalIdField;
	}

	public void setExternalIdField(String externalIdField) {
		this.externalIdField = externalIdField;
	}


	@Column(name="EXTERNAL_TABLE_NAME", length=128)
	public String getExternalTableName() {
		return this.externalTableName;
	}

	public void setExternalTableName(String externalTableName) {
		this.externalTableName = externalTableName;
	}


	@Column(nullable=false, precision=3)
	public BigDecimal getFieldorder() {
		return this.fieldorder;
	}

	public void setFieldorder(BigDecimal fieldorder) {
		this.fieldorder = fieldorder;
	}


	@Column(nullable=false, precision=3)
	public BigDecimal getFilterfldorder() {
		return this.filterfldorder;
	}

	public void setFilterfldorder(BigDecimal filterfldorder) {
		this.filterfldorder = filterfldorder;
	}


	@Column(nullable=false, precision=3)
	public BigDecimal getKeystate() {
		return this.keystate;
	}

	public void setKeystate(BigDecimal keystate) {
		this.keystate = keystate;
	}


	@Column(name="\"LENGTH\"", nullable=false, precision=4)
	public BigDecimal getLength() {
		return this.length;
	}

	public void setLength(BigDecimal length) {
		this.length = length;
	}


	@Column(name="LOOKUP_LIST_ID", length=16)
	public String getLookupListId() {
		return this.lookupListId;
	}

	public void setLookupListId(String lookupListId) {
		this.lookupListId = lookupListId;
	}


	@Column(nullable=false, precision=3)
	public BigDecimal getMasterfldorder() {
		return this.masterfldorder;
	}

	public void setMasterfldorder(BigDecimal masterfldorder) {
		this.masterfldorder = masterfldorder;
	}


	@Column(name="\"MAXVALUE\"", nullable=false, precision=28)
	public BigDecimal getMaxvalue() {
		return this.maxvalue;
	}

	public void setMaxvalue(BigDecimal maxvalue) {
		this.maxvalue = maxvalue;
	}


	@Column(name="\"MINVALUE\"", precision=28)
	public BigDecimal getMinvalue() {
		return this.minvalue;
	}

	public void setMinvalue(BigDecimal minvalue) {
		this.minvalue = minvalue;
	}


	@Column(nullable=false, precision=3)
	public BigDecimal getMultivalue() {
		return this.multivalue;
	}

	public void setMultivalue(BigDecimal multivalue) {
		this.multivalue = multivalue;
	}


	@Column(nullable=false, length=256)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}


	@Column(name="PARENT_FIELD_ORDER", length=16)
	public String getParentFieldOrder() {
		return this.parentFieldOrder;
	}

	public void setParentFieldOrder(String parentFieldOrder) {
		this.parentFieldOrder = parentFieldOrder;
	}


	@Column(name="PARENT_TABLE_ID", length=16)
	public String getParentTableId() {
		return this.parentTableId;
	}

	public void setParentTableId(String parentTableId) {
		this.parentTableId = parentTableId;
	}


	@Column(nullable=false, precision=3)
	public BigDecimal getRefflddisporder() {
		return this.refflddisporder;
	}

	public void setRefflddisporder(BigDecimal refflddisporder) {
		this.refflddisporder = refflddisporder;
	}


	@Column(nullable=false, precision=3)
	public BigDecimal getReffldorder() {
		return this.reffldorder;
	}

	public void setReffldorder(BigDecimal reffldorder) {
		this.reffldorder = reffldorder;
	}


	@Column(nullable=false, length=16)
	public String getReftabid() {
		return this.reftabid;
	}

	public void setReftabid(String reftabid) {
		this.reftabid = reftabid;
	}


	@Column(nullable=false, length=1)
	public String getRequired() {
		return this.required;
	}

	public void setRequired(String required) {
		this.required = required;
	}


	@Column(nullable=false, length=1)
	public String getRetain() {
		return this.retain;
	}

	public void setRetain(String retain) {
		this.retain = retain;
	}


	@Column(name="TABLE_ID", nullable=false, length=16)
	public String getTableId() {
		return this.tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}


	@Column(name="\"TYPE\"", nullable=false, precision=3)
	public BigDecimal getType() {
		return this.type;
	}

	public void setType(BigDecimal type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Field{" +
				"fieldid='" + fieldid + '\'' +
				", autoinc='" + autoinc + '\'' +
				", defaultvalue='" + defaultvalue + '\'' +
				", externalDatasourceId='" + externalDatasourceId + '\'' +
				", externalDisplayField='" + externalDisplayField + '\'' +
				", externalFilterField='" + externalFilterField + '\'' +
				", externalIdField='" + externalIdField + '\'' +
				", externalTableName='" + externalTableName + '\'' +
				", fieldorder=" + fieldorder +
				", filterfldorder=" + filterfldorder +
				", keystate=" + keystate +
				", length=" + length +
				", lookupListId='" + lookupListId + '\'' +
				", masterfldorder=" + masterfldorder +
				", maxvalue=" + maxvalue +
				", minvalue=" + minvalue +
				", multivalue=" + multivalue +
				", name='" + name + '\'' +
				", parentFieldOrder='" + parentFieldOrder + '\'' +
				", parentTableId='" + parentTableId + '\'' +
				", refflddisporder=" + refflddisporder +
				", reffldorder=" + reffldorder +
				", reftabid='" + reftabid + '\'' +
				", required='" + required + '\'' +
				", retain='" + retain + '\'' +
				", tableId='" + tableId + '\'' +
				", type=" + type +
				'}';
	}
}