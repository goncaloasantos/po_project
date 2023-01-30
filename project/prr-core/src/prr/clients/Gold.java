package prr.clients;

import java.util.ArrayList;

import prr.Communications.Communication;

public class Gold extends Client.ClientType{
    
    public Gold(Client c, long creditos){
        c.super(creditos);
    }

    @Override
    public boolean shouldUpgradeGold(){return false;}


    @Override
    public boolean shouldUpgradePlat(){
        int _cont = 0;
        ArrayList<Communication> _coms = getClient().getAllCommunications();
        if (_coms.size() > 5){
            for(int i = _coms.size()-1; i >= _coms.size()-5; --i){
                if(_coms.get(i).isVideo()){
                    _cont++;
                }
            }
        }
        if (_cont == 5 && getCreditos() >= 0) {return true;}
        else return false;
    }

    @Override
    public void upgrade(){
        if (shouldUpgradePlat()){
            getClient().getClientType().setType(new Platinum(this.getClient(), getCreditos()));
        }
    }

    @Override
    public boolean shouldDowngrade(){
        setCreditos();
        long cred = getCreditos();
        return cred < 0;
    }

    @Override
    public void downgrade(){
        if (shouldDowngrade()){
            getClient().getClientType().setType(new Normal(this.getClient(), getCreditos()));
        }
    }

    @Override
    public long getCostText(int size){
        if (size < 50){ return 10;}
        if (size >= 50 || size < 100) {
            return 10;
        } else {
            return 2*size;
        }
    }

    @Override
    public long getCostVoice(int time){
        return time*10;
    }

    @Override
    public long getCostVideo(int time){
        return time*20;
    }

    @Override
    public String getTypeName(){
        return "GOLD";
    }

}
