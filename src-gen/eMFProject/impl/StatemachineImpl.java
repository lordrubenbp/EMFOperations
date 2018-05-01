/**
 */
package eMFProject.impl;

import eMFProject.EMFProjectPackage;
import eMFProject.Event;
import eMFProject.State;
import eMFProject.Statemachine;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Statemachine</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link eMFProject.impl.StatemachineImpl#getEvents <em>Events</em>}</li>
 *   <li>{@link eMFProject.impl.StatemachineImpl#getResetEvents <em>Reset Events</em>}</li>
 *   <li>{@link eMFProject.impl.StatemachineImpl#getStates <em>States</em>}</li>
 * </ul>
 *
 * @generated
 */
public class StatemachineImpl extends MinimalEObjectImpl.Container implements Statemachine {
	/**
	 * The cached value of the '{@link #getEvents() <em>Events</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEvents()
	 * @generated
	 * @ordered
	 */
	protected EList<Event> events;
	/**
	 * The cached value of the '{@link #getResetEvents() <em>Reset Events</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResetEvents()
	 * @generated
	 * @ordered
	 */
	protected EList<Event> resetEvents;
	/**
	 * The cached value of the '{@link #getStates() <em>States</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStates()
	 * @generated
	 * @ordered
	 */
	protected EList<State> states;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected StatemachineImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return EMFProjectPackage.Literals.STATEMACHINE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Event> getEvents() {
		if (events == null) {
			events = new EObjectContainmentEList<Event>(Event.class, this, EMFProjectPackage.STATEMACHINE__EVENTS);
		}
		return events;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Event> getResetEvents() {
		if (resetEvents == null) {
			resetEvents = new EObjectResolvingEList<Event>(Event.class, this,
					EMFProjectPackage.STATEMACHINE__RESET_EVENTS);
		}
		return resetEvents;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<State> getStates() {
		if (states == null) {
			states = new EObjectContainmentEList<State>(State.class, this, EMFProjectPackage.STATEMACHINE__STATES);
		}
		return states;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case EMFProjectPackage.STATEMACHINE__EVENTS:
			return ((InternalEList<?>) getEvents()).basicRemove(otherEnd, msgs);
		case EMFProjectPackage.STATEMACHINE__STATES:
			return ((InternalEList<?>) getStates()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case EMFProjectPackage.STATEMACHINE__EVENTS:
			return getEvents();
		case EMFProjectPackage.STATEMACHINE__RESET_EVENTS:
			return getResetEvents();
		case EMFProjectPackage.STATEMACHINE__STATES:
			return getStates();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case EMFProjectPackage.STATEMACHINE__EVENTS:
			getEvents().clear();
			getEvents().addAll((Collection<? extends Event>) newValue);
			return;
		case EMFProjectPackage.STATEMACHINE__RESET_EVENTS:
			getResetEvents().clear();
			getResetEvents().addAll((Collection<? extends Event>) newValue);
			return;
		case EMFProjectPackage.STATEMACHINE__STATES:
			getStates().clear();
			getStates().addAll((Collection<? extends State>) newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
		case EMFProjectPackage.STATEMACHINE__EVENTS:
			getEvents().clear();
			return;
		case EMFProjectPackage.STATEMACHINE__RESET_EVENTS:
			getResetEvents().clear();
			return;
		case EMFProjectPackage.STATEMACHINE__STATES:
			getStates().clear();
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
		case EMFProjectPackage.STATEMACHINE__EVENTS:
			return events != null && !events.isEmpty();
		case EMFProjectPackage.STATEMACHINE__RESET_EVENTS:
			return resetEvents != null && !resetEvents.isEmpty();
		case EMFProjectPackage.STATEMACHINE__STATES:
			return states != null && !states.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //StatemachineImpl
