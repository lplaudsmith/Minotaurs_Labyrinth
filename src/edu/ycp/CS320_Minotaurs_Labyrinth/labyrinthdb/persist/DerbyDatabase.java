package edu.ycp.CS320_Minotaurs_Labyrinth.labyrinthdb.persist;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import edu.ycp.CS320_Minotaurs_Labyrinth.classes.Ability;
import edu.ycp.CS320_Minotaurs_Labyrinth.classes.Enemy;
import edu.ycp.CS320_Minotaurs_Labyrinth.classes.Inventory;
import edu.ycp.CS320_Minotaurs_Labyrinth.classes.Item;
import edu.ycp.CS320_Minotaurs_Labyrinth.classes.Message;
import edu.ycp.CS320_Minotaurs_Labyrinth.classes.NPC;
import edu.ycp.CS320_Minotaurs_Labyrinth.classes.Obstacle;
import edu.ycp.CS320_Minotaurs_Labyrinth.classes.Player;
import edu.ycp.CS320_Minotaurs_Labyrinth.classes.Room;
import edu.ycp.CS320_Minotaurs_Labyrinth.labyrinthdb.persist.DBUtil;


public class DerbyDatabase implements IDatabase {
	static {
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
		} catch (Exception e) {
			throw new IllegalStateException("Could not load Derby driver");
		}
	}
	
	private interface Transaction<ResultType> {
		public ResultType execute(Connection conn) throws SQLException;
	}

	private static final int MAX_ATTEMPTS = 10;
	
	// wrapper SQL transaction function that calls actual transaction function (which has retries)
	public<ResultType> ResultType executeTransaction(Transaction<ResultType> txn) {
		try {
			return doExecuteTransaction(txn);
		} catch (SQLException e) {
			throw new PersistenceException("Transaction failed", e);
		}
	}
	
	// SQL transaction function which retries the transaction MAX_ATTEMPTS times before failing
	public<ResultType> ResultType doExecuteTransaction(Transaction<ResultType> txn) throws SQLException {
		Connection conn = connect();
		
		try {
			int numAttempts = 0;
			boolean success = false;
			ResultType result = null;
			
			while (!success && numAttempts < MAX_ATTEMPTS) {
				try {
					result = txn.execute(conn);
					conn.commit();
					success = true;
				} catch (SQLException e) {
					if (e.getSQLState() != null && e.getSQLState().equals("41000")) {
						// Deadlock: retry (unless max retry count has been reached)
						numAttempts++;
					} else {
						// Some other kind of SQLException
						throw e;
					}
				}
			}
			
			if (!success) {
				throw new SQLException("Transaction failed (too many retries)");
			}
			
			// Success!
			return result;
		} finally {
			DBUtil.closeQuietly(conn);
		}
	}

	// TODO: Here is where you name and specify the location of your Derby SQL database
	// TODO: Change it here and in SQLDemo.java under CS320_LibraryExample_Lab06->edu.ycp.cs320.sqldemo
	// TODO: DO NOT PUT THE DB IN THE SAME FOLDER AS YOUR PROJECT - that will cause conflicts later w/Git
	private Connection connect() throws SQLException {
		Connection conn = DriverManager.getConnection("jdbc:derby:C:/Documents/CS320/Minotaurs_Labyrinth/labyrinth.db;create=true");		
		
		// Set autocommit() to false to allow the execution of
		// multiple queries/statements as part of the same transaction.
		conn.setAutoCommit(false);
		
		return conn;
	}
	

	private void loadMessage(Message<String, Integer> message, ResultSet resultSet, int index) throws SQLException {
		message.setMessage(resultSet.getString(index++));
		message.setPlayerAction(resultSet.getInt(index++));
	}
	
	private void loadAbility(Ability ability, ResultSet resultSet, int index) throws SQLException {
		
		
		ability.setName(resultSet.getString(index++));
		
		ability.setDescription(resultSet.getString(index++));
		
		ability.setVariety(resultSet.getString(index++));
	
		ability.setAffectedStat(resultSet.getString(index++));
		
		ability.setEffect(resultSet.getInt(index++));
		
		ability.setCost(resultSet.getInt(index++));
	}
	
	private void loadRoom(Room room, ResultSet resultSet, int index, HashMap<String, Integer> roomMap) throws SQLException {
		room.setDescription(resultSet.getString(index++));
		room.setInventory(null);
		index++;
		room.setObstacle(null);
		index++;
		room.setRoomMap(roomMap);
		index++;
		if(resultSet.getInt(index++)==1) {
			room.setIsFound(true);
		}else {
			room.setIsFound(false);
		}
	}
	

	private void loadPlayer(Player player, ResultSet resultSet, int index, ArrayList<Ability> abilities, Room currentRoom) throws SQLException {

		player.setMaxHP(resultSet.getInt(index++));
		player.setHP(resultSet.getInt(index++));
		player.setMaxResource(resultSet.getInt(index++));
		player.setResource(resultSet.getInt(index++));
		player.setAtk(resultSet.getInt(index++));
		player.setDef(resultSet.getInt(index++));
		player.setGold(resultSet.getInt(index++));
		player.setXP(resultSet.getInt(index++));
		player.setAbilities(abilities);
		index++;
		player.setStatus(resultSet.getString(index++));
		player.setInventory(null);
		index++;
		player.setCurrentRoom(currentRoom);
		index++;
		if(resultSet.getInt(index++)==1) {
			player.setIsDead(true);
		}
		else {
			player.setIsDead(false);
		}
		player.setName(resultSet.getString(index++));
	}
	
	//  creates the Authors and Books tables
	public void createTables() {
		executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
				PreparedStatement stmt2 = null;
				PreparedStatement stmt3 = null;
				PreparedStatement stmt4 = null;	
				PreparedStatement stmt5 = null;	
				PreparedStatement stmt6 = null;	
				PreparedStatement stmt7 = null;	
				PreparedStatement stmt8 = null;	
				PreparedStatement stmt9 = null;	
				PreparedStatement stmt10 = null;	
				PreparedStatement stmt11 = null;	
				PreparedStatement stmt12 = null;	
				PreparedStatement stmt13 = null;	
				PreparedStatement stmt14 = null;	
				
			
				try {
					stmt1 = conn.prepareStatement(
						"create table ability (" +
						"	ability_id integer primary key " +
						"		generated always as identity (start with 1, increment by 1), " +									
						"	name varchar(40)," +
						"	description varchar(7999)," +
						"	variety varchar(40)," +
						"	affectedStat varchar(40)," +
						"	effect integer," +
						"	cost integer" +
						")"
					);	
					stmt1.executeUpdate();
					
					System.out.println("Ability table created");
					
					stmt2 = conn.prepareStatement(
							"create table abilityList (" +
							"	abilityList_id integer primary key " +
							"		generated always as identity (start with 1, increment by 1), " +
							"	ability1 varchar(40)," +
							"	ability2 varchar(40)," +
							"	ability3 varchar(40)," +
							"	ability4 varchar(40)," +
							"	ability5 varchar(40)" +
							")"
					);
					stmt2.executeUpdate();
					
					System.out.println("abilityList table created");					
					
					stmt3 = conn.prepareStatement(
							"create table account (" +
							"	account_id integer primary key " +
							"		generated always as identity (start with 1, increment by 1), " +
							"	username varchar(40)," +
							"	password varchar(40)" +
							")"
					);
					stmt3.executeUpdate();
					
					System.out.println("account table created");
					
					stmt4 = conn.prepareStatement(
							"create table enemy (" +
							"	enemy_id integer primary key " +
							"		generated always as identity (start with 1, increment by 1), " +
							"	maxHP integer," +
							"	HP integer," +
							"	maxResource integer," +
							"	resource integer," +
							"	atk integer," +
							"	def integer," +
							"	gold integer," +
							"	XP integer," +
							"	abilities integer," +
							"	status varchar(40)," +
							"	dialogue varchar(7999)," +
							"	attitude integer," +
							"	description varchar(7999)," +
							"	name varchar(40)," +
							"	inventory integer," +
							"	currentRoom integer," +
							"	isDead integer" +
							")"
					);
					stmt4.executeUpdate();
					
					System.out.println("enemy table created");
					
					stmt5 = conn.prepareStatement(
							"create table gear (" +
							"	gear_id integer primary key " +
							"		generated always as identity (start with 1, increment by 1), " +
							"	atk integer," +
							"	def integer," +
							"	HP integer," +
							"	variety varchar(40)," +
							"	equipped integer," +
							"	description varchar(7999)," +
							"	effect integer," +
							"	flammable integer," +
							"	lit integer," +
							"	throwable integer," +
							"	value integer," +
							"	name varchar(40)," +
							"	affectedStat varchar(40)" +
							")"
					);
					stmt5.executeUpdate();
					
					System.out.println("gear table created");
					
					stmt6 = conn.prepareStatement(
							"create table inventory (" +
							"	inventory_id integer primary key " +
							"		generated always as identity (start with 1, increment by 1), " +
							"	maxStorage integer," +
							"	maxQuant integer," +
							"	inventory integer" +
							")"
					);
					stmt6.executeUpdate();
					
					System.out.println("inventory table created");
					
					stmt7 = conn.prepareStatement(
							"create table item (" +
							"	item integer primary key " +
							"		generated always as identity (start with 1, increment by 1), " +
							"	description varchar(7999)," +
							"	effect integer," +
							"	flammable integer," +
							"	lit integer," +
							"	throwable integer," +
							"	value integer," +
							"	name varchar(40)," +
							"	variety varchar(40)," +
							"	affectedStat varchar(40)" +
							")"
					);
					stmt7.executeUpdate();
					
					System.out.println("item table created");
					
					stmt8 = conn.prepareStatement(
							"create table itemList (" +
							"	itemList_id integer primary key " +
							"		generated always as identity (start with 1, increment by 1), " +
							"	item1 varchar(40)," +
							"	item2 varchar(40)," +
							"	item3 varchar(40)," +
							"	item4 varchar(40)," +
							"	item5 varchar(40)," +
							"	item6 varchar(40)," +
							"	item7 varchar(40)," +
							"	item8 varchar(40)," +
							"	item9 varchar(40)," +
							"	item10 varchar(40)," +
							"	item11 varchar(40)," +
							"	item12 varchar(40)," +
							"	item13 varchar(40)," +
							"	item14 varchar(40)," +
							"	item15 varchar(40)," +
							"	item16 varchar(40)," +
							"	item17 varchar(40)," +
							"	item18 varchar(40)," +
							"	item19 varchar(40)," +
							"	item20 varchar(40)," +
							"	item21 varchar(40)," +
							"	item22 varchar(40)," +
							"	item23 varchar(40)," +
							"	item24 varchar(40)," +
							"	item25 varchar(40)," +
							"	item26 varchar(40)," +
							"	item27 varchar(40)," +
							"	item28 varchar(40)," +
							"	item29 varchar(40)," +
							"	item30 varchar(40)," +
							"	item31 varchar(40)," +
							"	item32 varchar(40)," +
							"	item33 varchar(40)," +
							"	item34 varchar(40)," +
							"	item35 varchar(40)," +
							"	item36 varchar(40)," +
							"	item37 varchar(40)," +
							"	item38 varchar(40)," +
							"	item39 varchar(40)," +
							"	item40 varchar(40)," +
							"	item41 varchar(40)," +
							"	item42 varchar(40)," +
							"	item43 varchar(40)," +
							"	item44 varchar(40)," +
							"	item45 varchar(40)," +
							"	item46 varchar(40)," +
							"	item47 varchar(40)," +
							"	item48 varchar(40)," +
							"	item49 varchar(40)," +
							"	item50 varchar(40)" +
							")"
					);
					stmt8.executeUpdate();
					
					System.out.println("itemList table created");
					
					stmt9 = conn.prepareStatement(
							"create table NPC (" +
							"	NPC_id integer primary key " +
							"		generated always as identity (start with 1, increment by 1), " +
							"	maxHP integer," +
							"	HP integer," +
							"	maxResource integer," +
							"	resource integer," +
							"	atk integer," +
							"	def integer," +
							"	gold integer," +
							"	XP integer," +
							"	abilities integer," +
							"	status varchar(40)," +
							"	dialogue varchar(7999)," +
							"	attitude integer," +
							"	description varchar(7999)," +
							"	name varchar(40)," +
							"	inventory integer," +
							"	currentRoom integer," +
							"	isDead integer" +
							")"
					);
					stmt9.executeUpdate();
					
					System.out.println("NPC table created");
					
					stmt10 = conn.prepareStatement(
							"create table obstacle (" +
							"	obstacle_id integer primary key " +
							"		generated always as identity (start with 1, increment by 1), " +
							"	description varchar(7999)," +
							"	status varchar(40)," +
							"	requirement integer" +
							")"
					);
					stmt10.executeUpdate();
					
					System.out.println("obstacle table created");
								
					
					stmt11 = conn.prepareStatement(
							"create table player (" +
							"	player_id integer primary key " +
							"		generated always as identity (start with 1, increment by 1), " +
							"	maxHP integer," +
							"	HP integer," +
							"	maxResource integer," +
							"	resource integer," +
							"	atk integer," +
							"	def integer," +
							"	gold integer," +
							"	XP integer," +
							"	abilities integer," +
							"	status varchar(40)," +
							"	inventory integer," +
							"	currentRoom integer," +
							"	isDead integer," +
							"	name varchar(40)" +
							")"
					);
					stmt11.executeUpdate();
					
					System.out.println("player table created");
					
					stmt12 = conn.prepareStatement(
							"create table room (" +
							"	room_id integer primary key " +
							"		generated always as identity (start with 1, increment by 1), " +
							"	description varchar(7999)," +
							"	inventory integer," +
							"	obstacle integer," +
							"	roomMap integer," +
							"	isFound integer" +
							")"
					);
					stmt12.executeUpdate();
					
					System.out.println("room table created");
					
					stmt13 = conn.prepareStatement(
							"create table roomMap (" +
							"	roomId integer," +
							"	direction varchar(40)," +
							"	mappedRoomId integer" +
							")"
					);
					stmt13.executeUpdate();
					
					System.out.println("roomMap table created");
					
					stmt14 = conn.prepareStatement(
							"create table textHistory (" +
							"	message varchar(7999)," +
							"	playerAction integer" +
							")"
					);
					stmt14.executeUpdate();
					
					System.out.println("textHistory table created");
					
					return true;
				} finally {
					DBUtil.closeQuietly(stmt1);
					DBUtil.closeQuietly(stmt2);
					DBUtil.closeQuietly(stmt3);
					DBUtil.closeQuietly(stmt4);
					DBUtil.closeQuietly(stmt5);
					DBUtil.closeQuietly(stmt6);
					DBUtil.closeQuietly(stmt7);
					DBUtil.closeQuietly(stmt8);
					DBUtil.closeQuietly(stmt9);
					DBUtil.closeQuietly(stmt10);
					DBUtil.closeQuietly(stmt11);
					DBUtil.closeQuietly(stmt12);
					DBUtil.closeQuietly(stmt13);
					DBUtil.closeQuietly(stmt14);
				}
			}
		});
	}
	
	// loads data retrieved from CSV files into DB tables in batch mode
	public void loadInitialData() {
		executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				List<Ability> Abilities;
				List<ArrayList<Ability>> AbilitiesList;
				List<Inventory> InventoryList;
				List<Item> Items;
				List<ArrayList<Item>> ItemList;
				List<Room> Rooms;
				List<Obstacle> Obstacles;
				List<HashMap<String, Integer>> Maps;
				List<Enemy> Enemies;
				List<Player> Players;
				List<NPC> NPCs;
				List<Message<String, Integer>> Messages;
				
				try {
					
					Abilities      = InitialData.getAbilities();
					AbilitiesList  = InitialData.getAbilitiesList();
					InventoryList  = InitialData.getInventory();
					Items          = InitialData.getItems();
				    ItemList       = InitialData.getItemList();
					Rooms          = InitialData.getRooms();
					Maps           = InitialData.getMaps();
					Obstacles      = InitialData.getObstacles();
					Enemies        = InitialData.getEnemies();
					Players        = InitialData.getPlayers();
					NPCs           = InitialData.getNPCs();
					Messages       = InitialData.getTextHistory();
					
					
				} catch (IOException e) {
					throw new SQLException("Couldn't read initial data", e);
				}

				PreparedStatement insertAbilities   = null;
				PreparedStatement insertAbilitiesList   = null;
				PreparedStatement insertInventoryList   = null;
				PreparedStatement insertItems   = null;
				PreparedStatement insertItemList   = null;
				PreparedStatement insertRooms   = null;
				PreparedStatement insertObstacles   = null;
				PreparedStatement insertMaps   = null;
				PreparedStatement insertEnemies   = null;
				PreparedStatement insertPlayers   = null;
				PreparedStatement insertNPCs   = null;
				PreparedStatement insertMessages = null;

				try {
					
					insertAbilities = conn.prepareStatement("insert into ability (name, description, variety, affectedStat, "
							+ "effect, cost) values (?, ?, ?, ?, ?, ?)");
					for (Ability ability : Abilities) {
						insertAbilities.setString(1, ability.getName());
						insertAbilities.setString(2, ability.getDescription());
						insertAbilities.setString(3, ability.getVariety());
						insertAbilities.setString(4, ability.getAffectedStat());
						insertAbilities.setInt(5, ability.getEffect());
						insertAbilities.setInt(6, ability.getCost());
						insertAbilities.addBatch();
					}
					insertAbilities.executeBatch();
					
					System.out.println("Abilities table populated");
					

					
					insertAbilitiesList = conn.prepareStatement("insert into abilitylist (ability1, ability2, ability3, "
							+ "ability4, ability5) values (?, ?, ?, ?, ?)");
					for (ArrayList<Ability> ability : AbilitiesList) {
						for(int i=1; i<=ability.size(); i++) {
						insertAbilitiesList.setString(i, ability.get(i-1).getName());
						}
						insertAbilitiesList.addBatch();

					}
					insertAbilitiesList.executeBatch();					
					
					
					System.out.println("AbilitiesList table populated");					
					
					
					insertPlayers = conn.prepareStatement("insert into player (maxHP, HP, maxResource, resource, "
							+ "atk, def, gold, XP, abilities, status, "
							+ "inventory, currentRoom, isDead, name) values "
							+ "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
					for (Player player : Players) {
						insertPlayers.setInt(1, player.getMaxHP());
						insertPlayers.setInt(2, player.getHP());
						insertPlayers.setInt(3, player.getMaxResource());
						insertPlayers.setInt(4, player.getResource());
						insertPlayers.setInt(5, player.getAtk());
						insertPlayers.setInt(6, player.getDef());
						insertPlayers.setInt(7, player.getGold());
						insertPlayers.setInt(8, player.getXP());
						insertPlayers.setInt(9, AbilityIDbyList(player.getAbilities(), AbilitiesList));
						insertPlayers.setString(10, player.getStatus());
						insertPlayers.setInt(11, InventoryIDbyList(player.getInventory(), InventoryList));
						insertPlayers.setInt(12, player.getCurrentRoom().getRoomId());
						if(player.getIsDead()) {
						insertPlayers.setInt(13, 1);
						}else{
						insertPlayers.setInt(13, 0);
						}
						insertPlayers.setString(14, player.getName());
						insertPlayers.addBatch();
					}
					insertPlayers.executeBatch();	
					
					System.out.println("Players table populated");	
					
					insertEnemies = conn.prepareStatement("insert into enemy (maxHP, HP, maxResource, resource, "
							+ "atk, def, gold, XP, abilities, "
							+ "status, dialogue, attitude, description, "
							+ "name, inventory, currentRoom, isDead) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
					for (Enemy enemy : Enemies) {
						insertEnemies.setInt(1, enemy.getMaxHP());
						insertEnemies.setInt(2, enemy.getHP());
						insertEnemies.setInt(3, enemy.getMaxResource());
						insertEnemies.setInt(4, enemy.getResource());
						insertEnemies.setInt(5, enemy.getAtk());
						insertEnemies.setInt(6, enemy.getDef());
						insertEnemies.setInt(7, enemy.getGold());
						insertEnemies.setInt(8, enemy.getXP());
						insertEnemies.setInt(9, AbilityIDbyList(enemy.getAbilities(), AbilitiesList));
						insertEnemies.setString(10, enemy.getStatus());
						insertEnemies.setString(11, enemy.getDialogue());
						insertEnemies.setInt(12, enemy.getAttitude());
						insertEnemies.setString(13, enemy.getDescription());
						insertEnemies.setString(14, enemy.getName());
						insertEnemies.setInt(15, InventoryIDbyList(enemy.getInventory(), InventoryList));
						insertEnemies.setInt(16, enemy.getCurrentRoom().getRoomId());
						if(enemy.getIsDead()) {
						insertEnemies.setInt(17, 1);
						}else{
						insertEnemies.setInt(17, 0);
						}
						insertEnemies.setString(14, enemy.getName());
						insertEnemies.addBatch();
					}
					insertEnemies.executeBatch();
					
					System.out.println("Enemies table populated");		
					
					insertNPCs = conn.prepareStatement("insert into npc (maxHP, HP, maxResource, resource, "
							+ "atk, def, gold, XP, abilities, "
							+ "status, dialogue, attitude, description, "
							+ "name, inventory, currentRoom, isDead) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
					
					for (NPC npc : NPCs) {
						insertNPCs.setInt(1, npc.getMaxHP());
						insertNPCs.setInt(2, npc.getHP());
						insertNPCs.setInt(3, npc.getMaxResource());
						insertNPCs.setInt(4, npc.getResource());
						insertNPCs.setInt(5, npc.getAtk());
						insertNPCs.setInt(6, npc.getDef());
						insertNPCs.setInt(7, npc.getGold());
						insertNPCs.setInt(8, npc.getXP());
						insertNPCs.setInt(9, AbilityIDbyList(npc.getAbilities(), AbilitiesList));
						insertNPCs.setString(10, npc.getStatus());
						insertNPCs.setString(11, npc.getDialogue());
						insertNPCs.setInt(12, npc.getAttitude());
						insertNPCs.setString(13, npc.getDescription());
						insertNPCs.setString(14, npc.getName());
						insertNPCs.setInt(15, InventoryIDbyList(npc.getInventory(), InventoryList));
						insertNPCs.setInt(16, npc.getCurrentRoom().getRoomId());
						if(npc.getIsDead()) {
						insertNPCs.setInt(17, 1);
						}else{
						insertNPCs.setInt(17, 0);
						}
						insertNPCs.setString(14, npc.getName());
						insertNPCs.addBatch();
					}
					insertNPCs.executeBatch();
					
					System.out.println("NPC table populated");		
					
					
					insertItems = conn.prepareStatement("insert into item (description, effect, flammable, lit, throwable, "
							+ "value, name, variety, affectedStat) values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
					for (Item item : Items) {
						insertItems.setString(1, item.getDescription());
						insertItems.setInt(2, item.getEffect());
						if(item.getFlammable()) {
							insertItems.setInt(3, 1);
						}else{
							insertItems.setInt(3, 0);
						}
						
						if(item.getLit()) {
							insertItems.setInt(4, 1);
						}else{
							insertItems.setInt(4, 0);
						}
						
						if(item.getThrowable()) {
							insertItems.setInt(5, 1);
						}else{
							insertItems.setInt(5, 0);
						}
						
						insertItems.setInt(6, item.getValue());
						insertItems.setString(7, item.getName());
						insertItems.setString(8, item.getVariety());
						insertItems.setString(9, item.getAffectedStat());
						insertItems.addBatch();
					}
					insertEnemies.executeBatch();	
					
					System.out.println("NPC table populated");	
					
					insertItemList = conn.prepareStatement("insert into itemlist (item1, "
							+ "item2, item3, item4, item5, item6, item7, item8, item9, item10, item11, item12, "
							+ "item13, item14, item15, item16, item17, item18, item19, item20, item21, item22, "
							+ "item23, item24, item25, item26, item27, item28, item29, item30, item31, item32, item33, item34, "
							+ "item35, item36, item37, item38, item39, item40, item41, item42, item43, item44, item45, "
							+ "item46, item47, item48, item49, item50) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
							+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
							+ "?, ?, ?, ?, ?, ?, ?)");
					for (List<Item> items : ItemList) {
						for(int i=1; i<=items.size(); i++) {
							insertItemList.setString(i, items.get(i-1).getName());
						}
						insertItemList.addBatch();
					}
					insertItemList.executeBatch();
					
					System.out.println("ItemList table populated");
					
					
					insertRooms = conn.prepareStatement("insert into room (description, inventory, obstacle, "
							+ "roomMap, isFound) values (?, ?, ?, ?, ?)");
					
					for (Room room : Rooms) {
						insertRooms.setString(1, room.getDescription());
						insertRooms.setInt(2, InventoryIDbyList(room.getInventory(), InventoryList));
						insertRooms.setInt(3, ObstacleIDbyList(room.getObstacle(), Obstacles));
						insertRooms.setInt(4, room.getRoomId());
						if(room.getIsFound()) {
							insertRooms.setInt(5, 1);
						}else {
							insertRooms.setInt(5, 0);
						}
						insertRooms.addBatch();
					}
					insertRooms.executeBatch();
					
					System.out.println("Rooms table populated");
					
					insertObstacles = conn.prepareStatement("insert into obstacle (description, status, "
							+ "requirement) values (?, ?, ?)");
					for (Obstacle obstacle : Obstacles) {
						insertObstacles.setString(1, obstacle.getDescription());
						insertObstacles.setString(2, obstacle.getStatus());
						insertObstacles.setInt(3, ItemIDbyList(obstacle.getRequirement(), Items));

						insertObstacles.addBatch();
					}
					insertObstacles.executeBatch();
					
					System.out.println("Obstacles table populated");
					
					insertMaps = conn.prepareStatement("insert into roomMap (roomId, direction, mappedRoomId) values (?, ?, ?)");
					int counter=0;
					for (HashMap<String, Integer> map : Maps) {
						counter++;
						Set<String> keys = map.keySet();
						ArrayList<String> strings = new ArrayList<String>();
						for(String s : keys) {
							strings.add(s);
						}
						
						for(int i=0; i<=strings.size()-1; i++) {
							insertMaps.setInt(1, counter);
							insertMaps.setString(2, strings.get(i));
							insertMaps.setInt(3, map.get(strings.get(i)));
							insertMaps.addBatch();
						}
					}
					insertMaps.executeBatch();

					System.out.println("Maps table populated");
					
					insertMessages = conn.prepareStatement("insert into textHistory (message, playerAction) values (?, ?)");
					for (Message<String,Integer> message : Messages) {
						insertMessages.setString(1, message.getMessage());
						insertMessages.setInt(2, message.getPlayerAction());
						insertMessages.addBatch();
					}
					insertMessages.executeBatch();
					
					return true;
				} finally {
					DBUtil.closeQuietly(insertAbilities);
					DBUtil.closeQuietly(insertAbilitiesList);
					DBUtil.closeQuietly(insertInventoryList);	
					DBUtil.closeQuietly(insertItems);
					DBUtil.closeQuietly(insertItemList);
					DBUtil.closeQuietly(insertRooms);
					DBUtil.closeQuietly(insertObstacles);
					DBUtil.closeQuietly(insertMaps);
					DBUtil.closeQuietly(insertEnemies);
					DBUtil.closeQuietly(insertPlayers);
					DBUtil.closeQuietly(insertNPCs);
				}
			}
		});
	}
	
	// The main method creates the database tables and loads the initial data.
	public static void main(String[] args) throws IOException {
		System.out.println("Creating tables...");
		DerbyDatabase db = new DerbyDatabase();
		db.createTables();
		
		System.out.println("Loading initial data...");
		db.loadInitialData();
		
		System.out.println("Labyrinth DB successfully initialized!");
	}
	
	public static int AbilityIDbyList(ArrayList<Ability> Inner, List<ArrayList<Ability>> Outer) {
		int count=0;
		for(ArrayList<Ability> arr : Outer) {
			count++;
			if (arr.equals(Inner)) {
				return count;
			}
		}
		return -1;
	}
	public static int InventoryIDbyList(Inventory Inner, List<Inventory> Outer) {
		int count=0;
		for(Inventory arr : Outer) {
			count++;
			if (arr.equals(Inner)) {
				return count;
			}
		}
		return -1;
	}
	public static int ObstacleIDbyList(Obstacle Inner, List<Obstacle> Outer) {
		int count=0;
		for(Obstacle arr : Outer) {
			count++;
			if (arr.equals(Inner)) {
				return count;
			}
		}
		return -1;
	}
	public static int ItemIDbyList(Item Inner, List<Item> Outer) {
		int count=0;
		for(Item arr : Outer) {
			count++;
			if (arr.equals(Inner)) {
				return count;
			}
		}
		return -1;
	}

	@Override
	public List<Player> findAllPlayers() {
		return executeTransaction(new Transaction<List<Player>>() {
			@Override
			public List<Player> execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				PreparedStatement stmt2 = null;
				PreparedStatement stmt3 = null;
				PreparedStatement stmt4 = null;
				PreparedStatement stmt5 = null;
				
				ResultSet resultSet = null;
				ResultSet resultSet2 = null;
				ResultSet resultSet3 = null;
				ResultSet resultSet4 = null;
				ResultSet resultSet5 = null;
				
				try {
					stmt = conn.prepareStatement(
							"select player.* " +
							"  from  player " 
					);
					resultSet = stmt.executeQuery();
					
					List<Player> result = new ArrayList<Player>();
					
					Boolean found = false;
					while (resultSet.next()) {
						
						found = true;
						
						stmt2 = conn.prepareStatement(
								"select abilityList.* " +
								"  from  abilityList " +
								"  where abilityList.abilityList_id = ?"
						);
						
						stmt2.setInt(1, resultSet.getInt(10));
						
						resultSet2 = stmt2.executeQuery();
						
						ArrayList<Ability> tempAbilList = new ArrayList<Ability>();
						
						resultSet2.next();
						for (int i=2; i<=6; i++) {
							stmt3 = conn.prepareStatement(
									"select ability.* " +
									"  from  ability " +
									"  where ability.name = ?"
							);
						
							System.out.println(resultSet2.getString(i));
							stmt3.setString(1, resultSet2.getString(i));
							
							resultSet3 = stmt3.executeQuery();
							System.out.println("I executed query 3!");
							Ability abil = new Ability(null, null, null, null, 0, 0);
							
							System.out.println(resultSet3.next());
							
							loadAbility(abil, resultSet3, 2);
							
							tempAbilList.add(abil);
						}
						
						
						stmt4 = conn.prepareStatement(
								"select room.* " +
								"  from  room " +
								"  where room.room_id = ?"
						);
						
						stmt4.setInt(1, resultSet.getInt(13));
						
						resultSet4 = stmt4.executeQuery();
						System.out.println("I executed query 4!");
						stmt5 = conn.prepareStatement(
								"select roomMap.* " +
								"  from  roomMap " +
								"  where roomMap.roomId = ?"
						);
						
						resultSet4.next();
						stmt5.setInt(1, resultSet4.getInt(5));
						
						resultSet5 = stmt5.executeQuery();
						System.out.println("I executed query 5!");
						HashMap<String, Integer> tmpMap = new HashMap<String, Integer>();
						while(resultSet5.next()) {
							tmpMap.put(resultSet5.getString(2), resultSet5.getInt(3));
						}
						
						Room room = new Room(null, null, null, null, false, 0);
						loadRoom(room, resultSet4, 1, tmpMap);
					
						
						Player player = new Player(0, 0, 0, 0, 0, 0, 0, 0, null, null, null, null, false, null);
						loadPlayer(player, resultSet, 1, tempAbilList, room);
						

						result.add(player);
					}
					
					// check if the title was found
					if (!found) {
						System.out.println("No players were found!");
					}
					
					return result;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
					DBUtil.closeQuietly(resultSet2);
					DBUtil.closeQuietly(stmt2);
					DBUtil.closeQuietly(resultSet3);
					DBUtil.closeQuietly(stmt3);
					DBUtil.closeQuietly(resultSet4);
					DBUtil.closeQuietly(stmt4);
					DBUtil.closeQuietly(resultSet5);
					DBUtil.closeQuietly(stmt5);
					
				}
			}
		});
	}
	

	@Override
	public List<Room> findAllRooms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Enemy> findAllEnemies() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NPC> findAllNPCs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Message<String, Integer>> findTextHistory() {
		return executeTransaction(new Transaction<List<Message<String, Integer>>>() {
			@Override
			public List<Message<String, Integer>> execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try {
					stmt = conn.prepareStatement(
							"select textHistory.* " +
							"  from  textHistory " 
					);
					
					
					List<Message<String, Integer>> result = new ArrayList<Message<String, Integer>>();
					
					resultSet = stmt.executeQuery();
					
					// for testing that a result was returned
					Boolean found = false;
					
					while (resultSet.next()) {
						found = true;
						
						Message<String, Integer> message = new Message<String, Integer>(null, 0);
						loadMessage(message, resultSet, 1);
						

						result.add(message);
					}
					
					// check if the title was found
					if (!found) {
						System.out.println("No messages were found!");
					}
					
					return result;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}
}
