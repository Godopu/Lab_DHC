package kr.pe.vanilet;

public class SensorResource {
	private double humidity;
	private double temperature;
	private int ECG ; 
	private int STRESS;
	private int HR ; 
	public SensorResource() {
		this(-1, -1);
	}
	
	public SensorResource(double humidity, double temperature) {
		this.humidity = humidity;
		this.temperature = temperature;
	}

	public SensorResource(int stress, int HR, int ECG) {
		this.STRESS = stress;
		this.HR = HR;
		this.ECG = ECG ;
	}
	public double getHumidity() {
		return humidity;
	}

	public void setHumidity(double humidity) {
		this.humidity = humidity;
	}

	public int getECG()
	{
		return ECG ; 
	}
	
	public int getHR()
	{
		return HR ; 
	}
	
	public int getStress()
	{
		return STRESS ; 
	}
	
	public void setECG(int _ECG) 
	{
		ECG = _ECG; 
	}
	
	public void setHR(int _HR) 
	{
		HR = _HR; 
	}
	
	public void setStress(int _stress) 
	{
		STRESS = _stress; 
	}
	
	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	@Override
	public String toString() {
		return "SensorResource [getHumidity()=" + getHumidity()
				+ ", getTemperature()=" + getTemperature() + "]";
	}
}
