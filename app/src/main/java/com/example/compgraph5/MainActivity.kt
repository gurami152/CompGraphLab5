package com.example.compgraph5

import android.opengl.GLSurfaceView
import android.os.Bundle
import android.view.MotionEvent
import android.view.Window
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    /** The OpenGL view  */
    private var glSurfaceView: GLSurfaceView? = null
    private var glRenderer:GlRenderer?=null

    /** Called when the activity is first created.  */
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // requesting to turn the title OFF
        requestWindowFeature(Window.FEATURE_NO_TITLE)


        // Initiate the Open GL view and

        // create an instance with this activity
        glSurfaceView = GLSurfaceView(this)

        // set our renderer to be the main renderer with
        glRenderer= GlRenderer(this)
        // the current activity context
        glSurfaceView!!.setRenderer(glRenderer)
        setContentView(glSurfaceView)

    }

    /**
     * Remember to resume the glSurface
     */
    override fun onResume() {
        super.onResume()
        glSurfaceView!!.onResume()
    }

    /**
     * Also pause the glSurface
     */
    override fun onPause() {
        super.onPause()
        glSurfaceView!!.onPause()
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.
        when (e.action) {
            MotionEvent.ACTION_MOVE -> {
                glRenderer?.randomevent(e)
            }
        }
        return true
    }
}