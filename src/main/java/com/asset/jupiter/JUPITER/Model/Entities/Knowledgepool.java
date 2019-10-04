package com.asset.jupiter.JUPITER.Model.Entities;

import java.io.Serializable;
import javax.persistence.*;
import javax.persistence.Table;
import java.math.BigDecimal;


/**
 * The persistent class for the KNOWLEDGEPOOL database table.
 * 
 */
@Entity
@Table(name="KNOWLEDGEPOOL")
@NamedQuery(name="Knowledgepool.findAll", query="SELECT k FROM Knowledgepool k")
public class Knowledgepool implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false, length=16)
	private String poolid;

	@Column(nullable=false, precision=16)
	private BigDecimal creationdate;

	@Column(nullable=false, precision=16)
	private BigDecimal creator;

	@Column(name="DOMAIN_ID", precision=6)
	private BigDecimal domainId;

	@Column(name="KNOWLEDGEPOOL_TYPE", precision=1)
	private BigDecimal knowledgepoolType;

	@Column(nullable=false, length=256)
	private String name;

	public Knowledgepool() {
	}

	public String getPoolid() {
		return this.poolid;
	}

	public void setPoolid(String poolid) {
		this.poolid = poolid;
	}

	public BigDecimal getCreationdate() {
		return this.creationdate;
	}

	public void setCreationdate(BigDecimal creationdate) {
		this.creationdate = creationdate;
	}

	public BigDecimal getCreator() {
		return this.creator;
	}

	public void setCreator(BigDecimal creator) {
		this.creator = creator;
	}

	public BigDecimal getDomainId() {
		return this.domainId;
	}

	public void setDomainId(BigDecimal domainId) {
		this.domainId = domainId;
	}

	public BigDecimal getKnowledgepoolType() {
		return this.knowledgepoolType;
	}

	public void setKnowledgepoolType(BigDecimal knowledgepoolType) {
		this.knowledgepoolType = knowledgepoolType;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Knowledgepool{" +
				"poolid='" + poolid + '\'' +
				", creationdate=" + creationdate +
				", creator=" + creator +
				", domainId=" + domainId +
				", knowledgepoolType=" + knowledgepoolType +
				", name='" + name + '\'' +
				'}';
	}
}