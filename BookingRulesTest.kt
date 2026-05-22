package com.nammamela.app

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BookingRulesTest {
    @Test
    fun calculateTotalAddsSelectedSeatPrices() {
        val seats = listOf(
            Seat("A1", SeatSection.FRONT, 100, true),
            Seat("C4", SeatSection.MIDDLE, 50, true),
            Seat("E8", SeatSection.BACK, 20, true)
        )

        assertEquals(170, BookingRules.calculateTotal(seats))
    }

    @Test
    fun selectionAllowsMaximumTenSeats() {
        val seats = List(10) { Seat("A$it", SeatSection.FRONT, 100, true) }

        assertTrue(BookingRules.isSelectionAllowed(seats))
    }

    @Test
    fun selectionRejectsMoreThanTenSeats() {
        val seats = List(11) { Seat("A$it", SeatSection.FRONT, 100, true) }

        assertFalse(BookingRules.isSelectionAllowed(seats))
    }
}
