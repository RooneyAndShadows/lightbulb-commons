package com.github.rooneyandshadows.lightbulb.commons.databinding

import kotlin.reflect.KProperty

class ObservableDelegate<V : Any>(initialValue: V, private val id: Int, private val observable: ObservableWithNotify) {
    private var value: V = initialValue

    operator fun getValue(thisRef: Any, property: KProperty<*>): V {
        return value
    }

    operator fun setValue(thisRef: Any, property: KProperty<*>, value: V) {
        if (value === this.value) {
            return
        }
        this.value = value
        observable.notifyPropertyChanged(id)
    }
}