package com.github.rooneyandshadows.lightbulb.commons.databinding

import androidx.databinding.Observable
import androidx.databinding.Observable.OnPropertyChangedCallback

@Suppress("unused")
interface ObservableWithNotify : Observable {

    override fun addOnPropertyChangedCallback(callback: OnPropertyChangedCallback)

    override fun removeOnPropertyChangedCallback(callback: OnPropertyChangedCallback)

    /**
     * Clears all the listeners for all the properties of this instance.
     */
    fun clearObservableCallbacks()

    /**
     * Notifies listeners that all properties of this instance have changed.
     */
    fun notifyChange()

    /**
     * Notifies listeners that a specific property has changed. The getter for the property
     * that changes should be marked with [Bindable] to generate a field in
     * `BR` to be used as `fieldId`.
     *
     * @param fieldId The generated BR id for the Bindable field.
     */
    fun notifyPropertyChanged(fieldId: Int)
}