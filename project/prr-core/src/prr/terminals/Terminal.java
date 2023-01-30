package prr.terminals;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import prr.Network;
import prr.Communications.Communication;
import prr.Communications.TextCommunication;
import prr.Communications.VideoCommunication;
import prr.Communications.VoiceCommunication;
import prr.Notifications.Notifications;
import prr.Notifications.NotificationBusyToIdle;
import prr.Notifications.NotificationOffToIdle;
import prr.Notifications.NotificationOffToSilence;
import prr.Notifications.NotificationSilentToIdle;
import prr.balance.Balance;
import prr.clients.Client;
import prr.clients.Client.ClientType;
import prr.exceptions.CommunicationNotSupportedAtDestinationException;
import prr.exceptions.CommunicationNotSupportedAtOriginException;
import prr.exceptions.InvalidCommunicationException;
import prr.exceptions.NoOnGoingCommunicationException;
import prr.exceptions.TerminalAlreadyIdleException;
import prr.exceptions.TerminalAlreadyOffException;
import prr.exceptions.TerminalAlreadySilentException;
import prr.exceptions.TerminalDoesntExistException;
import prr.exceptions.TerminalIsBusyException;
import prr.exceptions.TerminalIsOffException;
import prr.exceptions.TerminalIsSilentException;
import prr.exceptions.UnrecognizedEntryException;



/**
 * Terminal.
 */
public class Terminal implements Serializable {

	/** Serial number for serialization. */
	private static final long serialVersionUID = 202208091753L;


        private ArrayList<Terminal> _friends = new ArrayList<Terminal>();
        private Map<Integer, Communication> _communications = new TreeMap<Integer, Communication>();
        private Map<Integer, Communication> _recivedCommunications = new TreeMap<Integer, Communication>();
        private ArrayList<Terminal> _tryToCommunicateWM = new ArrayList<Terminal>();
        private String _terminalID; 
        private Client _client;
        private TerminalState _tState;
        private TerminalType _type;
        private Balance _balance;
        private Boolean _hasFriends = false;
        private boolean _isCommunicating = false;
        private boolean _canEndCommunication = false;
        private Communication _currentOnCommunication = null;



        public Terminal(String terminalID, Client client, String state, String type) throws UnrecognizedEntryException{
                if(!terminalID.matches("\\d{6}"))
                        throw new UnrecognizedEntryException(terminalID);
                _terminalID = terminalID;
                _client = client;
                _tState = setStateByString(state);
                _type = setTypeByString(type);
                _balance = new Balance();
        }

        public void setState(TerminalState state){_tState = state;}

        public void setToSilence(){
                _tState.setToSilence();
                if(_tState.getPreviousState().isOff())
                        generateOfftoSilenceNot();
        }

        public void setToOff(){_tState.setToOff();}
    
        public void setToIdle(){
                _tState.setToIdle();
                if(_tState.getPreviousState().isOff())
                        generateOffToIdleNot();
                else if(_tState.getPreviousState().isSilence())
                        generateSilentToIdleNot();
                else if(_tState.getPreviousState().isBusy())
                        generateBusyToIdleNot();
        }
        
        public void setToBusy(){_tState.setToBusy();}

        public void trySetToOff() throws TerminalAlreadyOffException{
                if(_tState.isOff()){
                        throw new TerminalAlreadyOffException();
                }
                else if(!_tState.isBusy())
                        setToOff();
        }

        public void trySetToIdle() throws TerminalAlreadyIdleException{
                if(_tState.isIdle()){
                        throw new TerminalAlreadyIdleException();
                }
                else if(!_tState.isBusy())
                        setToIdle();
        }

        public void trySetToSilence() throws TerminalAlreadySilentException{
                if(_tState.isSilence()){
                        throw new TerminalAlreadySilentException();
                }
                else if(!_tState.isBusy())
                        setToSilence();
        }

        public void generateOfftoSilenceNot(){
                for(int i = 0; i < _tryToCommunicateWM.size(); i++){
                        Notifications n = new NotificationOffToSilence(this);
                        _tryToCommunicateWM.get(i).getClient().addNotifications(n);
                }
                _tryToCommunicateWM = new ArrayList<Terminal>();
        }

