function enthrall_getJSON(url){
  req = new XMLHttpRequest();
  req.open("GET", "/enthrall/" + url, false);
  req.send();
  //return JSON.parse(req.responseText);
  return req.responseText;
}


SpriteMorph.prototype.getJSON = function(url){
	req = new XMLHttpRequest();
	req.open("GET", url, false);
	req.send();
	return req.responseText;
}

SpriteMorph.prototype.var_createOrSet = function(name, value, global){
	value ? value : 0;
	global = global ? true : false;
	//TODO assert name is a string
	if(!this.variables.silentFind(name)){
		this.addVariable(name,global);
		this.toggleVariableWatcher(name,global);
	}
	this.variables.setVar(name, value);
	this.blocksCache["variables"]=null;
	this.paletteCache["variables"]=null;
	this.parentThatIsA(IDE_Morph).refreshPalette();
}

SpriteMorph.prototype.var_getByName = function(attributeName){
	if(this.variables.silentFind(attributeName)){
		return this.variables.getVar(attributeName);			
	}else{
		return "NIL";
	}
}

SpriteMorph.prototype.var_delete = function(name){
	if(this.variables.silentFind(name)){
		this.deleteVariableWatcher(name);
		this.variables.deleteVar(name);
		this.blocksCache["variables"]=null;
		this.paletteCache["variables"]=null;
		this.parentThatIsA(IDE_Morph).refreshPalette();
	}
}

SpriteMorph.prototype.customBlock_exists = function(name){
	for(i=0; i < this.customBlocks.length; i += 1){
		if(this.customBlocks[i].spec === name)
		return true;
	}
	return false;
}

// use splice to add attribute reporters on top: customBlocks.splice(0,0,mystuff)
SpriteMorph.prototype.customBlock_createFromTemplates = function(category){
	var stage = this.parentThatIsA(StageMorph);
	var ide = this.parentThatIsA(IDE_Morph);
	for(var i = 0; i<stage.globalBlocks.length; i+=1){
		if(stage.globalBlocks[i].category === "template-" + category){
			if( !this.customBlock_exists(stage.globalBlocks[i].spec )){
				var cb = stage.globalBlocks[i].copyAndBindTo(this);
				cb.isGlobal = false;
				cb.category=category;
				ide.currentSprite.customBlocks.push(cb);
			}
		}
	}
	ide.flushBlocksCache();	
	ide.flushPaletteCache();
	ide.refreshPalette();	
}

SpriteMorph.prototype.robot_createOrSet = function(type,address){
  this.var_createOrSet("type", type);
  this.var_createOrSet("address", address);
}

SpriteMorph.prototype.motor_createOrSet = function(name,port,type){
  this.var_createOrSet(name, {port: port, type: type}, false);
  this.motor_createReader(name);
  var ide = this.parentThatIsA(IDE_Morph);  
  ide.flushBlocksCache();	
  ide.flushPaletteCache();
  ide.refreshPalette();	  
}

SpriteMorph.prototype.motor_createReader = function(name){
	var stage = this.parentThatIsA(StageMorph);
	var ide = this.parentThatIsA(IDE_Morph);
	for(var i = 0; i<stage.globalBlocks.length; ++i){
	  if(stage.globalBlocks[i].spec === "motor-reader"){
	    if( !this.customBlock_exists(name) ){
	      var cb = stage.globalBlocks[i].copyAndBindTo(this);
	      cb.isGlobal = false;
	      cb.category = "motors";
	      cb.spec="$gears " + name;
	      cb.body.expression.children[1].blockSpec=name;
	      //cb.body.expression.children[1].children[0].parent.blockspec=name;
	      cb.body.expression.children[1].children[0].text=name;
	      ide.currentSprite.customBlocks.splice(0,0,cb);
	    }
	  }
	}
}

SpriteMorph.prototype.motor_getMaxSpeed = function(motor){
  var url = this.var_getByName("type") + "/motor_getMaxSpeed?address=" + this.var_getByName("address") + "&port=" + motor["port"] + "&motorType=" + motor["type"];
  return enthrall_getJSON(url);  
}

SpriteMorph.prototype.motor_getSpeed = function(motor){
  var url = this.var_getByName("type") + "/motor_getSpeed?address=" + this.var_getByName("address") + "&port=" + motor["port"] + "&motorType=" + motor["type"];
  return enthrall_getJSON(url);
}

SpriteMorph.prototype.motor_setSpeed = function(motor, speed){
  var url = this.var_getByName("type") + "/motor_setSpeed?address=" + this.var_getByName("address") + "&port=" + motor["port"] + "&motorType=" + motor["type"];
  url += "&speed=" + speed;
  enthrall_getJSON(url);
}

SpriteMorph.prototype.motor_setAcceleration = function(motor, acceleration){
  var url = this.var_getByName("type") + "/motor_setAcceleration?address=" + this.var_getByName("address") + "&port=" + motor["port"] + "&motorType=" + motor["type"];
  url += "&acceleration=" + acceleration;
  return enthrall_getJSON(url);  
}

