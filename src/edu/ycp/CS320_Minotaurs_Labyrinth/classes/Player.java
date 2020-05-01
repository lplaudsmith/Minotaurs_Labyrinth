package edu.ycp.CS320_Minotaurs_Labyrinth.classes;

import java.util.ArrayList;
import java.util.HashMap;


public class Player extends Actor {
	
	//methods
	public Player(int maxHP, int HP, int maxResource, int resource, int atk, int def, int gold, int XP,
			ArrayList<Ability> abilities, String status, Inventory inventory, Room currentRoom, boolean isDead, String name) {
		super(maxHP, HP, maxResource, resource, atk, def, gold, XP, abilities, status, inventory, currentRoom, isDead, name);
		
	}
	
	public void crawl(String direction, ArrayList<Room> allRooms) {
		
		String tmpStatus = getStatus();
		
		setStatus("crawling");
		
		this.move(direction, allRooms);
		
		setStatus(tmpStatus);		
	}
	
	public void jump(String direction, ArrayList<Room> allRooms) {
		
		String tmpStatus = getStatus();
		
		setStatus("jumping");
		
		this.move(direction, allRooms);
		
		setStatus(tmpStatus);
		
	}
	
	public void light(Item item) {
		if(getInventory().getInventory().contains(item) && item.getFlammable() && item.getLit() == false) {
			item.setLit(true);
		}
		
	}
	
	public void equip(Gear gear) {
		if(getInventory().getInventory().contains(gear) && gear.getEquipped() == false) {
			gear.setEquipped(true);
			int atk = getAtk() + gear.getAtk();
			int def = getDef() + gear.getDef();
			setAtk(atk);
			setDef(def);
		}
		
	}
	
	public void unequip(Gear gear) {
		if(getInventory().getInventory().contains(gear) && gear.getEquipped() == true) {
			gear.setEquipped(false);
			int atk = getAtk() - gear.getAtk();
			int def = getDef() - gear.getDef();
			setAtk(atk);
			setDef(def);
		}
		
	}
	
	public void drop(Item item) {
		getInventory().removeItem(item);
		getCurrentRoom().getInventory().addItem(item);
		
	}
	
	public String run() {
		this.status = "normal";
		return "AAAAAAAHHHHHHHHHHHHHHHHHHH!";
	}
	
	public void take(Item item) {
		getInventory().addItem(item);
		
	}
	
	public String talk(Actor target) {
		
		if(target.getClass() != this.getClass()) {
			this.status = "talking";
			NPC npc = (NPC) target; 
			return npc.getDialogue();
		}
		return "You can't talk to yourself!";
	}
	
	public void throNPCItem(NPC target, Item item) {
		if(item.getThrowable()) {
			if(item.getVariety().equals("potion")) {
				item.addEffect(target);
			}
			
			else if(item.getVariety().equals("misc")) {
				target.setAttitude(target.getAttitude() - 10);
			}
			else if(item.getVariety().equals("harmmisc")) {
				target.setHP(getHP() - 1);
				target.setAttitude(target.getAttitude() - 10);
			}
		}
		
	}
	
	public void throNPCGear(NPC target, Gear gear) {
		if(gear.getThrowable()){
			target.setHP(target.getHP() - gear.getAtk());
			target.setAttitude(target.getAttitude() - 20);
		}
		
	}
	
	public void throObs(Obstacle target, Item item) {
		if(item.getThrowable()) {
			if(item.getName().equals("rope") && target.getStatus().equals("jumping")) {
				target.setStatus("normal");
			}
		}
	}
	
	public void use(Item item, Actor target) {
		if(getInventory().getInventory().contains(item)) {
			target.setHP(target.getHP()+item.getEffect());
		}
		
	}

