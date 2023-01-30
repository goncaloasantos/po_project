package prr;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import prr.exceptions.ImportFileException;
import prr.exceptions.MissingFileAssociationException;
import prr.exceptions.UnavailableFileException;
import prr.exceptions.UnrecognizedEntryException;

/**
 * Manage access to network and implement load/save operations.
 */
public class NetworkManager {

	/**
	 * Name of file storing current store.
	 */
	private String _filename = "";

	/** The network itself. */
	private Network _network = new Network();

	public boolean changed() {
		return _network.hasChanged();
	}

    /**
	 * Gets the network.
	 * 
	 * @return The network itself
	 */
	public Network getNetwork() {
		return _network;
	}

	/**
	 * @param filename name of the file containing the serialized application's state
         *        to load.
	 * @throws UnavailableFileException if the specified file does not exist or there is
     *         an error while processing this file.
	 * @throws IOException if there is some error while serializing the state of the network to disk
	 * @throws ClassNotFoundException if the application tries to load in a class but no definition 
	 * 								for the class with the specified name could be found.
	 */
	public void load(String filename) throws UnavailableFileException {
		_filename = filename;
		try (ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)))) {
			_network = (Network) in.readObject();
			_network.setChanged(false);
		}
		catch(IOException | ClassNotFoundException e){
			throw new UnavailableFileException(filename);
		}
	}

	/**
     * Saves the serialized application's state into the file associated to the current network.
     *
	 * @throws FileNotFoundException if for some reason the file cannot be created or opened. 
	 * @throws MissingFileAssociationException if the current network does not have a file.
	 * @throws IOException if there is some error while serializing the state of the network to disk.
	 */
	public void save() throws FileNotFoundException, MissingFileAssociationException, IOException {
		if ( _filename == null || _filename.equals("")) 
			throw new MissingFileAssociationException();
		try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(
			new FileOutputStream(_filename)))){
				oos.writeObject(_filename);
				_network.setChanged(false);
			}
		catch(IOException e){e.printStackTrace();}
		}
				

	/**
         * Saves the serialized application's state into the specified file. The current network is
         * associated to this file.
         *
	 * @param filename the name of the file.
	 * @throws FileNotFoundException if for some reason the file cannot be created or opened.
	 * @throws MissingFileAssociationException if the current network does not have a file.
	 * @throws IOException if there is some error while serializing the state of the network to disk.
	 */
	public void saveAs(String filename) throws FileNotFoundException, MissingFileAssociationException, IOException {
		_filename = filename;
		save();
	}

	/**
	 * Read text input file and create domain entities..
	 * 
	 * @param filename name of the text input file
	 * @throws ImportFileException
	 */
	public void importFile(String filename) throws ImportFileException {
		try {
            _network.importFile(filename);
         } catch (IOException | UnrecognizedEntryException e) {
                throw new ImportFileException(filename, e);
    	}
	}

}
