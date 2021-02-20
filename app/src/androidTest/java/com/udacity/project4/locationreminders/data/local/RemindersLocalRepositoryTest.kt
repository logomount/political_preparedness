package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Medium Test to test the repository
@MediumTest
class RemindersLocalRepositoryTest {

//    TODO: Add testing implementation to the RemindersLocalRepository.kt

    private lateinit var database: RemindersDatabase
    private lateinit var remindersLocalRepository: RemindersLocalRepository

    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupRepository() {
        database = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                RemindersDatabase::class.java
        )
                .allowMainThreadQueries()
                .build()

        remindersLocalRepository = RemindersLocalRepository(
                database.reminderDao(),
                Dispatchers.Main
        )
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun saveReminder_retrieveReminder() = runBlocking {
        val reminderDTO = ReminderDTO(
                "Bangkok",
                "Capital of Thailand",
                "Thailand",
                13.7563,
                100.5018
        )
        remindersLocalRepository.saveReminder(reminderDTO)
        val retrievedReminder = remindersLocalRepository.getReminder(reminderDTO.id)
        assertThat(retrievedReminder is Result.Success, `is`(true))
        retrievedReminder as Result.Success
        assertThat(retrievedReminder.data.title, `is`(reminderDTO.title))
        assertThat(retrievedReminder.data.description, `is`(reminderDTO.description))
        assertThat(retrievedReminder.data.location, `is`(reminderDTO.location))
        assertThat(retrievedReminder.data.latitude, `is`(reminderDTO.latitude))
        assertThat(retrievedReminder.data.longitude, `is`(reminderDTO.longitude))
        assertThat(retrievedReminder.data.id, `is`(reminderDTO.id))
    }

    @Test
    fun deleteAllReminders() = runBlocking {
        remindersLocalRepository.deleteAllReminders()
        val retrievedReminders = remindersLocalRepository.getReminders()
        retrievedReminders as Result.Success
        assertThat(retrievedReminders.data.firstOrNull(), `is`(nullValue()))
    }

}