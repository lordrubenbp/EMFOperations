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
 *   <li>{@link eMFProject.Statemachine#getEvents <em>Events</em>}</li>
 *   <li>{@link eMFProject.Statemachine#getResetEvents <em>Reset Events</em>}</li>
 *   <li>{@link eMFProject.Statemachine#getStates <em>States</em>}</li>
 * </ul>
 *
 * @see eMFProject.EMFProjectPackage#getStatemachine()
 * @model
 * @generated
 */
public interface Statemachine extends EObject {
	/**
	 * Returns the value of the '<em><b>Events</b></em>' containment reference list.
	 * The list contents are of type {@link eMFProject.Event}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Events</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Events</em>' containment reference list.
	 * @see eMFProject.EMFProjectPackage#getStatemachine_Events()
	 * @model containment="true"
	 * @generated
	 */
	EList<Event> getEvents();

	/**
	 * Returns the value of the '<em><b>Reset Events</b></em>' reference list.
	 * The list contents are of type {@link eMFProject.Event}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Reset Events</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Reset Events</em>' reference list.
	 * @see eMFProject.EMFProjectPackage#getStatemachine_ResetEvents()
	 * @model
	 * @generated
	 */
	EList<Event> getResetEvents();

	/**
	 * Returns the value of the '<em><b>States</b></em>' containment reference list.
	 * The list contents are of type {@link eMFProject.State}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>States</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>States</em>' containment reference list.
	 * @see eMFProject.EMFProjectPackage#getStatemachine_States()
	 * @model containment="true"
	 * @generated
	 */
	EList<State> getStates();

} // Statemachine
