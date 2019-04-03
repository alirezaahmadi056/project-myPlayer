package application.ahmadi.myplayer

import java.io.File

class SongsManager {

    companion object {

        private val root = android.os.Environment.getExternalStorageDirectory()!!
        private val songList = arrayListOf<HashMap<String, String>>()
        //private val folderList = arrayListOf<HashMap<String, String>>()

        fun getAllMusic(): ArrayList<HashMap<String, String>> {

            val dir = File(root.absolutePath + "/Music")
            val listFile = dir.listFiles()

            for (item in 0 until listFile.size) {
                val file = listFile[item]
                val name = file.name!!
                var char = 'a'
                if (name.length > 5) {
                    char = name[name.length - 4]
                }
                val checkName = name.endsWith(".mp3") || name.endsWith(".wav") || name.endsWith(".wma") || name.endsWith(".MP3") || name.endsWith(".WAV") || name.endsWith(".WMA")

                if (checkName)
                    setSongs(file)
                else if (char != '.')
                    checkFolder(file)

            }

            return songList

        }

        private fun setSongs(file: File) {

            val song = HashMap<String, String>()

            song["title"] = file.name!!.substring(0, file.name!!.length - 4)
            song["path"] = file.absolutePath!!
            songList.add(song)

        }

        /*private fun setFolder(path: String) {

            val file = File(path)
            val folder = HashMap<String, String>()

            folder["title"] = file.name
            folder["path"] = path
            folderList.add(folder)

        }*/

        private fun checkFolder(file1: File) {

            val dir = File(file1.absolutePath)
            val listFile = dir.listFiles()

            if (listFile.isNotEmpty()) {

                for (item in 0 until listFile.size) {
                    val file = listFile[item]
                    val name = file.name!!
                    var char = 'a'
                    if (name.length > 5) {
                        char = name[name.length - 4]
                    }
                    val checkName = name.endsWith(".mp3") || name.endsWith(".wav") || name.endsWith(".wma") || name.endsWith(".MP3") || name.endsWith(".WAV") || name.endsWith(".WMA")

                    if (checkName)
                        setSongs(file)
                    else if (char != '.')
                        checkFolder(file)

                }

            }

        }

    }

}