        public void generateOffToIdleNot(){
                for(int i = 0; i < _tryToCommunicateWM.size(); i++){
                        Notifications n = new NotificationOffToIdle(this);
                        _tryToCommunicateWM.get(i).getClient().addNotifications(n);
                }
                _tryToCommunicateWM = new ArrayList<Terminal>();
        }

        public void generateSilentToIdleNot(){
                for(int i = 0; i < _tryToCommunicateWM.size(); i++){
                        Notifications n = new NotificationSilentToIdle(this);
                        _tryToCommunicateWM.get(i).getClient().addNotifications(n);
                }
                _tryToCommunicateWM = new ArrayList<Terminal>();
        }

        public void generateBusyToIdleNot(){
                for(int i = 0; i < _tryToCommunicateWM.size(); i++){
                        Notifications n = new NotificationBusyToIdle(this);
                        _tryToCommunicateWM.get(i).getClient().addNotifications(n);
                }
                _tryToCommunicateWM = new ArrayList<Terminal>();
        }

        public void toNotifyWhenFree(Terminal t){
                _tryToCommunicateWM.add(t);
        }

        public TerminalState getTerminalState() {return _tState;}
    
        public String getStateName(){return _tState.toString();}

        public String getTypeName(){return _type.toString();}

        public Balance getTerminalBalance(){
                return _balance;
        }

        public Collection<Communication> getAllCommunications(){
                return _communications.values();
        }

        public String getTerminalID(){
                return _terminalID;
        }

        public Client getClient(){
                return _client;
        }

        public String getClientID(){
                return _client.getChave();
        }

        public TerminalState getState(){
                return _tState;
        }

        public TerminalType getType(){
                return _type;
        }

        public void isCommunicating(){
                _isCommunicating = true;
        }

        public void currentOnGoingCommunication(Communication c){
                _currentOnCommunication = c;
        }

        public void isNoLongerCommunicating(){
                _isCommunicating = false;
        }

        public void addRecivedCommunication(int id, Communication c, Client client){
                _recivedCommunications.put(id, c);
                client.newReceivedCommunication(c);
        }

        public boolean canInteractAsDestination(String type) throws CommunicationNotSupportedAtDestinationException{
                if(!_type.canInteract())
                        throw new CommunicationNotSupportedAtDestinationException(this.getTerminalID(), type);
                return _type.canInteract();
        }

        public boolean canInteractAsOrigin(String type) throws CommunicationNotSupportedAtOriginException{
                if(!_type.canInteract())
                        throw new CommunicationNotSupportedAtOriginException(this.getTerminalID(), type);
                return _type.canInteract();
        }

        public TerminalState setStateByString(String state){
                TerminalState ts = new TerminalStateIdle(this, _tState);
                switch (state) {
                        case "OFF"      -> ts = new TerminalStateOff(this, _tState);
                        case "SILENCE"  -> ts = new TerminalStateSilence(this, _tState);
                } return ts;
        }

        public TerminalType setTypeByString(String type) throws UnrecognizedEntryException{
                TerminalType tt;
                switch (type){
                        case "FANCY" -> tt = new TerminalFancy();
                        case "BASIC" -> tt = new TerminalBasic();
                        default -> throw new UnrecognizedEntryException(type);
                }
                return tt;
        }

        public void addFriend(Terminal t){
                if(!_terminalID.equals(t.getTerminalID()) && !_friends.contains(t)){
                        _friends.add(t);
                        _hasFriends = true;
                }
        }

        public void addFriendByID(String terminalID, Network network) throws TerminalDoesntExistException{
                addFriend(network.getTerminal(terminalID));
        }

        public void removeFriend(String terminalID, Network network) throws TerminalDoesntExistException{
                Terminal t = network.getTerminal(terminalID);
                if(_friends.contains(t)){
                        _friends.remove(t);
                }
                if(_friends.size() == 0){
                        _hasFriends = false;
                }
        }

        public boolean hasFriends(){
                return _hasFriends;
        }

        public boolean hasPositiveBalance(){
                return _balance.getPaid()-_balance.getDebt()>0;
        }

