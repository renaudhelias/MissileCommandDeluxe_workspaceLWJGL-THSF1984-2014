package net.stinfoservices.helias.renaud.tempest.level;

import net.stinfoservices.helias.renaud.tempest.tools.Point3D;
import net.stinfoservices.helias.renaud.tempest.tools.Point3D.Double;

public class CalibrateResult {

	private Double origine;
	private double angleCanon;
	private String canonMouseName;
	private double canonMouseValue;
	private double anglePlateau;
	private String plateauMouseName;
	private double plateauMouseValue;

	/**
	 * FIXME : Plateau : 180.0°=226.0pas. Canon : 143.015549017747°=-251.0pas. 143.015549017747 !!!!!!!!!!??????
	 * @param origine
	 * @param angleCanon
	 * @param canonMouseName
	 * @param canonMouseValue
	 * @param anglePlateau
	 * @param plateauMouseName
	 * @param plateauMouseValue
	 */
	public CalibrateResult(
			Point3D.Double origine,
			double angleCanon, String canonMouseName, double canonMouseValue,
			double anglePlateau, String plateauMouseName, double plateauMouseValue) {
		this.origine = origine;
		this.angleCanon=angleCanon;
		this.canonMouseName = canonMouseName;
		this.canonMouseValue =canonMouseValue;
		this.anglePlateau = anglePlateau;
		this.plateauMouseName = plateauMouseName;
		this.plateauMouseValue = plateauMouseValue;
	}

	/**
	 * Position du centre du plateau, par rapport à la caméra. C'est un centre de rotation fixe.
	 * @return
	 */
	public Double getOrigine() {
		return origine;
	}

	/**
	 * Angle maximum du canon
	 * @return
	 */
	public double getAngleCanon() {
		return angleCanon;
	}

	/**
	 * Sourie utilisé pour le canon
	 * @return
	 */
	public String getCanonMouseName() {
		return canonMouseName;
	}

	/**
	 * Parcours maximum de la sourie du canon
	 * @return
	 */
	public double getCanonMouseValue() {
		return canonMouseValue;
	}

	/**
	 * Angle maximum du plateau
	 * @return
	 */
	public double getAnglePlateau() {
		return anglePlateau;
	}

	/**
	 * Sourie utilisé pour le plateau
	 * @return
	 */
	public String getPlateauMouseName() {
		return plateauMouseName;
	}

	/**
	 * Parcours maximum de la sourie du plateau
	 * @return
	 */
	public double getPlateauMouseValue() {
		return plateauMouseValue;
	}

	@Override
	public String toString() {
		return "Plateau : "+anglePlateau+"°="+plateauMouseValue+"pas. Canon : "+angleCanon+"°="+canonMouseValue+"pas.";
	}
}
