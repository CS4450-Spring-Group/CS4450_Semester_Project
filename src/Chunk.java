import java.nio.FloatBuffer;
import java.util.Random;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

/**
 *
 * @author jellyj. sarmi, Max
 */
public class Chunk {
    
    static final int CHUNK_SIZE = 30;
    static final int CUBE_LENGTH = 2;
    public Block[][][] Blocks;
    private int VBOVertexHandle;
    private int VBOColorHandle;
    public int StartX, StartY, StartZ;
    public Random r;
    private int VBOTextureHandle;
    public Texture texture;
    
    public void render(){
        
        glPushMatrix();
            glBindBuffer(GL_ARRAY_BUFFER, VBOVertexHandle);
            glVertexPointer(3, GL_FLOAT, 0, 0L);
            glBindBuffer(GL_ARRAY_BUFFER, VBOColorHandle);
            glColorPointer(3, GL_FLOAT, 0, 0L);
            glBindBuffer(GL_ARRAY_BUFFER, VBOTextureHandle);
            glBindTexture(GL_TEXTURE_2D, 1);
            glTexCoordPointer(2,GL_FLOAT,0,0L);
            glDrawArrays(GL_QUADS, 0, CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE * 24);
        glPopMatrix();
        
    }
    
    public void rebuildMesh(float startX, float startY, float startZ){
        
        VBOColorHandle = glGenBuffers();
        VBOVertexHandle = glGenBuffers();
        VBOTextureHandle = glGenBuffers();
        //create SimplexNoise object
        //(int largestFeature,double persistence, int seed)
        SimplexNoise noise = new SimplexNoise(128, .7, 17);
        
        FloatBuffer VertexPositionData = 
                BufferUtils.createFloatBuffer(
                        (CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE) * 6 * 12);
        FloatBuffer VertexColorData = 
                BufferUtils.createFloatBuffer(
                        (CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE) * 6 * 12);
        FloatBuffer VertexTextureData =
                BufferUtils.createFloatBuffer(
                        (CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE) * 6 * 12);
        
        for(float x = 0; x < CHUNK_SIZE; x+=1){
            for(float z = 0; z < CHUNK_SIZE; z+=1){
                // Scale the coordinates to control terrain smoothness
                float scaledX = (StartX + x) * 0.1f;
                float scaledZ = (StartZ + z) * 0.1f;

                // Use noise to get height for this (x, z) column
                float noiseValue = (float) noise.getNoise(scaledX, scaledZ);
                int height = (int)(noiseValue * 10 + CHUNK_SIZE * 0.5f);  // adjust as needed
                for(float y = 0; y <= height; y++){
                    VertexPositionData.put(createCube(
                            (float)(startX + x * CUBE_LENGTH), 
                            (float)(y * CUBE_LENGTH + (int)(CHUNK_SIZE * .8)), 
                            (float)(startZ + z * CUBE_LENGTH)));
                    VertexColorData.put(createCubeVertexCol(
                            getCubeColor(Blocks[(int)x][(int)y][(int)z])));
                    VertexTextureData.put(createTexCube(
                            (float) 0, 
                            (float) 0,
                            Blocks[(int)(x)][(int) (y)][(int) (z)]));
                    
                }
                
            }
            
        }
        
        VertexTextureData.flip();
        VertexColorData.flip();
        VertexPositionData.flip();
        
        glBindBuffer(GL_ARRAY_BUFFER, VBOVertexHandle);
        glBufferData(GL_ARRAY_BUFFER, VertexPositionData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, VBOColorHandle);
        glBufferData(GL_ARRAY_BUFFER, VertexColorData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, VBOTextureHandle);
        glBufferData(GL_ARRAY_BUFFER, VertexTextureData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        
    }
    
    private float[] createCubeVertexCol(float[] CubeColorArray) {
        
        float[] cubeColors = new float[CubeColorArray.length * 4 * 6];
        for (int i = 0; i < cubeColors.length; i++) {
            cubeColors[i] = CubeColorArray[i % CubeColorArray.length];
        } 
        return cubeColors;
        
    }
    
    public static float[] createCube(float x, float y, float z){
        
        int offset = CUBE_LENGTH / 2;
        
        return new float[] {
            // TOP QUAD
            x + offset, y + offset, z,
            x - offset, y + offset, z,
            x - offset, y + offset, z - CUBE_LENGTH,
            x + offset, y + offset, z - CUBE_LENGTH,
            // BOTTOM QUAD
            x + offset, y - offset, z - CUBE_LENGTH,
            x - offset, y - offset, z - CUBE_LENGTH,
            x - offset, y - offset, z,
            x + offset, y - offset, z,
            // FRONT QUAD
            x + offset, y + offset, z - CUBE_LENGTH,
            x - offset, y + offset, z - CUBE_LENGTH,
            x - offset, y - offset, z - CUBE_LENGTH,
            x + offset, y - offset, z - CUBE_LENGTH,
            // BACK QUAD
            x + offset, y - offset, z,
            x - offset, y - offset, z,
            x - offset, y + offset, z,
            x + offset, y + offset, z,
            // LEFT QUAD
            x - offset, y + offset, z - CUBE_LENGTH,
            x - offset, y + offset, z,
            x - offset, y - offset, z,
            x - offset, y - offset, z - CUBE_LENGTH,
            // RIGHT QUAD
            x + offset, y + offset, z,
            x + offset, y + offset, z - CUBE_LENGTH,
            x + offset, y - offset, z - CUBE_LENGTH,
            x + offset, y - offset, z
        
        };
                
    }
    
    private float[] getCubeColor(Block block) {
        return new float[]{1, 1, 1};
    }

    public Chunk(int startX, int startY , int startZ) {
        
        try{
            texture = TextureLoader.getTexture("png",
            ResourceLoader.getResourceAsStream("terrain.png"));
        }
        catch(Exception e)
        {
            System.out.print("ER-ROAR!");
        }
        
        r= new Random();
        Blocks = new Block[CHUNK_SIZE][CHUNK_SIZE][CHUNK_SIZE];
        
        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int y = 0; y < CHUNK_SIZE; y++) {
                for (int z = 0; z < CHUNK_SIZE; z++) {
                    if(r.nextFloat()>0.7f){
                        Blocks[x][y][z] = new
                            Block(Block.BlockType.BlockType_Grass);
                    }else if(r.nextFloat()>0.4f){
                        Blocks[x][y][z] = new
                            Block(Block.BlockType.BlockType_Dirt);
                    }else if(r.nextFloat()>0.2f){
                        Blocks[x][y][z] = new
                            Block(Block.BlockType.BlockType_Water);
                    }else{
                        //Blocks[x][y][z] = new
                            //Block(Block.BlockType.BlockType_Default);
                    }
                    
                }
                
            }
            
        }
        
