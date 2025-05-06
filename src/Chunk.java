import java.io.IOException;
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
        
        renderWater(600, 40);
        
    }
    
    public void rebuildMesh(float startX, float startY, float startZ){
        
        VBOColorHandle = glGenBuffers();
        VBOVertexHandle = glGenBuffers();
        VBOTextureHandle = glGenBuffers();
        //create SimplexNoise object
        //(int largestFeature,double persistence, int seed)
        SimplexNoise noise = new SimplexNoise(128, .7, new Random().nextInt());
        
        FloatBuffer VertexPositionData = 
                BufferUtils.createFloatBuffer(
                        (CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE) * 6 * 12);
        FloatBuffer VertexColorData = 
                BufferUtils.createFloatBuffer(
                        (CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE) * 6 * 12);
        FloatBuffer VertexTextureData =
                BufferUtils.createFloatBuffer(
                        (CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE) * 6 * 12);     
        
        for (float x = 0; x < CHUNK_SIZE; x++) {
            for (float z = 0; z < CHUNK_SIZE; z++) {
                float scaledX = (StartX + x) * 0.1f;
                float scaledZ = (StartZ + z) * 0.1f;
                float noiseValue = (float) noise.getNoise(scaledX, scaledZ);
                int height = (int) (noiseValue * 10 + CHUNK_SIZE * 0.5f);

                for (float y = 0; y < CHUNK_SIZE; y++) {
                    Block.BlockType type;

                    if (y == 0) {
                        type = Block.BlockType.BlockType_Bedrock;
                    } else if (y > height) {
                        continue; // Empty air space
                    } else if (y == height) {
                        float topNoise = (float) noise.getNoise(scaledX * 2, scaledZ * 2);
                        if (topNoise > 0.2f) {
                            type = Block.BlockType.BlockType_Grass;
                        } else if (topNoise > 0.0f) {
                            type = Block.BlockType.BlockType_Sand;
                        } else {
                            type = Block.BlockType.BlockType_Water;
                        }if (type == Block.BlockType.BlockType_Grass && r.nextFloat() < 0.05f) {
                            // choose red or yellow at random
                            Block.BlockType flowerType = (r.nextFloat() < 0.5f)
                                ? Block.BlockType.BlockType_RedFlower
                                : Block.BlockType.BlockType_YellowFlower;

                            // position the flower one block above
                            VertexPositionData.put(createCube(
                                startX + x * CUBE_LENGTH,
                                (y + 1) * CUBE_LENGTH + (int)(CHUNK_SIZE * 0.8f),
                                startZ + z * CUBE_LENGTH
                            ));
                            VertexColorData.put(
                                createCubeVertexCol(getCubeColor(new Block(flowerType)))
                            );
                            VertexTextureData.put(
                                createTexCube(0, 0, new Block(flowerType))
                            );
                        }
                    } else {
                        // underground layers
                        if (y < 5) {
                            float oreNoise = (float) noise.getNoise(scaledX * 5, scaledZ * 5);
                            if (oreNoise > 0.6f) {
                                type = Block.BlockType.BlockType_DiamondOre;
                            } else if (oreNoise > 0.3f) {
                                type = Block.BlockType.BlockType_GoldOre;
                            } else {
                                type = Block.BlockType.BlockType_Stone;
                            }
                        } else if (y < 10) {
                            float oreNoise = (float) noise.getNoise(scaledX * 4, scaledZ * 4);
                            if (oreNoise > 0.6f) {
                                type = Block.BlockType.BlockType_IronOre;
                            } else if (oreNoise > 0.3f) {
                                type = Block.BlockType.BlockType_CoalOre;
                            } else {
                                type = Block.BlockType.BlockType_Stone;
                            }
                        } else {
                            type = Block.BlockType.BlockType_Dirt;
                        }
                    }

                    Blocks[(int) x][(int) y][(int) z] = new Block(type);
                    
                    VertexPositionData.put(createCube(
                        startX + x * CUBE_LENGTH,
                        y * CUBE_LENGTH + (int) (CHUNK_SIZE * 0.8f),
                        startZ + z * CUBE_LENGTH
                    ));
                    VertexColorData.put(createCubeVertexCol(
                        getCubeColor(Blocks[(int) x][(int) y][(int) z])
                    ));
                    VertexTextureData.put(createTexCube(
                        0,
                        0,
                        Blocks[(int) x][(int) y][(int) z]
                    ));
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
        catch(IOException e)
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
        if (block == null) return createDefaultTexCoords(offset, 1, 0);

        return switch (block.GetID()) {
            case 0 -> createGrassTexCoords(offset); // <---- SPECIAL handling for grass!!
            case 1 -> createUniformTexCoords(offset, 1, 0); // Stone
            case 2 -> createUniformTexCoords(offset, 2, 0); // Dirt
            case 3 -> createUniformTexCoords(offset, 2, 1); // Sand
            //case 4 -> createUniformTexCoords(offset, 1, 11); // water
            case 5 -> createUniformTexCoords(offset, 1, 1); // Bedrock (fix bedrock location!)
            case 6 -> createUniformTexCoords(offset, 0, 2); // Gold Ore
            case 7 -> createUniformTexCoords(offset, 1, 2); // Iron Ore
            case 8 -> createUniformTexCoords(offset, 2, 2); // Coal Ore
            case 9 -> createUniformTexCoords(offset, 2, 3); // Diamond Ore
            case 10 -> createUniformTexCoords(offset, 3, 3); // Redstone Ore
            case 11 -> createUniformTexCoords(offset, 0, 10); // Lapis Ore
            case 12 -> createUniformTexCoords(offset, 1, 9); // Netherrack
            case 13 -> createUniformTexCoords(offset, 8, 6); // Soul Sand
            case 14 -> createUniformTexCoords(offset, 9, 6); // Glowstone
            case 15 -> createUniformTexCoords(offset, 12, 0); // Red flower
            case 16 -> createUniformTexCoords(offset, 13, 0); // Yellow flower 
            default -> createDefaultTexCoords(offset, 1, 0);
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
        float topX = 2 * offset, topY = 9 * offset;
        float sideX = 3 * offset, sideY = 0 * offset;
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
    
    
    private void renderWater(int size, int height) {
        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, texture.getTextureID());

        // location of water in terrain.png
        float tileSize = 1.0f / 16.0f;
        float xMin = 1 * tileSize;
        float xMax = xMin + tileSize;
        float yMin = 11 * tileSize;
        float yMax = yMin + tileSize;

        float y = height; 
        int half = size / 2;

        glBegin(GL_QUADS);
        for (int x = -half; x < half; x++) {
            for (int z = -half; z < half; z++) {
                // Skip any blocks inside the chunk
                if (x >= 0 && x < CHUNK_SIZE-1 && z >= 0 && z < CHUNK_SIZE-1) continue;

                float wx = StartX + x * CUBE_LENGTH; // world x coordinate for center of tile
                float wz = StartZ + z * CUBE_LENGTH; // world z coordinate for center of tile

                int offset = CUBE_LENGTH / 2; // offset from center to edge of tile
                float left = wx - offset, right = wx + offset; 
                float back = wz - offset, front = wz + offset; 

                // Top face of a water cube
                glTexCoord2f(xMax, yMin); glVertex3f(right, y, back);
                glTexCoord2f(xMin, yMin); glVertex3f(left, y, back);
                glTexCoord2f(xMin, yMax); glVertex3f(left, y, front);
                glTexCoord2f(xMax, yMax); glVertex3f(right, y, front);
            }
        }
        glEnd();
    }
    

}
