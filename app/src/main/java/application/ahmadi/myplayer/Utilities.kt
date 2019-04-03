package application.ahmadi.myplayer

class Utilities {

    companion object {

        fun milli_to_time(milliSecound: Long): String {

            var hours = (milliSecound / (60 * 60 * 1000)).toString()
            var minutes = (milliSecound % (60 * 60 * 1000) / (60 * 1000)).toString()
            var seconds = (((milliSecound % (60 * 60 * 100)) % (60 * 1000)) / 1000).toString()
            if (hours.toInt() < 10) {
                hours = "0$hours"
            }
            if (minutes.toInt() < 10) {
                minutes = "0$minutes"
            }
            if (seconds.toInt() < 10) {
                seconds = "0$seconds"
            }
            return if (hours.toInt() > 0)
                "$hours:$minutes:$seconds"
            else
                "$minutes:$seconds"
        }

        fun setProgressPercentage(currentDuration: Long, totalDuration: Long): Int {

            val currentSeconds = (currentDuration / 1000).toDouble()
            val totalSecond = (totalDuration / 1000).toDouble()

            val percentage = (currentSeconds / totalSecond) * 1000

            return percentage.toInt()

        }

        fun progressToTime(progress: Int, totalDuration: Int): Int {

            val totalSeconds = (totalDuration / 1000)
            val currentDuration = ((progress.toDouble() / 1000) * totalSeconds).toInt()

            return currentDuration * 1000
        }

    }

}