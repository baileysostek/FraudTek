package entity.component;

import Base.engine.Game;
import Base.util.StringUtils;
import ScriptingEngine.Script;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import entity.Attribute;
import entity.Entity;
import graphics.Renderer;
import shaders.StaticShader;

import java.nio.file.NoSuchFileException;
import java.util.LinkedList;

/**
 * Created by Bailey on 9/15/2017.
 */
public class ComponentScript extends Component{
    private Attribute<String> name = new Attribute<String>("filename", "script.js");
    private Script script;

    public ComponentScript(Entity e, JsonObject data) {
        super(EnumComponentType.SCRIPT, e);
//        name.setPrivate(true);

        name.setDataFromJson(data);

        if (!name.getData().endsWith(".js")) {
           name.setData(name.getData() + ".js");
        }

        try {
            script = Game.scriptingEngine.add(name.getData(), e);
            if(script == null){
                StringUtils.saveData("/Scripting/"+name.getData(), buildScript());
                script = Game.scriptingEngine.add(name.getData(), e);
            }
        }catch(Exception ex){
            StringUtils.saveData("/Scripting/"+name.getData(), buildScript());
            script = Game.scriptingEngine.add(name.getData(), e);
        }
        e.setScript(script);
    }

    public ComponentScript(Entity e, String data) {
        super(EnumComponentType.SCRIPT, e);
//        name.setPrivate(true);

        if (!data.endsWith(".js")) {
            name.setData(data + ".js");
        }

        script = Game.scriptingEngine.add(name.getData(), e);
        if(script == null){
            StringUtils.saveData("/Scripting/"+name.getData(), buildScript());
            script = Game.scriptingEngine.add(name.getData(), e);
        }
    }

    @Override
    public void onAdd() {

    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Renderer r, StaticShader shader) {

    }

    public String[] buildScript(){
        //Put metadata in the out file
        String[] out = new String[]{"//"+name.getData()};
        LinkedList<String> lines = new LinkedList<String>();
        //Link this components entity to the attribute entity.
        lines.addLast("var entity;");
        lines.addLast("");
        lines.addLast("//Attributes");
        for(Attribute attribute : e.getAttributes()){
            if(!attribute.isPrivate()) {
                lines.addLast("var " + attribute.getID()+";");
            }
        }
        lines.addLast("");
        lines.addLast("function init(reference){");
        lines.addLast("     entity = reference;");
        for(Attribute attribute : e.getAttributes()){
            if(!attribute.isPrivate()) {
                lines.addLast("     "+attribute.getID() + " = entity.getAttribute(\"" + attribute.getID() + "\");");
            }
        }
        lines.addLast("}");
        lines.addLast("");
        lines.addLast("//Tick function is called (Game.FPS) times per second.");
        lines.addLast("function tick(){");
        lines.addLast("");
        lines.addLast("}");
        lines.addLast("");
        lines.addLast("//Functions");
        for(Component component : e.getComponents()){
            for(Function function : component.getFunctions()){
                lines.addLast("//Added from "+component.getType().name().toLowerCase()+" component.");
                lines.addLast("function "+function.getFunctionName()+"(){");
                lines.addLast("");
                lines.addLast("}");
                lines.addLast("");
            }
        }

        //Synch the lines to out
        int index = 0;
        for(String s : lines){
            out = StringUtils.addLine(out, lines.get(index));
            index++;
        }
        return out;
    }
}