	public int barter(NPC npc, Item item) {
		int itemVal=item.getValue();
		int priceOff = 0;
		if(npc.getAttitude()==100) {
			priceOff =(int) (itemVal*.3);
		}
		else if(npc.getAttitude()<100 && npc.getAttitude()>=80) {
			priceOff=(int) (itemVal*.2);
		}
		else if(npc.getAttitude()<80 && npc.getAttitude()>=60) {
			priceOff=(int) (itemVal*.1);
		}
		else {
			priceOff=(int)(itemVal*0);
		}
		return priceOff;
	}
	
	public String leave() {
		this.status = "normal";
		return "Goodbye!";
	}
	
	public String move(String direction, ArrayList<Room> allRooms) {
		
		HashMap<String, Integer> roomMap = this.currentRoom.getRoomMap();
		
		if(!this.isDead) {
			if(roomMap.containsKey(direction)){
				
				Room newRoom = getRoomByRoomId(roomMap.get(direction), allRooms);
				if(newRoom.getObstacle().checkStatus(this) || newRoom.getObstacle().getStatus().equals("normal") || newRoom.getObstacle().checkRequirement(this)){
					if(newRoom.getIsFound()==false) {
						newRoom.setIsFound(true);
					}
					this.currentRoom = newRoom;
					
				}else if(!newRoom.getObstacle().checkStatus(this)) {
					return "There is an obstacle in that direction!";
				}
			}else {
				return "You can't move in that direction!";
			}
			
			String tempStr = this.currentRoom.getDescription();
			Inventory inventory = this.currentRoom.getInventory();
			for(Item item : inventory.getInventory()) {
				String resp = "There is a " + item.getName() + " in the room.";
				tempStr= tempStr + " " + resp;
			}
			return tempStr;
			
		}else {
			return "You are dead!";
		}
		
	}
	
	private Room getRoomByRoomId(Integer room_id, ArrayList<Room> allRooms) {
		for(Room room : allRooms) {
			if(room.getRoomId() == room_id) {
				return room;
			}
		}
		return null;
	}

