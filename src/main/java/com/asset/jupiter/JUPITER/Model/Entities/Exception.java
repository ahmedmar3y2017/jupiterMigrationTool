package com.asset.jupiter.JUPITER.Model.Entities;

import java.io.Serializable;
import javax.persistence.*;
import javax.persistence.Table;
import java.math.BigDecimal;


/**
 * The persistent class for the MG_EXCEPTIONS database table.
 * 
 */
@Entity
@Table(name="MG_EXCEPTIONS")
public class Exception implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ExceptionPK id;

	@Column(name="EXCEPTION_COUNT")
	private BigDecimal exceptionCount;

	public Exception() {
	}

	public Exception(ExceptionPK id, BigDecimal exceptionCount) {
		this.id = id;
		this.exceptionCount = exceptionCount;
	}

	public ExceptionPK getId() {
		return this.id;
	}

	public void setId(ExceptionPK id) {
		this.id = id;
	}

	public BigDecimal getExceptionCount() {
		return this.exceptionCount;
	}

	public void setExceptionCount(BigDecimal exceptionCount) {
		this.exceptionCount = exceptionCount;
	}

}