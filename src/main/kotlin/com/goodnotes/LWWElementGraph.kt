package com.goodnotes

class LWWElementGraph<T>(
    private val vertices: LWWElementSet<T> = LWWElementSet(),
    private val edges: LWWElementSet<Pair<T, T>> = LWWElementSet()
) {

    fun addVertex(value: T, timestamp: Int) {
        vertices.addElement(value, timestamp)
    }

    fun removeVertex(value: T, timestamp: Int) {
        vertices.removeElement(value, timestamp)
    }

    fun addEdge(source: T, destination: T, timestamp: Int) {
        if (!vertices.hasElement(source) || !vertices.hasElement(destination)) {
            throw IllegalStateException("Both from and to vertices must be present in graph")
        }
        edges.addElement(source to destination, timestamp)
    }

    fun removeEdge(source: T, destination: T, timestamp: Int) {
        edges.removeElement(source to destination, timestamp)
    }

    fun hasVertex(value: T): Boolean {
        return vertices.hasElement(value)
    }

    fun getAdjacent(value: T): List<T> =
        edges.getElements().filter { it.first == value }.map { it.second }

    fun getPathBetweenVertices(source: T, destination: T): List<T> {
        TODO()
    }

    fun mergeWith(otherGraph: LWWElementGraph<T>): LWWElementGraph<T> {
        return LWWElementGraph(
            vertices.mergeWith(otherGraph.vertices), edges.mergeWith(otherGraph.edges)
        )
    }

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
