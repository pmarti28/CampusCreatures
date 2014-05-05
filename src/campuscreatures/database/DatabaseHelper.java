package campuscreatures.database;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Region;
import campuscreatures.location.LocationService;
import android.util.Log;

/**
 * Class DatabaseHelper extends SQLiteOpenHelper
 * Creates a Database "creature.db" with three tables: Creatures, Player, and MapZones
 * Both tables use the same column names, but Player has 3 additional columns
 * Includes all the functions needed to access the database, tables, creatures, and values
 */

public class DatabaseHelper extends SQLiteOpenHelper {
	
	Context myContext;
	private static final String TAG = DatabaseHelper.class.getSimpleName();
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "creature.db";
	private static final String TABLE_CREATURES = "Creatures";
	private static final String TABLE_PLAYER = "Player";
	private static final String TABLE_REGIONS = "Regions";
		
	//Common column names (Creatures Table consist of only these column names)
	public static final String KEY_ID = "_id";
	private static final String COLUMN_NAME = "name";
	private static final String COLUMN_REGION = "region";
	private static final String COLUMN_DISTRICT = "district";
	private static final String COLUMN_TYPE = "type";
	private static final String COLUMN_HEALTH = "health";
	private static final String COLUMN_MAGIC = " magic";
	private static final String COLUMN_ATTACK = "attack";
	private static final String COLUMN_DEFENSE = "defense";
	private static final String COLUMN_SPEED = "speed";
	private static final String COLUMN_MPT = "moves_per_turn";
	private static final String COLUMN_EXPERIENCE = "experience";
	private static final String COLUMN_LEVEL = "level";
	private static final String COLUMN_SEEN = "seen_or_not";
	private static final String COLUMN_CAPTURED = "captured";
	
	//Column names - unique to Player Table
	private static final String PLAYER_ID = "_id";
	
	//Column names - unique to Regions Table
	private static final String ZONE_ID = "_id";
	private static final String REGION_NAME = "name";
	private static final String POINT_ONE_LAT = "Point1_Lat";
	private static final String POINT_ONE_LONG = "Point1_Long";
	private static final String POINT_TWO_LAT = "Point2_Lat";
	private static final String POINT_TWO_LONG = "Point2_Long";
	private static final String POINT_THREE_LAT = "Point3_Lat";
	private static final String POINT_THREE_LONG = "Point3_Long";
	private static final String POINT_FOUR_LAT = "Point4_Lat";
	private static final String POINT_FOUR_LONG = "Point4_Long";

	
	private static final String CREATE_CREATURES_TABLE = "CREATE TABLE " 
			+ TABLE_CREATURES + " (" 				
			+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ COLUMN_NAME + " TEXT NOT NULL, "
			+ COLUMN_REGION + " TEXT NOT NULL, " 
			+ COLUMN_DISTRICT + " TEXT NOT NULL, "
			+ COLUMN_TYPE + " TEXT NOT NULL, "
			+ COLUMN_HEALTH + " INTEGER NOT NULL, "  
			+ COLUMN_MAGIC + " INTEGER NOT NULL, "
			+ COLUMN_ATTACK + " INTEGER NOT NULL, "
			+ COLUMN_DEFENSE + " INTEGER NOT NULL, "
			+ COLUMN_SPEED + " INTEGER NOT NULL, "
			+ COLUMN_MPT + " INTEGER NOT NULL, "
			+ COLUMN_EXPERIENCE + " INTEGER NOT NULL, "
			+ COLUMN_LEVEL + " INTEGER NOT NULL, " 
			+ COLUMN_SEEN + " INTEGER NOT NULL, "
			+ COLUMN_CAPTURED + " INTEGER NOT NULL" + ")";
	
	private static final String CREATE_PLAYER_TABLE = "CREATE TABLE "
			+ TABLE_PLAYER + " (" 				
			+ PLAYER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ COLUMN_NAME + " TEXT NOT NULL, "
			+ COLUMN_REGION + " TEXT NOT NULL, " 
			+ COLUMN_DISTRICT + " TEXT NOT NULL, "
			+ COLUMN_TYPE + " TEXT NOT NULL, "
			+ COLUMN_HEALTH +  " INTEGER NOT NULL, "  
			+ COLUMN_MAGIC + " INTEGER NOT NULL, "
			+ COLUMN_ATTACK + " INTEGER NOT NULL, "
			+ COLUMN_DEFENSE + " INTEGER NOT NULL, "
			+ COLUMN_SPEED + " INTEGER NOT NULL, "
			+ COLUMN_MPT + " INTEGER NOT NULL, "
			+ COLUMN_EXPERIENCE + " INTEGER NOT NULL, "
			+ COLUMN_LEVEL + " INTEGER NOT NULL, " 
			+ COLUMN_SEEN + " INTEGER NOT NULL, "
			+ COLUMN_CAPTURED + " INTEGER NOT NULL" + ")";
	
