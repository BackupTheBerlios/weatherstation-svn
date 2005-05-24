package org.idanso.dataset;

import java.util.List;

import org.idanso.weather.Station;
import org.idanso.weather.StationRecord;
import org.jfree.data.DomainOrder;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.general.DatasetGroup;
import org.jfree.data.xy.XYDataset;

public class StationRecordsXYDataset implements XYDataset {
	
	public static final int DATA_TEMPERATURE=2;
	public static final int DATA_HUMADITY=4;
	public static final int DATA_PRESSURE=8;
	
	protected Station station;
	protected List records;
	protected int[] sets;
	protected float[] minValues=null;
	protected float[] maxValues=null;
	
	public StationRecordsXYDataset(Station station,List list,int[] sets)
	{
		this.station=station;
		this.records=list;
		this.sets=sets;
	}
	
	
	

	public DomainOrder getDomainOrder() {
		return DomainOrder.ASCENDING;
	}

	public int getItemCount(int series) {
		return records.size();
	}

	public Number getX(int series, int item) {
		return new Float(getXValue(series,item));
	}

	public double getXValue(int series, int item) {
		return ((StationRecord) records.get(item)).getStamp().getTime();
	}

	public Number getY(int series, int item) {
		return new Float(getYValue(series,item));
	}

	public double getYValue(int series, int item) {
		StationRecord record=(StationRecord) records.get(item);
		int type=sets[series];
		if (type==DATA_TEMPERATURE)
			return record.getTemperature();
		else if (type==DATA_HUMADITY)
			return record.getHumadity();
		else if (type==DATA_PRESSURE)
			return record.getPressure();
		else
		{
			return 0;
		}
	}
	
	private void checkMinMax(StationRecord record)
	{
		boolean needInit=false;
		if (minValues==null || maxValues==null)
		{
			minValues=new float[sets.length];
			maxValues=new float[sets.length];
			needInit=true;
		}
		for(int i=0;i<sets.length;++i)
		{
			int type=sets[i];
			float value=0;
			if (type==DATA_TEMPERATURE)
				value=record.getTemperature();
			else if (type==DATA_HUMADITY)
				value=record.getHumadity();
			else if (type==DATA_PRESSURE)
				value=record.getPressure();
			if (!needInit)
			{
				minValues[i]=Math.min(minValues[i],value);
				maxValues[i]=Math.max(maxValues[i],value);
			}
			else
			{
				minValues[i]=value;
				maxValues[i]=value;				
			}
		}
		
	}

	public int getSeriesCount() {
		return sets.length;
	}

	public String getSeriesName(int series) {
		return "";
	}

	public void addChangeListener(DatasetChangeListener arg0) {
		// TODO Auto-generated method stub

	}

	public void removeChangeListener(DatasetChangeListener arg0) {
		// TODO Auto-generated method stub

	}

	public DatasetGroup getGroup() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setGroup(DatasetGroup arg0) {
		// TODO Auto-generated method stub

	}

}
