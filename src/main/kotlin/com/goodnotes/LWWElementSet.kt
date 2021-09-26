package com.goodnotes

class LWWElementSet<T>(
    private val addSet: MutableMap<T, Int> = mutableMapOf(),
    private val removeSet: MutableMap<T, Int> = mutableMapOf()
) {

    fun addElement(value: T, timestamp: Int) =
        addSet.set(value, greaterTimestamp(timestamp, addSet[value]))

    fun removeElement(value: T, timestamp: Int) =
        removeSet.set(value, greaterTimestamp(timestamp, removeSet[value]))

    fun hasElement(value: T): Boolean {
        val addedTimestamp = addSet[value] ?: return false
        val removedTimestamp = removeSet[value] ?: return true

        return addedTimestamp > removedTimestamp
    }

    fun getElements(): List<T> {
        return addSet.filter { hasElement(it.key) }.keys.toList()
    }

    fun mergeWith(otherSet: LWWElementSet<T>): LWWElementSet<T> = LWWElementSet(
        mergeSets(addSet, otherSet.addSet), mergeSets(removeSet, otherSet.removeSet)
    )

    private fun mergeSets(set1: Map<T, Int>, set2: Map<T, Int>): MutableMap<T, Int> {
        val result = mutableMapOf<T, Int>()
        set1.forEach { result[it.key] = greaterTimestamp(it.value, set2[it.key]) }
        set2.forEach { result[it.key] = greaterTimestamp(it.value, set1[it.key]) }
        return result
    }

    private fun greaterTimestamp(timestamp1: Int, timestamp2: Int?): Int {
        if (timestamp2 == null) return timestamp1
        return maxOf(timestamp1, timestamp2)
    }

    override fun equals(other: Any?): Boolean {
        if (other !is LWWElementSet<*>) return false
        return addSet == other.addSet && removeSet == other.removeSet
    }

    override fun hashCode(): Int {
        var result = addSet.hashCode()
        result = 31 * result + removeSet.hashCode()
        return result
    }
}

