package com.manager.entity;


import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name="companies")
public class Company implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
    private int id;
	@Column(name="name")
    private String name;
	@Column(name="self_earnings")
	private long selfEstimatedEarnings;
	@Column(name="total_earnings")
	private long totalEstimatedEarnings;
	@Column(name="lft")
	private int lft;
	@Column(name="rgt")
	private int rgt;
	@Column(name="depth")
	private int depth;
	@Column(name="visual")
	private String visual;
	@Column(name="parentsName")
	private String parentsName;

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public long getSelfEstimatedEarnings() {
		return selfEstimatedEarnings;
	}
	public void setSelfEstimatedEarnings(long selfEstimatedEarnings) {
		this.selfEstimatedEarnings = selfEstimatedEarnings;
	}

	public long getTotalEstimatedEarnings() {
		return totalEstimatedEarnings;
	}
	public void setTotalEstimatedEarnings(long totalEstimatedEarnings) {
		this.totalEstimatedEarnings = totalEstimatedEarnings;
	}

	public int getLft() {
		return lft;
	}
	public void setLft(int lft) {
		this.lft = lft;
	}

	public int getRgt() {
		return rgt;
	}
	public void setRgt(int rgt) {
		this.rgt = rgt;
	}

	public int getDepth() {
		return depth;
	}
	public void setDepth(int depth) {
		this.depth = depth;
	}

	public void setVisual(String visual) {
		this.visual = visual;
	}
	public String getVisual() { return visual;}

	public String getParentsName() {
		return parentsName;
	}
	public void setParentsName(String parentsName) { this.parentsName = parentsName; }

	@Override
	public int hashCode(){
		final int prime = 7;
		int result = 1;
		result = prime * result + this.getRgt();
		result = prime * result + this.getLft();
		return result;
	}

	@Override
	public boolean equals(Object object){
		if(this == object){ return true; }
		if(object == null){	return false; }
		if(object.getClass() != this.getClass()){ return false; }
		Company company = (Company) object;
		if(!company.getName().equals(this.getName())){ return false; }
		if(company.getSelfEstimatedEarnings() != this.getSelfEstimatedEarnings()){ return false;}
		if(company.getLft() != this.getLft()){ return false;}
		if(company.getRgt() != this.getRgt()){ return false;}

		return true;
	}
}