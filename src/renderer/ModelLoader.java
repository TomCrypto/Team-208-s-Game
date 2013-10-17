package renderer;

import java.io.*;
import java.nio.*;
import java.util.*;
import org.lwjgl.*;
import org.lwjgl.opengl.*;
import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

public class ModelLoader {

	int attribListCount = 0;

	public ModelLoader(){}

	public Model load(String modelPath, String texturePath, String vsPath, String fsPath){

		ArrayList<Float> vertices = new ArrayList<Float>();
		ArrayList<Float> vertexNormals = new ArrayList<Float>();
		ArrayList<Float> textureCoords = new ArrayList<Float>();

		ArrayList<Float> packed  = new ArrayList<Float>();

		try{
			Scanner sc = new Scanner(new File(modelPath));

			while(sc.hasNextLine()){
				String line = sc.nextLine();
				if(line.length() == 0)		{ continue; } //Skip empty lines
				if(line.charAt(0) == '#')	{ continue; } //Skip comments
				if(line.length() < 3)		{ continue; } //Skip lines that have junk data

				String[] args = line.split(" ");

				if(args[0].equals("v")){
					if(args.length != 5){ System.out.println("Invalid vertex data."); continue; }
					//Note: Obj files I assume are not meant to be parsed as an array like this
					//The identifier on each line is a constant length of 2
					//So 'v' is 'v ', which gives us a junk entry at args[1]
					vertices.add(Float.parseFloat(args[2]));
					vertices.add(Float.parseFloat(args[3]));
					vertices.add(Float.parseFloat(args[4]));
				}

				if(args[0].equals("vn")){
					if(args.length != 4){ System.out.println("Invalid normal data."); continue; }
					vertexNormals.add(Float.parseFloat(args[1]));
					vertexNormals.add(Float.parseFloat(args[2]));
					vertexNormals.add(Float.parseFloat(args[3]));
				}

				if(args[0].equals("vt")){
					if(args.length != 4){ System.out.println("Invalid texture data."); continue; }
					textureCoords.add(Float.parseFloat(args[1]));
					textureCoords.add(Float.parseFloat(args[2]));
				}

				if(args[0].equals("f")){
					if(args.length != 4){ System.out.println("Invalid index data."); continue; }

					int posIndex;
					int texIndex;
					int normIndex;

					String[] indexOne = args[1].split("/");

					posIndex = (Integer.parseInt(indexOne[0])-1)*3;
					packed.add(vertices.get(posIndex));
					packed.add(vertices.get(posIndex+1));
					packed.add(vertices.get(posIndex+2));

					texIndex = (Integer.parseInt(indexOne[1])-1)*2;
					packed.add(textureCoords.get(texIndex));
					packed.add(textureCoords.get(texIndex+1));

					normIndex = (Integer.parseInt(indexOne[2])-1)*3;
					packed.add(vertexNormals.get(normIndex));
					packed.add(vertexNormals.get(normIndex+1));
					packed.add(vertexNormals.get(normIndex+2));

					String[] indexTwo = args[2].split("/");

					posIndex = (Integer.parseInt(indexTwo[0])-1)*3;
					packed.add(vertices.get(posIndex));
					packed.add(vertices.get(posIndex+1));
					packed.add(vertices.get(posIndex+2));

					texIndex = (Integer.parseInt(indexTwo[1])-1)*2;
					packed.add(textureCoords.get(texIndex));
					packed.add(textureCoords.get(texIndex+1));

					normIndex = (Integer.parseInt(indexTwo[2])-1)*3;
					packed.add(vertexNormals.get(normIndex));
					packed.add(vertexNormals.get(normIndex+1));
					packed.add(vertexNormals.get(normIndex+2));

					String[] indexThree = args[3].split("/");

					posIndex = (Integer.parseInt(indexThree[0])-1)*3;
					packed.add(vertices.get(posIndex));
					packed.add(vertices.get(posIndex+1));
					packed.add(vertices.get(posIndex+2));

					texIndex = (Integer.parseInt(indexThree[1])-1)*2;
					packed.add(textureCoords.get(texIndex));
					packed.add(textureCoords.get(texIndex+1));

					normIndex = (Integer.parseInt(indexThree[2])-1)*3;
					packed.add(vertexNormals.get(normIndex));
					packed.add(vertexNormals.get(normIndex+1));
					packed.add(vertexNormals.get(normIndex+2));
				}
			}
			
			sc.close();
		}
		catch (FileNotFoundException e){ System.out.println(modelPath + " is not a a valid path to a model."); }

		float[] packedArray = new float[packed.size()];

		for(int i = 0; i < packed.size(); i++){ packedArray[i] = packed.get(i); }

		FloatBuffer verticesFloatBuffer = BufferUtils.createFloatBuffer(packedArray.length);

		for (int i = 0; i < packedArray.length; i++) {
			verticesFloatBuffer.put(packedArray[i]);
		}
		verticesFloatBuffer.flip();

		int length = (packedArray.length/8);

		int vboLocation;

		// Create a new Vertex Buffer Object in memory and select it (bind)
		vboLocation = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboLocation);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesFloatBuffer, GL15.GL_STREAM_DRAW);

		int positionListLocation = attribListCount++;
		int textureListLocation = attribListCount++;
		int normalListLocation = attribListCount++;

		// Put the position coordinates in attribute list 0
		GL20.glVertexAttribPointer(positionListLocation, 3, GL11.GL_FLOAT, false, 32, 0);
		// Put the texture coordinates in attribute list 1
		GL20.glVertexAttribPointer(textureListLocation, 2, GL11.GL_FLOAT, false, 32, 12);
		// Put the normal vector in attribute list 2
		GL20.glVertexAttribPointer(normalListLocation, 3, GL11.GL_FLOAT, false, 32, 20);

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		int programLocation = setupShaders(vsPath, fsPath, positionListLocation, textureListLocation, normalListLocation);;
		int textureLocation = this.loadPNGTexture(texturePath, GL13.GL_TEXTURE0);

		return new Model(vboLocation, length, textureLocation, programLocation, positionListLocation, textureListLocation, normalListLocation);
	}


	private int setupShaders(String vsPath, String fsPath, int pll, int tll, int nll) {
		// Load the vertex shader
		int vsId = this.loadShader(vsPath, GL20.GL_VERTEX_SHADER);
		// Load the fragment shader
		int fsId = this.loadShader(fsPath, GL20.GL_FRAGMENT_SHADER);

		// Create a new shader program that links both shaders
		int pId = GL20.glCreateProgram();
		GL20.glAttachShader(pId, vsId);
		GL20.glAttachShader(pId, fsId);

		//Position information will be attribute 0
		GL20.glBindAttribLocation(pId, pll, "in_Position");
		//Normal information will be attribute 1
		GL20.glBindAttribLocation(pId, tll, "in_TextureCoord");
		//Textute information will be attribute 2
		GL20.glBindAttribLocation(pId, nll, "in_Normal");

		GL20.glLinkProgram(pId);
		GL20.glValidateProgram(pId);

		return pId;
	}


	private int loadShader(String filename, int type) {
		StringBuilder shaderSource = new StringBuilder();
		int shaderID = 0;

		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line;
			while ((line = reader.readLine()) != null) {
				shaderSource.append(line).append("\n");
			}
			reader.close();
		} catch (IOException e) {
			System.err.println("Could not read file.");
			e.printStackTrace();
			System.exit(-1);
		}

		shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);

		if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.err.printf("Could not compile shader: %s.\n", GL20.glGetShaderInfoLog(shaderID, 65536));
			System.err.println(GL20.glGetShaderInfoLog(shaderID, 1000));
			System.exit(-1);
		}

		return shaderID;
	}

	private int loadPNGTexture(String filename, int textureUnit) {
		ByteBuffer buf = null;
		int tWidth = 0;
		int tHeight = 0;

		try {
			// Open the PNG file as an InputStream
			InputStream in = new FileInputStream(filename);
			// Link the PNG decoder to this stream
			PNGDecoder decoder = new PNGDecoder(in);

			// Get the width and height of the texture
			tWidth = decoder.getWidth();
			tHeight = decoder.getHeight();

			// Decode the PNG file in a ByteBuffer
			buf = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
			decoder.decodeFlipped(buf, decoder.getWidth() * 4, Format.RGBA);
			buf.flip();

			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		// Create a new texture object in memory and bind it
		int texId = GL11.glGenTextures();
		GL13.glActiveTexture(textureUnit);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);


		// All RGB bytes are aligned to each other and each component is 1 byte
		//GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

		// Upload the texture data and generate mip maps (for scaling)
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, tWidth, tHeight, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buf);

		// Setup what to do when the texture has to be scaled
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);

		return texId;
	}

}