        public boolean checkDestionationTerminal(Terminal t) throws TerminalIsOffException, TerminalIsBusyException, TerminalIsSilentException{
                boolean ok = false;
                if(t.getState().isBusy()){
                        if(this.getClient().getNotifState())
                                t.toNotifyWhenFree(this);
                        throw new TerminalIsBusyException(t.getTerminalID());
                }
                else if(t.getState().isOff()){
                        if(this.getClient().getNotifState())
                                t.toNotifyWhenFree(this);
                        throw new TerminalIsOffException(t.getTerminalID());
                }
                else if(t.getState().isSilence()){
                        if(this.getClient().getNotifState())
                                t.toNotifyWhenFree(this);       
                        throw new TerminalIsSilentException(t.getTerminalID());
                }
                return ok;
        }

        public void payCommunication(int id) throws InvalidCommunicationException{
                if(_communications.containsKey(id)){
                        Communication c = _communications.get(id);
                        if(!c.isOn() && !c.hasBeenPayed()){
                                c.pay();
                                this._balance.addPaid(c.getPrice());
                                getClient().getClientType().upgrade();
                        }
                        else
                                throw new InvalidCommunicationException(id);
                }
                else
                        throw new InvalidCommunicationException(id); 
        }

        public ArrayList<Terminal> getFriends(){
                return _friends;
        }

        public String getOnGoingCommunication() throws NoOnGoingCommunicationException{
                if(!_isCommunicating || !_tState.isBusy())
                        throw new NoOnGoingCommunicationException(this._terminalID);       
                return _currentOnCommunication.renderCommunications();
                }   

        public void iniciateVoiceCommunication(String receiverTerminalID, Network network) throws TerminalDoesntExistException, 
        TerminalIsOffException, TerminalIsSilentException, TerminalIsBusyException {
                Terminal receiverTerminal =  network.getTerminal(receiverTerminalID);
                checkDestionationTerminal(receiverTerminal);
                Communication c = new VoiceCommunication(this, receiverTerminal, 0, network); 
                _communications.put(c.getcomID(), c); 
                setToBusy(); 
                isCommunicating();
                _canEndCommunication = true;
                currentOnGoingCommunication(c);
                network.newTerminalWithCommunications(this._terminalID);
                network.newTerminalWithCommunications(receiverTerminalID);
                _client.newDoneCommunication(c);
                receiverTerminal.setToBusy();
                receiverTerminal.isCommunicating();
                receiverTerminal.currentOnGoingCommunication(c);
                receiverTerminal.addRecivedCommunication(c.getcomID(), c, receiverTerminal.getClient());
                network.addCommunication(c);
                
        }

        public void iniciateVideoCommunication(String receiverTerminalID, Network network) throws TerminalDoesntExistException, TerminalIsOffException,
        CommunicationNotSupportedAtDestinationException, CommunicationNotSupportedAtOriginException, TerminalIsSilentException, 
        TerminalIsBusyException{
                Terminal receiverTerminal =  network.getTerminal(receiverTerminalID);
                if(this.canInteractAsOrigin("VIDEO") && receiverTerminal.canInteractAsDestination("VIDEO")){
                        checkDestionationTerminal(receiverTerminal);
                        Communication c = new VideoCommunication(this, receiverTerminal, 0, network); 
                        _communications.put(c.getcomID(), c); 
                        setToBusy(); 
                        isCommunicating();
                        _canEndCommunication = true;
                        currentOnGoingCommunication(c);
                        network.newTerminalWithCommunications(this._terminalID);
                        network.newTerminalWithCommunications(receiverTerminalID);
                        _client.newDoneCommunication(c);
                        receiverTerminal.setToBusy();
                        receiverTerminal.isCommunicating();
                        receiverTerminal.currentOnGoingCommunication(c);
                        receiverTerminal.addRecivedCommunication(c.getcomID(), c, receiverTerminal.getClient());
                        network.addCommunication(c);
                        
                }
        }

