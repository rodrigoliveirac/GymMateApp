package com.rodcollab.gymmateapp.worker

import android.content.Context
import androidx.work.Configuration
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.rodcollab.gymmateapp.core.data.AppDatabase
import com.rodcollab.gymmateapp.core.data.model.BodyPart
import com.rodcollab.gymmateapp.core.data.model.ExerciseLocal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONObject

class CoroutineDownloadWorker(
    context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    private val coroutine = CoroutineScope(Dispatchers.IO + Job())
    override fun doWork(): Result {
        coroutine.launch {
            try {
                val database = AppDatabase.getInstance(applicationContext)
                if(database.exerciseDao().getAll().isEmpty() && database.bodyPartDao().getAll().isEmpty()) {
                    val bodyParts = async {
                        val jsonString: String =
                            applicationContext.assets.open("BodyPartList.json").bufferedReader().use {
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
                            list.add(BodyPart(name = name, imgUrl = url))
                            count++
                        }
                        list
                    }.await()
                    val exercises = async {
                        val jsonString: String =
                            applicationContext.assets.open("Exercises.json").bufferedReader().use {
                                it.readText()
                            }
                        val jsonObject = JSONObject(jsonString)
                        val jsonArray = jsonObject.getJSONArray("exercises")
                        var count = 0
                        val list = mutableListOf<ExerciseLocal>()
                        while (jsonArray.length() != count) {
                            val exercise = jsonArray[count] as JSONObject
                            var countInstructions = 0
                            var notes = ""
                            while (exercise.getJSONArray("instructions")
                                    .length() != countInstructions
                            ) {
                                notes += exercise.getJSONArray("instructions")[countInstructions]
                                countInstructions++
                            }
                            list.add(
                                ExerciseLocal(
                                    name = exercise.getString("name"),
                                    image = exercise.getString("gifUrl"),
                                    bodyPart = exercise.getString("bodyPart"),
                                    notes = notes
                                )
                            )
                            count++
                        }
                        list
                    }.await()
                    database.bodyPartDao().upsert(bodyParts)
                    database.exerciseDao().upsert(exercises)
                }
                Result.success()
            } catch (ex: Exception) {
                Result.failure()
            }
        }
        return Result.success()
    }

    companion object {

        fun schedule(appContext: Context) {

            val myConfig = Configuration.Builder()
                .setMinimumLoggingLevel(android.util.Log.INFO)
                .build()

            if(!WorkManager.isInitialized()) {
                WorkManager.initialize(appContext, myConfig)
            }

            val request = OneTimeWorkRequestBuilder<CoroutineDownloadWorker>().build()
            WorkManager.getInstance(appContext).enqueue(request)
        }
    }
}