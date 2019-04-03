package application.ahmadi.myplayer

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_list_sounds.*
import android.view.MotionEvent
import android.support.v7.widget.RecyclerView
import android.view.GestureDetector
import android.view.View
import android.widget.Toast
import java.io.File


class ListSounds : AppCompatActivity() {

    var view: View? = null
    var ViewItemPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_sounds)

        val array = SongsManager.getAllMusic()
        val adapter = CategoryRecyclerAdapter(this, array)
        val layoutManager = LinearLayoutManager(this)

        listViewSounds.adapter = adapter
        listViewSounds.layoutManager = layoutManager
        listViewSounds.setHasFixedSize(true)

        listViewSounds.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {

            var gestureDetector = GestureDetector(this@ListSounds, object : GestureDetector.SimpleOnGestureListener() {

                override fun onSingleTapUp(motionEvent: MotionEvent): Boolean {

                    return true
                }

            })

            override fun onInterceptTouchEvent(Recyclerview: RecyclerView, motionEvent: MotionEvent): Boolean {

                view = Recyclerview.findChildViewUnder(motionEvent.x, motionEvent.y)

                if (view != null && gestureDetector.onTouchEvent(motionEvent)) {

                    ViewItemPosition = Recyclerview.getChildAdapterPosition(view)

                    val intent = Intent(this@ListSounds, MainActivity::class.java)
                    intent.putExtra("position", ViewItemPosition)
                    intent.putExtra("seek", 0)
                    setResult(100, intent)
                    finish()

                }

                return false
            }

            override fun onTouchEvent(Recyclerview: RecyclerView, motionEvent: MotionEvent) {

            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

            }
        })

    }

}
