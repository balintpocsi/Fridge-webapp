package com.fridge.domain

data class Item(val id: String, val name: String)

interface Repository<in Id, Entity> {
    fun getAll(): List<Entity>
    fun add(entity: Entity): Item
    fun remove(id: Id)
}

class ItemRepository : Repository<String, Item> {

    private val dataSource = mutableMapOf<String, Item>()

    override fun getAll(): List<Item> {
        return dataSource.values.toList()
    }

    override fun add(entity: Item): Item {
        dataSource[entity.id] = entity
        return entity
    }

    override fun remove(id: String) {
        dataSource.remove(id)
    }

}
