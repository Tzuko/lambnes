package com.lambelly.lambnes.platform.ppu;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.*;

import com.lambelly.lambnes.LambNes;
import com.lambelly.lambnes.cartridge.Ines;
import com.lambelly.lambnes.platform.Platform;

public class NesPpuMemory
{
	private Logger logger = Logger.getLogger(NesPpuMemory.class);
	// VRAM
	private int[] patternTable0 = new int[4096]; // 0x0000 -- holds 256 tiles 
	private int[] patternTable1 = new int[4096]; // 0x1000 -- holds 256 tiles
	private PPUNameTable nameTable0 = null;      // 0x2000
	private PPUNameTable nameTable1 = null;      // 0x2400
	private PPUNameTable nameTable2 = null;      // 0x2800
	private PPUNameTable nameTable3 = null;      // 0x2C00
	private int[] imagePalette = new int[16];    // 0x3F00
	private int[] spritePalette = new int[16];   // 0x3F10
	private Ines cartridge;
	
	// SPR-RAM
	private SpriteAttribute[] sprRam = new SpriteAttribute[64];     
	private SpriteTile[] spriteBuffer = new SpriteTile[NesPpu.SPRITE_COUNT];
	
	public static final int BACKGROUND_PALETTE_ADDRESS = 0x3F00;
	public static final int SPRITE_PALETTE_ADDRESS = 0x3F10;

	public NesPpuMemory()
	{
		// initialize spr-ram
		for (int i = 0; i < sprRam.length; i++)
		{
			this.sprRam[i] = new SpriteAttribute();
		}
	}
	
	public void establishMirroring()
	{
		// logger.debug("setting up name tables");
		PPUNameTable nameTableA = new PPUNameTable();
		PPUNameTable nameTableB = new PPUNameTable();
		
		// logger.debug("applying mirroring: " + (this.getCartridge().getHeader().isHorizontalMirroring()?"HORIZONTAL":"VERTICAL"));
		// set up name tables -- establishes how mirroring is being done. 
		if (this.getCartridge().getHeader().isHorizontalMirroring())
		{
			this.setNameTable0(nameTableA);
			this.setNameTable1(nameTableA);
			this.setNameTable2(nameTableB);
			this.setNameTable3(nameTableB);
		} 
		else if (this.getCartridge().getHeader().isVerticalMirroring())
		{
			this.setNameTable0(nameTableA);
			this.setNameTable1(nameTableB);
			this.setNameTable2(nameTableA);
			this.setNameTable3(nameTableB);			
		}

	}

	public NesPpuMemory(int[] patternTiles)
	{
		this.setPatternTiles(patternTiles);
	}	
	
	public void setPatternTiles(int[] chrRom)
	{
		if (chrRom.length > 4096)
		{
			// split in half
			int[] table0 = ArrayUtils.subarray(chrRom, 0, 4096);
			int[] table1 = ArrayUtils.subarray(chrRom, 4096, 8193);
			
			this.setPatternTable0(table0);
			this.setPatternTable1(table1);
		}		
		else
		{
			//logger.warn("chr rom length small!");
		}
	}
	
	public PPUNameTable getNameTableFromHexAddress(int address) throws IllegalStateException
	{
		if (address >= 0x2000 && address <= 0x23FF)
		{
			return this.getNameTable0();
		}
		else if (address >= 0x2400 && address <= 0x27FF)
		{
			return this.getNameTable1();
		}
		else if (address >= 0x2800 && address <= 0x2BFF)
		{
			return this.getNameTable2();
		}
		else if (address >= 0x2C00 && address <= 0x2FFF)
		{
			return this.getNameTable3();
		}		
		else if (address >= 0x3000 && address <= 0x33FF)
		{
			// mirror of name table 0
			return this.getNameTable0();				
		}
		else if (address >= 0x3400 && address <= 0x37FF)
		{
			// mirror of name table 1
			return this.getNameTable1();	
		}
		else if (address >= 0x3800 && address <= 0x3BFF)
		{
			// mirror of name table 2
			return this.getNameTable2();	
		}
		else if (address >= 0x3C00 && address <= 0x3FFF)
		{
			// mirror of name table 3
			return this.getNameTable3();	
		}		
		else
		{
			throw new IllegalStateException("tried to access name table at 0x" + Integer.toHexString(address));
		}
	}
	
