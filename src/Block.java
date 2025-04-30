
/**
 *
 * @author jellyj. sarmi, Max
 */
public class Block {

    private boolean isActive;
    private final BlockType Type;
    private float x, y, z;

    public enum BlockType {
        BlockType_Grass(0),
        BlockType_Stone(1),
        BlockType_Dirt(2),
        BlockType_Sand(3),
        BlockType_Water(4),
        BlockType_Bedrock(5),
        BlockType_GoldOre(6),
        BlockType_IronOre(7),
        BlockType_CoalOre(8),
        BlockType_DiamondOre(9),
        BlockType_RedstoneOre(10),
        BlockType_LapisOre(11),
        BlockType_Netherrack(12),
        BlockType_SoulSand(13),
        BlockType_Glowstone(14);

        private int BlockID;

        BlockType(int i) {
            BlockID = i;
        }

        public int GetID() {
            return BlockID;
        }

        public void SetID(int i) {
            BlockID = i;
        }
    }

    public Block(BlockType type) {
        Type = type;
    }

    public void setCoords(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public boolean isActive() {
        return isActive;
    }

    public void SetActive(boolean active) {
        isActive = active;
    }

    public int GetID() {
        return Type.GetID();
    }
}

