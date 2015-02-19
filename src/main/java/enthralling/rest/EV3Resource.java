package enthralling.rest;
//TODO force snap client to create robot first. All methods must return immediately if robot is not connected first
//to avoid freeze in waiting for server response, set perhaps a short timeout also
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;

import javax.inject.*;

import restx.annotations.*;
import restx.RestxContext;
import restx.RestxRequest;
import restx.RestxRequestMatch;
import restx.RestxResponse;
import restx.StdRestxRequestMatcher;
import restx.StdRoute;
import restx.factory.Component;
import restx.http.HTTP;
import restx.factory.Component;
import restx.security.PermitAll;
import restx.security.RolesAllowed;
import restx.security.RestxSession;
import lejos.hardware.Audio;
import lejos.hardware.port.Port;
import lejos.hardware.port.UARTPort;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.remote.ev3.RMIRegulatedMotor;
import lejos.remote.ev3.RMIRemoteRegulatedMotor;
import lejos.remote.ev3.RMISampleProvider;
import lejos.remote.ev3.RemoteEV3;

@Component @RestxResource
public class EV3Resource {
    
    @GET("/EV3/motor_rotate")
    @PermitAll
    public String motor_rotate(String address, String port, String motorType, int angle, boolean immediateReturn){
    	try{
    		getMotor(address,port,motorType).rotate(angle, immediateReturn);
    	}
    	catch(Exception x){
    		return x.toString();
    	}
    	return "ok";
    }
    
    @GET("/EV3/motor_forward")
    @PermitAll
    public String motor_forward(String address, String port, String motorType){
    	try{
    		getMotor(address,port,motorType).forward();
    	}
    	catch(Exception x){
    		return x.toString();
    	}
    	return "ok";
    }
    
    @GET("/EV3/motor_backward")
    @PermitAll
    public String motor_backward(String address, String port, String motorType){
    	try{
    		getMotor(address,port,motorType).backward();
    	}
    	catch(Exception x){
    		return x.toString();
    	}
    	return "ok";
    }
    
    @GET("/EV3/motor_getLimitAngle")
    @PermitAll
    public String motor_getLimitAngle(String address, String port, String motorType){
    	try{
    		return new Integer(getMotor(address,port,motorType).getLimitAngle()).toString();
    	}
    	catch(Exception x){
    		return x.toString();
    	}
    }

    @GET("/EV3/motor_getMaxSpeed")
    @PermitAll
    public String motor_getMaxSpeed(String address, String port, String motorType){
    	try{
    		return new Float(getMotor(address,port,motorType).getMaxSpeed()).toString();
    	}
    	catch(Exception x){
    		return x.toString();
    	}
    }

    @GET("/EV3/motor_getSpeed")
    @PermitAll
    public String motor_getSpeed(String address, String port, String motorType){
    	try{
    		return new Integer(getMotor(address,port,motorType).getSpeed()).toString();
    	}
    	catch(Exception x){
    		return x.toString();
    	}
    }

    @GET("/EV3/motor_getTachoCount")
    @PermitAll
    public String motor_getTachoCount(String address, String port, String motorType){
    	try{
    		return new Integer(getMotor(address,port,motorType).getTachoCount()).toString();
    	}
    	catch(Exception x){
    		return x.toString();
    	}
    }

    @GET("/EV3/motor_isMoving")
    @PermitAll
    public String motor_isMoving(String address, String port, String motorType){
    	try{
    		return new Boolean(getMotor(address,port,motorType).isMoving()).toString();
    	}
    	catch(Exception x){
    		return x.toString();
    	}
    }
    
    @GET("/EV3/motor_isStalled")
    @PermitAll
    public String motor_isStalled(String address, String port, String motorType){
    	try{
    		return new Boolean(getMotor(address,port,motorType).isStalled()).toString();
    	}
    	catch(Exception x){
    		return x.toString();
    	}
    }
    
    @GET("/EV3/motor_resetTachoCount")
    @PermitAll
    public String motor_resetTachoCount(String address, String port, String motorType){
    	try{
    		getMotor(address,port,motorType).resetTachoCount();
    		return "ok";
    	}
    	catch(Exception x){
    		return x.toString();
    	}
    }
    