	public String checkMap() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("TODO - implement");

	}
	
	public String checkStats() {
		String Message = "You have: " + this.HP + " HP, " + this.resource + " Mana, " + this.atk + " Attack, " 
				+ this.def + " Defense";
		return Message;
	}
	
	public String checkNPC(NPC npc) {
		String Message = npc.getDescription();
		return Message;
	}
	
	public String checkItem(Item item) {
		String Message = item.getDescription();
		return Message;
	}
	
	public String checkValue(Item item) {
		String Message = item.getName() + " is worth " + item.getValue() + " Gold";
		return Message;
	}
	
	public String checkRoom(Room room) {
		String Message = room.getDescription();
		return Message;
	}
	
	public String checkInventory(Inventory inventory) {
		String Message = "";
		if(inventory.getInventory().isEmpty() != true) {
			for(Item item: inventory.getInventory()) {
				Message+=item.getName();
				Message+=" ";
			}
		}
		else {
			Message="Your inventory is empty!";
		}
		return Message;
	}

	public String basicAttack(Actor target) {
		this.status = "combat";
		if(!this.isDead) {
			target.setHP((target.getHP() - getAtk())); 
			
			if(target.getHP()<=0 || target.getIsDead()) {
				target.setIsDead(true);
				this.status = "normal";
				return target.getName() + " is dead.";
				
			}
			
			return "You did " + this.getAtk() + " to " + target.getName() + ", it now has " + target.getHP() + " HP.";
		}
		return "";
	}

	public String cast(Actor target, Ability spell) {
		
		if(!this.isDead || spell.getName().equals("godmode")) {
			if(!target.getName().equals("player")) {
				this.status = "combat";
			}
			if(abilities.contains(spell) && spell.getCost() <= this.resource) {
				spell.addEffect(target);
				setResource(getResource()-spell.getCost());
				if(target.getHP()<=0 || target.getIsDead()) {
					target.setIsDead(true);
					this.status = "normal";
					return target.getName() + " is dead.";
					
				}
				if(spell.getAffectedStat().equals("HP")) {
					return "You cast " + spell.getName() + " it did " + Math.abs(spell.getEffect()) + " to " + target.getName() + "'s " + spell.getAffectedStat() + ", it now has " + target.getHP() + " " + spell.getAffectedStat();
				  
				} 
				else if(spell.getAffectedStat().equals("maxHP")) {
					return "You cast " + spell.getName() + " it did " + Math.abs(spell.getEffect()) + " to " + target.getName() + "'s " + spell.getAffectedStat() + ", it now has " + target.getMaxHP() + " " + spell.getAffectedStat();
		
				}
				else if(spell.getAffectedStat().equals("resource")) {
					return "You cast " + spell.getName() + " it did " + Math.abs(spell.getEffect()) + " to " + target.getName() + "'s " + spell.getAffectedStat() + ", it now has " + target.getResource() + " " + spell.getAffectedStat();
		
				}
				else if(spell.getAffectedStat().equals("maxResource")) {
					return "You cast " + spell.getName() + " it did " + Math.abs(spell.getEffect()) + " to " + target.getName() + "'s " + spell.getAffectedStat() + ", it now has " + target.getMaxResource() + " " + spell.getAffectedStat();
		
				}
				else if(spell.getAffectedStat().equals("atk")) {
					return "You cast " + spell.getName() + " it did " + Math.abs(spell.getEffect()) + " to " + target.getName() + "'s " + spell.getAffectedStat() + ", it now has " + target.getAtk() + " " + spell.getAffectedStat();
		
				}
				else if(spell.getAffectedStat().equals("def")) {
					return "You cast " + spell.getName() + " it did " + Math.abs(spell.getEffect()) + " to " + target.getName() + "'s " + spell.getAffectedStat() + ", it now has " + target.getDef() + " " + spell.getAffectedStat();
		
				}
				else if(spell.getAffectedStat().equals("godmode")) {
					this.status = "normal";
					return "Godmode activated.";
				}
				}
			if(spell.getCost() > this.resource) {
				return "You don't have enough resource to cast " + spell.getName();
			}
		}
		return "You are dead!";
	}

	//getters
	public int getMaxHP() {
		return maxHP;
	}

	public int getHP() {
		return HP;
	}
	
	public int getMaxResource() {
		return maxResource;
	}
	
	public int getResource() {
		return resource;
	}

	public int getAtk() {
		return atk;
	}
	
	public int getDef() {
		return def;
	}
	
	public int getGold() {
		return gold;
	}

	public int getXP() {
		return XP;
	}
	
	public ArrayList<Ability> getAbilities() {
		return abilities;
	}
	
	public String getStatus() {
		return status;
	}
	
	public Inventory getInventory() {
		
		return inventory;
	}
	
	public Room getCurrentRoom() {
		return currentRoom;
	}
	
	public Boolean getIsDead() {
		return isDead;
	}
	
	public String getName() {
		return name;
	}
	
		
	//setters
	public void setHP(int HP) {
		this.HP = HP;
	}
	
	public void setMaxHP(int maxHP) {
		this.maxHP = maxHP;
	}
	
	public void setResource(int resource) {
		this.resource =  resource;
	}
	
	public void setMaxResource(int maxResource) {
		this.maxResource = maxResource;
	}

	public void setAtk(int atk) {
		this.atk = atk;
	}
	
	public void setDef(int def) {
		this.def = def;
	}
	
	public void setGold(int gold) {
		this.gold = gold;
	}

	public void setXP(int XP) {
		this.XP = XP;
	}

	public void setCurrentRoom(Room currentRoom) {
		this.currentRoom = currentRoom;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}

	public void setIsDead(Boolean isDead) {
		this.isDead = isDead;
	}

	public void setAbilities(ArrayList<Ability> abilities) {
		this.abilities = abilities;
		
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
		
	}
	
	public void setName(String name) {
		this.name = name;
	}

	
}
