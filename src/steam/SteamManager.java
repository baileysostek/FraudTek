/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package steam;

import base.engine.Engine;
import com.codedisaster.steamworks.SteamAPI;
import javax.script.ScriptEngine;

/**
 *
 * @author Bailey
 */
public class SteamManager extends Engine{

    public SteamManager() {
        super("Steam");
    }

    @Override
    public void init() {
        try {
            SteamAPI.init();
            SteamAPI.printDebugInfo(System.out);
        } catch (Exception e) {
            // Error extracting or loading native libraries
            e.printStackTrace();
        }
    }

    @Override
    public void tick() {
        SteamAPI.runCallbacks();
    }

    @Override
    public void registerForScripting(ScriptEngine engine) {
        
    }

    @Override
    public void onShutdown() {
        SteamAPI.shutdown();
    }
    
}
