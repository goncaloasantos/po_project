package prr;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import prr.Communications.Communication;
import prr.clients.Client;
import prr.exceptions.ImportFileException;
import prr.exceptions.TerminalDoesntExistException;
import prr.exceptions.TerminalExistsException;
import prr.exceptions.UnrecognizedEntryException;
import prr.exceptions.ClientDoesntExistException;
import prr.exceptions.InvalidTerminalIDException;
import prr.exceptions.ClientExistsException;
import prr.terminals.Terminal;

/**
 * Class Store implements a store.
 */
public class Network implements Serializable {

	/** 
	 * Serial number for serialization.
	 */
	private static final long serialVersionUID = 202208091753L;

	/** 
	 * Clients.
	 */
	private SortedMap<String, Client> clients = new TreeMap<String, Client>(String.CASE_INSENSITIVE_ORDER);

	/** 
	 * Terminals. 
	*/
	private Map<String, Terminal> terminals = new TreeMap<String, Terminal>();

	/** 
	 * Terminals with no communications.
	 */
	private Map<String, Terminal> terminalsWNCommunication = new TreeMap<String, Terminal>();


	private List<Communication> allCommunications = new ArrayList<Communication>();
	/**
	 * An iterator to implement in methods
	 */
	private int iterator;

	/**
	 * Communication id 
     */
	private int communicationID = 1;

	private boolean _changed = false;

	public Network getNetwork(){
		return this;
	}

	public void changed() {
		setChanged(true);
	}

	public Client getLowestClientPayment(){
		Client c1;
		ArrayList<Client> list = new ArrayList<Client>();
                getAllClients().stream().forEach(c -> list.add(c));
                c1 = list.get(0);
		for(Client c:list){
                        if(c.getPayment()<c1.getPayment()){
                                c1 = c;
                        }
                }
        	return c1;
	}


	public boolean hasChanged() {
		return _changed;
	  }

	public void setChanged(boolean changed) {
    _changed = changed;
	}

	public void addCommunication(Communication c){
		allCommunications.add(c);
	}

