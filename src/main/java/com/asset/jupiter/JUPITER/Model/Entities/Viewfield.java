package com.asset.jupiter.JUPITER.Model.Entities;

import java.io.Serializable;
import javax.persistence.*;
import javax.persistence.Table;
import java.math.BigDecimal;


/**
 * The persistent class for the VIEWFIELDS database table.
 * 
 */
@Entity
@Table(name="VIEWFIELDS")
@NamedQuery(name="Viewfield.findAll", query="SELECT v FROM Viewfield v")
public class Viewfield implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ViewfieldPK id;

	private BigDecimal accesstype;

	private String displayname;

	private String fieldcolor;

	private String fieldfont;

	private BigDecimal fieldhieght;

	private BigDecimal fieldwidth;

	private BigDecimal fieldx;

	private BigDecimal fieldy;

	private String labelcolor;

	private String labelfont;

	private BigDecimal labelhieght;

	private BigDecimal labelwidth;

	private BigDecimal labelx;

	private BigDecimal labely;

	private BigDecimal ltr;

	@Column(name="SORT_TYPE")
	private BigDecimal sortType;

	private BigDecimal taborder;

	public Viewfield() {
	}

	public ViewfieldPK getId() {
		return this.id;
	}

	public void setId(ViewfieldPK id) {
		this.id = id;
	}

	public BigDecimal getAccesstype() {
		return this.accesstype;
	}

	public void setAccesstype(BigDecimal accesstype) {
		this.accesstype = accesstype;
	}

	public String getDisplayname() {
		return this.displayname;
	}

	public void setDisplayname(String displayname) {
		this.displayname = displayname;
	}

	public String getFieldcolor() {
		return this.fieldcolor;
	}

	public void setFieldcolor(String fieldcolor) {
		this.fieldcolor = fieldcolor;
	}

	public String getFieldfont() {
		return this.fieldfont;
	}

	public void setFieldfont(String fieldfont) {
		this.fieldfont = fieldfont;
	}

	public BigDecimal getFieldhieght() {
		return this.fieldhieght;
	}

	public void setFieldhieght(BigDecimal fieldhieght) {
		this.fieldhieght = fieldhieght;
	}

	public BigDecimal getFieldwidth() {
		return this.fieldwidth;
	}

	public void setFieldwidth(BigDecimal fieldwidth) {
		this.fieldwidth = fieldwidth;
	}

	public BigDecimal getFieldx() {
		return this.fieldx;
	}

	public void setFieldx(BigDecimal fieldx) {
		this.fieldx = fieldx;
	}

	public BigDecimal getFieldy() {
		return this.fieldy;
	}

	public void setFieldy(BigDecimal fieldy) {
		this.fieldy = fieldy;
	}

	public String getLabelcolor() {
		return this.labelcolor;
	}

	public void setLabelcolor(String labelcolor) {
		this.labelcolor = labelcolor;
	}

	public String getLabelfont() {
		return this.labelfont;
	}

	public void setLabelfont(String labelfont) {
		this.labelfont = labelfont;
	}

	public BigDecimal getLabelhieght() {
		return this.labelhieght;
	}

	public void setLabelhieght(BigDecimal labelhieght) {
		this.labelhieght = labelhieght;
	}

	public BigDecimal getLabelwidth() {
		return this.labelwidth;
	}

	public void setLabelwidth(BigDecimal labelwidth) {
		this.labelwidth = labelwidth;
	}

	public BigDecimal getLabelx() {
		return this.labelx;
	}

	public void setLabelx(BigDecimal labelx) {
		this.labelx = labelx;
	}

	public BigDecimal getLabely() {
		return this.labely;
	}

	public void setLabely(BigDecimal labely) {
		this.labely = labely;
	}

	public BigDecimal getLtr() {
		return this.ltr;
	}

	public void setLtr(BigDecimal ltr) {
		this.ltr = ltr;
	}

	public BigDecimal getSortType() {
		return this.sortType;
	}

	public void setSortType(BigDecimal sortType) {
		this.sortType = sortType;
	}

	public BigDecimal getTaborder() {
		return this.taborder;
	}

	public void setTaborder(BigDecimal taborder) {
		this.taborder = taborder;
	}

}