        public void iniciateInteractiveCommunication(String destinationTerminal, Network network, String communicationType) throws UnrecognizedEntryException, TerminalIsOffException,
                        TerminalDoesntExistException, CommunicationNotSupportedAtOriginException, CommunicationNotSupportedAtDestinationException, 
                        TerminalIsSilentException, TerminalIsBusyException{
                if(!destinationTerminal.equals(_terminalID)){
                        switch (communicationType){
                                case "VIDEO" -> iniciateVideoCommunication(destinationTerminal, network);
                                case "VOICE" -> iniciateVoiceCommunication(destinationTerminal, network);
                                default -> throw new UnrecognizedEntryException(communicationType);
                        }
                }  
                else throw new TerminalIsBusyException(destinationTerminal);  
        }

        public long endInteractiveCommunication(int minutes){//tem de returnar um double
                Communication c = _currentOnCommunication;
                Terminal t = c.getReciverTerminal();
                long cost;
                c.endCommunication();
                c.setUnits(minutes);
                isNoLongerCommunicating();
                setState(_tState.getPreviousState());
                ClientType ct = _client.getClientType();
                if(c.isVideo()){
                        cost = ct.getCostVideo(minutes);
                }
                else
                        cost = ct.getCostVoice(minutes);
                if(_friends.contains(t))
                        cost *=0.50;
                c.setPrice(cost);
                _canEndCommunication = false;
                currentOnGoingCommunication(null);
                this._balance.addDebts(cost);
                t.isNoLongerCommunicating();
                t.setState(t.getState().getPreviousState());
                t.currentOnGoingCommunication(null);
                _client.getClientType().upgrade();
                _client.getClientType().downgrade();
                return cost;
        }

        public void initiateTextCommunication(String receiverTerminalID, String text, Network network) throws TerminalIsOffException, TerminalDoesntExistException{
                Terminal receiverTerminal =  network.getTerminal(receiverTerminalID);
                Long cost;
                if(receiverTerminal.getState().canReciveMessages()){
                        Communication c = new TextCommunication(this, receiverTerminal, text, network); 
                        _communications.put(c.getcomID(), c);
                        addRecivedCommunication(c.getcomID(), c, receiverTerminal.getClient());
                        cost = _client.getClientType().getCostText(c.getUnits());
                        c.setPrice(cost);
                        network.addCommunication(c);
                        network.newTerminalWithCommunications(this._terminalID);
                        network.newTerminalWithCommunications(receiverTerminalID);
                        receiverTerminal.addRecivedCommunication(c.getcomID(), c, receiverTerminal.getClient());
                        this._balance.addDebts(cost);
                        _client.newDoneCommunication(c);
                        _client.getClientType().upgrade();
                        _client.getClientType().downgrade();
                }
                else{
                        if(this.getClient().getNotifState())
                                receiverTerminal.toNotifyWhenFree(this);
                        throw new TerminalIsOffException(receiverTerminalID);
                }
        }

        /**
         * Checks if this terminal can end the current interactive communication.
         *
         * @return true if this terminal is busy (i.e., it has an active interactive communication) and
         *          it was the originator of this communication.
         **/
        public boolean canEndCurrentCommunication() {
                return _isCommunicating && _canEndCommunication;
        }

        /**
         * Checks if this terminal can start a new communication.
         *
         * @return true if this terminal is neither off neither busy, false otherwise.
         **/
        public boolean canStartCommunication() {
                return _tState.canCommunicate();
        }

        public String friendsToString(){
                String friendsString = "";
                ArrayList<String> friends = new ArrayList<String>();
                if(_friends.isEmpty()){
                        return friendsString;
                }
                else{
                        
                        for(Terminal f:_friends){
                                friends.add(f.getTerminalID());
                        }
                        Collections.sort(friends, String.CASE_INSENSITIVE_ORDER);
                        for(int i=0 ; i < friends.size() ; i++){
                                friendsString += friends.get(i);
                                if(i != friends.size()-1) 
                                        friendsString += ",";
                        }
                        return friendsString;
                }
        }

        public String renderFields(){
                return getTypeName() + "|" + getTerminalID() + "|" + getClientID() 
                    + "|" + getStateName() + "|" + Math.round(getTerminalBalance().getPaid())+
                    "|" + Math.round(getTerminalBalance().getDebt())+ (hasFriends() ?  "|" + friendsToString() : "");
            }
}

