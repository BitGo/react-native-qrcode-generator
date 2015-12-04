var { NativeModules } = require('react-native');
var QRCodeAndroid = NativeModules.QRCodeModule;
var QRCodeModule = {
	generateBase64String: function(message, dimension) {
		return new Promise(function(resolve, reject) {
			QRCodeAndroid.base64Image(
				message,
				dimension,
				(result) => resolve(result),
				(message) => reject(message)
			);
		});
	}
}
module.exports = QRCodeModule;
