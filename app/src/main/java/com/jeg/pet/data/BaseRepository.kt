package com.jeg.pet.data

abstract class BaseRepository  {
    companion object{
        const val EMULATOR_LOCALHOST: String = "10.0.2.2:8080"
        const val PHYSICAL_LOCALHOST: String = "localhost:8080"
        const val BASE_URL: String = "http://$EMULATOR_LOCALHOST/jeg_pet_adoption/"
    }
}