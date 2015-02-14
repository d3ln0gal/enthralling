if (typeof String.prototype.startsWith != 'function') {
  String.prototype.startsWith = function (str){
    return this.slice(0, str.length) == str;
  };
}

if (typeof String.prototype.endsWith != 'function') {
  String.prototype.endsWith = function (str){
    return this.slice(-str.length) == str;
  };
}

if (!document.thrall_initiated){
	document.thrall_initiated = true;

SpriteMorph.prototype.var_getByName = function(attributeName){
	if(this.variables.silentFind(attributeName)){
		return this.variables.getVar(attributeName);			
	}else{
		return "NIL";
	}
}

SpriteMorph.prototype.var_createOrSet = function(name, value, global){
	var ide = this.parentThatIsA(IDE_Morph);
	value ? value : 0;
	global = global ? true : false;
	//TODO assert name is a string
	if( typeof value === "string" )
		value = value.trim();
	if(!this.variables.silentFind(name)){
		this.addVariable(name,global);
	}
	this.variables.setVar(name, value);
	ide.flushPaletteCache();
	ide.refreshPalette();	
}

SpriteMorph.prototype.var_delete = function(name){
	if(this.variables.silentFind(name)){
		this.deleteVariableWatcher(name);
		this.variables.deleteVar(name);
	}
}

SpriteMorph.prototype.get_template = function(name){
	var ide = this.parentThatIsA(IDE_Morph);
	var stage = this.parentThatIsA(StageMorph);
	var clone;
	for( i=0; i<stage.globalBlocks.length; i += 1){
		if( stage.globalBlocks[i].spec === "template-" + name ){
			return stage.globalBlocks[i].copyAndBindTo();
		}
	}
	return null;
}

SpriteMorph.prototype.motor_create_reader = function(name){
	var ide = this.parentThatIsA(IDE_Morph);
	var copy = this.get_template("motor-reader");
	copy.isGlobal = false;
	copy.category="motors";
	copy.spec="Motor:" + name;
	copy.body.expression.children[1].children[1].children[0].text=name;
	//TODO find and destroy previous version of the motor: same name or same port
	//ide.currentSprite.customBlocks.push(copy);
	ide.currentSprite.customBlocks = [copy].concat(ide.currentSprite.customBlocks);
	ide.flushBlocksCache();	
	ide.flushPaletteCache();
	ide.refreshPalette();	
	//ide.refreshIDE();
}

SpriteMorph.prototype.customBlockExists = function(name){
	for(i=0; i < this.customBlocks.length; i += 1){
		if(this.customBlocks[i].spec === name)
		return true;
	}
	return false;
}

SpriteMorph.prototype.motor_customBlocks = function(){
	var stage = this.parentThatIsA(StageMorph);
	var ide = this.parentThatIsA(IDE_Morph);
	var clones = [];
	for( i=0; i<stage.globalBlocks.length; i += 1){
		if( stage.globalBlocks[i].spec.startsWith("template-motor-method-")){
			clones.push(stage.globalBlocks[i].copyAndBindTo());
		}
	}	
	for(i=0; i<clones.length; i += 1){
		clones[i].isGlobal = false;
		clones[i].category = "motors";
		var newspec = clones[i].spec.replace("template-motor-method-", "");
		clones[i].spec = newspec;
		if(!this.customBlockExists(newspec)){
			ide.currentSprite.customBlocks.push(clones[i]);
		}
		
	}
	ide.flushBlocksCache();	
	ide.flushPaletteCache();
	ide.refreshPalette();		
}

SpriteMorph.prototype.thrall_setRobotAttribute = function(attributeName, attributeCategory, attributeValue){
	var def;
	var ide = this.parentThatIsA(IDE_Morph);
	def = new CustomBlockDefinition(attributeName);
	def.type = 'reporter';
	def.category = attributeCategory;
	def.declarations = null;
	def.isGlobal = false;
	def.body = Process.prototype.reify.call(
					null,
					SpriteMorph.prototype.blockForSelector('doReport'),
					new List(),
					true // ignore empty slots for custom block reification
				);
	def.body.expression.children[1].setContents(attributeValue);
	//this.replaceDoubleDefinitionsFor(def); //avoid duplicates, TODO wouldn't be better a replace than a delete and push?
	var duplicate=false;
	var duplicateIndexes = [];
	for( i = 0; i < this.customBlocks.length; i += 1){
		if( this.customBlocks[i].spec === attributeName )
			duplicateIndexes.push(i);
	}
	if(duplicateIndexes.length == 0){
		this.customBlocks.push(def)
	}else{
		myself = this;
		duplicateIndexes.forEach(function(i){
			myself.customBlocks[i] = def;
		})
	}
	ide.flushPaletteCache();
	ide.refreshPalette();
}

	StageMorph.prototype.categories.push("robot");
	StageMorph.prototype.blockColor["robot"] = new Color(10, 10, 10);
	StageMorph.prototype.categories.push("motors");
	StageMorph.prototype.blockColor["motors"] = new Color(0, 255, 255);
	StageMorph.prototype.categories.push("sensors");
	StageMorph.prototype.blockColor["sensors"] = new Color(155, 0, 155);
	StageMorph.prototype.categories.push("driver");
	StageMorph.prototype.blockColor["driver"] = new Color(0, 255, 10);
	StageMorph.prototype.categories.push("private"); //comment this line to have private methods invisible
	StageMorph.prototype.blockColor["private"] = new Color(255, 255, 255);

}	
/*
function enthralling(){
	const RobotModels = {
		"Lego Mindstorms EV3": "EV3",
		"Lego Mindstorms NXT": "NXT", 
		"HummingBird": "HB"		
	}
	
	var robots = {};
}

var Thrall = new enthralling();


ThrallRobot = function( model, address, motors, sensors ){
	this.model = model;
	this.address = address;
	this.motors = motors;
	this.sensors = sensors;
}

var myRobot = new ThrallRobot("EV3", "10.0.1.1",
							{ A: "EV3M", B: "EV3L", C: "EV3L", D: "EV3G"},
							{ 1: "EV3C", 2: "NXTL", 3: "EV3IR" });
*/
