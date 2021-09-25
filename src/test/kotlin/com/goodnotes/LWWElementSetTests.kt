package com.goodnotes

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach

class LWWElementSetTests {
    private lateinit var set: LWWElementSet<String>

    @BeforeEach
    fun init() {
        set = LWWElementSet()
    }

    @Test
    fun `element is in set after it has been added`() {
        set.addElement("John", 1)
        assertTrue(set.hasElement("John"))
    }

    @Test
    fun `element is in set if it has been added later than removed`() {
        set.addElement("John", 2)
        set.removeElement("John", 1)
        assertTrue(set.hasElement("John"))
    }

    @Test
    fun `element is not in set if it has not been added`() {
        assertFalse(set.hasElement("John"))
    }

    @Test
    fun `element is not in set if it has been removed later than added`() {
        set.addElement("John", 1)
        set.removeElement("John", 2)
        assertFalse(set.hasElement("John"))
    }

    @Test
    fun `returns all elements`() {
        set.addElement("John", 1)
        set.addElement("Jane", 1)
        set.addElement("Alex", 1)
        set.removeElement("Jane", 2)

        val elements = set.getElements()

        assertEquals(listOf("John", "Alex"), elements)
    }

    @Test
    fun `merging with another sets results in a union set`() {
        set.addElement("John", 25)
        set.addElement("Jane", 10)
        set.addElement("Alex", 10)

        val anotherSet = LWWElementSet<String>()
        anotherSet.addElement("John", 25)
        anotherSet.addElement("Jane", 10)
        anotherSet.addElement("Mark", 15)

        val resultSet = set.mergeWith(anotherSet)
        val resultElements = resultSet.getElements()

        assertEquals(listOf("John", "Jane", "Alex", "Mark"), resultElements)
    }

    @Test
    fun `merging two sets is commutative`() {
        val set1 = LWWElementSet<String>()
        set1.addElement("John", 25)
        set1.addElement("Jane", 10)
        set1.addElement("Alex", 10)
        set1.removeElement("Jane", 12)

        val set2 = LWWElementSet<String>()
        set2.addElement("John", 25)
        set2.addElement("Jane", 10)
        set2.addElement("Alex", 15)
        set2.removeElement("Jane", 11)

        assertTrue(set1.mergeWith(set2) == set2.mergeWith(set1))
    }

    @Test
    fun `merging sets is associative`() {
        val set1 = LWWElementSet<String>()
        set1.addElement("John", 10)
        val set2 = LWWElementSet<String>()
        set2.addElement("John", 10)
        set2.addElement("Alex", 55)
        val set3 = LWWElementSet<String>()
        set3.addElement("John", 11)
        set3.addElement("Jane", 40)

        val set1xSet2 = set1.mergeWith(set2)
        val set2xSet3 = set2.mergeWith(set3)

        assertTrue(set1xSet2.mergeWith(set3) == set1.mergeWith(set2xSet3))
    }

    @Test
    fun `merging sets is idempotent`() {
        set.addElement("John", 10)
        set.addElement("Jane", 11)

        val merged = set.mergeWith(set)

        assertTrue(merged == set)
    }

}