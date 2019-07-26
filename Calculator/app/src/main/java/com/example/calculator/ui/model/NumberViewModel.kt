package com.example.calculator.ui.model

import com.example.calculator.business.model.Entity
import com.example.calculator.ui.model.base.IItemViewModel


interface INumberViewModel : IItemViewModel<Entity> {
    val entity: Entity
    val title: String
    val color: Int
    val background: Int
}

class NumberViewModel(
    override val entity: Entity, override val color: Int,
    override val background: Int, override val itemClick: (item: Entity) -> Unit) : INumberViewModel {

    override val title: String
        get() = entity.value

    override fun onClick() {
        itemClick.invoke(entity)
    }
}