        VBOColorHandle = glGenBuffers();
        VBOVertexHandle = glGenBuffers();
        VBOTextureHandle = glGenBuffers();
        StartX = startX;
        StartY = startY;
        StartZ = startZ;
        
        rebuildMesh(startX, startY , startZ);
        
    }
    
    public static float[] createTexCube(float x, float y, Block block) {
        float offset = 1.0f / 16.0f;

        if (block == null) {
            return createDefaultTexCoords(offset, 1, 0); // default texture at (1,0)
        }

        return switch (block.GetID()) {
            case 1 -> createGrassTexCoords(offset); // Grass
            case 2 -> createUniformTexCoords(offset, 2, 0); // Dirt
            case 3 -> createUniformTexCoords(offset, 15, 12); // Water
            default -> createDefaultTexCoords(offset, 1, 0); // Default
        };
    }

    private static float[] createUniformTexCoords(float offset, int texX, int texY) {
        float xMin = texX * offset;
        float xMax = xMin + offset;
        float yMin = texY * offset;
        float yMax = yMin + offset;

        return new float[] {
            // TOP
            xMax, yMin, xMin, yMin, xMin, yMax, xMax, yMax,
            // BOTTOM
            xMax, yMax, xMin, yMax, xMin, yMin, xMax, yMin,
            // FRONT
            xMax, yMin, xMin, yMin, xMin, yMax, xMax, yMax,
            // BACK
            xMax, yMax, xMin, yMax, xMin, yMin, xMax, yMin,
            // LEFT
            xMax, yMin, xMin, yMin, xMin, yMax, xMax, yMax,
            // RIGHT
            xMax, yMin, xMin, yMin, xMin, yMax, xMax, yMax
        };
    }

    private static float[] createGrassTexCoords(float offset) {
        float topX = 0 * offset, topY = 0 * offset;
        float sideX = 3 * offset, sideY = 1 * offset;
        float dirtX = 2 * offset, dirtY = 0 * offset;

        float topXMax = topX + offset, topYMax = topY + offset;
        float sideXMax = sideX + offset, sideYMax = sideY + offset;
        float dirtXMax = dirtX + offset, dirtYMax = dirtY + offset;

        return new float[]{
            // TOP
            topXMax, topY, topX, topY, topX, topYMax, topXMax, topYMax,
            // BOTTOM (dirt)
            dirtXMax, dirtYMax, dirtX, dirtYMax, dirtX, dirtY, dirtXMax, dirtY,
            // FRONT
            sideXMax, sideY, sideX, sideY, sideX, sideYMax, sideXMax, sideYMax,
            // BACK
            sideXMax, sideYMax, sideX, sideYMax, sideX, sideY, sideXMax, sideY,
            // LEFT
            sideXMax, sideY, sideX, sideY, sideX, sideYMax, sideXMax, sideYMax,
            // RIGHT
            sideXMax, sideY, sideX, sideY, sideX, sideYMax, sideXMax, sideYMax
        };
    }

    private static float[] createDefaultTexCoords(float offset, int texX, int texY) {
        return createUniformTexCoords(offset, texX, texY);
    }

}
