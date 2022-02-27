package com.example.FitnessAlarm.CountAlgorithm

import android.util.Log
import com.example.FitnessAlarm.data.BodyPart
import com.example.FitnessAlarm.data.Person

// pushup count algorithm을 위한 클래스
// not yet developed
class PushupCounter : WorkoutCounter() {

    override var MIN_AMPLITUDE = 20

    var prev_y_right = 0
    var prev_dy_right = 0
    var top_right = 0
    var bottom_right = 0
    var prev_y_left = 0
    var prev_dy_left = 0
    var top_left = 0
    var bottom_left = 0

    override fun countAlgorithm(person: Person): Int {

            Log.d("PushUpCounter","PushUp Algorithm")


            if (person.keyPoints[BodyPart.LEFT_SHOULDER.ordinal].score >= MIN_CONFIDENCE && person.keyPoints[BodyPart.RIGHT_SHOULDER.ordinal].score >= MIN_CONFIDENCE) {

                var yRight = 1000 - person.keyPoints[BodyPart.RIGHT_SHOULDER.ordinal].coordinate.y
                var dyRight = yRight - prev_y_right
                var yLeft = 1000 - person.keyPoints[BodyPart.LEFT_SHOULDER.ordinal].coordinate.y
                var dyLeft = yLeft - prev_y_left

                Log.d("Right Shoulder",person.keyPoints[BodyPart.RIGHT_SHOULDER.ordinal].coordinate.y.toString())
                Log.d("Left Shoulder",person.keyPoints[BodyPart.LEFT_SHOULDER.ordinal].coordinate.y.toString())

                if (!first) {
                    if (bottom_right != 0 && top_right != 0) {
                        if (goal == 1 && dyRight > 0 && (yRight - bottom_right) > (top_right - bottom_right) * REP_THRESHOLD) {

                            if (dyLeft > 0 && (yLeft - bottom_left) > (top_left - bottom_left) * REP_THRESHOLD) {
                                if (top_right - bottom_right > MIN_AMPLITUDE && top_left - bottom_left > MIN_AMPLITUDE) {
                                    count++
                                    goal = -1
                                }
                            }
                        }
                        else if (goal == -1 && dyRight < 0 && (top_right - yRight) > (top_right - bottom_right) * REP_THRESHOLD) {
                            if (dyLeft < 0 && (top_left - yLeft) > (top_left - bottom_left) * REP_THRESHOLD) {
                                goal = 1
                            }
                        }
                    }

                    // TODO: Use MIN_AMPLITUDE
                    if (dyRight < 0 && prev_dy_right >= 0 && prev_y_right - bottom_right > MIN_AMPLITUDE) {
                        if (dyLeft < 0 && prev_dy_left >= 0 && prev_y_left - bottom_left > MIN_AMPLITUDE)
                        top_left = prev_y_left
                        top_right = prev_y_right
                    }
                    else if (dyRight > 0 && prev_dy_right <= 0 && top_right - prev_y_right > MIN_AMPLITUDE) {
                        if (dyLeft > 0 && prev_dy_left <= 0 && top_left - prev_y_left > MIN_AMPLITUDE){
                            bottom_right = prev_y_right
                            bottom_left = prev_y_left
                        }
                    }
                }

                first = false
                prev_y_right = yRight.toInt()
                prev_dy_right = dyRight.toInt()
                prev_y_left = yLeft.toInt()
                prev_dy_left = dyLeft.toInt()

            }

            return count

    }
}