/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import com.google.gson.JsonObject;

/**
 *
 * @author Bailey
 */
public class EntityNotRecognisedException extends Exception{
    public EntityNotRecognisedException(EnumEntityType entity, JsonObject data){
        System.err.println(entity+":"+data);
    }
}
