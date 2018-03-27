package base.util;

import base.engine.Engine;
import base.engine.Game;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import javax.script.ScriptEngine;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by Bailey on 1/21/2018.
 */
public class LogManager extends Engine {

    private LinkedList<String> logData = new LinkedList<>();
    private boolean includeTimestamp = true;
    private String fileName = "log.txt";
    private boolean doOutput = false;

    private boolean lastWasPrintLn = false;

    public LogManager() {
        super("LogManager");
    }

    @Override
    public void init() {

    }

    @Override
    public void tick() {

    }

    @Override
    public void registerForScripting(ScriptEngine engine) {

    }

    @Override
    public void onShutdown() {
        flush();
    }

    public void println(){
        String line = "---------------------------------------------";
        logData.add(line);
        if(doOutput) {
            System.out.println(line);
        }
    }

    public void println(String line){
        if(lastWasPrintLn) {
            String current = logData.getLast();
            logData.removeLast();
            logData.addLast(current + line);
            lastWasPrintLn = false;
        }else{
            if(includeTimestamp){
                line = "["+LocalDateTime.now().getHour()+":"+LocalDateTime.now().getMinute()+":"+LocalDateTime.now().getSecond()+"]"+line;
            }
            logData.add(line);
        }
        if(doOutput) {
            System.out.println(line);
        }
    }

    public void println(String line, EnumErrorLevel errorLevel){
        if(lastWasPrintLn) {
            String current = logData.getLast();
            logData.removeLast();
            logData.addLast(current + "["+errorLevel.name()+"]" +line);
            lastWasPrintLn = false;
        }else{
            if(includeTimestamp){
                line = "["+errorLevel.name()+"]" + "["+LocalDateTime.now().getHour()+":"+LocalDateTime.now().getMinute()+":"+LocalDateTime.now().getSecond()+"]"+line;
            }
            logData.add(line);
        }
        if(doOutput) {
            System.out.println(line);
        }
    }

    public void printObject(Object inData){
        Gson gson = new Gson();
        String json = gson.toJson(inData);
        JsonElement element = gson.fromJson(json, JsonElement.class);
        logData.addAll(buildJsonStack(element, new LinkedList<String>(), 0));
    }

    private LinkedList<String> buildJsonStack(JsonElement element, LinkedList<String> stack, int depth){
        depth++;
        JsonObject object = element.getAsJsonObject();
        for(Map.Entry<String, JsonElement> objectElement:object.entrySet()){
            String name = objectElement.getKey();
            JsonElement value = objectElement.getValue();
            if(value.isJsonObject()){
                String offsetDistance = "";
                LinkedList<String> subData = buildJsonStack(value, new LinkedList<String>(), depth);
                for(int j = 0; j < depth; j++){
                    offsetDistance+="\t";
                }
                for(int j = 0; j < subData.size(); j++){
                    subData.set(j, offsetDistance+subData.get(j));
                }
                subData.addFirst(offsetDistance.replace("\t", "")+name+"={");
                subData.addLast(offsetDistance.replace("\t", "")+"}");
                stack.addAll(subData);
            }else{
                stack.add(name+"="+value);
            }
        }
        System.out.println(stack);
        return stack;
    }

    public void println(String[] lines){
        if(lastWasPrintLn) {
            String current = logData.getLast();
            logData.removeLast();
            logData.addLast(current + lines[0]);
            for(int i = 1; i < lines.length; i++) {
                String line = lines[i];
                if (includeTimestamp) {
                    line = "[" + LocalDateTime.now().getHour() + ":" + LocalDateTime.now().getMinute() + ":" + LocalDateTime.now().getSecond() + "]" + line;
                }
                logData.add(line);
            }
            lastWasPrintLn = false;
        }else{
            for(int i = 0; i < lines.length; i++) {
                String line = lines[i];
                if (includeTimestamp) {
                    line = "[" + LocalDateTime.now().getHour() + ":" + LocalDateTime.now().getMinute() + ":" + LocalDateTime.now().getSecond() + "]" + line;
                }
                logData.add(line);
            }
        }
        if(doOutput) {
            for(int i = 0; i < lines.length; i++) {
                String line = lines[i];
                System.out.println(line);
            }
        }
    }

    public void print(String line){
        if(lastWasPrintLn) {
            String current = logData.getLast();
            logData.removeLast();
            logData.addLast(current + line);
        }else{
            if(includeTimestamp){
                line = "["+LocalDateTime.now().getHour()+":"+LocalDateTime.now().getMinute()+":"+LocalDateTime.now().getSecond()+"]"+line;
            }
            logData.add(line);
            lastWasPrintLn = true;
        }
        if(doOutput) {
            System.out.print(line);
        }
    }

    public void flush(){
        String[] lines = new String[logData.size()];
        int index = 0;
        for(String s: logData){
            lines[index] = s;
            index++;
        }
        StringUtils.saveData(Game.getFolder("Logs").getName()+"/"+fileName, lines);
    }

    public void setFileName(String name){
        if(!name.endsWith(".txt")){
            name = name+".txt";
        }
        this.fileName = name;
    }

    public void printStackTrace(Exception e){
        StackTraceElement[] errorMessage = e.getStackTrace();
        println();
        println("Error:"+e.getLocalizedMessage(), EnumErrorLevel.SEVERE);
        for(int i = 0; i < errorMessage.length; i++){
            println(errorMessage[i].getClassName()+"["+errorMessage[i].getLineNumber()+"]"+errorMessage[i].toString());
        }
        println();
    }
}
