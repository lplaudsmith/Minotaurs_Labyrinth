package edu.ycp.CS320_Minotaurs_Labyrinth.labyrinthdb.persist;

import java.util.List;

import edu.ycp.CS320_Minotaurs_Labyrinth.classes.Enemy;
import edu.ycp.CS320_Minotaurs_Labyrinth.classes.Message;
import edu.ycp.CS320_Minotaurs_Labyrinth.classes.NPC;
import edu.ycp.CS320_Minotaurs_Labyrinth.classes.Player;
import edu.ycp.CS320_Minotaurs_Labyrinth.classes.Room;

public interface IDatabase {

	public List<Player> findAllPlayers();
	
	public List<Room> findAllRooms();
	
	public List<Enemy> findAllEnemies();
	
	public List<NPC> findAllNPCs();
	
	public List<Message<String, Integer>> findTextHistory();
	
	public Player updatePlayer(Player newPlayer);
	
	public List<Message<String, Integer>> updateTextHistory(List<Message<String, Integer>> newMessages);
	
	public List<Enemy> updateEnemies(List<Enemy> enemyList);
	
	
}
