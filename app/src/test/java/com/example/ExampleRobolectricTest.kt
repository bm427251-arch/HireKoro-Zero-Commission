package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.BookingStatus
import com.example.data.repository.HireKoroRepository
import com.example.ui.components.getDocumentsForBooking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("HireKoro", appName)
    }

    @Test
    fun `verify rating submission updates provider average and review count`() {
        val repo = HireKoroRepository()
        val provider = repo.providers.value.first()
        val initialCount = provider.reviewCount

        val booking = repo.bookProvider(provider)
        assertEquals("HIRE_DRIVER", booking.bookingType)

        // Complete booking and submit rating
        repo.completeBooking(booking.id)
        repo.submitRating(booking.id, 5, listOf("On Time", "Skilled"), "Outstanding service!")

        val updatedProvider = repo.providers.value.find { it.id == provider.id }
        assertNotNull(updatedProvider)
        val expectedCount = repo.ratings.value.count { it.providerId == provider.id }
        assertEquals(expectedCount, updatedProvider!!.reviewCount)
        assertEquals(5.0, updatedProvider.rating, 0.01)

        val updatedBooking = repo.bookings.value.find { it.id == booking.id }
        assertTrue(updatedBooking!!.rated)
        assertEquals(5, updatedBooking.rateGiven)
    }

    @Test
    fun `verify verified documents are provided per booking type`() {
        val repo = HireKoroRepository()
        val driverBooking = repo.bookings.value.find { it.bookingType == "HIRE_DRIVER" }!!
        val driverDocs = getDocumentsForBooking(driverBooking, "DRIVER", repo)
        assertTrue(driverDocs.any { it.title.contains("Driving License (Front)") })
        assertTrue(driverDocs.any { it.title.contains("Aadhaar") })
        assertTrue(driverDocs.any { it.title.contains("RC Book") })

        val carBooking = repo.bookings.value.find { it.bookingType == "RENT_CAR" }!!
        val carDocs = getDocumentsForBooking(carBooking, "CAR", repo)
        assertTrue(carDocs.any { it.title.contains("RC Book") })
        assertTrue(carDocs.any { it.title.contains("Insurance") })

        val ownerDocs = getDocumentsForBooking(carBooking, "OWNER", repo)
        assertTrue(ownerDocs.any { it.title.contains("Owner Aadhaar") })
    }
}
