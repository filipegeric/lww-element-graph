package com.goodnotes

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class LWWElementGraphTests {

    @Test
    fun `merging graphs is commutative`() {
        val graph1 = LWWElementGraph<String>().apply {
            addVertex("John", 10)
            addVertex("Jane", 11)
            removeVertex("Jane", 12)
        }
        val graph2 = LWWElementGraph<String>().apply {
            addVertex("Jane", 13)
            addVertex("John", 9)
            addEdge("John", "Jane", 10)
        }

        assertTrue(graph1.mergeWith(graph2) == graph2.mergeWith(graph1))
    }

    @Test
    fun `merging graphs is associative`() {
        val graph1 = LWWElementGraph<String>().apply {
            addVertex("John", 10)
            addVertex("Jane", 11)
            addEdge("John", "Jane", 12)
            removeEdge("John", "Jane", 13)
        }
        val graph2 = LWWElementGraph<String>().apply {
            addVertex("John", 9)
            addVertex("Jane", 13)
            addEdge("John", "Jane", 10)
        }
        val graph3 = LWWElementGraph<String>().apply {
            addVertex("Alex", 50)
            addVertex("Adam", 44)
            addEdge("Adam", "Alex", 45)
        }
        val graph1xGraph2 = graph1.mergeWith(graph2)
        val graph2xGraph3 = graph2.mergeWith(graph3)

        assertTrue(graph1xGraph2.mergeWith(graph3) == graph1.mergeWith(graph2xGraph3))
    }

    @Test
    fun `merging graphs is idempotent`() {
        val graph = LWWElementGraph<String>().apply {
            addVertex("John", 10)
            addVertex("Hannah", 44)
            addEdge("Hannah", "John", 55)
        }

        assertTrue(graph.mergeWith(graph) == graph)
    }

    @Test
    fun `hasVertex returns true if vertex is in graph`() {
        val graph = LWWElementGraph<String>().apply {
            addVertex("John", 10)
            removeVertex("John", 9)
        }

        assertTrue(graph.hasVertex("John"))
    }

    @Test
    fun `getAdjacent returns adjacent vertices`() {
        val graph = LWWElementGraph<String>().apply {
            addVertex("John", 10)
            addVertex("Hannah", 10)
            addVertex("Alex", 10)
            removeEdge("Alex", "John", 19)
            addEdge("Alex", "John", 15)
            addEdge("Alex", "Hannah", 15)
        }

        val adjacent = graph.getAdjacent("Alex")

        assertEquals(listOf("Hannah"), adjacent)
    }

    @Test
    fun `getPathBetweenVertices returns just one vertex if source is equal to destination`() {
        val graph = LWWElementGraph<String>().apply {
            addVertex("John", 10)
            addVertex("Anna", 11)
            addEdge("John", "Anna", 12)
        }

        val path = graph.getPathBetweenVertices("John", "John")
        assertEquals(listOf("John"), path)
    }

    @Test
    fun `getPathBetweenVertices throws exception if source or destination is not in graph`() {
        val graph = LWWElementGraph<String>().apply {
            addVertex("John", 10)
            addVertex("Anna", 11)
            addEdge("John", "Anna", 12)
        }
        assertThrows(Exception::class.java) {
            graph.getPathBetweenVertices("Jane", "John")
        }
    }

    @Test
    fun `getPathBetweenVertices returns empty list if there is no path between source and destination`() {
        val graph = LWWElementGraph<String>().apply {
            addVertex("John", 10)
            addVertex("Anna", 11)
            addVertex("Jane", 11)
            addEdge("John", "Anna", 12)
            addEdge("Jane", "Anna", 12)
        }

        val path = graph.getPathBetweenVertices("John", "Jane")

        assertEquals(emptyList<String>(), path)
    }

    @Test
    fun `getPathBetweenVertices returns path`() {
        val graph = LWWElementGraph<String>().apply {
            addVertex("1", 10)
            addVertex("2", 10)
            addVertex("3", 10)
            addVertex("4", 10)
            addVertex("5", 10)
            addVertex("6", 10)
            addEdge("1", "2", 12)
            addEdge("1", "3", 12)
            addEdge("3", "2", 12)
            addEdge("2", "4", 12)
            addEdge("2", "5", 12)
            addEdge("4", "5", 12)
            addEdge("5", "3", 12)
            addEdge("5", "6", 12)
        }

        val path = graph.getPathBetweenVertices("1", "6")

        assertEquals(listOf("1", "2", "4", "5", "6"), path)
    }
}