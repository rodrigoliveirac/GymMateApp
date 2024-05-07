package com.rodcollab.gymmateapp.firebase

import com.rodcollab.gymmateapp.core.Collections
import com.rodcollab.gymmateapp.core.Collections.EXERCISES_COLLECTION
import com.rodcollab.gymmateapp.core.data.model.ExerciseExternal

internal class GymMateModelSerializer<T> {
    fun invoke(model: T): Map<String, Any?> {
        val data: Map<String, Any?> = when (model) {
            is ExerciseExternal -> {
                hashMapOf<String, Any?>(
                    "uuid" to model.uuid,
                    "name" to model.name,
                    "bodyPart" to model.bodyPart,
                    "image" to model.image,
                    "notes" to model.notes
                )
            }

            else -> {
                throw Exception("This model is not supported")
            }
        }
        return data
    }

    fun read(collection: String): T {
        return when (collection) {
            EXERCISES_COLLECTION -> {
                ExerciseExternal() as T
            }
            else -> {
                throw Exception()
            }
        }
    }
}