	public int getMemoryFromHexAddress(int address) throws IllegalStateException
	{
		// logger.debug("getting memory from address: 0x" + Integer.toHexString(address));
		
		if (address >= 0x0000 && address <= 0xFFF)
		{
			// pattern table 0
			return this.getPatternTable0()[address];
		}
		else if (address >=0x1000 && address <= 0x1FFF)
		{
			// pattern table 1
			int arrayIndex = address - 0x01000;
			return this.getPatternTable1()[arrayIndex];
		}
		else if (address >= 0x2000 && address <= 0x23BF)
		{
			// name table 0
			return this.getNameTable0().getMemoryFromHexAddress(address);	
		}
		else if (address >= 0x23C0 && address <= 0x23FF)
		{
			// attribute table 0
			int arrayIndex = address - 0x023C0;
			return this.getNameTable0().getMemoryFromHexAddress(address);
		}
		else if (address >= 0x2400 && address <= 0x27BF)
		{
			// name table 1
			return this.getNameTable1().getMemoryFromHexAddress(address);	
		}
		else if (address >= 0x27C0 && address <= 0x27FF)
		{
			// attribute table 1
			return this.getNameTable1().getMemoryFromHexAddress(address);	
		}
		else if (address >= 0x2800 && address <= 0x2BBF)
		{
			// name table 2
			return this.getNameTable2().getMemoryFromHexAddress(address);	

		}
		else if (address >= 0x2BC0 && address <= 0x2BFF)
		{
			// attribute table 2
			return this.getNameTable2().getMemoryFromHexAddress(address);	
		}
		else if (address >= 0x2C00 && address <= 0x2FBF)
		{
			// name table 3
			return this.getNameTable3().getMemoryFromHexAddress(address);	
		}
		else if (address >= 0x2FC0 && address <= 0x2FFF)
		{
			// attribute table 3
			return this.getNameTable3().getMemoryFromHexAddress(address);	
		}
		else if (address >= 0x3000 && address <= 0x33BF)
		{
			// mirror of name table 0
			return this.getNameTable0().getMemoryFromHexAddress(address);				
		}
		else if (address >= 0x33C0 && address <= 0x33FF)
		{
			// mirror of attribute table 0
			return this.getNameTable0().getMemoryFromHexAddress(address);	
		}
		else if (address >= 0x3400 && address <= 0x37BF)
		{
			// mirror of name table 1
			return this.getNameTable1().getMemoryFromHexAddress(address);	
		}
		else if (address >= 0x37C0 && address <= 0x37FF)
		{
			// mirror of attribute table 1
			return this.getNameTable1().getMemoryFromHexAddress(address);	
		}
		else if (address >= 0x3800 && address <= 0x3BBF)
		{
			// mirror of name table 2
			return this.getNameTable2().getMemoryFromHexAddress(address);	
		}
		else if (address >= 0x3BC0 && address <= 0x3BFF)
		{
			// attribute table 2
			return this.getNameTable2().getMemoryFromHexAddress(address);	
		}
		else if (address >= 0x3C00 && address <= 0x3EFF)
		{
			// mirror of name table 3
			return this.getNameTable3().getMemoryFromHexAddress(address);	
		}
		else if (address >= 0x3F00 && address <= 0x3FFF)
		{
			// don't much care about high byte.
			int lowbyte = address & 0x00FF;
			
			// first half of low byte determines which palette, 2nd half determines index
			int palette = (lowbyte & 0xF0) / 0xF;
			int index = lowbyte & 0x0F;
			int value = 0;
			
			if (palette % 2 == 0)
			{
				// even, so image palette
				value = this.getImagePalette()[index];				
			}
			else
			{
				// odd, so sprite palette
				value = this.getSpritePalette()[index];
			}
			
			//logger.info("getting palette address: " + address + " with value: " + value + " determined palette: " + palette + " index: " + index);
			
			return value;
		}   
		else if (address >= 0x4000 && address <= 0x7FFF)
		{
			return this.getMemoryFromHexAddress(address - 0x4000);
		}
		else if (address >= 0x8000 && address <= 0xC000)
		{
			return this.getMemoryFromHexAddress(address - 0x8000);
		}		
		else
		{
			throw new IllegalStateException("tried to access memory address 0x" + Integer.toHexString(address) + " which is not mapped to any data structure");
		}
	}
	
