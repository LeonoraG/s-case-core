package eu.scasefp7.eclipse.core.ontology.tests;

import eu.scasefp7.eclipse.core.ontology.DynamicOntologyAPI;
import eu.scasefp7.eclipse.core.ontology.LinkedOntologyAPI;
import eu.scasefp7.eclipse.core.ontology.StaticOntologyAPI;

/**
 * Links the static and the dynamic ontologies into one linked ontology.
 * 
 * @author themis
 */
public class LinkedOntologyAPITest {

	/**
	 * Links the static and the dynamic ontologies. The ontologies are read and combined in a new ontology.
	 * 
	 * @param args unused parameter.
	 */
	public static void main(String[] args) {
		String projectName = "Restmarks";

		// Load the two ontologies
		StaticOntologyAPI staticOntology = new StaticOntologyAPI(projectName, false);
		DynamicOntologyAPI dynamicOntology = new DynamicOntologyAPI(projectName, false);

		// Create a new file for the linked ontology and instantiate it
		LinkedOntologyAPI linkedOntology = new LinkedOntologyAPI(projectName);

		// Iterate over all objects of the static ontology
		for (String object : staticOntology.getObjects()) {

			// Add the object as a resource in the linked ontology
			linkedOntology.addResource(object);

			// Iterate over all related objects of the object and add them to the linked ontology
			for (String relatedObject : staticOntology.getRelatedObjectsOfObject(object)) {
				linkedOntology.addResource(relatedObject);
				linkedOntology.addRelatedResourceToResource(object, relatedObject);
				for (String requirement : staticOntology.getRequirementsOfConcept(relatedObject)) {
					linkedOntology.addRequirement(requirement);
					linkedOntology.connectRequirementToElement(requirement, relatedObject);
				}
			}

			// Iterate over all properties of the object and add them to the linked ontology
			for (String property : staticOntology.getPropertiesOfObject(object)) {
				linkedOntology.addPropertyToResource(object, property);
				for (String requirement : staticOntology.getRequirementsOfConcept(property)) {
					linkedOntology.addRequirement(requirement);
					linkedOntology.connectRequirementToElement(requirement, property);
				}
			}

			// Iterate over all actions of the object and add them to the linked ontology
			for (String action : staticOntology.getActionsOfObject(object)) {
				String activity = action + " " + object;
				linkedOntology.addActivityToResource(object, activity);
				linkedOntology.addActionToActivity(activity, action);
				for (String requirement : staticOntology.getRequirementsOfOperation(action)) {
					linkedOntology.addRequirement(requirement);
					linkedOntology.connectRequirementToElement(requirement, activity);
					linkedOntology.connectRequirementToElement(requirement, action);
				}
			}

			// Iterate over all requirements containing the object and add them to the linked ontology
			for (String requirement : staticOntology.getRequirementsOfConcept(object)) {
				linkedOntology.addRequirement(requirement);
				linkedOntology.connectRequirementToElement(requirement, object);
			}
		}

		// Iterate over all activities of the dynamic ontology
		for (String dynactivity : dynamicOntology.getActivities()) {

			// Get the object of the activity and add it as a resource in the linked ontology
			String object = dynamicOntology.getObjectOfActivity(dynactivity);
			if (object != null) {
				linkedOntology.addResource(object);
				for (String diagram : dynamicOntology.getDiagramsOfConcept(dynactivity)) {
					linkedOntology.addActivityDiagram(diagram);
					linkedOntology.connectActivityDiagramToElement(diagram, object);
				}

				// Get the action of the activity and add it to the linked ontology
				String action = dynamicOntology.getActionOfActivity(dynactivity);
				if (action != null) {
					String activity = action + " " + object;
					linkedOntology.addActivityToResource(object, activity,
							dynamicOntology.getActivityTypeOfActivity(dynactivity));
					linkedOntology.addActionToActivity(activity, action);
					for (String diagram : dynamicOntology.getDiagramsOfConcept(dynactivity)) {
						linkedOntology.addActivityDiagram(diagram);
						linkedOntology.connectActivityDiagramToElement(diagram, object);
						linkedOntology.connectActivityDiagramToElement(diagram, action);
						linkedOntology.connectActivityDiagramToElement(diagram, activity);
					}
				}

				// Iterate over all properties of activity and add them as properties of object to the ontology
				for (String property : dynamicOntology.getPropertiesOfActivity(object)) {
					linkedOntology.addPropertyToResource(object, property);
					for (String diagram : dynamicOntology.getDiagramsOfConcept(property)) {
						linkedOntology.addActivityDiagram(diagram);
						linkedOntology.connectActivityDiagramToElement(diagram, property);
					}
				}
			}
		}

		// Iterate over all transitions of the dynamic ontology
		for (String transition : dynamicOntology.getTransitions()) {

			// Get the condition of the transition
			String condition = dynamicOntology.getConditionOfTransition(transition);

			// Get the source activity of the transition
			String sourcedynactivity = dynamicOntology.getSourceActivityOfTransition(transition);
			String saction = dynamicOntology.getActionOfActivity(sourcedynactivity);
			String sobject = dynamicOntology.getObjectOfActivity(sourcedynactivity);
			String sactivity = saction + " " + sobject;

			// Get the target activity of the transition
			String targetdynactivity = dynamicOntology.getTargetActivityOfTransition(transition);
			String taction = dynamicOntology.getActionOfActivity(targetdynactivity);
			String tobject = dynamicOntology.getObjectOfActivity(targetdynactivity);
			String tactivity = taction + " " + tobject;

			// Connect the activities
			if (taction != null && saction != null)
				linkedOntology.addNextActivityToActivity(sactivity, tactivity);

			// Add the condition of the transition to the respective activity of the linked ontology.
			if (condition != null && taction != null && saction != null) {
				linkedOntology.addConditionToActivity(tactivity, condition);
				for (String diagram : dynamicOntology.getDiagramsOfConcept(condition)) {
					linkedOntology.addActivityDiagram(diagram);
					linkedOntology.connectActivityDiagramToElement(diagram, condition);
				}
				linkedOntology.addNextActivityToActivity(sactivity, tactivity);
			}
		}

		// Close the linked ontology. The other two ontologies are not closed since they do not need to be saved.
		linkedOntology.close();
	}

}