    @GET("/EV3/motor_rotateTo")
    @PermitAll
    public String motor_rotateTo(String address, String port, String motorType, int angle, boolean immediateReturn){
    	try{
    		getMotor(address,port,motorType).rotateTo(angle, immediateReturn);
    		return "ok";
    	}
    	catch(Exception x){
    		return x.toString();
    	}
    }
    
    @GET("/EV3/motor_setAcceleration")
    @PermitAll
    public String motor_setAcceleration(String address, String port, String motorType, int acceleration){
    	try{
    		getMotor(address,port,motorType).setAcceleration(acceleration);
    		return "ok";
    	}
    	catch(Exception x){
    		return x.toString();
    	}
    }
    
    @GET("/EV3/motor_setSpeed")
    @PermitAll
    public String motor_setSpeed(String address, String port, String motorType, int speed){
    	try{
    		getMotor(address,port,motorType).setSpeed(speed);
    		return "ok";
    	}
    	catch(Exception x){
    		return x.toString();
    	}
    }
    
    @GET("/EV3/motor_setStallThreshold")
    @PermitAll
    public String motor_setStallThreshold(String address, String port, String motorType, int error, int time){
    	try{
    		getMotor(address,port,motorType).setStallThreshold(error, time);
    		return "ok";
    	}
    	catch(Exception x){
    		return x.toString();
    	}
    }
    
    @GET("/EV3/motor_stop")
    @PermitAll
    public String motor_stop(String address, String port, String motorType, boolean immediateReturn){
    	try{
    		getMotor(address,port,motorType).stop(immediateReturn);
    		return "ok";
    	}
    	catch(Exception x){
    		return x.toString();
    	}
    }
    
    @GET("/EV3/sensorIR_getDistance")
    @PermitAll
    public String sensorIR_getDistance(String address, String port){
    	try{
    		String res = "" + (getSensor(address,port,"lejos.hardware.sensor.EV3IRSensor", "Distance").fetchSample()[0]);
    		if(res == "Infinity") res = "" + -1;
    		return res;
    	}
    	catch(Exception x){
    		return x.toString();
    	}
    }
    
    @GET("/EV3/sensorIR_getSeek")
    @PermitAll
    public String sensor_IR_getBeacon(String address, String port){
    	try{
    		return "" + (getSensor(address,port,"lejos.hardware.sensor.EV3IRSensor", "Seek").fetchSample()[0]);
    	}
    	catch(Exception x){
    		return x.toString();
    	}
    }

    @GET("/EV3/sensorIR_getRC")
    @PermitAll
    public String sensorIR_getRC(String address, String port, int channel ){
    	try{
    		return Integer.toString(getRemoteControl(address,port).getRemoteCommand(channel));
    		/*
    		byte [] cmds = new byte[4];
    		cmds[0] = 0;
    		cmds[1] = 0;
    		cmds[2] = 0;
    		cmds[3] = 0;
    		getRemoteControl(address,port).getRemoteCommands(cmds, 0, 4);
    		for( int i = 0; i<4; i++){
    			System.out.println(cmds[i]);
    		}
    		String result = "";
    		result += getRemoteControl(address,port).getAvailableModes() + ";";
    		result += getRemoteControl(address,port).getCurrentMode() + ";--";
    		result += getRemoteControl(address, port).getRemoteCommand(0) + ";";
    		result += Integer.toString(getRemoteControl(address, port).getRemoteCommand(1)) + ";";
    		result += Integer.toString(getRemoteControl(address, port).getRemoteCommand(2)) + ";";
    		result += Integer.toString(getRemoteControl(address, port).getRemoteCommand(3)) + ";";
    		result += getRemoteControl(address,port).getRemoteCommand(0);
    		return result;
    		*/
    		//getRemoteControl(address, port).getRemoteCommands(cmds,0,4);
    		//String result = "";
    		//for( int i = 0; i < 4; i++ ){
    		//	result += cmds[i] + ";";
    		//}
    		//return result;
    	}
    	catch(Exception x){
    		return x.toString();
    	}
    }
    
    @GET("/EV3/sensorEV3Color_getRGB")
    @PermitAll
    public String sensorEV3Color_getRGB(String address, String port){
    	try{
    		float [] sample = getSensor(address,port,"lejos.hardware.sensor.EV3ColorSensor", "RGB").fetchSample();
    		String result = "[[\"R\"," + sample[0] + "],";
    		result += "[\"G\"," + sample[1] + "],";
    		result += "[\"B\"," + sample[2] + "]]";
    		return result;
    	}
    	catch(Exception x){
    		return x.toString();
    	}
    }
    
