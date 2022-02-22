package com.example.FitnessAlarm.CountAlgorithm

import com.example.FitnessAlarm.data.visualizeData.Person

abstract class WorkoutCounter() {

    val MIN_AMPLITUDE = 40
    val REP_THRESHOLD = 0.8
    val MIN_CONFIDENCE = 0.5

    var count = 0

    var first = true
    var goal = 1
    var prev_y = 0
    var prev_dy = 0
    var top = 0
    var bottom = 0
    var completeGoal = 0
    /*
    val tts : TextToSpeech

    init {
        tts = TextToSpeech(context, TextToSpeech.OnInitListener { status ->
            if (status == TextToSpeech.SUCCESS)
            {}
        })
    }
    */

    // default countAlgorithm는 SquatAlgorithm
    abstract fun countAlgorithm(person : Person) : Int

    /*
    fun speakRep()
    {
        // QUEUE_ADD : Queue mode where the new entry is added at the end of the playback queue.
        // QUEUE_FLUSH : Queue mode where all entries in the playback queue (media to be played and text to be synthesized) are dropped and replaced by the new entry.

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(count.toString(), TextToSpeech.QUEUE_ADD, null,null)

        } else {
            tts.speak(count.toString(), TextToSpeech.QUEUE_FLUSH, null);
        }
    }
    */
    fun reset()
    {
        count = 0
    }

}