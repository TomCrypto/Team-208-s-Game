package renderer;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

/**
 * This component holds all the information about a 3D model required when it is drawn,
 * plus some other methods required by the renderer.
 *
 * @author Marc Sykes
 *
 */

public class Model{

	private final int vboLocation;
	private final int textureLocation;
	private final int programLocation;
	private final int length;
	private final int pll;
	private final int tll;
	private final int nll;

	private final double PI = 3.14159265358979323846;

	private Matrix4f modelMatrix;

	private Vector3f modelPos;
	private Vector3f modelAngle;
	private Vector3f modelScale;

	private FloatBuffer matrix44Buffer;

	public Model(int vboLocation, int length, int textureLocation, int programLocation, int pll, int tll, int nll){

		this.length 				= length;
		this.vboLocation			= vboLocation;
		this.textureLocation		= textureLocation;
		this.programLocation		= programLocation;
		this.pll = pll;
		this.tll = tll;
		this.nll = nll;

		modelMatrix = new Matrix4f();
		matrix44Buffer = BufferUtils.createFloatBuffer(16);

		modelPos = new Vector3f(0.0f, 0.0f, 0.0f);
		modelAngle = new Vector3f(0.0f, 0.0f, 0.0f);
		modelScale = new Vector3f(0.15f, 0.15f, 0.15f);
	}

	public void drawAs(Vector3f position, Vector3f rotation, Vector3f scale, Matrix4f projectionMatrix, Matrix4f viewMatrix){
		modelPos = position;
		modelAngle = rotation;
		modelScale = scale;
		updateMatrices(projectionMatrix, viewMatrix);
		draw();
	}

	public void draw() {
		GL20.glUseProgram(programLocation);
		// Bind the texture

		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureLocation);

		// Bind to the VAO that has all the information about the vertices
		GL20.glEnableVertexAttribArray(pll);
		GL20.glEnableVertexAttribArray(tll);
		GL20.glEnableVertexAttribArray(nll);

		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboLocation);

		// Draw the vertices
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, length);

		// Put everything back to default (deselect)
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER,0);
		GL20.glDisableVertexAttribArray(pll);
		GL20.glDisableVertexAttribArray(tll);
		GL20.glDisableVertexAttribArray(nll);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

		GL20.glUseProgram(0);
	}

	public void updateMatrices(Matrix4f projectionMatrix, Matrix4f viewMatrix){
		modelMatrix = new Matrix4f();

		// Scale, translate and rotate model
		Matrix4f.translate(modelPos, modelMatrix, modelMatrix);
		Matrix4f.scale(modelScale, modelMatrix, modelMatrix);

		Matrix4f.rotate(this.degreesToRadians(modelAngle.z), new Vector3f(0, 0, 1), modelMatrix, modelMatrix);
		Matrix4f.rotate(this.degreesToRadians(modelAngle.y), new Vector3f(0, 1, 0), modelMatrix, modelMatrix);
		Matrix4f.rotate(this.degreesToRadians(modelAngle.x), new Vector3f(1, 0, 0), modelMatrix, modelMatrix);

		this.exitOnGLError("Matrix1");

		// Upload matrices to the uniform variables
		GL20.glUseProgram(programLocation);

		this.exitOnGLError("Matrix3");

		// Get matrices uniform locations
		int projectionMatrixLocation = GL20.glGetUniformLocation(programLocation,"projectionMatrix");
		int viewMatrixLocation = GL20.glGetUniformLocation(programLocation, "viewMatrix");
		int modelMatrixLocation = GL20.glGetUniformLocation(programLocation, "modelMatrix");

		this.exitOnGLError("Matrix2");

		projectionMatrix.store(matrix44Buffer);
		matrix44Buffer.flip();
		GL20.glUniformMatrix4(projectionMatrixLocation, false, matrix44Buffer);

		this.exitOnGLError("Matrix3");

		viewMatrix.store(matrix44Buffer);
		matrix44Buffer.flip();
		GL20.glUniformMatrix4(viewMatrixLocation, false, matrix44Buffer);

		this.exitOnGLError("Matrix4");

		modelMatrix.store(matrix44Buffer);
		matrix44Buffer.flip();
		GL20.glUniformMatrix4(modelMatrixLocation, false, matrix44Buffer);

		this.exitOnGLError("Matrix5");

		GL20.glUseProgram(0);
	}

	public void setPos(Vector2f pos){ this.modelPos = new Vector3f(pos.x, pos.y, 0.0f); }

	public void destroy() {
		// Delete the texture
		GL11.glDeleteTextures(textureLocation);

		// Delete the shaders
		GL20.glUseProgram(0);
		GL20.glDeleteProgram(programLocation);

		// Disable the VBO index from the VAO attributes list
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);

		// Delete the vertex VBO
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glDeleteBuffers(vboLocation);

		// Delete the index VBO
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
	}

	private float degreesToRadians(float degrees){ return degrees * (float)(PI / 180d); }
	public Matrix4f getModelMatrix(){ return modelMatrix; }

	private void exitOnGLError(String errorMessage) {
		int errorValue = GL11.glGetError();

		if (errorValue != GL11.GL_NO_ERROR) {
			String errorString = GLU.gluErrorString(errorValue);
			System.err.println("ERROR - " + errorMessage + ": " + errorString);

			if (Display.isCreated()) Display.destroy();
			System.exit(-1);
		}
	}
}