	public void setMemoryFromHexAddress(int address, int value) throws IllegalStateException
	{
		// logger.debug("setting value " + value + " to ppu address: 0x" + Integer.toHexString(address));
		
		if (address >= 0x0000 && address <= 0xFFF)
		{
			// pattern table 0
			this.getPatternTable0()[address] = value;
		}
		else if (address >=0x1000 && address <= 0x1FFF)
		{
			// pattern table 1
			int arrayIndex = address - 0x01000;
			this.getPatternTable1()[arrayIndex] = value;
		}
		else if (address >= 0x2000 && address <= 0x23FF)
		{
			// name table 0
			this.getNameTable0().setMemoryFromHexAddress(address, value);	
		}
		else if (address >= 0x2400 && address <= 0x27FF)
		{
			// name table 1
			this.getNameTable1().setMemoryFromHexAddress(address, value);
		}
		else if (address >= 0x2800 && address <= 0x2BFF)
		{
			// name table 2
			this.getNameTable2().setMemoryFromHexAddress(address, value);
		}
		else if (address >= 0x2C00 && address <= 0x2FFF)
		{
			// name table 3
			this.getNameTable3().setMemoryFromHexAddress(address, value);
		}
		else if (address >= 0x3000 && address <= 0x33FF)
		{
			// mirror of name table 0
			this.getNameTable0().setMemoryFromHexAddress(address, value);			
		}
		else if (address >= 0x3400 && address <= 0x37FF)
		{
			// mirror of name table 1
			this.getNameTable1().setMemoryFromHexAddress(address, value);
		}
		else if (address >= 0x3800 && address <= 0x3BFF)
		{
			// mirror of name table 2
			this.getNameTable2().setMemoryFromHexAddress(address, value);
		}
		else if (address >= 0x3C00 && address <= 0x3EFF)
		{
			// mirror of name table 3
			this.getNameTable3().setMemoryFromHexAddress(address, value);
		}
		else if (address >= 0x3F00 && address <= 0x3FFF)
		{
			//logger.info("setting address: " + Integer.toHexString(address) + " to value: " + value);
			// don't much care about most significant byte.
			int lsb = address & 0x00FF;
			
			// first half of low byte determines which palette, 2nd half determines index
			int palette = ((lsb & 0xF0) / 0xF) % 2;
			int index = lsb & 0x0F;
			
			//logger.info("setting palette address: " + address + " with value: " + value + " determined palette: " + palette + " index: " + index);
			
			if (palette == 0)
			{
				// even, so image palette
				this.setImagePaletteValue(index, value);				
			}
			else
			{
				// odd, so sprite palette
				this.setSpritePaletteValue(index, value);
			}
			
			// mirroring
			if ((index == 0x0) || (index == 0x4) || (index == 0x8) || (index == 0xC))
			{
				// flop palette affected by palette bit for mirroring
				if (palette == 0)
				{
					this.setSpritePaletteValue(0x0,value);
					this.setSpritePaletteValue(0x4,value);
					this.setSpritePaletteValue(0x8,value);
					this.setSpritePaletteValue(0xC,value);
				}
				else
				{
					this.setImagePaletteValue(0x0, value);
					this.setImagePaletteValue(0x4, value);
					this.setImagePaletteValue(0x8, value);
					this.setImagePaletteValue(0xC, value);
				}
				
			}
		}
		else if (address >= 0x4000 && address <= 0x7FFF)
		{
			this.setMemoryFromHexAddress(address - 0x4000, value);
		}
		else if (address >= 0x8000 && address <= 0xC000)
		{
			this.setMemoryFromHexAddress(address - 0x8000, value);
		}
		else
		{
			throw new IllegalStateException("tried to access memory address 0x" + Integer.toHexString(address) + " which is not mapped to any data structure");
		}
	}
	
	public int[] getPatternTable0()
	{
		return patternTable0;
	}

	public void setPatternTable0(int[] patternTable0)
	{
		this.patternTable0 = patternTable0;
	}

	public int[] getPatternTable1()
	{
		return patternTable1;
	}

	public void setPatternTable1(int[] patternTable1)
	{
		this.patternTable1 = patternTable1;
	}

	public SpriteAttribute[] getSprRam()
	{
		return sprRam;
	}
	
	public SpriteAttribute getSprRam(int index)
	{
		return sprRam[index];
	}
	
