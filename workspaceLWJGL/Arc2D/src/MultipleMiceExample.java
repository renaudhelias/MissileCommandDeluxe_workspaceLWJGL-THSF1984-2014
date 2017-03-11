import java.util.ArrayList;
import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.EventQueue;
import net.java.games.input.Controller.Type;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Event;


public class MultipleMiceExample {
    private static final int SCREEN_WIDTH = 1066;
    private static final int SCREEN_HEIGHT = 400;
    
    public static void main(String[] args) throws InterruptedException {
        try {
            // context display important...
            Display.setDisplayMode(new DisplayMode(SCREEN_WIDTH, SCREEN_HEIGHT));
            Display.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(0);
        }
        List<Controller> mice = new ArrayList<Controller>();
        
        Controller[] controls = ControllerEnvironment.getDefaultEnvironment().getControllers();
        for (Controller c : controls) {
            System.out.println(c.getName()+" in port "+c.getPortNumber()+" ");
            Type t = c.getType();
            if (t== Type.MOUSE) {
                mice.add(c);
                c.poll();
                for (Component cc : c.getComponents()) {
                    System.out.println(" "+cc.getName()+" "+cc.getPollData());
                }
            }
        }
        System.out.println(mice.size()+" mice");
        
        while (!Display.isCloseRequested() ) {
            for (Controller c : mice) {
                if (c.poll()) {
                    Event event = new Event();
                    EventQueue eventQueue = c.getEventQueue();
                    while (eventQueue.getNextEvent(event )) {
                        // Vue qu'on utilise event, mieux vaut se fier aux value que pollData (Queue est une pile...)
                        System.out.println("Mouse number "+mice.indexOf(c)+" port"+c.getPortNumber()+" event : "+event.getComponent().getName()+" : "+event.getValue()+" &lt;= "+ event.getComponent().getPollData());
                    }

                }
            }
            Display.sync(60);
            Display.update();
        }
        
        System.out.println("bye bye");
    }
}