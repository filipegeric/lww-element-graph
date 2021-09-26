package com.goodnotes

import java.util.Stack

class LWWElementGraph<T>(
    private val vertices: LWWElementSet<T> = LWWElementSet(),
    private val edges: LWWElementSet<Pair<T, T>> = LWWElementSet()
) {
    fun addVertex(value: T, timestamp: Int) = vertices.addElement(value, timestamp)

    fun removeVertex(value: T, timestamp: Int) = vertices.removeElement(value, timestamp)

    fun addEdge(source: T, destination: T, timestamp: Int) {
        if (!vertices.hasElement(source) || !vertices.hasElement(destination)) {
            throw IllegalStateException("Both from and to vertices must be present in graph")
        }
        edges.addElement(source to destination, timestamp)
    }

    fun removeEdge(source: T, destination: T, timestamp: Int) =
        edges.removeElement(source to destination, timestamp)

    fun hasVertex(value: T): Boolean = vertices.hasElement(value)

    fun getAdjacent(value: T): List<T> =
        edges.getElements().filter { it.first == value }.map { it.second }

    fun getPathBetweenVertices(source: T, destination: T): List<T> {
        if (!hasVertex(source) || !hasVertex(destination)) {
            throw Exception("Both source and destination must be in graph")
        }
        if (source == destination) return listOf(source)

        return getPathRecursive(source, destination, mutableSetOf(), Stack<T>()).toList()
    }

    private fun getPathRecursive(
        source: T, destination: T, visited: MutableSet<T>, path: Stack<T>
    ): Stack<T> {
        visited.add(source)
        path.add(source)
        if (source == destination) return path
        for (vertex in getAdjacent(source)) {
            if (visited.contains(vertex)) continue
            val p = getPathRecursive(vertex, destination, visited, path)
            if (p.lastElement() == destination) return p
        }
        path.pop()
        return path
    }

    fun mergeWith(otherGraph: LWWElementGraph<T>): LWWElementGraph<T> = LWWElementGraph(
        vertices.mergeWith(otherGraph.vertices), edges.mergeWith(otherGraph.edges)
    )

    override fun equals(other: Any?): Boolean {
        if (other !is LWWElementGraph<*>) return false
        return vertices == other.vertices && edges == other.edges
    }

    override fun hashCode(): Int {
        var result = vertices.hashCode()
        result = 31 * result + edges.hashCode()
        return result
    }
}
