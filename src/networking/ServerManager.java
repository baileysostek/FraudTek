package networking;

import base.engine.Engine;
import base.engine.Game;
import base.util.DynamicCollection;

import javax.script.ScriptEngine;

/**
 * Created by Bailey on 3/30/2018.
 */
public class ServerManager extends Engine{

    DynamicCollection<Client> clients = new DynamicCollection<Client>() {
        @Override
        public void onAdd(Client object) {

        }

        @Override
        public void onRemove(Client object) {

        }
    };

    public ServerManager() {
        super("ServerManager");
    }

    @Override
    public void init() {

    }

    @Override
    public void tick() {

    }

    @Override
    public void registerForScripting(ScriptEngine engine) {
        engine.put(this.getName(), this);
    }

    @Override
    public void onShutdown() {
        //Close all connections
    }

    public void connect(){
        Client client;
        try {
            client = new Client();
            clients.add(client);
        }catch (Exception e){
            Game.logManager.printStackTrace(e);
        }
    }
}
