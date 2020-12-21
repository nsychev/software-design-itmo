package ru.nsychev.sd.lru

interface ILRUCache<K, V> {
    val size: Int

    operator fun set(key: K, value: V)
    operator fun get(key: K): V?
    fun pop(key: K): V?
    fun clear()
}
