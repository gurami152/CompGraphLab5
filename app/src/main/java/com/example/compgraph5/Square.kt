package com.example.compgraph5

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.opengles.GL10

class Square {
    private val vertexBuffer // buffer holding the vertices
            : FloatBuffer
    private val vertices = floatArrayOf(
            -1.0f, -0.56f, 0.0f,  // V1 - bottom left
            -1.0f, 0.56f, 0.0f,  // V2 - top left
            1.0f, -0.56f, 0.0f,  // V3 - bottom right
            1.0f, 0.56f, 0.0f // V4 - top right
    )
    private val textureBuffer // buffer holding the texture coordinates
            : FloatBuffer
    private val texture = floatArrayOf( // Mapping coordinates for the vertices
            0.0f, 1.0f,  // top left     (V2)
            0.0f, 0.0f,  // bottom left  (V1)
            1.0f, 1.0f,  // top right    (V4)
            1.0f, 0.0f // bottom right (V3)
    )

    /** The texture pointer  */
    private val textures = IntArray(1)

    /**
     * Load the texture for the square
     * @param gl
     * @param context
     */
    fun loadGLTexture(gl: GL10, context: Context) {
        // loading texture
        val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.text)

        // generate one texture pointer
        gl.glGenTextures(1, textures, 0)
        // ...and bind it to our array
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0])
        // create nearest filtered texture
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST.toFloat())
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR.toFloat())

        // Use Android GLUtils to specify a two-dimensional texture image from our bitmap
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0)
        // Clean up
        bitmap.recycle()
    }

    /** The draw method for the square with the GL context  */
    fun draw(gl: GL10,angle:Float) {
        // bind the previously generated texture
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0])

        gl.glRotatef(angle,0f,0f,-5.0f)
        // Point to our buffers
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY)
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY)

        // Set the face rotation
        gl.glFrontFace(GL10.GL_CW)

        // Point to our vertex buffer
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer)
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer)

        // Draw the vertices as triangle strip
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.size / 3)

        //Disable the client state before leaving
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY)
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY)
    }

    init {
        // a float has 4 bytes so we allocate for each coordinate 4 bytes
        var byteBuffer = ByteBuffer.allocateDirect(vertices.size * 4)
        byteBuffer.order(ByteOrder.nativeOrder())

        // allocates the memory from the byte buffer
        vertexBuffer = byteBuffer.asFloatBuffer()

        // fill the vertexBuffer with the vertices
        vertexBuffer.put(vertices)

        // set the cursor position to the beginning of the buffer
        vertexBuffer.position(0)
        byteBuffer = ByteBuffer.allocateDirect(texture.size * 4)
        byteBuffer.order(ByteOrder.nativeOrder())
        textureBuffer = byteBuffer.asFloatBuffer()
        textureBuffer.put(texture)
        textureBuffer.position(0)
    }
}