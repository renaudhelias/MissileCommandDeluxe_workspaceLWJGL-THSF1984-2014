package net.stinfoservices.helias.renaud.tempest.level.properties;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.stinfoservices.helias.renaud.tempest.TempestMain;
import net.stinfoservices.helias.renaud.tempest.tools.Point3D;

public class Scenario {
	Map<Integer,List<Point3D.Integer>> maisonCoords=new HashMap<Integer,List<Point3D.Integer>>();
	Map<Integer,List<String>> maisonNames=new HashMap<Integer,List<String>>();
	Map<Integer,Map<Integer,List<Point3D.Integer>>> missileAngles=new HashMap<Integer,Map<Integer,List<Point3D.Integer>>>();
	Map<Integer,Map<Integer,List<Integer>>> missilePeriods=new HashMap<Integer,Map<Integer,List<Integer>>>();
	private Map<Integer, Integer> munitions = new HashMap<Integer, Integer>();
	private Map<Integer, Map<Integer, Integer>> scores = new HashMap<Integer, Map<Integer,Integer>>();
	private Map<Integer, Map<Integer, Integer>> pauses = new HashMap<Integer, Map<Integer,Integer>>();
	private Map<Integer,List<Double>> maisonZooms = new HashMap<Integer,List<Double>>();
	private Scenario() {
		try {
			URL ressource = TempestMain.class.getResource("MissileCommandDeluxe.scenario");
			if (ressource==null) throw new Error("MissileCommandDeluxe.scenario not found");
			if (ressource!= null) {
				File f = new File(ressource.toURI());
				FileReader fr = new FileReader(f);
				BufferedReader br = new BufferedReader(fr);
				String line=null;
				String n="^NIVEAU ([0-9]+)$";
				String cplus="^Maison (-?[0-9]+),(-?[0-9]+),(-?[0-9]+) ([a-zA-Z0-9]+) ([0-9]+(?:\\.[0-9]+)?)$";
				String c="^Maison (-?[0-9]+),(-?[0-9]+),(-?[0-9]+) ([a-zA-Z0-9]+)$";
				String a="^munitions ([0-9]+)$";
				String v="^VAGUE ([0-9]+)$";
				String r="^score ([0-9]+)$";
				String s="^pause ([0-9]+)$";
				String m="^Missile ([0-9]+),(0),([0-9]+) ([0-9]+)$";
				Pattern patternN = Pattern.compile(n);
				Pattern patternCplus = Pattern.compile(cplus);
				Pattern patternC = Pattern.compile(c);
				Pattern patternA = Pattern.compile(a);
				Pattern patternV = Pattern.compile(v);
				Pattern patternR = Pattern.compile(r);
				Pattern patternS = Pattern.compile(s);
				Pattern patternM = Pattern.compile(m);
				int niveau=0;
				int vague=0;
				while ((line=br.readLine())!=null) {
					Matcher matcherN = patternN.matcher(line);
					if (matcherN.matches()) {
						System.out.println("========NIVEAU "+Integer.valueOf(matcherN.group(1)));
						niveau = Integer.valueOf(matcherN.group(1))-1;
						maisonCoords.put(niveau, new ArrayList<Point3D.Integer>());
						maisonNames.put(niveau, new ArrayList<String>());
						missileAngles.put(niveau, new HashMap<Integer,List<Point3D.Integer>>());
						missilePeriods.put(niveau, new HashMap<Integer,List<Integer>>());
						pauses.put(niveau, new HashMap<Integer,Integer>());
						scores.put(niveau, new HashMap<Integer,Integer>());
						maisonZooms.put(niveau, new ArrayList<Double>());
					}
					Matcher matcherCplus = patternCplus.matcher(line);
					Matcher matcherC = patternC.matcher(line);
					if (matcherCplus.matches()) {
						System.out.println("========Maison ++ "+Integer.valueOf(matcherCplus.group(1))+","+Integer.valueOf(matcherCplus.group(2))+","+Integer.valueOf(matcherCplus.group(3))+" "+matcherCplus.group(4)+" ratio : "+matcherCplus.group(5));
						maisonCoords.get(niveau).add(new Point3D.Integer(Integer.valueOf(matcherCplus.group(1)),Integer.valueOf(matcherCplus.group(2)),Integer.valueOf(matcherCplus.group(3)) ));
						maisonNames.get(niveau).add(matcherCplus.group(4));
						maisonZooms.get(niveau).add(Double.valueOf(matcherCplus.group(5)));
					} else if (matcherC.matches()) {
						System.out.println("========Maison "+Integer.valueOf(matcherC.group(1))+","+Integer.valueOf(matcherC.group(2))+","+Integer.valueOf(matcherC.group(3))+" "+matcherC.group(4));
						maisonCoords.get(niveau).add(new Point3D.Integer(Integer.valueOf(matcherC.group(1)),Integer.valueOf(matcherC.group(2)),Integer.valueOf(matcherC.group(3)) ));
						maisonNames.get(niveau).add(matcherC.group(4));
						maisonZooms.get(niveau).add(1.0);
					}
					Matcher matcherA = patternA.matcher(line);
					if (matcherA.matches()) {
						System.out.println("========Munitions "+Integer.valueOf(matcherA.group(1)));
						munitions.put(niveau,Integer.valueOf(matcherA.group(1)));
					}
					Matcher matcherV = patternV.matcher(line);
					if (matcherV.matches()) {
						System.out.println
						("========VAGUE "+Integer.valueOf(matcherV.group(1)));
						vague = Integer.valueOf(matcherV.group(1))-1;
						missileAngles.get(niveau).put(vague, new ArrayList<Point3D.Integer>());
						missilePeriods.get(niveau).put(vague, new ArrayList<Integer>());
					}
					Matcher matcherR = patternR.matcher(line);
					if (matcherR.matches()) {
						System.out.println("========Score par maison "+Integer.valueOf(matcherR.group(1)));
						scores.get(niveau).put(vague,Integer.valueOf(matcherR.group(1)));
					}
					Matcher matcherS = patternS.matcher(line);
					if (matcherS.matches()) {
						System.out.println("========Pause "+Integer.valueOf(matcherS.group(1)));
						pauses.get(niveau).put(vague,Integer.valueOf(matcherS.group(1)));
					}
					Matcher matcherM = patternM.matcher(line);
					if (matcherM.matches()) {
						System.out.println("========Missile "+Integer.valueOf(matcherM.group(1))+","+Integer.valueOf(matcherM.group(2))+","+Integer.valueOf(matcherM.group(3))+" period "+Integer.valueOf(matcherM.group(4)));
						missileAngles.get(niveau).get(vague).add(new Point3D.Integer(Integer.valueOf(matcherM.group(1)),Integer.valueOf(matcherM.group(2)),Integer.valueOf(matcherM.group(3))));
						missilePeriods.get(niveau).get(vague).add(Integer.valueOf(matcherM.group(4)));
					}
				}
				br.close();
				fr.close();
			}		
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private static Scenario instance=null;
	public static Scenario getInstance() {
		if (instance==null) {
			instance= new Scenario();
		}
		return instance;
	}
	
	public int getNbNiveaux() {
		return maisonCoords.keySet().size();
	}
	public int getNbVagues(int niveau) {
		return missileAngles.get(niveau).keySet().size();
	}
	public int getNbMaisons(int niveau) {
		return maisonCoords.get(niveau).size();
	}
	public Point3D.Integer getMaisonCoords(int niveau, int noMaison) {
		return maisonCoords.get(niveau).get(noMaison);
	}
	
	public String getMaisonName(int niveau, int noMaison) {
		return maisonNames.get(niveau).get(noMaison);
	}
	public double getMaisonRatio(int niveau, int noMaison) {
		return maisonZooms.get(niveau).get(noMaison);
	}
	public int getNbMissiles(int niveau, int vague) {
		return missileAngles.get(niveau).get(vague).size();
	}
	public Point3D.Integer getMissileAngle(int niveau, int vague, int noMissile) {
		return missileAngles.get(niveau).get(vague).get(noMissile);
	}
	public int getMissilePeriod(int niveau, int vague, int noMissile) {
		return missilePeriods.get(niveau).get(vague).get(noMissile);
	}

	public int getNbNiveauxPhases() {
		int c=0;
		for (int n=0;n<Scenario.getInstance().getNbNiveaux();n++) {
			for (int v=0;v<Scenario.getInstance().getNbVagues(n);v++) {
				c++;
			}
		}
		return c;
	}

	public int getMunitions(int niveau) {
		if (!munitions.containsKey(niveau)) return 0;
		return munitions.get(niveau);
	}
	public int getPause(int niveau, int vague) {
		if (!pauses.containsKey(niveau)) return 0;
		if (!pauses.get(niveau).containsKey(vague)) return 0;
		return pauses.get(niveau).get(vague);
	}
	public int getScoreParMaison(int niveau, int vague) {
		if (!scores.containsKey(niveau)) return 0;
		if (!scores.get(niveau).containsKey(vague)) return 0;
		return scores.get(niveau).get(vague);
	}
	
}
