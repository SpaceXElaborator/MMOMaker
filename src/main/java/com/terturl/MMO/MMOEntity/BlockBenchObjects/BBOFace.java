package com.terturl.MMO.MMOEntity.BlockBenchObjects;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class BBOFace {

	@Getter
	private double[] uv = new double[4];
	
	@Getter @Setter
	private Integer rotation = 0;
	
	@Getter @Setter
	private Integer texture;
	
	public void setUV(Double a, Double b, Double c, Double d) {
		uv[0] = a;
		uv[1] = b;
		uv[2] = c;
		uv[3] = d;
	}
	
}