	private static final String CREATE_REGIONS_TABLE = "CREATE TABLE "
			+ TABLE_REGIONS + " ("
			+ ZONE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ REGION_NAME + " TEXT NOT NULL, "
			+ POINT_ONE_LAT + " REAL, "
			+ POINT_ONE_LONG + " REAL, "
			+ POINT_TWO_LAT + " REAL, "
			+ POINT_TWO_LONG + " REAL, "
			+ POINT_THREE_LAT + " REAL, "
			+ POINT_THREE_LONG + " REAL, "
			+ POINT_FOUR_LAT + " REAL, "
			+ POINT_FOUR_LONG + " REAL" + ")";
	
/*	private static final String CREATE_REGIONS_TABLE = "CREATE TABLE " 
			+ TABLE_REGIONS + " ("
			+ ZONE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ REGION_NAME + " TEXT NOT NULL, "
			+ POINT_ONE_LAT + " STRING NOT NULL, "
			+ POINT_ONE_LONG + " STRING NOT NULL, "
			+ POINT_TWO_LAT + " STRING NOT NULL, "
			+ POINT_TWO_LONG + " STRING NOT NULL, "
			+ POINT_THREE_LAT +  " STRING NOT NULL, "
			+ POINT_THREE_LONG + " STRING NOT NULL, "
			+ POINT_FOUR_LAT + " STRING NOT NULL, "
			+ POINT_FOUR_LONG + " STRING NOT NULL" + ")"; */
	
