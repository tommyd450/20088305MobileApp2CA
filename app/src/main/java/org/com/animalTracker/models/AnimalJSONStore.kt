package org.com.animalTracker.models


import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import org.com.animalTracker.helpers.*
import org.com.animalTracker.main.App
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*
import kotlin.math.log

const val JSON_FILE = "animals2.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listType: Type = object : TypeToken<ArrayList<AnimalModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

object AnimalJSONStore : AnimalStoreInterface {
    var animals = mutableListOf<AnimalModel>()
    lateinit var context: Context


    fun init() {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }



    override fun findAll(): MutableList<AnimalModel> {
        logAll()

        return animals
    }

    override fun findById(id: Long): AnimalModel? {
        val foundAnimal: AnimalModel? = AnimalJSONStore.animals.find { it.id == id }
        return foundAnimal
    }

    override fun create(animal: AnimalModel) {
        Timber.i("Created Successfully")
        animal.id = generateRandomId()
        animals.add(animal)
        logAll()
        serialize()
    }


    override fun update(animal: AnimalModel) {
        var foundAnimal: AnimalModel? = animals.find { p -> p.id == animal.id }
        if (foundAnimal != null) {
            Timber.i("Updated")
            foundAnimal.animalName = animal.animalName
            foundAnimal.animalSpecies = animal.animalSpecies
            foundAnimal.region = animal.region
            foundAnimal.diet = animal.diet


            logAll()
        }
        serialize()
    }

    override fun delete(animal: AnimalModel) {
        var foundAnimal: AnimalModel? = animals.find { p -> p.id == animal.id }
        if (foundAnimal != null) {
            animals.remove(animal)
            logAll()
        }
        serialize()
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(animals, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        animals = gsonBuilder.fromJson(jsonString, listType)
    }

    private fun logAll() {
        animals.forEach { Timber.i("$it") }
    }


}


class UriParser : JsonDeserializer<Uri>,JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }

    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }
}