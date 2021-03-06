package edu.ycp.CS320_Minotaurs_Labyrinth.classes;

import java.util.ArrayList;

public abstract class Actor {
	
	protected int maxHP;

	protected int HP;

	protected int maxResource;

	protected int resource;

	protected int atk;

	protected int def;

	protected int gold;

	protected int XP;
	
	protected ArrayList<Ability> abilities;
	
	protected String status;
	
	protected String name;
	
	protected Inventory inventory;
	
	protected Room currentRoom;
	
	protected Boolean isDead;
	
	public Actor(int maxHP, int HP, int maxResource, int resource, int atk, int def, int gold, int XP, ArrayList<Ability> abilities, String status, Inventory inventory, Room currentRoom, boolean isDead, String name){
		this.maxHP = maxHP;
		this.HP = HP;
		this.maxResource = maxResource;
		this.resource = resource;
		this.atk = atk;
		this.def = def;
		this.gold = gold;
		this.XP = XP;
		this.abilities = abilities;
		this.status = status;
		this.inventory = inventory;
		this.currentRoom = currentRoom;
		this.isDead = isDead;
		this.name = name;
	}
	
	public abstract String basicAttack(Actor target);
	
	public abstract String cast(Actor target, Ability spell);
	
	//getters
	public abstract int getMaxHP();

	public abstract int getHP(); 
	
	public abstract int getMaxResource(); 
	
	public abstract int getResource();

	public abstract int getAtk();
	
	public abstract int getDef();
	
	public abstract int getGold();

	public abstract int getXP();
	
	public abstract ArrayList<Ability> getAbilities();
	
	public abstract String getStatus();
	
	public abstract Inventory getInventory();

	public abstract Room getCurrentRoom();

	public abstract Boolean getIsDead();
	
	public abstract String getName();
	
	//setters
	public abstract void setMaxHP(int maxHP);

	public abstract void setHP(int HP); 
	
	public abstract void setMaxResource(int maxResource); 
	
	public abstract void setResource(int resource);

	public abstract void setAtk(int atk);
	
	public abstract void setDef(int def);
	
	public abstract void setGold(int gold);

	public abstract void setXP(int XP);
	
	public abstract void setAbilities(ArrayList<Ability> abilities);
	
	public abstract void setStatus(String status);
	
	public abstract void setInventory(Inventory inventory);

	public abstract void setCurrentRoom(Room currentRoom);

	public abstract void setIsDead(Boolean isDead);}
