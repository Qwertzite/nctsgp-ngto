package qwertzite.ngtoplugin;

import com.google.gson.JsonObject;

import jp.ngt.ngtlib.block.NGTObject;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import qwertzite.mctsg.ModLog;
import qwertzite.mctsg.api.BuildingContext;
import qwertzite.mctsg.api.IBuildingEntry;
import qwertzite.mctsg.util.JsonHelper;

public class NgtoBuilding implements IBuildingEntry {
	private static final String KEY_ENABLED = "enabled";
	private static final String KEY_HAS_LIMIT = "has_limit";
	private static final String KEY_LIMIT = "limit";
	private static final String KEY_WEIGHT = "weight";
	private static final String KEY_DEPTH = "depth";
	private static final String KEY_FACING = "facing";
	
	private boolean isEnabled = true;
	private boolean hasLimit = false;
	private int limit = 100;
	private int weight = 100;
	private int width;
	private int length;
	private int height;
	private int depth;
	private EnumFacing facing;
	private NGTObject ngtObj;
	
	public NgtoBuilding(NGTObject ngtObj) {
		this.ngtObj = ngtObj;
		this.height = ngtObj.ySize;
		this.depth = -1;
		this.facing = EnumFacing.NORTH;
		this.setWidthAndLength(ngtObj.xSize, ngtObj.zSize, this.facing);
	}
	
	@Override
	public boolean loadSettings(JsonObject obj) {
		boolean isEnabled = this.isEnabled;
		boolean hasLimit = this.hasLimit;
		int limit = this.limit;
		int weight = this.weight;
		int depth = this.depth;
		EnumFacing facing = this.facing;
		try {
			isEnabled = JsonHelper.getBoolean(obj, KEY_ENABLED, isEnabled);
			hasLimit = JsonHelper.getBoolean(obj, KEY_HAS_LIMIT, hasLimit);
			if (hasLimit) { limit = JsonHelper.getInt(obj, KEY_LIMIT, limit); }
			weight = JsonHelper.getInt(obj, KEY_WEIGHT, weight);
			depth = JsonHelper.getInt(obj, KEY_DEPTH, depth);
			facing = EnumFacing.byName(JsonHelper.getString(obj, KEY_FACING, facing.getName2()));
		} catch (ClassCastException e) {
			ModLog.warn("Caught an exception while loading setting", e);
			return false;
		} catch (IllegalStateException e) {
			ModLog.warn("Caught an exception while loading setting", e);
			return false;
		}
		this.isEnabled = isEnabled;
		this.hasLimit = hasLimit;
		this.limit = limit;
		this.weight = weight;
		this.depth = depth;
		this.facing = facing;
		this.setWidthAndLength(this.ngtObj.xSize, this.ngtObj.zSize, facing);
		return true;
	}
	
	private void setWidthAndLength(int xSize, int zSize, EnumFacing facing) {
		switch(facing) {
		case EAST:
		case WEST:
			this.width = zSize;
			this.length = xSize;
			break;
		case NORTH:
		case SOUTH:
		default:
			this.width = xSize;
			this.length = zSize;
			break;
		}
	}
	
	@Override public boolean isEnabled() { return this.isEnabled; }
	@Override public boolean hasLimit() { return this.hasLimit; }
	@Override public int getLimit() { return this.limit; }
	@Override public int getWeight() { return this.weight; }
	@Override public int getWidth() { return this.width; }
	@Override public int getLength() { return this.length; }
	@Override public int getHeight() { return this.height; }
	@Override public int getDepth() { return this.depth; }

	@Override
	public void generateBuilding(BuildingContext context) {
		final int maxX = this.ngtObj.xSize;
		final int maxY = this.ngtObj.ySize;
		final int maxZ = this.ngtObj.zSize;
		context.pushMatrix();
		this.rotateBuilding(context);
		for (int x = 0; x < maxX; x++) {
			for (int y = 0; y < maxY; y++) {
				for (int z = 0; z < maxZ; z++) {
					context.setBlockState(new BlockPos(x, y, z), this.ngtObj.getBlockSet(x, y, z).toBlockState());
				}
			}
		}
		context.popMatrix();
	}
	
	private void rotateBuilding(BuildingContext context) {
		switch(this.facing) {
		case EAST:
			context.translate(0, this.depth, this.length-1);
			context.rotate(Rotation.COUNTERCLOCKWISE_90);
			break;
		case NORTH:
			context.translate(0, this.depth, 0);
			break;
		case SOUTH:
			context.translate(this.width-1, this.depth, this.length-1);
			context.rotate(Rotation.CLOCKWISE_180);
			break;
		case WEST:
			context.translate(this.width-1, this.depth, 0);
			context.rotate(Rotation.CLOCKWISE_90);
			break;
		default:
			break;
		}
	}
}
