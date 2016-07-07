var exec = require("cordova/exec");

var Adplay = function () {
	this.name = "Adplay"
};

Adplay.prototype.init = function(userId, key,success_callback,error_callback){
	exec(success_callback,error_callback,"Adplay","init",[{"userId":userId,"key":key}])
};

Adplay.prototype.show = function(ad_id,success_callback,error_callback){
	exec(success_callback,error_callback,"Adplay","show",[{"ad_id":ad_id}])
};

module.exports = new Adplay();