	public SpriteAttribute getSprRamForSpriteNumber(int spriteNumber)
	{
		int x = 0;
		SpriteAttribute s = new SpriteAttribute();
		
		while (spriteNumber != s.getTileIndex() && x < 64)
		{
			s = this.getSprRam(x);
			x++;
		}
		
		if (s.getTileIndex() == spriteNumber)
		{
			//logger.info("found attribute with tile index " + s.getTileIndex() + " for sprite " + spriteNumber);
			return s;
		}
		else
		{
			return new SpriteAttribute();
		}
			
	}
	
	public int getSprRamFromHexAddress(int address)
	{
		int sprRamIndex = address / 4;
		int sprAttributeIndex = address % 4;
		
		switch (sprAttributeIndex)
		{
			case 0:
				return this.sprRam[sprRamIndex].getyCoordinate();
			case 1:
				return this.sprRam[sprRamIndex].getTileIndex();
			case 2:
				return this.sprRam[sprRamIndex].getColorHighBit();
			case 3:
				return this.sprRam[sprRamIndex].getxCoordinate();
			default:
				return 0;
		}
	}	

	public void setSprRam(SpriteAttribute[] sprRam)
	{
		this.sprRam = sprRam;
	}
	
	public void setSprRamFromHexAddress(int address, int value)
	{
		int sprRamIndex = address / 4;
		int sprAttributeIndex = address % 4;
		
		//logger.info("received address: " + address + " value: " + value + " setting to sprRamIndex : " + sprRamIndex + " sprAttributeIndex: " + sprAttributeIndex);
		
		switch (sprAttributeIndex)
		{
			case 0:
				this.sprRam[sprRamIndex].setyCoordinate(value);
			case 1:
				this.sprRam[sprRamIndex].setTileIndex(value);
			case 2:
				this.sprRam[sprRamIndex].parseSprRam3(value);
			case 3:
				this.sprRam[sprRamIndex].setxCoordinate(value);
		}
	}	
	 
	public int[] getImagePalette()
	{
		return imagePalette;
	}

	public void setImagePalette(int[] imagePalette)
	{
		this.imagePalette = imagePalette;
	}
	
	public void setImagePaletteValue(int index, int value)
	{
		//logger.info("setting image palette index: " + index + " with value: " + value);
		this.getImagePalette()[index] = value;
	}

	public int[] getSpritePalette()
	{
		return spritePalette;
	}

	public void setSpritePalette(int[] spritePalette)
	{
		this.spritePalette = spritePalette;
	}
	
	public void setSpritePaletteValue(int index, int value)
	{
		this.getSpritePalette()[index] = value;
	}

	public PPUNameTable getNameTable0()
	{
		return nameTable0;
	}

	public void setNameTable0(PPUNameTable nameTable0)
	{
		this.nameTable0 = nameTable0;
	}

	public PPUNameTable getNameTable1()
	{
		return nameTable1;
	}

	public void setNameTable1(PPUNameTable nameTable1)
	{
		this.nameTable1 = nameTable1;
	}

	public PPUNameTable getNameTable2()
	{
		return nameTable2;
	}

	public void setNameTable2(PPUNameTable nameTable2)
	{
		this.nameTable2 = nameTable2;
	}

	public PPUNameTable getNameTable3()
	{
		return nameTable3;
	}

	public void setNameTable3(PPUNameTable nameTable3)
	{
		this.nameTable3 = nameTable3;
	}

	public SpriteTile getSpriteFromBuffer(int index)
	{
		return spriteBuffer[index];
	}
	
	public SpriteTile[] getSpriteBuffer()
	{
		return spriteBuffer;
	}
	
	public int getSpritesInBufferCount()
	{
		int spriteCount = 0;
		for (SpriteTile sprite : spriteBuffer)
		{
			if (sprite != null)
			{
				spriteCount++;
			}
		}
		return spriteCount;
	}

	public void setSpriteBuffer(SpriteTile[] spriteBuffer)
	{
		this.spriteBuffer = spriteBuffer;
	}	
	
	public void setSpriteToBuffer(int index, SpriteTile sprite)
	{
		this.spriteBuffer[index] = sprite;
	}
	
	public void clearSpriteBuffer()
	{
		this.spriteBuffer = new SpriteTile[NesPpu.SPRITE_COUNT];
	}

	public Ines getCartridge()
    {
    	return cartridge;
    }

	public void setCartridge(Ines cartridge)
    {
    	this.cartridge = cartridge;
    }
}
