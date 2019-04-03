package application.ahmadi.myplayer

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.*
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity() {

    private var media: MediaPlayer? = null
    private var pref: SharedPreferences? = null
    private var songList: ArrayList<HashMap<String, String>>? = null
    private var position: Int? = null
    private val mHandler = Handler()
    private var positionsRand: Int? = null
    var isChecked = true
    var seek = 0

    var isShuffle = false
    var isRepeat = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermissionStorage()

        pref = getSharedPreferences("pref", Context.MODE_PRIVATE)
        media = MediaPlayer()

        val isCheckedMusicName = pref?.getString("musicName", "null")!!
        val isCheckedMusicPath = pref?.getString("musicPath", "null")!!
        if (isCheckedMusicName != "null" && isCheckedMusicPath != "null") {
            txtMusicName.text = isCheckedMusicName
        }

        val pos = pref?.getInt("musicPosition", -1)!!
        position = pos

        seekBarSound.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {

                val total = media?.duration!!
                val current = media?.currentPosition!!
                txtTimeCurrent.text = Utilities.milli_to_time(current.toLong())
                txtTimeRemain.text = Utilities.milli_to_time(total.toLong())

            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

                isChecked = false

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

                isChecked = true

                val total = media?.duration
                val current = Utilities.progressToTime(seekBar.progress, total!!)

                media?.seekTo(current)
                seek = current

                updateProgress()

            }
        })

        media?.setOnCompletionListener {
            when {
                isRepeat -> {
                    seek = 0
                    playSound(position!!)
                }
                isShuffle -> {
                    seek = 0
                    val rand = Random()
                    position = rand.nextInt(songList?.size!! - 1)
                    playSound(position!!)
                }
                else -> {
                    seek = 0
                    position = position!! + 1
                    playSound(position!!)
                }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == 100) {

            position = data?.extras?.getInt("position")
            seek = data?.extras?.getInt("seek")!!
            playSound(position!!)

        }

    }

    private fun playSound(positions: Int) {

        try {

            songList = SongsManager.getAllMusic()

            if (songList?.isNotEmpty()!!) {
                if (positions < songList!!.size && positions >= 0) {
                    val hashMap = songList!![positions]
                    media?.reset()

                    media?.setDataSource(hashMap["path"])
                    media?.prepare()
                    if (seek > 0) {
                        media?.seekTo(seek)
                        seek = 0
                    }
                    media?.start()

                    txtMusicName.text = hashMap["title"]

                    val editor = pref?.edit()
                    editor?.putString("musicName", hashMap["title"])
                    editor?.putString("musicPath", hashMap["path"])
                    editor?.putInt("musicPosition", positions)
                    editor?.apply()

                    imgPlay.setImageResource(R.drawable.img_btn_pause)

                    seekBarSound.max = 1000
                    seekBarSound.progress = 0

                    updateProgress()

                } else if (positions < 0) {
                    position = songList!!.size - 1
                    playSound(position!!)
                } else if (positions >= songList!!.size) {
                    position = 0
                    playSound(position!!)
                }
            } else {
                Toast.makeText(this, "آهنگی پیدا نشد", Toast.LENGTH_LONG).show()
            }

        } catch (ex: IOException) {
            ex.printStackTrace()
        }

    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.imgForWard -> {
                if (media?.isPlaying!!) {
                    val currentPosition = media?.currentPosition!!
                    if (currentPosition + 10000 <= media?.duration!!)
                        media?.seekTo(currentPosition + 10000)
                    else
                        media?.seekTo(media?.duration!!)
                }
            }
            R.id.imgMenu -> {
                val intent = Intent(this, ListSounds::class.java)
                startActivityForResult(intent, 100)
            }
            R.id.imgBackWard -> {
                if (media?.isPlaying!!) {
                    val currentPosition = media?.currentPosition!!
                    if (currentPosition - 10000 >= 0)
                        media?.seekTo(currentPosition - 10000)
                    else
                        media?.seekTo(0)
                }
            }
            R.id.imgNext -> {
                seek = 0
                if (isShuffle) {
                    positionsRand = position
                    val rand = Random()
                    position = rand.nextInt(songList?.size!! - 1)
                    playSound(position!!)
                } else {
                    position = position!! + 1
                    playSound(position!!)
                }
            }
            R.id.imgPrevios -> {
                seek = 0
                if (isShuffle && positionsRand != null) {
                    playSound(positionsRand!!)
                    positionsRand = positionsRand!! - 1
                } else {
                    position = position!! - 1
                    playSound(position!!)
                }
            }
            R.id.imgPlay -> {
                if (media?.isPlaying!!) {
                    media?.pause()
                    seek = media?.currentPosition!!
                    imgPlay.setImageResource(R.drawable.img_btn_play)
                } else {
                    playSound(position!!)
                    imgPlay.setImageResource(R.drawable.img_btn_pause)
                }
            }
            R.id.imgShuffle -> {
                if (isShuffle) {
                    isShuffle = false
                    imgShuffle.setImageResource(R.drawable.img_btn_shuffle)
                } else {
                    isShuffle = true
                    imgShuffle.setImageResource(R.drawable.img_btn_shuffle_pressed)
                    isRepeat = false
                    imgRepeat.setImageResource(R.drawable.img_btn_repeat)
                }
            }
            R.id.imgRepeat -> {
                if (isRepeat) {
                    isRepeat = false
                    imgRepeat.setImageResource(R.drawable.img_btn_repeat)
                } else {
                    isRepeat = true
                    imgRepeat.setImageResource(R.drawable.img_btn_repeat_pressed)
                    isShuffle = false
                    imgShuffle.setImageResource(R.drawable.img_btn_shuffle)
                }
            }
        }
    }

    fun updateProgress() {

        Thread(Runnable {

            while (isChecked) {
                try {
                    Thread.sleep(100)
                    mHandler.post {

                        val total = media?.duration!!
                        val current = media?.currentPosition!!
                        txtTimeCurrent.text = Utilities.milli_to_time(current.toLong())
                        txtTimeRemain.text = Utilities.milli_to_time(total.toLong())

                        val progresses = Utilities.setProgressPercentage(current.toLong(), total.toLong())
                        seekBarSound.progress = progresses

                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }).start()

    }

    private fun checkPermissionStorage() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            val array = Array(1) { android.Manifest.permission.READ_EXTERNAL_STORAGE }
            ActivityCompat.requestPermissions(this, array, 1234)
        } else
            return
    }

}