	/**
	 * Read text input file and create corresponding domain entities.
	 * 
	 * @param filename name of the text input file
     * @throws UnrecognizedEntryException if some entry is not correct
	 * @throws IOException if there is an IO erro while processing the text file
	 * @throws ImportFileException if there is no file
	 */
	void importFile(String filename) throws UnrecognizedEntryException, IOException, ImportFileException{
		try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
			String line;
			while ((line = reader.readLine()) != null) {
			  String[] fields = line.split("\\|");
			  try {
				registerEntry(fields);
			  } catch (UnrecognizedEntryException | IOException | ImportFileException | TerminalExistsException | 
			  TerminalDoesntExistException | ClientExistsException | ClientDoesntExistException | InvalidTerminalIDException e) {
				e.printStackTrace();
			  }
			}
		  } catch (IOException e1) {
			throw new ImportFileException(filename);
		  }
		}

	/**
	 * Register accordingly to the input
	 * 
	 * @param fields The fields of the entry to register, that were split by the ImportFile
     * @throws UnrecognizedEntryException 	if some entry is not correct or if the 
	 * 										type is unknown and not supported by the program 
	 * @throws IOException if there is an IO erro while processing the text file
	 * @throws ImportFileException if there is no file
	 * @throws TerminalExistsException 	if , when registering a terminal, the terminal 
	 * 										with the refered id already exists
	 * @throws ClientExistsException if, when registering a client, the client with the refered id already exists
	 * @throws ClientDoesntExistException if, when looking for a client, the client with the 
	 * 										refered id doens't exists
	 * @throws TerminalDoesntExistException if, when looking for a terminal, the terminal doesnt exist
	 */
	public void registerEntry(String[] fields) throws UnrecognizedEntryException, IOException, 
		ImportFileException, TerminalExistsException, ClientExistsException, ClientDoesntExistException,
		TerminalDoesntExistException, InvalidTerminalIDException {
	  switch (fields[0]) {
	  case "CLIENT" -> 			registerClient(fields[1], fields[2], Integer.parseInt(fields[3]));
	  case "BASIC" , "FANCY" -> registerTerminal(fields);
	  case "FRIENDS" -> 		registerFriends(fields);
	  default -> throw new UnrecognizedEntryException(fields[0]);
	  }
	}

	/**
	 * Reads terminals id's and makes them friends of the first given terminal id
	 * 
	 * @param fields where the terminal id's are 
     * @throws TerminalDoesntExistException if the first given terminal doesnt exist
	 */
	public void registerFriends(String[] fields) throws TerminalDoesntExistException{
		if(!terminals.containsKey(fields[1]))
			throw new TerminalDoesntExistException(fields[1]);
		Terminal t = terminals.get(fields[1]);
		String[] s = fields[2].split("\\,");
		for(iterator=0; iterator!=s.length; iterator++){
			Terminal t1 = terminals.get(s[iterator]);
			if(t1 == null)
				throw new TerminalDoesntExistException(s[iterator]);
			t.addFriend(t1);
		}
		changed();
	}

	/**
	 * Gtes the current commonication id.
	 * 
	 * @return The communication id
	 */
	public int getGommunicationID(){
		return communicationID++;
	}

	public void newTerminalWithCommunications(String terminalID){
		terminalsWNCommunication.remove(terminalID);
	}
	/**
	 * Makes a new communication, removing the terminal who did the communication from
	 * terminals with no communication, and sets a new communication id.
	 * 
	 * @param terminalID the terminal who did the communication
	 */
	public void makeNewCommunication(String terminalID){
		communicationID += 1;
		newTerminalWithCommunications(terminalID);
	}

	public ArrayList<Long> getAllPaymentsAndDebst() {
		long allPayments = 0;
		long allDebt = 0;
		ArrayList<Long> allPaymentsAndDebt = new ArrayList<Long>();
		for(Map.Entry<String, Client> entry : clients.entrySet()) {
			Client c = entry.getValue();
			allDebt += c.getDebt();
			allPayments += c.getPayment();
		}
		allPaymentsAndDebt.add(allDebt);
		allPaymentsAndDebt.add(allPayments);
		return allPaymentsAndDebt;
	}

	/**
	 * Registers a new client.
	 * 
	 * @param id the client id
	 * @param name the client name
	 * @param nif the client "número de segurança social"
	 * 
	 * @return The client
	 * 
	 * @throws UnrecognizedEntryException if some entry is not correct or if the 
	 * 									type is unknown and not supported by the program 
	 * @throws ClientExistsException if the client with the given id already exists
	 */
	public Client registerClient(String id, String name, int nif)
	throws  UnrecognizedEntryException, ClientExistsException{
		if (clients.get(id) != null) {
		throw new ClientExistsException(id);
		}
		Client c = new Client(id, name, nif);
		this.clients.put(id, c);
		changed();
		return c;
	}

	/**
	 * Gets the client with the given id.
	 * 
	 * @param clientID the id of the client
	 * 
	 * @return the client we are looking for
	 * 
	 * @throws ClientDoesntExistException if the client doesnt exist
	 */
	public Client getClient(String clientID) throws ClientDoesntExistException{
		if(!clients.containsKey(clientID)){
			throw new ClientDoesntExistException(clientID);
		} 
		return clients.get(clientID);
	}

	/**
	 * Gets the terminal with the given id.
	 * 
	 * @param terminalID the id of the terminal
	 * 
	 * @return the terminal we are looking for
	 * 
	 * @throws TerminalDoesntExistException if the terminal doesnt exist
	 */
	public Terminal getTerminal(String terminalID) throws TerminalDoesntExistException{
		if(!terminals.containsKey(terminalID)){
			throw new TerminalDoesntExistException(terminalID);
		} 
		return terminals.get(terminalID);
	}

	/**
	 * Register a new terminal
	 *  
	 * @param fields
	 * @return
	 * @throws UnrecognizedEntryException
	 * @throws TerminalExistsException
	 * @throws ClientDoesntExistException
	 * @throws InvalidTerminalIDException
	 */
	public Terminal registerTerminal(String[] fields)
	throws  UnrecognizedEntryException, TerminalExistsException, ClientDoesntExistException, ClientDoesntExistException, InvalidTerminalIDException{
		if (!this.clients.containsKey(fields[2]))
			throw new ClientDoesntExistException(fields[2]);
		if (this.terminals.containsKey(fields[1])) {
			throw new TerminalExistsException(fields[1]);
		}
		if (!rightTerminalidString(fields[1])){
			throw new InvalidTerminalIDException(fields[1]);
		}
		Terminal t = new Terminal(fields[1], getClient(fields[2]), fields[3], fields[0]);
		getClient(fields[2]).addTerminal(fields[1], t);
		this.terminals.put(fields[1], t);
		this.terminalsWNCommunication.put(fields[1], t);
		changed();
		return t;
	}


	public boolean rightTerminalidString(String id) {
		if (id.length()!= 6){
			return false;
		}
		for(int i = 0; i < 6; i++) {
            if (!Character.isDigit(id.charAt(i))) {
                return false;
			}
		}
		return true;
	}

	/**
	 * Gets all registered terminals until now, in a collection.
	 * 
	 * @return a collection of terminals
	 */
	public Collection<Terminal> getAllTerminals(){
		return this.terminals.values();
	}

	/**
	 * Gets all registered terminals that havent make any communication, in a collection.
	 * 
	 * @return a collection of terminals
	 */
	public Collection<Terminal> getTerminalsWNCommunication(){
		return this.terminalsWNCommunication.values();
	}

	public Collection<Communication> getAllCommunicationsFromClient(String ClientID) throws ClientDoesntExistException{
        return getClient(ClientID).getAllCommunications();
    }

	/**
	 * Gets all registered clients until now, in a collection.
	 * 
	 * @return a collection of terminals
	 */
	public Collection<Client> getAllClients(){
		return this.clients.values();
	}

	public Collection<Communication> getAllCommunications(){
		return this.allCommunications;
	}

	public Collection<Terminal> getTerminalsWPositiveBalance(){
        SortedMap<String, Terminal> terminalsWPB = new TreeMap<String, Terminal>();
		ArrayList<Terminal> list = new ArrayList<Terminal>();
		getAllTerminals().stream().forEach(t -> list.add(t));
		for(int i = 0; i< list.size(); i++){
			Terminal t = list.get(i);
			if(t.hasPositiveBalance()){
				terminalsWPB.put(t.getTerminalID(), t);
			}
		}
        return terminalsWPB.values();
    }

	public Collection<Client> getClientsWithoutDebts(){
        SortedMap<String, Client> clientsWoutDebts = new TreeMap<String, Client>();
		ArrayList<Client> list = new ArrayList<Client>();
		getAllClients().stream().forEach(c -> list.add(c));
		for(int i = 0; i< list.size(); i++){
			Client c = list.get(i);
			if(c.getDebt()==0){
				clientsWoutDebts.put(c.getChave(), c);
			}
		}
        return clientsWoutDebts.values();
    }
	
	public Collection<Client> getAllClientsWDebts(){
		SortedMap<Long, Client> clientsWDebts = new TreeMap<Long, Client>(Collections.reverseOrder());
		ArrayList<Client> list = new ArrayList<Client>();
		getAllClients().stream().forEach(c -> list.add(c));
		for(int i = 0; i< list.size(); i++){
			Client c = list.get(i);
			if(c.getDebt()>0){
				clientsWDebts.put(c.getDebt(), c);
			}
		}
		return clientsWDebts.values();
	}
}
