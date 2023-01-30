package prr.app.terminal;

import prr.Network;
import prr.app.exceptions.UnknownTerminalKeyException;
import prr.exceptions.CommunicationNotSupportedAtDestinationException;
import prr.exceptions.CommunicationNotSupportedAtOriginException;
import prr.exceptions.InvalidTerminalIDException;
import prr.exceptions.TerminalDoesntExistException;
import prr.exceptions.TerminalIsBusyException;
import prr.exceptions.TerminalIsOffException;
import prr.exceptions.TerminalIsSilentException;
import prr.exceptions.UnrecognizedEntryException;
import prr.terminals.Terminal;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

/**
 * Command for starting communication.
 */
class DoStartInteractiveCommunication extends TerminalCommand {

	DoStartInteractiveCommunication(Network context, Terminal terminal) {
		super(Label.START_INTERACTIVE_COMMUNICATION, context, terminal, receiver -> receiver.canStartCommunication());
	}

	@Override
	protected final void execute() throws CommandException {
		String destinationTerminalId = Form.requestString(Prompt.terminalKey());
		String type = Form.requestOption(Prompt.commType(), "VIDEO", "VOICE");
		try{
			_receiver.iniciateInteractiveCommunication(destinationTerminalId, _network, type);
		}
		catch(CommunicationNotSupportedAtOriginException e){
			_display.popup(Message.unsupportedAtOrigin(e.getKey(), e.getType()));  
		}
		catch(CommunicationNotSupportedAtDestinationException e){
			_display.popup(Message.unsupportedAtDestination(e.getKey(), e.getType()));
		}
		catch(TerminalIsOffException e){
			_display.popup(Message.destinationIsOff(e.getKey()));
		}
		catch(TerminalIsSilentException e){
			_display.popup(Message.destinationIsSilent(e.getKey()));
		}
		catch(TerminalIsBusyException e){
			_display.popup(Message.destinationIsBusy(e.getKey()));
		}
		catch(TerminalDoesntExistException e){
			throw new UnknownTerminalKeyException(e.getKey());
		}
		catch(UnrecognizedEntryException e){
			e.printStackTrace();
		}
	}
}
