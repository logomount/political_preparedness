package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import com.udacity.project4.locationreminders.data.dto.ReminderDTO

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

import kotlinx.coroutines.ExperimentalCoroutinesApi;
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Test

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Unit test the DAO
@SmallTest
class RemindersDaoTest {

//    TODO: Add testing implementation to the RemindersDao.kt

    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: RemindersDatabase

    @Before
    fun initialiseDatabase(){
        database = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                RemindersDatabase::class.java
        ).build()
    }

    @After
    fun closeDatabase() = database.close()

    @Test
    fun insertReminderDTOAndGetById() = runBlockingTest {
        val reminderDTO = ReminderDTO(
                "Bangkok",
                "Capital of Thailand",
                "Bangkok",
                13.7563,
                100.5018,
                "101"
        )

        database.reminderDao().saveReminder(reminderDTO)

        val savedReminderDTO = database.reminderDao().getReminderById(reminderDTO.id)

        assertThat<ReminderDTO>(savedReminderDTO, notNullValue())

        assertThat(savedReminderDTO?.id, `is`(reminderDTO.id))
        assertThat(savedReminderDTO?.title, `is`(reminderDTO.title))
        assertThat(savedReminderDTO?.description, `is`(reminderDTO.description))
        assertThat(savedReminderDTO?.location, `is`(reminderDTO.location))
        assertThat(savedReminderDTO?.latitude, `is`(reminderDTO.latitude))
        assertThat(savedReminderDTO?.longitude, `is`(reminderDTO.longitude))
        assertThat(savedReminderDTO?.id, `is`(reminderDTO.id))
    }

    @Test
    fun insertAndCleanDatabase() = runBlockingTest {
        val reminderDTO = ReminderDTO(
                "Bangkok",
                "Capital of Thailand",
                "Bangkok",
                13.7563,
                100.5018,
                "101"
        )

        database.reminderDao().saveReminder(reminderDTO)
        database.reminderDao().deleteAllReminders()
        val savedRemindersDTO = database.reminderDao().getReminders()
        assertThat(savedRemindersDTO.isEmpty(), `is`(true))
    }

}