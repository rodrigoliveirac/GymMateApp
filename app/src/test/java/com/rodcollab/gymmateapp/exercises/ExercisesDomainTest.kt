package com.rodcollab.gymmateapp.exercises

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.google.firebase.FirebaseApp
import com.rodcollab.gymmateapp.GymMateApp
import com.rodcollab.gymmateapp.core.data.AppDatabase
import com.rodcollab.gymmateapp.core.data.dao.BodyPartDao
import com.rodcollab.gymmateapp.core.data.dao.ExerciseDao
import com.rodcollab.gymmateapp.core.data.model.BodyPart
import com.rodcollab.gymmateapp.core.data.model.ExerciseLocal
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual.equalTo
import org.json.JSONObject
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.IOException


@RunWith(RobolectricTestRunner::class)
class ExercisesDomainTest {

    private lateinit var database: AppDatabase
    private lateinit var bodyPartDao: BodyPartDao
    private lateinit var exercisesDao: ExerciseDao
    private lateinit var bodyPartsFromJSON: List<BodyPart>
    private lateinit var exercisesFromJSON: List<ExerciseLocal>
    private lateinit var context: Context

    @Before fun setUp() {
        val app = getApplicationContext<GymMateApp>()
        context = app.applicationContext
        database = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeJsonFilesIntoLocalStorage() = runTest {
        val jsonString: String =
            context.assets.open("BodyPartList.json").bufferedReader().use {
                it.readText()
            }
        val jsonObject = JSONObject(jsonString)
        val jsonArray = jsonObject.getJSONArray("bodyPartList")
        var count = 0
        val list = mutableListOf<BodyPart>()
        while (jsonArray.length() != count) {
            val bodyPart = jsonArray[count] as JSONObject
            val name = bodyPart.getString("name")
            val url = bodyPart.getString("url")
            list.add(BodyPart(id = (count + 1).toLong(), name = name, imgUrl = url))
            count++
        }
        bodyPartsFromJSON = list.toList()

        val jsonExercisesString: String =
            context.assets.open("Exercises.json").bufferedReader().use {
                it.readText()
            }
        val jsonExercisesObject = JSONObject(jsonExercisesString)
        val jsonExercisesArray = jsonExercisesObject.getJSONArray("exercises")
        var countExercises = 0
        val exercises = mutableListOf<ExerciseLocal>()
        while (jsonArray.length() != countExercises) {
            val exercise = jsonExercisesArray[countExercises] as JSONObject
            var countInstructions = 0
            var notes = ""
            while (exercise.getJSONArray("instructions")
                    .length() != countInstructions
            ) {
                notes += exercise.getJSONArray("instructions")[countInstructions]
                countInstructions++
            }
            exercises.add(
                ExerciseLocal(
                    id = (countExercises + 1).toLong(),
                    name = exercise.getString("name"),
                    image = exercise.getString("gifUrl"),
                    bodyPart = exercise.getString("bodyPart"),
                    notes = notes
                )
            )
            countExercises++
        }
        exercisesFromJSON = exercises
        bodyPartDao = database.bodyPartDao()
        exercisesDao = database.exerciseDao()
        database.bodyPartDao().upsert(bodyPartsFromJSON)
        database.exerciseDao().upsert(exercisesFromJSON)
        val bodyPartsLocal: List<BodyPart> = bodyPartDao.getAll()
        val exercisesLocal: List<ExerciseLocal> = exercisesDao.getAll()

        assertThat(bodyPartsFromJSON.toList(), equalTo(bodyPartsLocal))
        assertThat(exercisesFromJSON.toList(), equalTo(exercisesLocal))
    }

}
