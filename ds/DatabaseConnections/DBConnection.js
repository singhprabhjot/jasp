var mongoose = require('mongoose');

USER_NAME = "jaspreet";
PASSWORD = "jaspreet";
DATABASE = "csu_distributed";
var uri = 'mongodb://' + USER_NAME + ':' + PASSWORD + '@cluster0-shard-00-00-txvpb.mongodb.net:27017,cluster0-shard-00-01-txvpb.mongodb.net:27017,cluster0-shard-00-02-txvpb.mongodb.net:27017/' + DATABASE + '?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin';

// var options = {
//     db: {native_parser: true},
//     server: {poolSize: 5},
//     user: 'rw',
//     pass: 'adminpwd'
// }

exports.createConnection = mongoose.createConnection(uri,{
  useMongoClient: true,
  /* other options */
});

exports.UserSchema = new mongoose.Schema({
    userId: String,
    emailId: String,
    fname: String,
    lname: String,
    mobileNum: Number
});

var CommentSchema = new mongoose.Schema({
	CommentId : String,
	Comment : String,
	userId : String
	});
exports.CommentSchema = CommentSchema;

exports.OfferSchema = new mongoose.Schema({
	offerId: String,
	buyingQty: Number,
	offeredDetails: String,
	buyerStatus: String,
	sellerStatus: String,
	offerExpiry: Date,
	productId: Number,
	buyerId: Number,
	Comment: [CommentSchema],
	lastModified: Date
});

exports.ProductSchema = new mongoose.Schema({
		productId:Number,
		productName:String,
		quantity:Number,
		userId:String,
		expectedOffer:String,
		productDesc:String,
		prodExpDate:Date,
		isValid:Number,
		categoryId:Number,
		lastUpdated:Date
	    
	});
