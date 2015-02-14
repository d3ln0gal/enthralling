package enthralling.domain;

public interface Robot {
	
	public RegulatedMotor getMotor(String portName, String motorType);
	public void putMotor(String portName, String motorType);

}
