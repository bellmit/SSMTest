package cn.gtmap.onemap.platform.utils;

import java.math.BigDecimal;


/**
 * @描述 AI智能分析结果子模型
 * @作者 卜祥东
 * @创建日期 2019年7月3日 
 * @创建时间 下午2:49:34
 * @版本号 V 1.0
 */
public class RecognitionResultSubModel {
	
	private Integer recogType;//分析模型子类型
	
	private BigDecimal ymax;//目录矩形范围，也是百分比坐标
	
	private BigDecimal ymin;
	
	private BigDecimal xmax;
	
	private BigDecimal xmin;
	
	private BigDecimal score;//相似度评分0-1

	public Integer getRecogType() {
		return recogType;
	}

	public void setRecogType(Integer recogType) {
		this.recogType = recogType;
	}

	public BigDecimal getYmax() {
		return ymax;
	}

	public void setYmax(BigDecimal ymax) {
		this.ymax = ymax;
	}

	public BigDecimal getYmin() {
		return ymin;
	}

	public void setYmin(BigDecimal ymin) {
		this.ymin = ymin;
	}

	public BigDecimal getXmax() {
		return xmax;
	}

	public void setXmax(BigDecimal xmax) {
		this.xmax = xmax;
	}

	public BigDecimal getXmin() {
		return xmin;
	}

	public void setXmin(BigDecimal xmin) {
		this.xmin = xmin;
	}

	public BigDecimal getScore() {
		return score;
	}

	public void setScore(BigDecimal score) {
		this.score = score;
	}

	
}
