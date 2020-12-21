package ru.nsychev.sd.lru

class LRUCache<K, V>(private val capacity: Int): ILRUCache<K, V> {
    private val itemsByUsage: LinkedList<Pair<K, V>> = LinkedList()
    private val itemsMap: MutableMap<K, LinkedList.Node<Pair<K, V>>> = mutableMapOf()

    override val size: Int
        get() = itemsMap.size

    override fun set(key: K, value: V) {
        if (size >= capacity) {
            itemsByUsage.pop()?.first?.let(itemsMap::remove)
        }

        itemsMap[key]?.let(itemsByUsage::remove)

        val nodeData = key to value
        itemsMap[key] = itemsByUsage.add(nodeData)

        checkInvariants()
        assert(itemsMap[key]?.value?.second == value)
    }

    override fun get(key: K): V? {
        val node = itemsMap[key] ?: return null
        itemsByUsage.remove(node)
        itemsMap[key] = itemsByUsage.add(node.value)

        checkInvariants()
        assert(itemsByUsage.head == itemsMap[key])

        return node.value.second
    }

    override fun pop(key: K): V? {
        val node = itemsMap[key] ?: return null
        itemsMap.remove(key)
        itemsByUsage.remove(node)

        assert(key !in itemsMap)

        return node.value.second
    }

    override fun clear() {
        itemsMap.clear()
        itemsByUsage.clear()
    }

    private fun checkInvariants() {
        assert(itemsMap.size == size)
        assert(itemsByUsage.size == size)
        assert(size <= capacity)
    }

    private class LinkedList<T> {
        data class Node<T>(
                var prev: Node<T>?,
                var next: Node<T>?,
                val value: T
        ) {
            override fun toString(): String {
                return "Node [${value.toString()}]"
            }
        }

        var head: Node<T>? = null
            private set
        var tail: Node<T>? = null
            private set

        var size: Int = 0
            private set

        fun clear() {
            head = null
            tail = null
        }

        fun add(value: T): Node<T> {
            synchronized(this) {
                val node = Node(null, head, value)
                head?.prev = node
                head = node
                if (tail == null) tail = node
                size++
                return node
            }
        }

        fun pop(): T? {
            synchronized(this) {
                if (size == 0) return null

                val result = tail?.value

                if (head == tail)
                    head = null

                tail?.prev?.next = null
                tail = tail?.prev
                size--

                return result
            }
        }

        fun remove(node: Node<T>) {
            synchronized(this) {
                if (node == head) {
                    head = head?.next
                }
                if (node == tail) {
                    tail = tail?.prev
                }
                node.next?.prev = node.prev
                node.prev?.next = node.next
                size--
            }
        }
    }
}