	// MySQLiteHelper constructor must call the super class constructor
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.myContext = context;
	}
	
	@Override
	public void onCreate(SQLiteDatabase database) {
		Log.d("onCreate function in DatabaseHelper", "Creating DB for first time");
		database.execSQL(CREATE_CREATURES_TABLE);
		database.execSQL(CREATE_PLAYER_TABLE);
		database.execSQL(CREATE_REGIONS_TABLE);
		Log.d("Created Tables onCreate:", "Creatures Table and Player Table");
	}

	/* Method is called for an upgrade of the database, 
	 * if table exists then drop it and call on create method. 
	 */
	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		Log.w(DatabaseHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data.");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_CREATURES); 
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYER);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_REGIONS);
		onCreate(database); 
	}
	/**
	 * 
	 * Creatures Table - 15 columns
	 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * | ID | Name | Region | District | Type | Health | Magic | Attack | Defense | Speed | Moves Per Turn | Experience | Level | Seen | Cap |   
	 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * |____|______|________|__________|______|________|_______|________|_________|_______|________________|____________|_______|______|_____|    
	 * |____|______|________|__________|______|________|_______|________|_________|_______|________________|____________|_______|______|_____|   
	 * |____|______|________|__________|______|________|_______|________|_________|_______|________________|____________|_______|______|_____|  
	 * |____|______|________|__________|______|________|_______|________|_________|_______|________________|____________|_______|______|_____|  
	 * 
	 * All CRUD Operations (Create, Read, Update, Delete)
	 * 
	 */
	
	
	// -------------------------------"CREATURES TABLE" methods -------------------------------//
	
	/**
	 * INSERTING NEW CREATURE 
	 * 	1. get reference to writable Database
	 * 	2. create ContentValues to add key "column"/value
	 * 	3. insert rows 
	 *	4. return creature_id ... which creates a key_ID in the table
	 */
	public long addCreature(Creatures creature){
		
		SQLiteDatabase database = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();		
		//Log.d("Putting name into name column: ", creature.getName());
		values.put(COLUMN_NAME, creature.getName());				
		values.put(COLUMN_REGION, creature.getRegion());			
		values.put(COLUMN_DISTRICT, creature.getDistrict());		
		values.put(COLUMN_TYPE, creature.getType());				
		values.put(COLUMN_HEALTH, creature.getHealth());			
		values.put(COLUMN_MAGIC, creature.getMagic());				
		values.put(COLUMN_ATTACK, creature.getAttack());			
		values.put(COLUMN_DEFENSE, creature.getDefense());			
		values.put(COLUMN_SPEED, creature.getSpeed());				
		values.put(COLUMN_MPT, creature.getMovesPerTurn());			
		values.put(COLUMN_EXPERIENCE, creature.getExperience());	
		values.put(COLUMN_LEVEL, creature.getLevel());		
		values.put(COLUMN_SEEN, creature.getSeen());
		values.put(COLUMN_CAPTURED, creature.getCaptured());
		
		//Log.d("About to insert the entire row for:", creature.getName());
		long creature_id = database.insert(TABLE_CREATURES, null, values);
		
		//Log.d("Inserted the row for: ", creature.getName());
		//Log.d("KEY ID for: ", creature.getName() + " is " + creature.getId());
		
		return creature_id;
		//Close database connection
		//DatabaseHelper.database.close();
	}
	
	/** 
	 * READING ROW(S) 
	 *  1. get reference to readable Database
	 * 	2. build query statement and create cursor
	 * 	3. if we get results, get the first one
	 * 	4. build creatures object
	 * 	5. return creature
	 * 
	 */
	public Creatures getCreature(int creature_id){
		SQLiteDatabase database = this.getReadableDatabase();	
		
		String selectQuery = "SELECT * FROM " + TABLE_CREATURES + " WHERE "
	            + KEY_ID + " = " + creature_id;
		Cursor cursor = database.rawQuery(selectQuery, null);

		Creatures creature = null; 
		if (cursor != null)
			cursor.moveToFirst();
		
		creature = new Creatures();
		creature.setId(cursor.getInt(0));
		creature.setName(cursor.getString(1));
		creature.setRegion(cursor.getString(2));
		creature.setDistrict(cursor.getString(3));
		creature.setType(cursor.getString(4));
		creature.setHealth(cursor.getInt(5));
		creature.setMagic(cursor.getInt(6));
		creature.setAttack(cursor.getInt(7));
		creature.setDefense(cursor.getInt(8));
		creature.setSpeed(cursor.getInt(9));
		creature.setMovesPerTurn(cursor.getInt(10));
		creature.setExperience(cursor.getInt(11));
		creature.setLevel(cursor.getInt(12));
		creature.setSeen(cursor.getInt(13));
		creature.setCaptured(cursor.getInt(14));
		//Log.d("getCreature(" + creature_id + ")", creature.toString());
		return creature;
	}
		
	/**
	* getAllCreatures() - returns a list of all creatures in database
	* 	1. get reference to writable Database
	* 	2. build the query statement, create cursor
	* 	3. go over each row, build creature
	* 	4. add creature to creatureList 
	* 
	*/
	public List<Creatures> getAllCreatures(){
		List<Creatures> creaturesList = new ArrayList<Creatures>();
		SQLiteDatabase database = this.getReadableDatabase();
		
		String selectQuery = "SELECT * FROM " + TABLE_CREATURES;
		//Log.d("Logging Query for getAllCreatures", selectQuery);
		Cursor cursor = database.rawQuery(selectQuery, null);
		
		Creatures creature = null;
		if (cursor.moveToFirst()) {
			do {
				creature = new Creatures();
				creature.setId(cursor.getInt(0));
				creature.setName(cursor.getString(1));
				creature.setRegion(cursor.getString(2));
				creature.setDistrict(cursor.getString(3));
				creature.setType(cursor.getString(4));
				creature.setHealth(cursor.getInt(5));
				creature.setMagic(cursor.getInt(6));
				creature.setAttack(cursor.getInt(7));
				creature.setDefense(cursor.getInt(8));
				creature.setSpeed(cursor.getInt(9));
				creature.setMovesPerTurn(cursor.getInt(10));
				creature.setExperience(cursor.getInt(11));
				creature.setLevel(cursor.getInt(12));
				creature.setSeen(cursor.getInt(13));
				creature.setCaptured(cursor.getInt(14));
				creaturesList.add(creature);
				//Log.d("ID and NAME", String.valueOf(creature.getId()) + " " + creature.getName());
			} while (cursor.moveToNext());
		}
		//Log.d("getAllCreatures().toString() function", creaturesList.toString());
		database.close();
		return creaturesList;		
	}
	
	public int getCreaturesCount(){
		String countQuery = " SELECT * FROM " + TABLE_CREATURES;
		SQLiteDatabase database = this.getReadableDatabase();
		Cursor cursor = database.rawQuery(countQuery, null);
		int count = cursor.getCount();
		cursor.close();
		Log.d("Logging countQuery getCreaturesCount()", String.valueOf(count));
		return count;
	}
	/**
	 * PJ's
	 * getAllCreaturesByRegion() - used to find creatures in a certain region and district
	 * @param region
	 * @param district
	 * @return
	 * 	1. get reference to readable Database
	 * 	2. build the query statement, create cursor
	 * 	3. go over each row, build creature (if in that region)
	 * 	4. add creature to the list
	 * 	5. return that list
	 */
	public List<Creatures> getAllCreaturesByRegion(String region){
		
		List<Creatures> regionCreatures = new ArrayList<Creatures>();
		String[] resultColumns = new String[]{KEY_ID, COLUMN_NAME, COLUMN_REGION, COLUMN_DISTRICT, COLUMN_TYPE, COLUMN_HEALTH,
				COLUMN_MAGIC, COLUMN_ATTACK, COLUMN_DEFENSE, COLUMN_SPEED, COLUMN_MPT, COLUMN_EXPERIENCE, COLUMN_LEVEL, 
				COLUMN_SEEN, COLUMN_CAPTURED}; 
		String[] condition = {region};
		SQLiteDatabase database = this.getReadableDatabase();
		Cursor cursor = database.query(TABLE_CREATURES, resultColumns, COLUMN_REGION + "=" + "?", condition, null, null, null);
		Creatures creature = null;
	
		if (cursor.moveToFirst()){
			do {
				creature = new Creatures();
				creature.setId(cursor.getInt(0));
				creature.setName(cursor.getString(1));
				creature.setRegion(cursor.getString(2));
				creature.setDistrict(cursor.getString(3));
				creature.setType(cursor.getString(4));
				creature.setHealth(cursor.getInt(5));
				creature.setMagic(cursor.getInt(6));
				creature.setAttack(cursor.getInt(7));
				creature.setDefense(cursor.getInt(8));
				creature.setSpeed(cursor.getInt(9));
				creature.setMovesPerTurn(cursor.getInt(10));
				creature.setExperience(cursor.getInt(11));
				creature.setLevel(cursor.getInt(12));
				creature.setSeen(cursor.getInt(13));
				creature.setCaptured(cursor.getInt(14));
				regionCreatures.add(creature);
			} while (cursor.moveToNext());
		}
		Log.d("Logging getAllCreaturesByRegion()", regionCreatures.toString());
		database.close();
		return regionCreatures;
	}
	

