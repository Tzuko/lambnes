package com.lambelly.lambnes.platform.cpu;

import java.util.Map;
import java.util.HashMap;
import java.util.EnumSet;

public enum Instruction
{
	ADC_IMMEDIATE(0x69),
	ADC_ZERO_PAGE(0x65),
	ADC_ZERO_PAGE_X(0x75),
	ADC_ABSOLUTE(0x6D),
	ADC_ABSOLUTE_X(0x7D),
	ADC_ABSOLUTE_Y(0x79),
	ADC_INDEXED_INDIRECT(0x61),
	ADC_INDIRECT_INDEXED(0x71),
	AND_IMMEDIATE(0x29),
	AND_ZERO_PAGE(0x25),
	AND_ZERO_PAGE_X(0x35),
	AND_ABSOLUTE(0x2D),
	AND_ABSOLUTE_X(0x3D),
	AND_ABSOLUTE_Y(0x39),
	AND_INDEXED_INDIRECT(0x21),
	AND_INDIRECT_INDEXED(0x31),
	ASL_ACCUMULATOR(0x0A),
	ASL_ZERO_PAGE(0x06),
	ASL_ZERO_PAGE_X(0x16),
	ASL_ABSOLUTE(0x0E),
	ASL_ABSOLUTE_X(0x1E),
	BCC(0x90),
	BCS(0xB0),
	BEQ(0xF0),
	BIT_ZERO_PAGE(0x24),
	BIT_ABSOLUTE(0x2C),
	BMI(0x30),
	BNE(0xD0),           
	BPL(0x10),
	BRK(0x00),
	BVC(0x50),
	BVS(0x70),
	CLC(0x18),
	CLD(0xD8),        
	CLI(0x58),
	CLV(0xB8),
	CMP_IMMEDIATE(0xC9),
	CMP_ZERO_PAGE(0xC5),
	CMP_ZERO_PAGE_X(0xD5),
	CMP_ABSOLUTE(0xCD),
	CMP_ABSOLUTE_X(0xDD),
	CMP_ABSOLUTE_Y(0xD9),
	CMP_INDEXED_INDIRECT(0xC1),
	CMP_INDIRECT_INDEXED(0xD1),
	CPX_IMMEDIATE(0xE0),
	CPX_ZERO_PAGE(0xE4),
	CPX_ABSOLUTE(0xEC),
	CPY_IMMEDIATE(0xC0),
	CPY_ZERO_PAGE(0xC4),
	CPY_ABSOLURE(0xCC),                
	DEC_ZERO_PAGE(0xC6),
	DEC_ZERO_PAGE_X(0xD6),
	DEC_ABSOLUTE(0xCE),
	DEX_ABSOLUTE_X(0xDE),
	DEX(0xCA),
	DEY(0x88),
	EOR_IMMEDIATE(0x49),
	EOR_ZERO_PAGE(0x45),
	EOR_ZERO_PAGE_X(0x55),
	EOR_ABSOLUTE(0x4D),
	EOR_ABSOLUTE_X(0x5D),
	EOR_ABSOLUTE_Y(0x59),
	EOR_INDEXED_INDIRECT(0x41),
	EOR_INDIRECT_INDEXED(0x51),
	INC_ZERO_PAGE(0xE6),
	INC_ZERO_PAGE_X(0xF6),
	INC_ABSOLUTE(0xEE),
	INC_ABSOLUTE_X(0xFE),
	INX(0xE8),
	INY(0xC8),
	JMP_ABSOLUTE(0x4C),
	JMP_RELATIVE(0x6C),
	JSR_ABSOLUTE(0x20),                
	LDA_IMMEDIATE(0xA9),
	LDA_ZERO_PAGE(0xA5),
	LDA_ZERO_PAGE_X(0xB5),
	LDA_ABSOLUTE(0xAD),
	LDA_ABSOLUTE_X(0xBD),
	LDA_ABSOLUTE_Y(0xB9),
	LDA_INDEXED_INDIRECT(0xA1),
	LDA_INDIRECT_INDEXED(0xB1),
	LDX_IMMEDIATE(0xA2),
	LDX_ZERO_PAGE(0xA6),
	LDX_ZERO_PAGE_Y(0xB6),
	LDX_ABSOLUTE(0xAE),
	LDX_ABSOLUTE_Y(0xBE),
	LDY_IMMEDIATE(0xA0),
	LDT_ZERO_PAGE(0xA4),
	LDT_ZERO_PAGE_X(0xB4),
	LDT_ABSOLUTE(0xAC),
	LDT_ABSOLUTE_X(0xBC),
	LSR_ACCUMULATOR(0x4A),
	LSD_ZERO_PAGE(0x46),
	LSD_ZERO_PAGE_X(0x56),
	LSD_ABSOLUTE(0x4E),
	LSD_ABSOLUTE_X(0x5E),
	NOP(0xEA),
	ORA_IMMEDIATE(0x09),
	ORA_ZERO_PAGE(0x05),
	ORA_ZERO_PAGE_X(0x15),
	ORA_ABSOLUTE(0x0D),
	ORA_ABSOLUTE_X(0x1D),
	ORA_ABSOLUTE_Y(0x19),
	ORA_INDEXED_INDIRECT(0x01),
	ORA_INDIRECT_INDEXED(0x11),
	PHA(0x48),    
	PHP(0x08),
	PLA(0x68),
	PLP(0x28),
	ROL_ACCUMULATOR(0x2A),
	ROL_ZERO_PAGE(0x26),
	ROL_ZERO_PAGE_X(0x36),
	ROL_ABSOLUTE(0x2E),
	ROL_ABSOLUTE_X(0x3E),
	ROR_ACCUMULATOR(0x6A),
	ROR_ZERO_PAGE(0x66),
	ROR_ZERO_PAGE_X(0x76),
	ROR_ABSOLUTE(0x6E),
	ROR_ABSOLUTE_X(0x7E),
	RTI(0x40),
	RTS(0x60),
	SBC_IMMEDIATE(0xE9),
	SBC_ZERO_PAGE(0xE5),
	SBC_ZERO_PAGE_X(0xF5),
	SBC_ABSOLUTE(0xED),
	SBC_ABSOLUTE_X(0xFD),
	SBC_ABSOLUTE_Y(0xF9),
	SBC_INDEXED_INDIRECT(0xE1),
	SBC_INDIRECT_INDEXED(0xF1),
	SEC(0x38),
	SED(0xF8),
	SEI(0x78),
	STA_ZERO_PAGE(0x85),
	STA_ZERO_PAGE_X(0x95),
	STA_ABSOLUTE(0x8D),
	STA_ABSOLUTE_X(0x9D),
	STA_ABSOLUTE_Y(0x99),
	STA_INDEXED_INDIRECT(0x81),
	STA_INDIRECT_INDEXED(0x91),
	STX_ZERO_PAGE(0x86),
	STX_ZERO_PAGE_Y(0x96),
	STX_ABSOLUTE(0x8E),
	STY_ZERO_PAGE(0x84),
	STY_ZERO_PAGE_X(0x94),
	STY_ABSOLUTE(0x8C),
	TAX(0xAA),
	TAY(0xA8),
	TSX(0xBA),
	TXA(0x8A),
	TXS(0x9A),
	TYA(0x98);	

	private static final Map<Integer,Instruction> lookup = new HashMap<Integer,Instruction>();

    static 
    {
        for(Instruction i : EnumSet.allOf(Instruction.class))
        {
             lookup.put(i.getOpCode(), i);
        }
    }
	
	private Instruction(int opCode)
	{
		this.opCode = opCode;
	}    
    
    public static Instruction get(int opCode) { 
        return lookup.get(opCode); 
    }	
	
	public int getOpCode()
	{
		return opCode;
	}
	
	public void setOpCode(int opCode)
	{
		this.opCode = opCode;
	}
	private int opCode = 0;
}
