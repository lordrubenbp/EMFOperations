/**
 */
package eMFProject;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Statemachine</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link eMFProject.Statemachine#getState <em>State</em>}</li>
 * </ul>
 *
 * @see eMFProject.EMFProjectPackage#getStatemachine()
 * @model
 * @generated
 */
public interface Statemachine extends EObject {
	/**
	 * Returns the value of the '<em><b>State</b></em>' containment reference list.
	 * The list contents are of type {@link eMFProject.State}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>State</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>State</em>' containment reference list.
	 * @see eMFProject.EMFProjectPackage#getStatemachine_State()
	 * @model containment="true"
	 * @generated
	 */
	EList<State> getState();

} // Statemachine
