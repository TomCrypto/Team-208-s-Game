package renderer;

import java.util.ArrayList;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 * The 3D Renderer. Running the main method in this class starts up the testing sequence.
 *
 * @author Marc Sykes
 */

public class Render3D {

	// Setup variables
	private final double PI = 3.14159265358979323846;


	// Moving variables
	private Matrix4f projectionMatrix;
	private Matrix4f viewMatrix;
	private Vector3f cameraPos;
	private float pitchAngleDegrees = 0.0f;
	private float headingAngleDegrees = 0.0f;

	/**
	 * This method should only be used for testing the renderer.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Display.setDisplayMode(new DisplayMode(800, 600));
			Display.setTitle("Testing");
			Display.create();
		}
		catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		ArrayList<Model> models = new ArrayList<Model>();

		Render3D r3d = new Render3D(800, 600);
		r3d.setupMatrices(800, 600);
		ModelLoader ml = new ModelLoader();
		models.add(ml.load("assets/box.obj", "assets/cube_texture.png", "assets/shaders/vertex.vs", "assets/shaders/fragment.fs"));

		//r3d.cameraPos = new Vector3f(0.0f, 0.0f, -10.0f);

		r3d.setupView(800, 600);

		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_DEPTH_TEST);

		while (!Display.isCloseRequested()) {



			r3d.loopCycle();

			models.get(0).updateMatrices(r3d.getProjectionMatrix(), r3d.getViewMatrix());
			models.get(0).draw();

			//for(Model m:models){ m.updateMatrices(projectionMatrix, viewMatrix);  m.draw(); }

			// Force a maximum FPS of about 60
			Display.sync(60);
			// Let the CPU synchronize with the GPU if GPU is tagging behind
			Display.update();
		}
		Display.destroy();
	}

	public Render3D(int width, int height) {
		// Initialize OpenGL (Display)
		this.setupMatrices(width, height);
		this.setupView(width, height);
	}

	/**
	 * Sets up the projection matrix.
	 * 
	 * @author Marc Sykes
	 * @param Dsiplay width
	 * @param Display height
	 */
	private void setupMatrices(int width, int height) {
		// Setup projection matrix
		projectionMatrix = new Matrix4f();
		float fieldOfView = 45f;
		float aspectRatio = (float)width/(float)height;
		float nearPlane = 0.1f;
		float farPlane = 100f;

		float yScale = this.coTangent(this.degreesToRadians(fieldOfView / 2f));
		float xScale = yScale / aspectRatio;
		float frustumLength = farPlane - nearPlane;

		projectionMatrix.m00 = xScale;
		projectionMatrix.m11 = yScale;
		projectionMatrix.m22 = -((farPlane + nearPlane) / frustumLength);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2*nearPlane*farPlane)/frustumLength);
        projectionMatrix.m33 = 0;

		// Setup view matrix
		viewMatrix = new Matrix4f();
	}

	/**
	 * Sets up the viewport
	 * @param Display width
	 * @param Display height
	 */
	private void setupView(int width, int height) {
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		// Map the internal OpenGL coordinate system to the entire screen
		GL11.glViewport(0, 0, width, height);

		cameraPos = new Vector3f(0.0f, 0.0f, -1.0f);
		this.exitOnGLError("setupView");
	}
	
	/**
	 * Refreshes the renderer
	 */
	public void loopCycle() {
		viewMatrix = new Matrix4f();

		this.exitOnGLError("One");

		float top = (float) Math.sin(degreesToRadians(pitchAngleDegrees))*cameraPos.z;
		float tad = (float) Math.cos(degreesToRadians(pitchAngleDegrees))*cameraPos.z;

		Vector3f cameraOffset = new Vector3f(0.0f, top, tad-cameraPos.z);

		Matrix4f.rotate(degreesToRadians(pitchAngleDegrees), new Vector3f(1.0f, 0.0f, 0.0f), viewMatrix, viewMatrix);

		Matrix4f.translate(cameraPos, viewMatrix, viewMatrix);
		Matrix4f.translate(cameraOffset, viewMatrix, viewMatrix);

		Matrix4f.translate(new Vector3f(0-cameraPos.x, 0-cameraPos.y, 0-cameraPos.z), viewMatrix, viewMatrix);
		Matrix4f.rotate(degreesToRadians(headingAngleDegrees), new Vector3f(0.0f, 0.0f, 1.0f), viewMatrix, viewMatrix);
		Matrix4f.translate(cameraPos, viewMatrix, viewMatrix);

		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		this.exitOnGLError("loopCycle");
	}

	public Matrix4f getViewMatrix(){ return viewMatrix; }
	public Matrix4f getProjectionMatrix(){ return projectionMatrix; }
	private float coTangent(float angle){ return (float)(1f / Math.tan(angle));}
	private float degreesToRadians(float degrees){ return degrees * (float)(PI / 180d); }
	public void setCameraPos(Vector3f pos){ cameraPos = pos; }

	private void exitOnGLError(String errorMessage) {
		int errorValue = GL11.glGetError();

		if (errorValue != GL11.GL_NO_ERROR) {
			String errorString = GLU.gluErrorString(errorValue);
			System.err.println("ERROR - " + errorMessage + ": " + errorString);

			if (Display.isCreated()) Display.destroy();
			System.exit(-1);
		}
	}

	public void incrementCameraPitch(){
		if(pitchAngleDegrees < 0){ pitchAngleDegrees++; }
	}

	public void decrementCameraPitch(){
		if(pitchAngleDegrees > -65){ pitchAngleDegrees--; }
	}

	public void incrementCameraHeading(){ headingAngleDegrees++; }
	public void decrementCameraHeading(){ headingAngleDegrees--; }
}