SpriteMorph.prototype.motor_rotate = function(motor, angle, immediateReturn){
  immediateReturn = immediateReturn ? true : false;
  var url = this.var_getByName("type") + "/motor_rotate?address=" + this.var_getByName("address") + "&port=" + motor["port"] + "&motorType=" + motor["type"];
  url += "&angle=" + angle + "&immediateReturn=" + immediateReturn;
  return enthrall_getJSON(url);
}

SpriteMorph.prototype.motor_getTachoCount = function(motor){
  var url = this.var_getByName("type") + "/motor_getTachoCount?address=" + this.var_getByName("address") + "&port=" + motor["port"] + "&motorType=" + motor["type"];
  return enthrall_getJSON(url);  
}

SpriteMorph.prototype.motor_resetTachoCount = function(motor){
  var url = this.var_getByName("type") + "/motor_resetTachoCount?address=" + this.var_getByName("address") + "&port=" + motor["port"] + "&motorType=" + motor["type"];
  return enthrall_getJSON(url);  
}

SpriteMorph.prototype.motor_rotateTo = function(motor, angle, angle, immediateReturn){
  immediateReturn = immediateReturn ? true : false;
  var url = this.var_getByName("type") + "/motor_rotateTo?address=" + this.var_getByName("address") + "&port=" + motor["port"] + "&motorType=" + motor["type"];
  url += "&angle=" + angle + "&immediateReturn=" + immediateReturn;
  return enthrall_getJSON(url);
}

SpriteMorph.prototype.motor_isMoving = function(motor){
  var url = this.var_getByName("type") + "/motor_isMoving?address=" + this.var_getByName("address") + "&port=" + motor["port"] + "&motorType=" + motor["type"];
  return enthrall_getJSON(url);  
}

SpriteMorph.prototype.motor_setStallThreshold = function(motor, error, time){
  var url = this.var_getByName("type") + "/motor_setStallThreshold?address=" + this.var_getByName("address") + "&port=" + motor["port"] + "&motorType=" + motor["type"];
  url += "&error=" + error + "&time=" + time;
  return enthrall_getJSON(url);  
}

SpriteMorph.prototype.motor_isStalled = function(motor){
  var url = this.var_getByName("type") + "/motor_isStalled?address=" + this.var_getByName("address") + "&port=" + motor["port"] + "&motorType=" + motor["type"];
  return enthrall_getJSON(url);  
}

SpriteMorph.prototype.motor_forward = function(motor){
  var url = this.var_getByName("type") + "/motor_forward?address=" + this.var_getByName("address") + "&port=" + motor["port"] + "&motorType=" + motor["type"];
  return enthrall_getJSON(url);
}

SpriteMorph.prototype.motor_backward = function(motor){
  var url = this.var_getByName("type") + "/motor_backward?address=" + this.var_getByName("address") + "&port=" + motor["port"] + "&motorType=" + motor["type"];
  return enthrall_getJSON(url);
}

SpriteMorph.prototype.motor_stop = function(motor){
  var url = this.var_getByName("type") + "/motor_stop?address=" + this.var_getByName("address") + "&port=" + motor["port"] + "&motorType=" + motor["type"] + "&immediateReturn=true";
  return enthrall_getJSON(url);
}

SpriteMorph.prototype.motor_getLimitAngle = function(motor){
  var url = this.var_getByName("type") + "/motor_getLimitAngle?address=" + this.var_getByName("address") + "&port=" + motor["port"] + "&motorType=" + motor["type"];
  return enthrall_getJSON(url);  
}




SpriteMorph.prototype.sensorIR_createOrSet = function(name,port,mode){
  this.var_createOrSet(name, {port: port, type: "lejos.hardware.sensor.EV3IRSensor", mode: mode}, false);
  //this.motor_createReader(name);
  var ide = this.parentThatIsA(IDE_Morph);  
  ide.flushBlocksCache();	
  ide.flushPaletteCache();
  ide.refreshPalette();	  
}

SpriteMorph.prototype.sensor_createOrSet = function(name,port){
  this.var_createOrSet(name, {port: ("S" + port)}, false);
  //this.motor_createReader(name);
  var ide = this.parentThatIsA(IDE_Morph);  
  ide.flushBlocksCache();	
  ide.flushPaletteCache();
  ide.refreshPalette();	  
}

SpriteMorph.prototype.sensorIR_getDistance = function(sensor){
  var url = this.var_getByName("type") + "/sensorIR_getDistance?address=" + this.var_getByName("address") + "&port=" + sensor["port"] ;
  return enthrall_getJSON(url);
}

SpriteMorph.prototype.sensorEV3Color_getRGB = function(sensor){
  var url = this.var_getByName("type") + "/sensorEV3Color_getRGB?address=" + this.var_getByName("address") + "&port=" + sensor["port"] ;
  return enthrall_getJSON(url);
}



