package prr.app.main;

import java.io.IOException;

import prr.NetworkManager;
import prr.app.exceptions.FileOpenFailedException;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import prr.exceptions.UnavailableFileException;


/**
 * Command to open a file.
 */
class DoOpenFile extends Command<NetworkManager> {

	DoOpenFile(NetworkManager receiver) {
		super(Label.OPEN_FILE, receiver);
	}

    @Override
	protected final void execute() throws CommandException {
        String fileName = Form.requestString(Prompt.openFile());
        try {
            if(_receiver.changed() && Form.confirm(Prompt.saveBeforeExit())){
                DoSaveFile cmd = new DoSaveFile(_receiver);
                cmd.execute();
            }
            _receiver.load(fileName);
        }
        catch (UnavailableFileException e) {
            throw new FileOpenFailedException(e);
        }
	}
}
