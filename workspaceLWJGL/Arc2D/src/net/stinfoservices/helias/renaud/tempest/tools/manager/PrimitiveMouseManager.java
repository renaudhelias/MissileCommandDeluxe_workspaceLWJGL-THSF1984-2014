package net.stinfoservices.helias.renaud.tempest.tools.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Controller.Type;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;

public class PrimitiveMouseManager {

	private static PrimitiveMouseManager instance;
	Map<String, Controller> mices = new HashMap<String,Controller>();
	
	String names[]= new String []{"MOUSE1","MOUSE2","MOUSE3","MOUSE4","MOUSE5"};
	private Map<String,List<IPrimitiveMouseListener>>primitiveMouseListeners = new HashMap<String,List<IPrimitiveMouseListener>>();
	private List<IPrimitiveMouseListener> globalPrimitiveMouseListeners = new ArrayList<IPrimitiveMouseListener>();
	
	public PrimitiveMouseManager() {
		
        
        Controller[] controls = ControllerEnvironment.getDefaultEnvironment().getControllers();
        for (Controller c : controls) {
            System.out.println(c.getName()+" in port "+c.getPortNumber()+" ");
            Type t = c.getType();
            if (t== Type.MOUSE) {
            	System.out.println("Entering mouse "+names[mices.size()]);
                mices.put(names[mices.size()], c);
                c.poll();
                for (Component cc : c.getComponents()) {
                    System.out.println(" "+cc.getName()+" "+cc.getPollData());
                }
            }
        }
        System.out.println(mices.size()+" mice");
	}
	
	public static PrimitiveMouseManager getInstance() {
		if (instance == null) {
			instance = new PrimitiveMouseManager();
		}
		return instance;
	}
	
	public void step() {
		for (String mouseName : mices.keySet()) {
			if (primitiveMouseListeners.containsKey(mouseName)) {
				Controller c = mices.get(mouseName);
				if (c.poll()) {
		            Event event = new Event();
		            EventQueue eventQueue = c.getEventQueue();
		            while (eventQueue.getNextEvent(event )) {
		                // Vue qu'on utilise event, mieux vaut se fier aux value que pollData (Queue est une pile...)
//		                System.out.println("Mouse "+mouseName+" event : "+event.getComponent().getName()+" : "+event.getValue()+" &lt;= "+ event.getComponent().getPollData());
		                for (IPrimitiveMouseListener pmf : primitiveMouseListeners.get(mouseName)) {
			                if (event.getComponent().getIdentifier() == Component.Identifier.Button.LEFT) {
			                	pmf.mousePrimitiveClick(mouseName,0,(int)event.getValue());
			                }
			                if (event.getComponent().getIdentifier() == Component.Identifier.Button.RIGHT) {
			                	pmf.mousePrimitiveClick(mouseName,1,(int)event.getValue());
			                }
			                if (event.getComponent().getIdentifier() == Component.Identifier.Button.MIDDLE) {
			                	pmf.mousePrimitiveClick(mouseName,2,(int)event.getValue());
			                }
			                if (event.getComponent().getIdentifier() == Component.Identifier.Axis.X) {
			                	pmf.mousePrimitiveMouve(mouseName,0,event.getValue());
			                }
			                if (event.getComponent().getIdentifier() == Component.Identifier.Axis.Y) {
			                	pmf.mousePrimitiveMouve(mouseName,1,event.getValue());
			                }
		                }
		                for (IPrimitiveMouseListener pmf : globalPrimitiveMouseListeners) {
			                if (event.getComponent().getIdentifier() == Component.Identifier.Button.LEFT) {
			                	pmf.mousePrimitiveClick(mouseName,0,(int)event.getValue());
			                }
			                if (event.getComponent().getIdentifier() == Component.Identifier.Button.RIGHT) {
			                	pmf.mousePrimitiveClick(mouseName,1,(int)event.getValue());
			                }
			                if (event.getComponent().getIdentifier() == Component.Identifier.Button.MIDDLE) {
			                	pmf.mousePrimitiveClick(mouseName,2,(int)event.getValue());
			                }
			                if (event.getComponent().getIdentifier() == Component.Identifier.Axis.X) {
			                	pmf.mousePrimitiveMouve(mouseName,0,event.getValue());
			                }
			                if (event.getComponent().getIdentifier() == Component.Identifier.Axis.Y) {
			                	pmf.mousePrimitiveMouve(mouseName,1,event.getValue());
			                }
		                }
	                }
	            }
			} else {
				Controller c = mices.get(mouseName);
				if (c.poll()) {
		            Event event = new Event();
		            EventQueue eventQueue = c.getEventQueue();
		            while (eventQueue.getNextEvent(event )) {
		                // Vue qu'on utilise event, mieux vaut se fier aux value que pollData (Queue est une pile...)
//		                System.out.println("Mouse "+mouseName+" event : "+event.getComponent().getName()+" : "+event.getValue()+" &lt;= "+ event.getComponent().getPollData());
		                
		                for (IPrimitiveMouseListener pmf : globalPrimitiveMouseListeners) {
			                if (event.getComponent().getIdentifier() == Component.Identifier.Button.LEFT) {
			                	pmf.mousePrimitiveClick(mouseName,0,(int)event.getValue());
			                }
			                if (event.getComponent().getIdentifier() == Component.Identifier.Button.RIGHT) {
			                	pmf.mousePrimitiveClick(mouseName,1,(int)event.getValue());
			                }
			                if (event.getComponent().getIdentifier() == Component.Identifier.Button.MIDDLE) {
			                	pmf.mousePrimitiveClick(mouseName,2,(int)event.getValue());
			                }
			                if (event.getComponent().getIdentifier() == Component.Identifier.Axis.X) {
			                	pmf.mousePrimitiveMouve(mouseName,0,event.getValue());
			                }
			                if (event.getComponent().getIdentifier() == Component.Identifier.Axis.Y) {
			                	pmf.mousePrimitiveMouve(mouseName,1,event.getValue());
			                }
		                }
	                }
				}
			}
			
//				System.out.println("WARNING mouseName " +mouseName+" not used");
			
        }
	}
	
	public void addListener(IPrimitiveMouseListener pml) {
		this.globalPrimitiveMouseListeners.add(pml);
	}

	public void addListener(String mouseName, IPrimitiveMouseListener pml) {
		if (!this.primitiveMouseListeners.containsKey(mouseName)) {
			this.primitiveMouseListeners.put(mouseName, new ArrayList<IPrimitiveMouseListener>());
		}
		this.primitiveMouseListeners.get(mouseName).add(pml);
	}
	public void resetListeners() {
		this.primitiveMouseListeners.clear();
		this.globalPrimitiveMouseListeners.clear();
	}
}
