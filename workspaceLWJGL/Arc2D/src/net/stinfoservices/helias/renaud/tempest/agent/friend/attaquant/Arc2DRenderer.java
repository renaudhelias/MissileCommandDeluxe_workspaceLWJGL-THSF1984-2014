package net.stinfoservices.helias.renaud.tempest.agent.friend.attaquant;

import java.awt.geom.Point2D;

public class Arc2DRenderer {

	private Point2D.Double haut = new Point2D.Double();

	private Point2D.Double bas = new Point2D.Double();

	private double ratio = 3;
	private double taille;
	private double demiCorde;
	
	private boolean impossible;
	
	/**
	 * Approximation
	 */
	private double demiArc;

	/**
	 * Refresh arc.
	 * 
	 * @param p1
	 *            origine (avant de l'arc)
	 * @param p2
	 *            taille (arrière de l'arc détendu)
	 * @param p3
	 *            tension (arrière de l'arc tendu)
	 */
	public void refreshArc(Point2D.Double p1, Point2D.Double p2,
			Point2D.Double p3) {
		taille = Math.sqrt((p2.x - p1.x) * (p2.x - p1.x) + (p2.y - p1.y)
				* (p2.y - p1.y));
		demiCorde = taille * ratio;
		demiArc = Math.sqrt(taille * taille + demiCorde * demiCorde);
		
		//taille : 70.2566722810012 demiCorde:210.7700168430036 demiArc:222.1711052319811
		//System.out.println("taille : "+taille+" demiCorde:"+demiCorde+" demiArc:"+demiArc);

		if ((p1.y-p3.y) == 0 && (p1.x-p3.x) == 0) {
			impossible = true;
		} else if ((p1.y-p3.y) == 0) {
			// reverse
			 refreshArc(new Point2D.Double(p1.y,p1.x),new Point2D.Double(p2.y,p2.x),new Point2D.Double(p3.y,p3.x));
			 haut = new Point2D.Double(haut.y,haut.x);
			 bas = new Point2D.Double(bas.y,bas.x);
			 return;
		}
		
		// http://www.ambrsoft.com/TrigoCalc/Circles2/Circle2.htm
		
		
		// (x-p3.x)^2+(y-p3.y)^2=demiCorde^2
		// (x-p1.x)^2+(y-p1.y)^2=demiArc^2
		
		// x^2+y^2-2*x*p3.x-2*y*p3.y+[p3.x^2+p3.y^2]=demiCorde^2
		// x^2+y^2-2*x*p1.x-2*y*p1.y+[p1.x^2+p1.y^2]=demiArc^2
		
		
		// x^2+y^2=demiCorde^2 - [-2*x*p3.x-2*y*p3.y+[p3.x^2+p3.y^2]]
		// x^2+y^2-2*x*p1.x-2*y*p1.y+[p1.x^2+p1.y^2]=demiArc^2
		
		// demiCorde^2 - [-2*x*p3.x-2*y*p3.y+[p3.x^2p3.y^2]] -2*x*p1.x-2*y*p1.y+[p1.x^2+p1.y^2]=demiArc^2
		// [demiCorde^2 - demiArc^2 +p1.x^2+p1.y^2]= -2*x*p3.x-2*y*p3.y+[p3.x^2+p3.y^2] +2*x*p1.x+2*y*p1.y
		// [demiCorde^2 - demiArc^2 +p1.x^2+p1.y^2]-[p3.x^2+p3.y^2]= -2*x*p3.x-2*y*p3.y +2*x*p1.x+2*y*p1.y
		// [demiCorde^2 - demiArc^2 +p1.x^2+p1.y^2-p3.x^2-p3.y^2]= -2*x*p3.x -2*y*p3.y +2*x*p1.x +2*y*p1.y
		// [demiCorde^2 - demiArc^2 +p1.x^2+p1.y^2-p3.x^2-p3.y^2]= 2*x*(p1.x-p3.x) + 2*y*(p1.y-p3.y)
		
		// [demiCorde^2 - demiArc^2 +p1.x^2+p1.y^2-p3.x^2-p3.y^2]/2= x*(p1.x-p3.x) + y*(p1.y-p3.y) // équation droite
		// (x-p3.x)^2+(y-p3.y)^2=demiCorde^2 // équation cercle (d'origine)

		// y*(p1.y-p3.y)= (([(demiCorde^2 - demiArc^2 +p1.x^2+p1.y^2-p3.x^2-p3.y^2)/2] - x*(p1.x-p3.x)))
		// y= ([((demiCorde^2 - demiArc^2 +p1.x^2+p1.y^2-p3.x^2-p3.y^2)/2)/(p1.y-p3.y)] - x*[(p1.x-p3.x)/(p1.y-p3.y)])

		// (x-p3.x)^2+( ([((demiCorde^2 - demiArc^2 +p1.x^2+p1.y^2-p3.x^2-p3.y^2)/2)/(p1.y-p3.y)] - x*[(p1.x-p3.x)/(p1.y-p3.y)])  -p3.y)^2=demiCorde^2
		// (x-p3.x)^2+( [((demiCorde^2 - demiArc^2 +p1.x^2+p1.y^2-p3.x^2-p3.y^2)/2)/(p1.y-p3.y)  -p3.y]  - x*[(p1.x-p3.x)/(p1.y-p3.y)] )^2=demiCorde^2
		// or (a+b)^2 = a^2+b^2+2*a*b
		// x^2 +p3.x^2 -2*x*p3.x +( [((demiCorde^2 - demiArc^2 +p1.x^2+p1.y^2-p3.x^2-p3.y^2)/2)/(p1.y-p3.y)-p3.y] - x*[(p1.x-p3.x)/(p1.y-p3.y)])^2=demiCorde^2
		// x^2 +p3.x^2 -2*x*p3.x +  [((demiCorde^2 - demiArc^2 +p1.x^2+p1.y^2-p3.x^2-p3.y^2)/2)/(p1.y-p3.y)-p3.y]^2 + x^2*[(p1.x-p3.x)/(p1.y-p3.y)]^2 - 2*x*[(p1.x-p3.x)/(p1.y-p3.y)]*[((demiCorde^2 - demiArc^2 +p1.x^2+p1.y^2-p3.x^2-p3.y^2)/2)/(p1.y-p3.y)-p3.y]=demiCorde^2
		// x^2    1       x                                                      1                                    x^2                                 x                                                                                                        =    1
		// x^2*(1+[(p1.x-p3.x)/(p1.y-p3.y)]^2) + p3.x^2 -2*x*p3.x +[((demiCorde^2 - demiArc^2 +p1.x^2+p1.y^2-p3.x^2-p3.y^2)/2)/(p1.y-p3.y)-p3.y]^2    - 2*x*[(p1.x-p3.x)/(p1.y-p3.y)]*[((demiCorde^2 - demiArc^2 +p1.x^2+p1.y^2-p3.x^2-p3.y^2)/2)/(p1.y-p3.y)-p3.y]=demiCorde^2
		// x^2                                     1       x                              1                                                               x                                                                                                        =    1
		// x^2*(1+[(p1.x-p3.x)/(p1.y-p3.y)]^2) + x*(-2*[p3.x +((p1.x-p3.x)/(p1.y-p3.y))*(((demiCorde^2 - demiArc^2 +p1.x^2+p1.y^2-p3.x^2-p3.y^2)/2)/(p1.y-p3.y)-p3.y)]) +[((demiCorde^2 - demiArc^2 +p1.x^2+p1.y^2-p3.x^2-p3.y^2)/2)/(p1.y-p3.y)-p3.y]^2 =demiCorde^2 -p3.x^2
		// x^2                                   x                                                                                                                                        1                                                              =    1           1
		// x^2*(1+[(p1.x-p3.x)/(p1.y-p3.y)]^2) + x*(-2*[p3.x +((p1.x-p3.x)/(p1.y-p3.y))*(((demiCorde^2 - demiArc^2 +p1.x^2+p1.y^2-p3.x^2-p3.y^2)/2)/(p1.y-p3.y)-p3.y)]) =demiCorde^2 -p3.x^2 -[((demiCorde^2 - demiArc^2 +p1.x^2+p1.y^2-p3.x^2-p3.y^2)/2)/(p1.y-p3.y)-p3.y]^2
		// x^2                                   x                                                                                                                      =    1           1               1
		// posons  x^2*A+x*B=C
		// delta = b^2-a*c
//		double a = 1+Math.pow(((p1.x-p3.x) / (p3.y-p1.y)),2);
//		double b = -2*p3.x +2*((Math.pow(demiArc, 2)-Math.pow(demiCorde, 2)-Math.pow(p1.y, 2)+Math.pow(p2.y, 2))/(2*(p2.y-p1.y))-p3.y)*((p1.x-p3.x) / (p3.y-p1.y));
//		double c = Math.pow(demiCorde,2) - Math.pow(p3.x,2) - Math.pow(((Math.pow(demiArc, 2)-Math.pow(demiCorde, 2)-Math.pow(p1.y, 2)+Math.pow(p2.y, 2))/(2*(p2.y-p1.y))-p3.y),2); 
//		double a =(1+Math.pow(((p1.x-p3.x)/(p1.y-p3.y)),2));
//	    double c =Math.pow(demiCorde,2) -Math.pow(p3.x,2) -Math.pow((((Math.pow(demiCorde,2) - Math.pow(demiArc,2) +Math.pow(p1.x,2)+Math.pow(p1.y,2)-Math.pow(p3.x,2)-Math.pow(p3.y,2))/2)/(p1.y-p3.y)-p3.y),2);
//		double b = (-2*(p3.x +((p1.x-p3.x)/(p1.y-p3.y))*(((Math.pow(demiCorde,2) - Math.pow(demiArc,2) +Math.pow(p1.x,2)+Math.pow(p1.y,2)-Math.pow(p3.x,2)-Math.pow(p3.y,2))/2)/(p1.y-p3.y)-p3.y)));
		
		
		// distance between circles
		double D = Math.sqrt(Math.pow(p3.x-p1.x,2)+Math.pow(p3.y-p1.y,2));
		
		double delta = (1.0/4.0)*Math.sqrt((D+demiArc+demiCorde)*(D+demiArc-demiCorde)*(D-demiArc+demiCorde)*(-D+demiArc+demiCorde));
//		double a = 1+Math.pow(((p1.x-p3.x) / (p3.y-p1.y)),2);
//		double b = -2*p3.x +2*(((Math.pow(demiArc, 2)-Math.pow(demiCorde, 2))+(Math.pow(p3.x, 2)-Math.pow(p1.x, 2)) + (Math.pow(p3.y, 2)-Math.pow(p1.y, 2)))/(2*(p3.y-p1.y))-p3.y)*((p1.x-p3.x) / (p3.y-p1.y));
//		double c = Math.pow(demiCorde,2) - Math.pow(p3.x,2) - Math.pow((((Math.pow(demiArc, 2)-Math.pow(demiCorde, 2))+(Math.pow(p3.x, 2)-Math.pow(p1.x, 2)) + (Math.pow(p3.y, 2)-Math.pow(p1.y, 2)))/(2*(p3.y-p1.y))-p3.y),2); 

//		b = b/2;
//		double delta = Math.pow(b,2)-a*c;
//		System.out.println("a:"+a+" b:"+b+" c:"+c+" d:"+delta);
		impossible = (demiCorde+demiArc<D);
		if (!impossible) {
			haut.x = (p1.x+p3.x)/2+((p3.x-p1.x)*(Math.pow(demiArc,2)-Math.pow(demiCorde,2))/(2*Math.pow(D, 2))) + 2* ((p1.y-p3.y)/Math.pow(D, 2))*delta; 
			//haut.y= ((((Math.pow(demiCorde,2) - Math.pow(demiArc,2) +Math.pow(p1.x,2)+Math.pow(p1.y,2)-Math.pow(p3.x,2)-Math.pow(p3.y,2))/2)/(p1.y-p3.y)) - haut.x*((p1.x-p3.x)/(p1.y-p3.y)));
			// http://www.bibmath.net/forums/viewtopic.php?id=1707
			haut.y = (p1.y+p3.y)/2+((p3.y-p1.y)*(Math.pow(demiArc,2)-Math.pow(demiCorde,2))/(2*Math.pow(D, 2))) - 2* ((p1.x-p3.x)/Math.pow(D, 2))*delta;
					//((p1.x-p3.x) / (p3.y-p1.y))*haut.x + ((Math.pow(demiArc, 2)-Math.pow(demiCorde, 2))+(Math.pow(p3.x, 2)-Math.pow(p1.x, 2)) + (Math.pow(p3.y, 2)-Math.pow(p1.y, 2)) )/(2*(p3.y-p1.y));
			
			bas.x = (p1.x+p3.x)/2+(p3.x-p1.x)*((Math.pow(demiArc,2)-Math.pow(demiCorde,2))/(2*Math.pow(D, 2))) - 2* ((p1.y-p3.y)/Math.pow(D, 2))*delta;
			//bas.y= ((((Math.pow(demiCorde,2) - Math.pow(demiArc,2) +Math.pow(p1.x,2)+Math.pow(p1.y,2)-Math.pow(p3.x,2)-Math.pow(p3.y,2))/2)/(p1.y-p3.y)) - bas.x*((p1.x-p3.x)/(p1.y-p3.y)));
			bas.y = (p1.y+p3.y)/2+((p3.y-p1.y)*(Math.pow(demiArc,2)-Math.pow(demiCorde,2))/(2*Math.pow(D, 2))) + 2* ((p1.x-p3.x)/Math.pow(D, 2))*delta;
			
			
			// (x-p3.x)^2+(y-p3.y)^2=demiCorde^2 // équation cercle (d'origine)
			// (x-p3.x)^2              +(    ((p1.x-p3.x) / (p3.y-p1.y))*x + ((Math.pow(demiArc, 2)-Math.pow(demiCorde, 2))+(Math.pow(p3.x, 2)-Math.pow(p1.x, 2)) + (Math.pow(p3.y, 2)-Math.pow(p1.y, 2)))/(2*(p3.y-p1.y))               -p3.y)^2=demiCorde^2
			// x^2 + p3.x^2 - 2*x*p3.x +(    ((p1.x-p3.x) / (p3.y-p1.y))*x +(((Math.pow(demiArc, 2)-Math.pow(demiCorde, 2))+(Math.pow(p3.x, 2)-Math.pow(p1.x, 2)) + (Math.pow(p3.y, 2)-Math.pow(p1.y, 2)))/(2*(p3.y-p1.y))-p3.y)              )^2=demiCorde^2
			// x^2 + p3.x^2 - 2*x*p3.x +     ((p1.x-p3.x) / (p3.y-p1.y))^2*x^2 + (((Math.pow(demiArc, 2)-Math.pow(demiCorde, 2))+(Math.pow(p3.x, 2)-Math.pow(p1.x, 2)) + (Math.pow(p3.y, 2)-Math.pow(p1.y, 2)))/(2*(p3.y-p1.y))-p3.y)^2 + 2*(((Math.pow(demiArc, 2)-Math.pow(demiCorde, 2))+(Math.pow(p3.x, 2)-Math.pow(p1.x, 2)) + (Math.pow(p3.y, 2)-Math.pow(p1.y, 2)))/(2*(p3.y-p1.y))-p3.y)*((p1.x-p3.x) / (p3.y-p1.y))*x=demiCorde^2
            // x^2     1        x                                          x^2                                      1                                                                                                                   |                                                                                                                                                                                    x=    1
//			double a = 1+Math.pow(((p1.x-p3.x) / (p3.y-p1.y)),2);
//			double b = -2*p3.x +2*(((Math.pow(demiArc, 2)-Math.pow(demiCorde, 2))+(Math.pow(p3.x, 2)-Math.pow(p1.x, 2)) + (Math.pow(p3.y, 2)-Math.pow(p1.y, 2)))/(2*(p3.y-p1.y))-p3.y)*((p1.x-p3.x) / (p3.y-p1.y));
//			double c = Math.pow(demiCorde,2) - Math.pow(p3.x,2) - Math.pow((((Math.pow(demiArc, 2)-Math.pow(demiCorde, 2))+(Math.pow(p3.x, 2)-Math.pow(p1.x, 2)) + (Math.pow(p3.y, 2)-Math.pow(p1.y, 2)))/(2*(p3.y-p1.y))-p3.y),2); 
		} else {
			System.out.println("impossible");
		}
		
	}
	
	/**
	 * Arc + corde trop tendus (théoriquement impossible d'aller plus loin !)
	 * @return
	 */
	public boolean isImpossible() {
		return impossible;
	}

	/**
	 * Point en haut de l'arc
	 * 
	 * @return
	 */
	public Point2D.Double getHaut() {
		return haut;
	}

	/**
	 * Point en bas de l'arc
	 * 
	 * @return
	 */
	public Point2D.Double getBas() {
		return bas;
	}
	
	public double getRatio() {
		return ratio;
	}
	public void setRatio(double ratio) {
		this.ratio = ratio;
	}

}