    @GET("/EV3/sensorEV3Color_getAmbient")
    @PermitAll
    public String sensorEV3Color_getAmbient(String address, String port){
    	try{
    		return Float.toString(getSensor(address,port, "lejos.hardware.sensor.EV3ColorSensor", "Ambient").fetchSample()[0]);
    		
    	}
    	catch(Exception x){
    		return x.toString();
    	}
    }
    
    @GET("/EV3/sensorEV3Color_getRed")
    @PermitAll
    public String sensorEV3Color_getRed(String address, String port){
    	try{
    		return Float.toString(getSensor(address,port, "lejos.hardware.sensor.EV3ColorSensor", "Red").fetchSample()[0]);
    	}
    	catch(Exception x){
    		return x.toString();
    	}
    }
    
    @GET("/EV3/sensorEV3Color_getColorID")
    @PermitAll
    public String sensorEV3Color_getColorID(String address, String port){
    	try{
    		return Float.toString(getSensor(address,port, "lejos.hardware.sensor.EV3ColorSensor", "ColorID").fetchSample()[0]);
    	}
    	catch(Exception x){
    		return x.toString();
    	}
    }
    
    @GET("/EV3/sensorEV3Touch_isPressed")
    @PermitAll
    public String sensorEV3Touch_isPressed(String address, String port){
    	try{
    		return Boolean.toString(
    				getSensor(address,port,"lejos.hardware.sensor.EV3TouchSensor", "Touch").fetchSample()[0] > 0 ? true : false
    						);
    	}
    	catch(Exception x){
    		return x.toString();
    	}
    }
    
    @GET("/EV3/sensor_close")
    @PermitAll
    public String sensorClose(String address, String port){
    	try{
    		closeSensor(address,port);
    		return "ok";
    	}
    	catch(Exception x){
    		return x.toString();
    	}
    }
    
    @GET("/EV3/sensorEV3Gyro_getAngle")
    @PermitAll
    public String sensorEV3Gyro_getAngle(String address, String port){
    	try{
    		return Float.toString(getSensor(address,port,"lejos.hardware.sensor.EV3GyroSensor", "Angle").fetchSample()[0]);
    	}
    	catch(Exception x){
    		return x.toString();
    	}
    }
    
    @GET("/EV3/sensorEV3Gyro_getRate")
    @PermitAll
    public String sensorEV3Gyro_getRate(String address, String port){
    	try{
    		return Float.toString(getSensor(address,port,"lejos.hardware.sensor.EV3GyroSensor", "Rate").fetchSample()[0]);
    	}
    	catch(Exception x){
    		return x.toString();
    	}
    }
    
    //TODO this one doesn't seem to work so I try to sequence both but bad
    @GET("/EV3/sensorEV3Gyro_getAngleAndRate")
    @PermitAll
    public String sensorEV3Gyro_getAngleAndRate(String address, String port){
    	try{
    		String res = "[";
    		res += Float.toString(getSensor(address,port, "lejos.hardware.sensor.EV3GyroSensor", "Angle").fetchSample()[0]);
    		closeSensor(address,port);
    		res += ",";
    		res += Float.toString(getSensor(address,port, "lejos.hardware.sensor.EV3GyroSensor", "Rate").fetchSample()[0]);
    		closeSensor(address,port);
    		res += "]";
    		return res;
    	}
    	catch(Exception x ){
    		return x.toString();
    	}
    }
    
    @GET("/EV3/playNote")
    @PermitAll
    public String playNote(String address, String instrument, int note, int length){
    	int [] inst;
    	switch( instrument ){
    	case "XYLOPHONE": 
    		inst = Audio.XYLOPHONE;
    		break;
    	case "FLUTE":
    		inst = Audio.FLUTE;
    		break;
    	case "PIANO":
    		inst = Audio.PIANO;
    		break;
    	default:
    		inst = Audio.PIANO;
    	}
    	try{
    		getAudio(address).playNote(inst, note, length);
    		return "ok";
    	}
    	catch(Exception x){
    		return x.toString();
    	}
    }
    
