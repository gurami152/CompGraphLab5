package com.example.compgraph5

import android.content.Context
import android.opengl.GLSurfaceView
import android.opengl.GLU
import android.view.MotionEvent
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class GlRenderer(private val context: Context) : GLSurfaceView.Renderer {

    private var previousX: Float = 0f
    private var previousY: Float = 0f

    private val square : Square = Square()
    private var angle:Float = 0.0F

    fun randomevent(e:MotionEvent){
        val x: Float = e.x
        val y: Float = e.y
        when (e.action) {
            MotionEvent.ACTION_MOVE -> {

                var dx: Float = x - previousX
                var dy: Float = y - previousY

                // reverse direction of rotation above the mid-line
                if (y > surfaceHeight / 2) {
                    dx *= -1
                }

                // reverse direction of rotation to left of the mid-line
                if (x < surfaceWidth / 2) {
                    dy *= -1
                }

                angle += (dx + dy) * TOUCH_SCALE_FACTOR
            }
        }

        previousX = x
        previousY = y
    }

    override fun onDrawFrame(gl: GL10) {
        // clear Screen and Depth Buffer
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT or GL10.GL_DEPTH_BUFFER_BIT)

        // Reset the Modelview Matrix
        gl.glLoadIdentity()

        // Drawing
        gl.glTranslatef(0.0f, 0.0f, -5.0f) // move 5 units INTO the screen

        square.draw(gl,angle) // Draw the triangle
    }

    private var surfaceHeight:Int=0
    private var surfaceWidth:Int=0
    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        var heightLoc = height
        surfaceHeight=height
        surfaceWidth=width
        if (heightLoc == 0) {                       //Prevent A Divide By Zero By
            heightLoc=1//Making Height Equal One
        }
        gl.glViewport(0, 0, width, height) //Reset The Current Viewport
        gl.glMatrixMode(GL10.GL_PROJECTION) //Select The Projection Matrix
        gl.glLoadIdentity() //Reset The Projection Matrix

        //Calculate The Aspect Ratio Of The Window
        GLU.gluPerspective(gl, 45.0f, width.toFloat() / heightLoc.toFloat(), 0.1f, 100.0f)
        gl.glMatrixMode(GL10.GL_MODELVIEW) //Select The Modelview Matrix
        gl.glLoadIdentity() //Reset The Modelview Matrix
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        // Load the texture for the square
        square.loadGLTexture(gl, context)
        gl.glEnable(GL10.GL_TEXTURE_2D) //Enable Texture Mapping ( NEW )
        gl.glShadeModel(GL10.GL_SMOOTH) //Enable Smooth Shading
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f) //Black Background
        gl.glClearDepthf(1.0f) //Depth Buffer Setup
        gl.glEnable(GL10.GL_DEPTH_TEST) //Enables Depth Testing
        gl.glDepthFunc(GL10.GL_LEQUAL) //The Type Of Depth Testing To Do

        //Really Nice Perspective Calculations
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST)
    }

    companion object {
        private const val TOUCH_SCALE_FACTOR: Float = 180.0f / 320f
    }
}