/**	 updateCreature()
 * @param creature
 * @return
 * 	1. get reference to writable Database
 * 	2. create ContentValues to add key "column"/value
 * 	3. update row using database.update(table, column/value, selections, selection arguments)
 * 	4. return creature
 */
	public long updateCreature(Creatures creature){
		
		SQLiteDatabase database = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME, creature.getName());			
		values.put(COLUMN_REGION, creature.getRegion());		
		values.put(COLUMN_DISTRICT, creature.getDistrict());	
		values.put(COLUMN_TYPE, creature.getType());	 		
		values.put(COLUMN_HEALTH, creature.getHealth());		
		values.put(COLUMN_MAGIC, creature.getMagic());			
		values.put(COLUMN_ATTACK, creature.getAttack());		
		values.put(COLUMN_DEFENSE, creature.getDefense());		
		values.put(COLUMN_SPEED, creature.getSpeed());		
		values.put(COLUMN_MPT, creature.getMovesPerTurn());		
		values.put(COLUMN_EXPERIENCE, creature.getExperience());		
		values.put(COLUMN_LEVEL, creature.getLevel());	
		values.put(COLUMN_SEEN, creature.getSeen());		
		values.put(COLUMN_CAPTURED, creature.getCaptured());
		
		long x = database.update(TABLE_CREATURES, 				 
				values, 										 	
				KEY_ID + " = ?", 									
				new String[] { String.valueOf(creature.getId()) });	
		//database.close();
		return x;
	}
	
	/**
	 * deleteCreature()
	 * @param creature_id
	 * 	1. get reference to writable Database
	 * 	2. use database.delete(table, selections, selection arguments)
	 * 	3. close database
	 */
	public void deleteCreature(long creature_id){
		
		SQLiteDatabase database = this.getWritableDatabase();
		database.delete(TABLE_CREATURES,							
				KEY_ID + " = ?",   									
				new String[] {String.valueOf(creature_id) });	
		database.close();
		Log.d("deleteCreature", String.valueOf(creature_id));
	}
	
	/**
	 * 
	 * Player Table - 15 columns
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * | ID | Name | Region | District | Type | Health | Magic | Attack | Defense | Speed | Moves Per Turn | Experience | Level | Seen | Captured |   
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * |____|______|________|__________|______|________|_______|________|_________|_______|________________|____________|_______|______|__________|    
	 * |____|______|________|__________|______|________|_______|________|_________|_______|________________|____________|_______|______|__________|  
	 * |____|______|________|__________|______|________|_______|________|_________|_______|________________|____________|_______|______|__________|  
	 * |____|______|________|__________|______|________|_______|________|_________|_______|________________|____________|_______|______|__________|  
	 * 
	 * All CRUD Operations (Create, Read, Update, Delete)
	 * 
	 */
	
	// -------------------------------"PLAYER TABLE" methods -------------------------------//
	
	/**
	 * INSERTING NEW CREATURE  
	 * 	1. get reference to writable Database
	 * 	2. create ContentValues to add key "column"/value
	 * 	3. insert rows 
	 *	4. return creature_id ... which creates a key_ID in the table
	 */
	public long addPlayerCreature(Player playerCreature){
		
		SQLiteDatabase database = this.getWritableDatabase(); 	
		ContentValues playerValues = new ContentValues();
		
		//Log.d("Putting creature name into player table's name column", playerCreature.getName());
		playerValues.put(COLUMN_NAME, playerCreature.getName());
		playerValues.put(COLUMN_REGION, playerCreature.getRegion());			
		playerValues.put(COLUMN_DISTRICT, playerCreature.getDistrict());		
		playerValues.put(COLUMN_TYPE, playerCreature.getType());				
		playerValues.put(COLUMN_HEALTH, playerCreature.getHealth());		
		playerValues.put(COLUMN_MAGIC, playerCreature.getMagic());			
		playerValues.put(COLUMN_ATTACK, playerCreature.getAttack());		
		playerValues.put(COLUMN_DEFENSE, playerCreature.getDefense());		
		playerValues.put(COLUMN_SPEED, playerCreature.getSpeed());			
		playerValues.put(COLUMN_MPT, playerCreature.getMovesPerTurn());		
		playerValues.put(COLUMN_EXPERIENCE, playerCreature.getExperience());
		playerValues.put(COLUMN_LEVEL, playerCreature.getLevel());			
		playerValues.put(COLUMN_SEEN, playerCreature.getSeen());
		playerValues.put(COLUMN_CAPTURED, playerCreature.getCaptured());
	
		//Log.d("About to insert the entire row for:", playerCreature.getName());
		long playerCreature_ID = database.insert(TABLE_PLAYER, null, playerValues);
		//Log.d("Inserted the row for: ", playerCreature.getName());
		//Log.d("KEY ID for: ", playerCreature.getName() + " is " + playerCreature.getId());
		
		return playerCreature_ID;
		//database.close();
	}
	
	/** 
	 * READING ROW(S) 
	 *  1. get reference to readable Database
	 * 	2. build query statement and create cursor
	 * 	3. if we get results, get the first one
	 * 	4. build creatures object
	 * 	5. return creature
	 */
	public Player getPlayerCreature(int playerCreature_id){
		
		SQLiteDatabase database = this.getReadableDatabase();
		String selectQuery = "SELECT * FROM " + TABLE_PLAYER + " WHERE "
	            + KEY_ID + " = " + playerCreature_id;	
		Cursor cursor = database.rawQuery(selectQuery, null);
		
		Player playerCreature = null; 
		if (cursor != null)
			cursor.moveToFirst();
		
		playerCreature = new Player();
		playerCreature.setId(cursor.getInt(0));
		playerCreature.setName(cursor.getString(1));
		playerCreature.setRegion(cursor.getString(2));
		playerCreature.setDistrict(cursor.getString(3));
		playerCreature.setType(cursor.getString(4));
		playerCreature.setHealth(cursor.getInt(5));
		playerCreature.setMagic(cursor.getInt(6));
		playerCreature.setAttack(cursor.getInt(7));
		playerCreature.setDefense(cursor.getInt(8));
		playerCreature.setSpeed(cursor.getInt(9));
		playerCreature.setMovesPerTurn(cursor.getInt(10));
		playerCreature.setExperience(cursor.getInt(11));
		playerCreature.setLevel(cursor.getInt(12));
		playerCreature.setSeen(cursor.getInt(13));
		playerCreature.setCaptured(cursor.getInt(14));
		
		//Log.d("getCreature(" + playerCreature_id + ")", playerCreature.toString());
		return playerCreature;		
	}	
	
	/**
	* getAllPlayerCreatures() - returns a list of all creatures in database
	* 	1. get reference to writable Database
	* 	2. build the query statement, create cursor
	* 	3. go over each row, build creature
	* 	4. add creature to creatureList 
	* 
	*/
	public List<Player> getAllPlayerCreatures(){
		List<Player> playercreaturesList = new ArrayList<Player>();
		SQLiteDatabase database = this.getReadableDatabase();
		
		String selectQuery = "SELECT * FROM " + TABLE_PLAYER;
		//Log.d("Logging Query for getAllPlayerCreatures", selectQuery);
		Cursor cursor = database.rawQuery(selectQuery, null);
		
		Player pcreature = null;
		if (cursor.moveToFirst()) {
			do {
				pcreature = new Player();
				pcreature.setId(cursor.getInt(0));
				pcreature.setName(cursor.getString(1));
				pcreature.setRegion(cursor.getString(2));
				pcreature.setDistrict(cursor.getString(3));
				pcreature.setType(cursor.getString(4));
				pcreature.setHealth(cursor.getInt(5));
				pcreature.setMagic(cursor.getInt(6));
				pcreature.setAttack(cursor.getInt(7));
				pcreature.setDefense(cursor.getInt(8));
				pcreature.setSpeed(cursor.getInt(9));
				pcreature.setMovesPerTurn(cursor.getInt(10));
				pcreature.setExperience(cursor.getInt(11));
				pcreature.setLevel(cursor.getInt(12));
				pcreature.setSeen(cursor.getInt(13));
				pcreature.setCaptured(cursor.getInt(14));
				playercreaturesList.add(pcreature);
				Log.d("ID and NAME", String.valueOf(pcreature.getId()) + " " + pcreature.getName());
			} while (cursor.moveToNext());
		}
		//Log.d("getAllCreatures().toString() function", playercreaturesList.toString());
		database.close();
		return playercreaturesList;		
	}
		
	/**
	 * 
	 * getAllPlayerCreaturesByRegion() - used to find creatures in a certain region and district
	 * @param region
	 * @param district
	 * @return
	 * 	1. get reference to readable Database
	 * 	2. build the query statement, create cursor
	 * 	3. go over each row, build creature (if in that region)
	 * 	4. add creature to the list
	 * 	5. return that list
	 */
	public List<Player> getAllPlayerCreaturesByRegion(String region){
		
		List<Player> playerregionCreatures = new ArrayList<Player>();
		String[] resultColumns = new String[]{PLAYER_ID, COLUMN_NAME, COLUMN_REGION, COLUMN_DISTRICT, COLUMN_TYPE, COLUMN_HEALTH,
				COLUMN_MAGIC, COLUMN_ATTACK, COLUMN_DEFENSE, COLUMN_SPEED, COLUMN_MPT, COLUMN_EXPERIENCE, COLUMN_LEVEL,
				COLUMN_SEEN, COLUMN_CAPTURED}; 
		String[] condition = {region};
		SQLiteDatabase database = this.getReadableDatabase();
		Cursor cursor = database.query(TABLE_PLAYER, resultColumns, COLUMN_REGION + "=" + "?", condition, null, null, null);
		Player pcreature = null;
	
		if (cursor.moveToFirst()){
			do {
				pcreature = new Player();
				pcreature.setId(cursor.getInt(0));
				pcreature.setName(cursor.getString(1));
				pcreature.setRegion(cursor.getString(2));
				pcreature.setDistrict(cursor.getString(3));
				pcreature.setType(cursor.getString(4));
				pcreature.setHealth(cursor.getInt(5));
				pcreature.setMagic(cursor.getInt(6));
				pcreature.setAttack(cursor.getInt(7));
				pcreature.setDefense(cursor.getInt(8));
				pcreature.setSpeed(cursor.getInt(9));
				pcreature.setMovesPerTurn(cursor.getInt(10));
				pcreature.setExperience(cursor.getInt(11));
				pcreature.setLevel(cursor.getInt(12));
				pcreature.setSeen(cursor.getInt(13));
				pcreature.setCaptured(cursor.getInt(14));
				playerregionCreatures.add(pcreature);
			} while (cursor.moveToNext());
		}
		//Log.d("Logging getAllPlayerCreaturesByRegion()", playerregionCreatures.toString());
		return playerregionCreatures;
	}
	
	/**
	* updatePlayerCreature()
	* @param creature
	* @return
	* 	1. get reference to writable Database
	* 	2. create ContentValues to add key "column"/value
	* 	3. update row using database.update(table, column/value, selections, selection arguments)
	* 	4. return creature
	*/
	public long updatePlayerCreature(Player pcreature){
		SQLiteDatabase database = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME, pcreature.getName());			
		values.put(COLUMN_REGION, pcreature.getRegion());		
		values.put(COLUMN_DISTRICT, pcreature.getDistrict());	
		values.put(COLUMN_TYPE, pcreature.getType());	 		
		values.put(COLUMN_HEALTH, pcreature.getHealth());		
		values.put(COLUMN_MAGIC, pcreature.getMagic());			
		values.put(COLUMN_ATTACK, pcreature.getAttack());		
		values.put(COLUMN_DEFENSE, pcreature.getDefense());		
		values.put(COLUMN_SPEED, pcreature.getSpeed());		
		values.put(COLUMN_MPT, pcreature.getMovesPerTurn());		
		values.put(COLUMN_EXPERIENCE, pcreature.getExperience());		
		values.put(COLUMN_LEVEL, pcreature.getLevel());			
		values.put(COLUMN_SEEN, pcreature.getSeen());
		values.put(COLUMN_CAPTURED, pcreature.getCaptured());
		
		long x = database.update(TABLE_PLAYER, 				 
				values, 										 	
				PLAYER_ID + " = ?", 									
				new String[] { String.valueOf(pcreature.getId()) });	
		//database.close();
		return x;
	}
	
	/**
	 * deletePlayerCreature()
	 * @param creature_id
	 * 	1. get reference to writable Database
	 * 	2. use database.delete(table, selections, selection arguments)
	 * 	3. close database
	 */
	public void deletePlayerCreature(long creature_id){
		
		SQLiteDatabase database = this.getWritableDatabase();
		database.delete(TABLE_PLAYER,							
				PLAYER_ID + " = ?",   									
				new String[] {String.valueOf(creature_id) });	
		database.close();
		//Log.d("deletePlayerCreature", String.valueOf(creature_id));
	}
	
	public int getPlayerCreaturesCount(){
		String countQuery = " SELECT * FROM " + TABLE_PLAYER;
		SQLiteDatabase database = this.getReadableDatabase();
		Cursor cursor = database.rawQuery(countQuery, null);
		int count = cursor.getCount();
		cursor.close();
		Log.d("Logging countQuery getPlayerCreaturesCount()", String.valueOf(count));
		return count;
	}
	
	/**
	 * 
	 * Regions Table - 1 columns
	 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * | ID | Name | Point 1 Lat | Point 1 Long | Point 2 Lat | Point 2 Long | Point 3 Lat | Point 3 Long | Point 4 Lat | Point 4 Long |
	 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * |____|______|_____________|______________|_____________|______________|_____________|______________|_____________|______________|   
	 * |____|______|_____________|______________|_____________|______________|_____________|______________|_____________|______________|   
	 * |____|______|_____________|______________|_____________|______________|_____________|______________|_____________|______________|   
	 * |____|______|_____________|______________|_____________|______________|_____________|______________|_____________|______________|   
	 * 
	 * All CRUD Operations (Create, Read, Update, Delete)
	 * 
	 */
	public long addRegion(MapZones region){
		
		SQLiteDatabase database = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();		
		//Log.d("Putting name into name column: ", creature.getName());
		values.put(REGION_NAME, region.getZoneName());			
		values.put(POINT_ONE_LAT, region.getLocationOneLat());			
		values.put(POINT_ONE_LONG, region.getLocationOneLong());
		values.put(POINT_TWO_LAT, region.getLocationTwoLat());			
		values.put(POINT_TWO_LONG, region.getLocationTwoLong());
		values.put(POINT_THREE_LAT, region.getLocationThreeLat());			
		values.put(POINT_THREE_LONG, region.getLocationThreeLong());
		values.put(POINT_FOUR_LAT, region.getLocationFourLat());			
		values.put(POINT_FOUR_LONG, region.getLocationFourLong());
		
		//Log.d("About to insert points for region:", region.getZoneName());
		long region_id = database.insert(TABLE_REGIONS, null, values);
		
		//Log.d("Inserted the row for: ", region.getZoneName());
		//Log.d("KEY ID for: ", creature.getName() + " is " + creature.getId());
		
		return region_id;
		//Close database connection
		//DatabaseHelper.database.close();
	}
	
	public MapZones getRegion(int region_id){
		SQLiteDatabase database = this.getReadableDatabase();	
		
		String selectQuery = "SELECT * FROM " + TABLE_REGIONS + " WHERE "
	            + ZONE_ID + " = " + region_id;
		Cursor cursor = database.rawQuery(selectQuery, null);

		MapZones region = null; 
		if (cursor != null)
			cursor.moveToFirst();
		
		region = new MapZones();
		region.setID(cursor.getInt(0));
		region.setZoneName(cursor.getString(1));
		region.setLocationOneLat(cursor.getInt(2));
		region.setLocationOneLong(cursor.getInt(3));
		region.setLocationTwoLat(cursor.getInt(4));
		region.setLocationTwoLong(cursor.getInt(5));
		region.setLocationThreeLat(cursor.getInt(6));
		region.setLocationThreeLong(cursor.getInt(7));
		region.setLocationFourLat(cursor.getInt(8));
		region.setLocationFourLong(cursor.getInt(9));
		Log.d("getRegion(" + region_id + ")", region.getZoneName());
		return region;
	}
	public List<MapZones> getAllRegions(){
		List<MapZones> regionsList = new ArrayList<MapZones>();
		SQLiteDatabase database = this.getReadableDatabase();
		
		String selectQuery = "SELECT * FROM " + TABLE_REGIONS;
		//Log.d("Logging Query for getAllCreatures", selectQuery);
		Cursor cursor = database.rawQuery(selectQuery, null);
		
		MapZones region = null;
		if (cursor.moveToFirst()) {
			do {
				region = new MapZones();
				region.setID(cursor.getInt(0));
				region.setZoneName(cursor.getString(1));
				region.setLocationOneLat(cursor.getInt(2));
				region.setLocationOneLong(cursor.getInt(3));
				region.setLocationTwoLat(cursor.getInt(4));
				region.setLocationTwoLong(cursor.getInt(5));
				region.setLocationThreeLat(cursor.getInt(6));
				region.setLocationThreeLong(cursor.getInt(7));
				region.setLocationFourLat(cursor.getInt(8));
				region.setLocationFourLong(cursor.getInt(9));
				regionsList.add(region);
				//Log.d("ID and NAME", String.valueOf(creature.getId()) + " " + creature.getName());
			} while (cursor.moveToNext());
		}
		//Log.d("getAllCreatures().toString() function", creaturesList.toString());
		database.close();
		return regionsList;		
	}

	public int getRegionsCount(){
		String countQuery = " SELECT * FROM " + TABLE_REGIONS;
		SQLiteDatabase database = this.getReadableDatabase();
		Cursor cursor = database.rawQuery(countQuery, null);
		int count = cursor.getCount();
		cursor.close();
		Log.d("Logging countQuery getRegionsCount()", String.valueOf(count));
		return count;
	}
	
	public long updateRegion(MapZones region){
		
		SQLiteDatabase database = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();		
		//Log.d("Putting name into name column: ", creature.getName());
		values.put(REGION_NAME, region.getZoneName());			
		values.put(POINT_ONE_LAT, region.getLocationOneLat());			
		values.put(POINT_ONE_LONG, region.getLocationOneLong());
		values.put(POINT_TWO_LAT, region.getLocationTwoLat());			
		values.put(POINT_TWO_LONG, region.getLocationTwoLong());
		values.put(POINT_THREE_LAT, region.getLocationThreeLat());			
		values.put(POINT_THREE_LONG, region.getLocationThreeLong());
		values.put(POINT_FOUR_LAT, region.getLocationFourLat());			
		values.put(POINT_FOUR_LONG, region.getLocationFourLong());
		
		long x = database.update(TABLE_REGIONS, 				 
				values, 										 	
				ZONE_ID + " = ?", 									
				new String[] { String.valueOf(region.getID()) });	
		//database.close();
		return x;
	}
}