    @GET("/EV3/playTone")
    @PermitAll
    public String playTone(String address, int tone, int length){
    	try{
    		//TODO remember playedTone, if same do nothing, change note only if diff or infinity
    		//do this for length 0, 
    		getAudio(address).playTone(tone, length);
    		return "ok";
    	}
    	catch(Exception x){
    		return x.toString();
    	}    	
    }
    
    private static final RMIRegulatedMotor getMotor(String address, String port, char motorType) throws RemoteException, MalformedURLException, NotBoundException{
    	//return getEV3(address).createRegulatedMotor(port, motorType);
    	if(motors.containsKey(address + port)){
    		return motors.get(address + port);
    	}else{
    		RMIRegulatedMotor motor = getEV3(address).createRegulatedMotor(port, motorType);
    		motors.put(address + port, motor);
    		return motor;
    	}
    }
    
   
    
    private static final int maxTrials = 3;
    private static int currentTrial = 0;
    
    //<IPAddress,ev3>
    private static final HashMap<String,RemoteEV3>ev3s = new HashMap<String,RemoteEV3>();
    //<IPAddress|port>
    private static final HashMap<String,RMIRegulatedMotor>motors = new HashMap<String,RMIRegulatedMotor>();
    //<IPAddress|port>
    private static final HashMap<String,RMISampleProvider>sensors = new HashMap<String,RMISampleProvider>();
    //<IPAddress|port>
    private static final HashMap<String,EV3IRSensor>remoteControls = new HashMap<String,EV3IRSensor>();
    //TODO <IPAddress|sensorName|mode> to use to auto close sensor port if changes(check if sensors can provide simultaneous samplers eg IR) 
    private static final HashMap<String,String>sensorsModes = new HashMap<String,String>();
    
    private static final HashMap<String,Audio>audios = new HashMap<String,Audio>();

    
    private static final RemoteEV3 getEV3(String address) throws RemoteException, MalformedURLException, NotBoundException{
    	if(ev3s.containsKey(address)){
    		return ev3s.get(address);
    	}else{
    		RemoteEV3 ev3 = new RemoteEV3(address);
    		ev3s.put(address, ev3);
    		return ev3;
    	}
    }
    
    private static final RMIRegulatedMotor getMotor(String address, String portName, String motorType) throws MalformedURLException, RemoteException, NotBoundException{
    	String key = address.concat(portName);
    	if( motors.containsKey(key)){
    		return motors.get(key);
    	}else{
    		RMIRegulatedMotor m;
   			m = getEV3(address).createRegulatedMotor(portName, motorType.charAt(0));
    		motors.put(key, m);
    		return m;
    	}
    	
    }  
    
    private static final RMISampleProvider getSensor(String address, String portName, String sensorName, String modeName) throws RemoteException, MalformedURLException, NotBoundException{
    	String key = address.concat(portName); //TODO .concat(sensorName).concat(modeName);
    	if( sensors.containsKey(key)){
    		return sensors.get(key);
    	}else{
    		RMISampleProvider s;
    		s = getEV3(address).createSampleProvider(portName, sensorName, modeName);
    		sensors.put(key, s);
    		return s;
    	}
    }
    
    private static final EV3IRSensor getRemoteControl(String address, String portName ) throws RemoteException, MalformedURLException, NotBoundException{
    	String key = address.concat(portName);
    	if(remoteControls.containsKey(key)){
    		return remoteControls.get(key);
    	}else{
    		Port port = getEV3(address).getPort(portName);
    		EV3IRSensor rc = new EV3IRSensor(port);
    		remoteControls.put(key, rc);
    		return rc;
    	}
    }
    
    private static final Audio getAudio(String address) throws RemoteException, MalformedURLException, NotBoundException{
    	if( audios.containsKey(address)){
    		return audios.get(address);
    	}else{
    		Audio a = getEV3(address).getAudio();
    		audios.put(address, a);
    		return a;
    	}
    }
    
    private static final void closeSensor(String address, String portName){
    	String key = address.concat(portName);
    	if( sensors.containsKey(key) && sensors.get(key) != null){
    		try{
    			sensors.get(key).close();
    		}
    		catch(Exception x){
    			
    		}
    		finally{
    			sensors.remove(key);
    		}
    	}
    	if( remoteControls.containsKey(key) ){
    		remoteControls.get(key).close();
    		remoteControls.remove(key);
    	}
    }
  
}
