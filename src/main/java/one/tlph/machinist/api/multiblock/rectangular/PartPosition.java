package one.tlph.machinist.api.multiblock.rectangular;

import net.minecraft.state.EnumProperty;

/*
 * A multiblock library for making irregularly-shaped multiblock machines
 *
 * Original author: Erogenous Beef
 * https://github.com/erogenousbeef/BeefCore
 *
 * Ported to Minecraft 1.9 by ZeroNoRyouki
 * https://github.com/ZeroNoRyouki/ZeroCore
 */

import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;

public enum PartPosition implements IStringSerializable {
	Unknown(null, Type.Unknown),
	Interior(null, Type.Unknown),
	FrameCorner(null, Type.Frame),
	FrameEastWest(null, Type.Frame),
	FrameSouthNorth(null, Type.Frame),
	FrameUpDown(null, Type.Frame),
	TopFace(Direction.UP, Type.Face),
	BottomFace(Direction.DOWN, Type.Face),
	NorthFace(Direction.NORTH, Type.Face),
	SouthFace(Direction.SOUTH, Type.Face),
	EastFace(Direction.EAST, Type.Face),
	WestFace(Direction.WEST, Type.Face);


	@Override
	public String getString() {
		return this.toString();
	}

	public enum Type {
		Unknown,
		Interior,
		Frame,
		Face
	}
	
	public boolean isFace() {
		return this._type == Type.Face;
	}

	public boolean isFrame() {
		return this._type == Type.Frame;
	}

	public Direction getFacing() {
		return this._facing;
	}

	public Type getType() {
		return this._type;
	}

	public static EnumProperty createProperty(String name) {

		return EnumProperty.create(name, PartPosition.class);
	}

	public String getName() {

		return this.toString();
	}

	PartPosition(Direction facing, Type type) {

		this._facing = facing;
		this._type = type;
	}

	private Direction _facing;
	private Type _type;
}
