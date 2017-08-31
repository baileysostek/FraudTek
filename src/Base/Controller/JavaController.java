/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Base.Controller;

import net.java.games.input.Controller;
import org.joml.Vector3f;

/**
 *
 * @author Bailey
 */
public class JavaController {
    
    private Vector3f leftThumbStick = new Vector3f(0,0,0);
    private Vector3f rightThumbStick = new Vector3f(0,0,0);
    private EnumButtonType[] button_names = new EnumButtonType[]{};
    private float[] button_values = new float[]{};
    
    private Controller controller;
    
    private boolean isDisconnected = false;
    
    private final float DEAD_ZONE = 0.1f;
    
    public JavaController(Controller controller){
        this.controller = controller;
        EnumButtonType[] mapping = EnumButtonType.values();
        
        if(controller.getName().toLowerCase().contains("xbox")){
            System.out.println("Xbox Controller Recognised.");
            mapping = new EnumButtonType[]{
                EnumButtonType.LEFT_STICK_Y,
                EnumButtonType.LEFT_STICK_X,
                EnumButtonType.RIGHT_STICK_Y,
                EnumButtonType.RIGHT_STICK_X,
                EnumButtonType.LEFT_TRIGGER,
                EnumButtonType.RIGHT_TRIGGER,
                EnumButtonType.A,
                EnumButtonType.B,
                EnumButtonType.X,
                EnumButtonType.Y,
                EnumButtonType.LEFT_BUMPER,
                EnumButtonType.RIGHT_BUMPER,
                EnumButtonType.START,
                EnumButtonType.SELECT,
                EnumButtonType.LEFT_STICK_PRESSED,
                EnumButtonType.RIGHT_STICK_PRESSED,
                EnumButtonType.NULL,
                EnumButtonType.HOME,
            };
        }

        button_names = mapping;
        button_values = new float[button_names.length];
        for(int i = 0; i < button_values.length; i++){
            button_values[i] = 0.0f;
        }
    }
    
    public void poll(){
        if(controller.poll()){
            for(int i = 0 ; i < controller.getComponents().length; i++){
                if(i < button_names.length){
                    button_values[i] = controller.getComponents()[i].getPollData();
                }
            }
            
            if(Math.abs(this.getButton(EnumButtonType.LEFT_STICK_X))<DEAD_ZONE){
                setButtonValue(EnumButtonType.LEFT_STICK_X, 0);
            }
            
            if(Math.abs(this.getButton(EnumButtonType.LEFT_STICK_Y))<DEAD_ZONE){
                setButtonValue(EnumButtonType.LEFT_STICK_Y, 0);
            }
            
            if(Math.abs(this.getButton(EnumButtonType.RIGHT_STICK_X))<DEAD_ZONE){
                setButtonValue(EnumButtonType.RIGHT_STICK_X, 0);
            }
            
            if(Math.abs(this.getButton(EnumButtonType.RIGHT_STICK_Y))<DEAD_ZONE){
                setButtonValue(EnumButtonType.RIGHT_STICK_Y, 0);
            }
            
            this.leftThumbStick = new Vector3f(this.getButton(EnumButtonType.LEFT_STICK_X), this.getButton(EnumButtonType.LEFT_STICK_Y), this.getButton(EnumButtonType.LEFT_STICK_PRESSED));
            this.rightThumbStick = new Vector3f(this.getButton(EnumButtonType.RIGHT_STICK_X), this.getButton(EnumButtonType.RIGHT_STICK_Y), this.getButton(EnumButtonType.RIGHT_STICK_PRESSED));
        }else{
            this.isDisconnected = true;
        }
    }
    
    public float getButton(EnumButtonType type){
        int index = 0;
        for(EnumButtonType button : button_names){
            if(button.equals(type)){
                return button_values[index];
            }
            index++;
        }
        return 0;
    }
    
    private void setButtonValue(EnumButtonType type, float value){
        int index = 0;
        for(EnumButtonType button : button_names){
            if(button.equals(type)){
                button_values[index] = value;
            }
            index++;
        }
    }
    
    public Vector3f getLeftThumbStick(){
        return this.leftThumbStick;
    }
    
    public Vector3f getRightThumbStick(){
        return this.rightThumbStick;
    }
    
    public boolean isConnected(){
        return !this.isDisconnected;
    }
    
    public boolean equalsController(Controller controller){
        return this.controller.hashCode() == controller.hashCode();
    }
}
