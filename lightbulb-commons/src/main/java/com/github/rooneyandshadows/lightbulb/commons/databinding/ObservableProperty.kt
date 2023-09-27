package com.github.rooneyandshadows.lightbulb.commons.databinding

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class ObservableProperty<V>(initialValue: V, private val id: Int) : ReadWriteProperty<ObservableWithNotify?, V> {
    private var value = initialValue

    override fun getValue(thisRef: ObservableWithNotify?, property: KProperty<*>): V {
        return value
    }

    override fun setValue(thisRef: ObservableWithNotify?, property: KProperty<*>, value: V) {
        val oldValue = this.value
        if (oldValue === value) {
            return
        }
        this.value = value
        thisRef?.notifyPropertyChanged(id)
    }
}