/**
 * 
 */
package de.cognicrypt.codegenerator.taskintegrator.models;

import java.util.ArrayList;

import de.cognicrypt.codegenerator.Constants;
import de.cognicrypt.codegenerator.Constants.FeatureType;

/**
 * @author rajiv
 *
 */
public class ClaferFeature {
	private FeatureType featureType;
	private String featureName;
	private String featureInheritance;
	private ArrayList<FeatureProperty> featureProperties; // <String name, String value>. The names in this collection cannot be repeated.
	private ArrayList<ClaferConstraint> featureConstraints; // each constraint will be generated as a "valid" string in the constraint generator pop up.
	/**
	 * @param featureType
	 * @param featureName
	 * @param featureInheritance
	 * 
	 * 
	 */
	public ClaferFeature(FeatureType featureType, String featureName, String featureInheritance) {
		super();
		this.setFeatureType(featureType);
		this.setFeatureName(featureName);
		this.setFeatureInheritance(featureInheritance);
		this.featureProperties = new ArrayList<FeatureProperty>();
		this.featureConstraints = new ArrayList<ClaferConstraint>();
	}
	/**
	 * @return the featureType
	 */
	public FeatureType getFeatureType() {
		return featureType;
	}
	/**
	 * @param featureType the featureType to set
	 */
	public void setFeatureType(FeatureType featureType) {
		this.featureType = featureType;
	}
	/**
	 * @return the featureName
	 */
	public String getFeatureName() {
		return featureName;
	}
	/**
	 * @param featureName the featureName to set
	 */
	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}

	public String getFeatureInheritance() {
		return featureInheritance;
	}

	public void setFeatureInheritance(String featureInheritance) {
		this.featureInheritance = featureInheritance;
	}

	/**
	 * @return the properties
	 */
	public ArrayList<FeatureProperty> getfeatureProperties() {
		return featureProperties;
	}

	public void setFeatureProperties(ArrayList<FeatureProperty> featureProperties) {
		this.featureProperties = featureProperties;
	}

	public ArrayList<ClaferConstraint> getFeatureConstraints() {
		return featureConstraints;
	}

	public void setFeatureConstraints(ArrayList<ClaferConstraint> featureConstraints) {
		this.featureConstraints = featureConstraints;
	}

	@Override
	public String toString() {
		StringBuilder strRepresentation = new StringBuilder();
		
		if (featureType == Constants.FeatureType.ABSTRACT) {
			strRepresentation.append("abstract ");
		}
		
		strRepresentation.append(getFeatureName());
		
		if (!getFeatureInheritance().isEmpty()) {
			strRepresentation.append(": ");
			strRepresentation.append(getFeatureInheritance());
		}
		
		for (FeatureProperty featureProperty : getfeatureProperties()) {
			strRepresentation.append("\n\t");
			strRepresentation.append(featureProperty.toString());			
		}
		
		for (ClaferConstraint featureConstraint : getFeatureConstraints()) {
			strRepresentation.append("\n\t[ ");
			strRepresentation.append(featureConstraint.getConstraint());
			strRepresentation.append(" ]");
		}
		
		strRepresentation.append("\n");
		
		return strRepresentation.toString();
	}

}