package campuscreatures.profile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

import campuscreatures.battleMechanics.Battle;
import campuscreatures.battleMechanics.BattleCreature;

import java.util.*;

import campuscreatures.database.Creatures;
import android.content.Context;
import android.text.format.Time;



public class UserProfile implements Serializable {
	private String fileName;
	private String firstName;
	private String lastName;
	private String userName;
	private Boolean hasSignedUp;
	private Boolean isCurrentlyInBattle;
	private Battle currentUserBattle;

	private ArrayList<BattleCreature> creaturesList;

	private Party party;

	
	public UserProfile() {
		fileName = "userProfile";
		firstName = "firstName";
		lastName = "lastName";
		userName = "userName";
		hasSignedUp = false;

		party = new Party();


		creaturesList = new ArrayList();

		Party party = new Party();
		isCurrentlyInBattle = false;
		currentUserBattle = null;


	}
	
	//loads the saved user profile in any view and instantiates a new UserProfile objects
	public UserProfile(Context context) {
		this.loadProfile(context);
		UserProfile tempProfile = new UserProfile();
		tempProfile = tempProfile.loadProfile(context);
		fileName = tempProfile.getFileName();
		firstName = tempProfile.getFirstName();
		lastName = tempProfile.getLastName();
		userName = tempProfile.getUserName();
		hasSignedUp = tempProfile.hasSignedUp();
		party = tempProfile.getParty();
		isCurrentlyInBattle = tempProfile.userCurrentlyInBattle();
	}
	
	public boolean hasSignedUp() {
		return hasSignedUp;
	}
	
	//TODO currently returning a default profile when an error occurs.
	//^figure out something better to do. This could easily create unexpected results.
	public UserProfile loadProfile(Context context) {
		try {
			FileInputStream fis;
			fis = context.openFileInput("userProfile");
			System.out.println(context);
			try {
				ObjectInputStream is = new ObjectInputStream(fis);
				UserProfile tempProf = (UserProfile) is.readObject();
				is.close();
				return tempProf;
			} catch (StreamCorruptedException e) {
				System.out.println("here C");
				e.printStackTrace();
				return this;
			} catch (IOException e) {
				System.out.println("here D");
				e.printStackTrace();
				return this;
			} catch (ClassNotFoundException e) {
				System.out.println("here E");
				e.printStackTrace();
				return this;
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			return this;
		}
	}
	
	public void saveProfile(Context context) {
		//remove template file
		if(userProfileExists(context)) {
			File delFile = new File(context.getFilesDir(),"userProfile");
			delFile.delete();
		}
		
		//create a new file with the same name
		File profile = new File(context.getFilesDir(), "userProfile");
		FileOutputStream fos;
		ObjectOutputStream os;
		try {
			System.out.println("gere A");
			System.out.println("firstnameA = " + getFirstName() );
			fos = context.openFileOutput("userProfile", Context.MODE_PRIVATE);
			os = new ObjectOutputStream(fos);
			os.writeObject(this);
			os.close();
		} catch (FileNotFoundException e) {
			System.out.println("gere B");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("gere C");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private boolean userProfileExists(Context context) {
		File directory = context.getFilesDir();
		File filesList[] = directory.listFiles();
		int listSize = filesList.length;
		for(int i = 0; i < listSize; i++ ) {
			if(filesList[i].getName().equals("userProfile")) { //this will be the name of the file to hold the user info
				System.out.println("there is a file with name 'userProfile'");
				//System.out.println(filesList[i].);
				UserProfile tempProf = new UserProfile();
				tempProf = tempProf.loadProfile(context);
				if(tempProf!=null) {
					System.out.println("as user has signed up = " + tempProf.hasSignedUp());
				}
				return true;
			}
		}
		return false;
	}

	
	public String getFileName() {
		return fileName;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public String getWholeName() {
		return firstName + " " + lastName;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public Party getParty() {
		return party;
	}
	
	public void setInitialProfile(String first, String last, String user) {
		if (hasSignedUp) {
			return;
		}
		firstName = first;
		lastName = last;
		userName = user;
		hasSignedUp = true;
		//(lastHealed = new Time()).setToNow();
	}
	
	public void addCreature(BattleCreature creature) {
		party.addPartyMember(creature);
	}
	
	public void removeCreature(BattleCreature creature) {
		party.removePartyMember(creature);
	}
	
	//sets the current battle
	public void setBattle(Battle newBattle) {
		if(newBattle != null) {
			isCurrentlyInBattle = true;
			currentUserBattle = newBattle;
		}
	}
	
	//gets the current battle if it is not null
	public Battle getCurrentUserBattle() {
		if (isCurrentlyInBattle) {
			return currentUserBattle;
		}
		else {
			return null;
		}
	}
	
	//destroys currentBattle
	public void destroyBattle() {
		isCurrentlyInBattle = false;
		currentUserBattle = null;
	}
	
	//returns if user was in battle last time the profile was saved
	public boolean userCurrentlyInBattle() {
		return isCurrentlyInBattle;
	}
	
	
}
