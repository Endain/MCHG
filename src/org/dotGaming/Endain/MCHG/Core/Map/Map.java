package org.dotGaming.Endain.MCHG.Core.Map;

public class Map {
	private String name;
	private String author;
	private String description;
	private String support;
	private int id;
	private double x;
	private double y;
	private double z;
	private double radius;
	
	public Map() {
		this.name = "DEFAULT";
		this.author = "HEROBRINE";
		this.description = "NONE";
		this.support = "NONE";
		this.id = -1;
		this.x = 0;
		this.y = 0;
		this.z = 0;
		this.radius = 64;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setSupport(String support) {
		this.support = support;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public void setZ(double z) {
		this.z = z;
	}
	
	public void setRadius(double radius) {
		this.radius = radius;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getAuthor() {
		return this.author;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public String getSupport() {
		return this.support;
	}
	
	public int getId() {
		return this.id;
	}
	
	public double getX() {
		return this.x;
	}
	
	public double getY() {
		return this.y;
	}
	
	public double getZ() {
		return this.z;
	}
	
	public double getradius() {
		return this.radius;
	}
}
