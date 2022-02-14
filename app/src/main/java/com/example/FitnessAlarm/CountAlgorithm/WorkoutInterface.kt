package com.example.FitnessAlarm.CountAlgorithm

import com.example.FitnessAlarm.data.Person
import com.example.FitnessAlarm.data.BodyPart

interface WorkoutInterface {

    fun countAlgorithm(person : Person) : Int

    fun speakRep()

    fun reset()


}